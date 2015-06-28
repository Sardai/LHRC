/**
 * 
 */
package lhrc.group3.tjooner.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lhrc.group3.tjooner.helpers.DateUtils;
import lhrc.group3.tjooner.helpers.FileUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.DataSource;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

/**
 * @author Chris
 * AsyncTask to upload a media item or a play list to the online TJOONER environment.
 */
public class UploadTask extends AsyncTask<Void, Integer, String> {
	private static final String BASE_URL = "http://setup.tjooner.tv/JCC/Saxion/TJOONER/REST/api/";
	private static final String PUT_CHUNKED_MEDIA_URL = BASE_URL
			+ "ChunkedMedia";
	private static final String PUT_MEDIA_URL = BASE_URL + "Media";
	private static final String PUT_PLAYLIST_URL = BASE_URL + "Playlist";
	private static final String emptyUUID = "00000000-0000-0000-0000-000000000000";
	private static final int CHUNKSIZE = 1024 * 8;
	private static final String PROGRESS_MESSAGE = "uploading media";

	private static final String CANCELLED = "cancelled";

	public static final int UPLOAD_MEDIA = 1;
	public static final int UPLOAD_PLAYLIST = 2;

	private int uploadType;
	private Media media;
	private Group group;
	private Context context;
	private int totalAmount;
	private int doneAmount;
	private ProgressDialog progressDialog;
	private File file;
	private DataSource dataSource;

	private String title;
	private List<Media> mediaList;

	/**
	 * Create a new upload task to upload a media item.
	 * @param context    the context
	 * @param media	     the media item to upload
	 * @param group   	 the group of the media item
	 * @param dataSource the data source
	 */
	public UploadTask(final Context context, Media media, Group group,
			DataSource dataSource) {

		uploadType = UPLOAD_MEDIA;
		this.context = context;
		this.media = media;
		this.group = group;
		this.dataSource = dataSource;
		createProgressbar();
	}

	/**
	 * Create a new upload task to upload a play list.
	 * @param context
	 * @param title
	 * @param mediaList
	 */
	public UploadTask(final Context context, String title, List<Media> mediaList) {
		uploadType = UPLOAD_PLAYLIST;

		this.context = context;
		this.title = title;
		this.mediaList = mediaList;

		createProgressbar();
	}

	@Override
	protected void onPreExecute() {

		if (media != null && media.getPath() != null) {
			//Get the media file and calculate the amount of packages to send.			
			Uri uri = media.getUri();
			if (media.getPath().toLowerCase().contains("file")) {
				uri = FileUtils.getImageContentUri(context, media.getPath());
			}
			file = new File(FileUtils.getPicturePath(context, uri));

			totalAmount = (int) file.length() / CHUNKSIZE;
		}
		progressDialog.setMax(totalAmount + 1);

		progressDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		switch (uploadType) {
		case UPLOAD_MEDIA:
			return uploadMedia();

		case UPLOAD_PLAYLIST:
			return uploadPlaylist();
		}
		return null;

	}

	@Override
	protected void onPostExecute(String result) {
		// if no result then show error, if cancelled then show cancelled else update media item and hide progressbar.
		if (result == null) {
			Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG)
					.show();
		} else if (result.equals(CANCELLED)) {
			Toast.makeText(context, CANCELLED, Toast.LENGTH_LONG).show();
		} else {
			dataSource.update(media);
			progressDialog.hide();
			new AlertDialog.Builder(context)
					.setMessage("Media upload is completed")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		//updates progressdialog with progress.
		progressDialog.setProgress(values[0]);
		// if (toast != null)
		// toast.cancel();
		// toast = Toast.makeText(context,
		// String.format("%d - %d", values[0], values[1]),
		// Toast.LENGTH_SHORT);
		// toast.show();
	}

	private String uploadMedia() {
		try {

			doneAmount = 0;

			byte[] buffer = new byte[CHUNKSIZE];
			FileInputStream is = new FileInputStream(file);
			String base64String = "";
			String id = emptyUUID;
			//reads a chunck of media item until all chunks are uploaded.
			while ((is.read(buffer)) != -1) {
				//if user cancelled upload stop the upload.
				if (isCancelled()) {
					is.close();
					return CANCELLED;
				}
				//if base64String is not empty a base64 string has been created and can be uploaded to the online enviroment.
				if (!base64String.isEmpty()) {
					// the xml to upload.
					String xml = getXmlString(id, base64String, false);
					String result = sendChunk(xml);
					// extracts the id from the result.
					id = result
							.replace(
									"<guid xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">",
									"").replace("</guid>", "");
				}
				//encode the chunk to a base64 string.
				base64String = Base64.encodeToString(buffer, 0);

			}
			//sends last chunk to online environment.
			String last = getXmlString(id, base64String, true);
			sendChunk(last);
			media.setRemoteId(id);
			is.close();
			
			//Sends the details of the media item to the online environment.
			String endResult = SendMedia();
			media.setRemoteId(endResult);
			return endResult;
		} catch (Exception e) {
			// file not found, handle case.
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Upload playlist to online environment.
	 * @return the result of the upload.
	 */
	private String uploadPlaylist() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("Title", title);
			JSONArray array = new JSONArray();
			for (Media media : mediaList) {
				group = media.getGroup();
				array.put(getMediaObject(media));
			}
			obj.put("Id", emptyUUID);
			obj.put("Media", array);

			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpPut httpPut = getHttpPut(PUT_PLAYLIST_URL, obj);

			String result = client.execute(httpPut, handler);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Creates a progressbar to show the progression of the upload.
	 */
	private void createProgressbar() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(PROGRESS_MESSAGE);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		progressDialog.setIndeterminate(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						progressDialog.cancel();
					}
				});
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				new AlertDialog.Builder(context)
						.setMessage(
								"Are you sure you want to cancel the upload?")
						.setPositiveButton("yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										UploadTask.this.cancel(true);
										dialog.dismiss();
										progressDialog.hide();
									}

								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										progressDialog.show();
									}
								}).create().show();
			}
		});
	}

