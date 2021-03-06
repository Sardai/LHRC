package lhrc.group3.tjooner.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import lhrc.group3.tjooner.MediaItemActivity;
import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.TjoonerApplication;
import lhrc.group3.tjooner.helpers.FileUtils;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Fragment for the floating action buttons to add photo's and video's.
 * @author Hugo,Luuk, Chris
 *
 */
public class FloatingActionButtonFragment extends Fragment implements
		OnClickListener {
	private static final int REQUEST_CODE_SELECT_PICTURE = 120;
	private static final int REQUEST_CODE_SELECT_VIDEO = 121;
	private static final int REQUEST_CODE_NEW_PICTURE = 122;
	private static final int REQUEST_CODE_NEW_VIDEO = 123;
	public static final String NEW_MEDIA_STRING = "HetIsNieweMedia";

	private ImageView expandImageView, selectPicture, selectVideo,
			makeNewVideo, makeNewPicture;
	private TjoonerApplication app;
	private DataSource source;
	private String uri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.floating_action_button_fragment,
				container, false);

		app = (TjoonerApplication) getActivity().getApplication();
		source = app.DataSource;

		expandImageView = (ImageView) view
				.findViewById(R.id.expandMenuImageView);
		selectPicture = (ImageView) view.findViewById(R.id.selectImageView);
		selectVideo = (ImageView) view.findViewById(R.id.selectVideoImageView);
		makeNewVideo = (ImageView) view.findViewById(R.id.newVideoImageView);
		makeNewPicture = (ImageView) view
				.findViewById(R.id.newPictureImageView);

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		double widthPercentagePortrait = 0.18;
		double widthPercentageLandscape = 0.15;
		int imagesDimensions = 0;
		Configuration config = getActivity().getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			imagesDimensions = (int) (width * widthPercentagePortrait);
		} else {
			imagesDimensions = (int) (height * widthPercentageLandscape);
		}

		// set all the images arcordingly to the screen size
		expandImageView.getLayoutParams().width = imagesDimensions;
		expandImageView.getLayoutParams().height = imagesDimensions;

		selectPicture.getLayoutParams().width = imagesDimensions;
		selectPicture.getLayoutParams().height = imagesDimensions;

		makeNewPicture.getLayoutParams().width = imagesDimensions;
		makeNewPicture.getLayoutParams().height = imagesDimensions;

		selectVideo.getLayoutParams().height = imagesDimensions;
		selectVideo.getLayoutParams().width = imagesDimensions;

		makeNewVideo.getLayoutParams().height = imagesDimensions;
		makeNewVideo.getLayoutParams().width = imagesDimensions;

		selectPicture.setVisibility(ImageView.GONE);
		selectVideo.setVisibility(ImageView.GONE);
		makeNewPicture.setVisibility(ImageView.GONE);
		makeNewVideo.setVisibility(ImageView.GONE);

		expandImageView.setOnClickListener(this);
		selectPicture.setOnClickListener(this);
		selectVideo.setOnClickListener(this);
		makeNewPicture.setOnClickListener(this);
		makeNewVideo.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// pick picture from gallery

		if (resultCode == Activity.RESULT_OK) {
			// result ok
			UUID id = null;
			switch (requestCode) {
			case REQUEST_CODE_SELECT_PICTURE:
				String uri = intent.getDataString();
				id = insertPicture(uri);
				break;
			case REQUEST_CODE_SELECT_VIDEO:
				id = insertVideo(intent.getData());
				break;
			case REQUEST_CODE_NEW_PICTURE:
				id = insertPicture(this.uri);
				break;
			case REQUEST_CODE_NEW_VIDEO:
				id = insertVideo(intent.getData());
				break;
			}

			if (id != null){
				Intent newIntent = new Intent(getActivity(),
						MediaItemActivity.class);
				newIntent.putExtra(Storage.ID, id.toString());
				newIntent.putExtra(NEW_MEDIA_STRING, true);

				if (getActivity().getIntent().hasExtra(Storage.GROUP_ID)) {
					String groupId = getActivity().getIntent().getExtras()
							.getString(Storage.GROUP_ID);
					;
					newIntent.putExtra(Storage.GROUP_ID, groupId);
				}
				changeVisibility();
				startActivity(newIntent);
			}

		} else {
			// result not ok
			switch (requestCode) {
			case REQUEST_CODE_SELECT_PICTURE:
				Toast.makeText(getActivity(), "You haven't picked an Image",
						Toast.LENGTH_LONG).show();
				break;

			case REQUEST_CODE_SELECT_VIDEO:
				Toast.makeText(getActivity(), "You haven't picked an Video",
						Toast.LENGTH_LONG).show();
				break;
			case REQUEST_CODE_NEW_PICTURE:

				break;
			case REQUEST_CODE_NEW_VIDEO:

				break;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.expandMenuImageView:
			changeVisibility();
			break;
		case R.id.selectImageView:
			selectPicture();
			break;
		case R.id.selectVideoImageView:
			selectVideo();
			break;
		case R.id.newPictureImageView:
			makePicture();
			break;
		case R.id.newVideoImageView:
			makeVideo();
			break;
		}
	}

