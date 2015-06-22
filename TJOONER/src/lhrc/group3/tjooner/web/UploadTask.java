/**
 * 
 */
package lhrc.group3.tjooner.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lhrc.group3.tjooner.helpers.DateUtils;
import lhrc.group3.tjooner.helpers.FileUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Chris
 *
 */
public class UploadTask extends AsyncTask<Void, Integer, String> {
	private static final String BASE_URL = "http://setup.tjooner.tv/JCC/Saxion/TJOONER/REST/api/";
	private static final String PUT_CHUNKED_MEDIA_URL = BASE_URL
			+ "ChunkedMedia";
	private static final String PUT_MEDIA_URL = BASE_URL + "Media";
	private static final String emptyUUID = "00000000-0000-0000-0000-000000000000";
	private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	private static final int CHUNKSIZE = 1024 * 8;
	private static final String PROGRESS_MESSAGE = "uploading media";

	private static final String CANCELLED = "cancelled";

	private Media media;
	private Group group;
	private Context context;
	private int totalAmount;
	private int doneAmount;
	private Toast toast;
	private ProgressDialog progressDialog;
	private File file;

	public UploadTask(final Context context, Media media, Group group) {
		this.context = context;
		this.media = media;
		this.group = group;
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(PROGRESS_MESSAGE);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(true);
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
						.setMessage("Are you sure to cancel the upload?")
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
									}
								}).create().show();
			}
		});
	}

	@Override
	protected void onPreExecute() {
		file = new File(FileUtils.getPicturePath(context, media.getUri()));

		totalAmount = (int) file.length() / CHUNKSIZE;
		progressDialog.setMax(totalAmount + 1);

		progressDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {

		try {

			doneAmount = 0;

			byte[] buffer = new byte[CHUNKSIZE];
			FileInputStream is = new FileInputStream(file);
			int n = 0;
			String base64String = "";
			String id = emptyUUID;

			while ((n = is.read(buffer)) != -1) {
				if (isCancelled()) {
					is.close();
					return CANCELLED;
				}
				if (!base64String.isEmpty()) {
					// JSONObject obj = getJsonObject(id, base64String, false);

					String xml = getXmlString(id, base64String, false);
					String result = sendChunk(xml);
					// if(!result.matches(UUID_REGEX)){
					// if(!result.contains("-")){
					// //Toast.makeText(context, id, Toast.LENGTH_LONG).show();
					// Log.e("media-upload", result);
					// return null;
					// }

					id = result
							.replace(
									"<guid xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">",
									"").replace("</guid>", "");

					if (id.equals(emptyUUID)) {
						id = result;
					}

				}

				base64String = Base64.encodeToString(buffer, 0);

			}

			// JSONObject last = getJsonObject(id, base64String, true);
			String last = getXmlString(id, base64String, true);
			String result = sendChunk(last);
			is.close();

			String endResult = SendMedia(id);
			return endResult;
		} catch (Exception e) {
			// file not found, handle case
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// Toast.makeText(context, "Done: " + result, Toast.LENGTH_LONG).show();
		if (result == null) {
			Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG)
					.show();
		} else if (result.equals(CANCELLED)) {
			Toast.makeText(context, CANCELLED, Toast.LENGTH_LONG).show();
		} else {
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
		progressDialog.setProgress(values[0]);
		// if (toast != null)
		// toast.cancel();
		// toast = Toast.makeText(context,
		// String.format("%d - %d", values[0], values[1]),
		// Toast.LENGTH_SHORT);
		// toast.show();
	}

	private JSONObject getJsonObject(String id, String chunk,
			boolean isLastChunk) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("FileExtension", "PNG");
		obj.put("MediaType", "image");
		obj.put("Chunk", chunk);
		obj.put("IsLastChunk", isLastChunk);
		obj.put("mediaId", id);
		return obj;
	}

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

	private String SendMedia(String remoteId) throws JSONException,
			ClientProtocolException, IOException {
		JSONObject obj = new JSONObject();
		obj.put("Id", remoteId);
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

		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpPut httpPut = getHttpPut(PUT_MEDIA_URL, obj);

		String result = client.execute(httpPut, handler);
		publishProgress(doneAmount, totalAmount);
		return result;
	}

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

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

}