//	private JSONObject getJsonObject(String id, String chunk,
//			boolean isLastChunk) throws JSONException {
//		JSONObject obj = new JSONObject();
//		obj.put("FileExtension", "PNG");
//		obj.put("MediaType", "image");
//		obj.put("Chunk", chunk);
//		obj.put("IsLastChunk", isLastChunk);
//		obj.put("mediaId", id);
//		return obj;
//	}

	/**
	 * Send chunk to the online environment.
	 * @param content the chunk to upload
	 * @return the id of the chunk
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String sendChunk(String content) throws ClientProtocolException,
			IOException {

		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpPut httpPut = getHttpPutXml(PUT_CHUNKED_MEDIA_URL, content);
		String id = client.execute(httpPut, handler);
		doneAmount++;
		publishProgress(doneAmount, totalAmount);
		return id;
	}

	/**
	 * Send media item to the online environment.
	 * @return the result of the upload
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String SendMedia() throws JSONException, ClientProtocolException,
			IOException {

		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpPut httpPut = getHttpPut(PUT_MEDIA_URL, getMediaObject(media));

		String result = client.execute(httpPut, handler);
		publishProgress(doneAmount, totalAmount);
		return result;
	}

	/**
	 * Converts a media item to a json object.
	 * @param media the media item
	 * @return the jsonobject of the media item
	 * @throws JSONException
	 */
	private JSONObject getMediaObject(Media media) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("Id", media.getRemoteId());
		obj.put("Description", media.getTitle());

		JSONArray categories = new JSONArray();
		JSONObject category = new JSONObject();
		category.put("Id", group.getId());
		category.put("BackgroundColor", group.getColorString());
		category.put("Description", group.getDescription());
		categories.put(category);
		obj.put("Categories", categories);

		JSONArray tags = new JSONArray();
		for (String tag : media.getTags()) {
			tags.put(tag);
		}
		obj.put("Tags", tags);
		if (media.getDatetime() != null) {
			obj.put("CreationDateTime", DateUtils.dateToString(
					media.getDatetime(), "yyyy-MM-ddThh:mm:ss.sTZD"));
		}
		obj.put("AuthorRights", media.hasCopyright());
		obj.put("Author", media.getAuthor());

		if (media instanceof Picture) {
			obj.put("MediaType", "image");
		} else if (media instanceof Video) {
			obj.put("MediaType", "video");
		}
		return obj;
	}

	/**
	 * Get a httpPut object with json object.
	 * @param url the url of the http call
	 * @param obj the jsonobject
	 * @return the httpput object
	 * @throws UnsupportedEncodingException
	 */
	private HttpPut getHttpPut(String url, JSONObject obj)
			throws UnsupportedEncodingException {
		HttpPut httpPut = new HttpPut(url);

		StringEntity se = new StringEntity(obj.toString());

		// sets the post request as the resulting string
		httpPut.setEntity(se);
		// sets a request header so the page receving the request
		// will know what to do with it
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");

		return httpPut;
	}

	/**
	 * Get a httpPut object with xml.
	 * @param url the url of the http call
	 * @param content the xml
	 * @return the httpPut object
	 * @throws UnsupportedEncodingException
	 */
	private HttpPut getHttpPutXml(String url, String content)
			throws UnsupportedEncodingException {
		HttpPut httpPut = new HttpPut(url);

		StringEntity se = new StringEntity(content);

		// sets the post request as the resulting string
		httpPut.setEntity(se);
		// sets a request header so the page receving the request
		// will know what to do with it
		httpPut.setHeader("Accept", "application/xml;charset=UTF-8");
		httpPut.setHeader("Content-type", "application/xml;charset=UTF-8");
		return httpPut;
	}

	/**
	 * Get a xml string for a chunk.
	 * @param mediaId the id of the media
	 * @param chunk the base64String of the chunk
	 * @param isLastChunk if this is the last chunk to upload
	 * @return the xml of the chunk
	 */
	private String getXmlString(String mediaId, String chunk,
			boolean isLastChunk) {

		String type = "";
		if (media instanceof Video) {
			type = "video";
		} else if (media instanceof Picture) {
			type = "image";
		}

		return "<MediaChunkUp xmlns:i='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://schemas.datacontract.org/2004/07/Tjooner.Service.Rest.Models'>"
				+ " <Chunk>"
				+ chunk
				+ "</Chunk>"
				+ "<FileExtension>"
				+ getExtension(file)
				+ "</FileExtension>"
				+ " <IsLastChunk>"
				+ (isLastChunk ? "true" : "false")
				+ "</IsLastChunk>"
				+ "<MediaType>"
				+ type
				+ "</MediaType>"
				+ "<mediaId>"
				+ mediaId
				+ "</mediaId> " + "</MediaChunkUp>";
	}

	/**
	 * Gets the extension of the media item.
	 * @param f the file of the media item
	 * @return the extension
	 */
	private static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

}