//	private Bitmap getBitmapFromPath(Uri uri) {
//		String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//		Cursor cursor = getActivity().getContentResolver().query(uri,
//				filePathColumn, null, null, null);
//
//		cursor.moveToFirst();
//
//		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//		String img = cursor.getString(columnIndex);
//		Bitmap bitmap = BitmapFactory.decodeFile(img);
//		cursor.close();
//		return bitmap;
//	}

	/**
	 * insert picture item in the database.
	 * @param path the path of the picture
	 * @return the id of the newly added picture item
	 */
	private UUID insertPicture(String path) {
		Picture pic = new Picture();
		pic.setPath(path);
		source.insert(pic);
		return pic.getId();
	}

	/**
	 * Convert a bitmap to bytes.
	 * @param bitmap the bitmap
	 * @return the byte array
	 */
	private byte[] bitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	/**
	 * Insert a video item in the database.
	 * @param videoUri the uri of the video 
	 * @return the id of the newly added video item
	 */
	private UUID insertVideo(Uri videoUri) {
		Video video = new Video();
		 
		video.setPath(videoUri.toString());
		Bitmap thumb = ThumbnailUtils.createVideoThumbnail(
				FileUtils.getVideoPath(getActivity(), videoUri),
				MediaStore.Images.Thumbnails.MINI_KIND);
		video.setData(bitmapToBytes(thumb));
		source.insert(video);

		return video.getId();
	}

	/**
	 * Opens or closes the floating action button menu.
	 */
	private void changeVisibility() {
		int visibility = makeNewPicture.getVisibility();
		if (visibility == ImageView.VISIBLE) {
			expandImageView.setImageResource(R.drawable.floatingactionbutton);
			selectPicture.setVisibility(ImageView.GONE);
			selectVideo.setVisibility(ImageView.GONE);
			makeNewPicture.setVisibility(ImageView.GONE);
			makeNewVideo.setVisibility(ImageView.GONE);
		} else {
			expandImageView
					.setImageResource(R.drawable.floatingactionbuttonrotate);
			selectPicture.setVisibility(ImageView.VISIBLE);
			selectVideo.setVisibility(ImageView.VISIBLE);
			makeNewPicture.setVisibility(ImageView.VISIBLE);
			makeNewVideo.setVisibility(ImageView.VISIBLE);
		}
	}

	/**
	 * Select a picture.
	 */
	private void selectPicture() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		//intent.setType("image/*");
		//intent.setAction(Intent.ACTION_GET_CONTENT);
		//setUri(intent);

		startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
	}

	/**
	 * Select a video.
	 */
	private void selectVideo() {
		// Use MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
	}

	/**
	 * Make a picture.
	 */
	private void makePicture() {
		Intent intentPicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		setUri(intentPicture);
		// start camera activity
		startActivityForResult(intentPicture, REQUEST_CODE_NEW_PICTURE);
	}

	/**
	 * Make a video.
	 */
	private void makeVideo() {
		Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		startActivityForResult(intentVideo, REQUEST_CODE_NEW_VIDEO);
	}
	
	/**
	 * Set uri to the intent.
	 * @param intent the intent
	 */
	private void setUri(Intent intent){
		Uri uri = getOutputUri();  // create a file to save the video in specific folder
	    if (uri != null) {
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	    }
	}

	/**
	 * Get output uri
	 * @return the uri.
	 */
	private Uri getOutputUri() {
		if (Environment.getExternalStorageState() == null) {
			return null;
		}
		
		File mediaStorage = new File(
				Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"Tjooner");
		if (!mediaStorage.exists() && !mediaStorage.mkdirs()) {
			
			return null;
		}
		
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
		.format(new Date());
		File mediaFile = new File(mediaStorage, "VID_" + timeStamp + ".mp4");
		Uri uri = Uri.fromFile(mediaFile);
		this.uri = uri.toString();
		return uri;
	}
}
