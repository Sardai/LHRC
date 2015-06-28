package lhrc.group3.tjooner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.json.JSONObject;

import lhrc.group3.tjooner.fragments.ChangeInformationFragment;
import lhrc.group3.tjooner.fragments.FloatingActionButtonFragment;
import lhrc.group3.tjooner.fragments.InformationFragment;
import lhrc.group3.tjooner.helpers.FileUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import lhrc.group3.tjooner.web.UploadTask;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Activity to show and edit details of a media item.
 * 
 * @author Chris
 *
 */
public class MediaItemActivity extends Activity {

	private Media media;
	private boolean editMedia;
	private ChangeInformationFragment changeInformationfragment;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private MenuItem actionSave, actionEdit, actionUpload;

	private ImageView imageViewMediaItem;
	private boolean isNew;

	private TjoonerApplication application;
	private Group group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		application = (TjoonerApplication) getApplication();

		String id = getIntent().getExtras().getString(Storage.ID);
		media = application.DataSource.getMedia(id);

		// Checks if a media item is a new item.
		if (media.getTitle() == null) {
			isNew = true;
		} else {
			isNew = false;
		}

		if (media.getGroupId() != null) {
			group = application.getGroup(media.getGroupId());
			ActionBar bar = getActionBar();
			// sets the actionbar to the color of the group.
			bar.setBackgroundDrawable(new ColorDrawable(group.getColor()));
		}

		imageViewMediaItem = (ImageView) findViewById(R.id.imageViewMediaItem);
		ImageView imageViewVideo = (ImageView) findViewById(R.id.imageViewVideo);

		Intent intent = getIntent();
		editMedia = intent.getBooleanExtra(
				FloatingActionButtonFragment.NEW_MEDIA_STRING, false);

		// initializes intents to show details or edit details of a media item.
		if (getIntent().hasExtra(Storage.GROUP_ID)) {
			String groupId = getIntent().getStringExtra(Storage.GROUP_ID);
			changeInformationfragment = new ChangeInformationFragment(media,
					groupId);
		} else {
			changeInformationfragment = new ChangeInformationFragment(media);
		}

		InformationFragment fragment = new InformationFragment(media);
		// replaces fragmentPlaceholder with the information fragment.
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceholder, fragment).commit();
		// if media is in edit mode, add changeInformationfragment to the
		// fragment stack.
		if (editMedia) {
			change();
		}

		if (media.getTitle() != null) {
			setTitle(media.getTitle());
		}

		RelativeLayout layoutVideo = (RelativeLayout) findViewById(R.id.layoutVideo);
		if (media instanceof Picture) {
			// hide video play icon.
			layoutVideo.setVisibility(View.GONE);
			imageViewMediaItem.setVisibility(View.VISIBLE);
			if (media.getTitle() == null) {
				setTitle(R.string.title_picture_add);
			}

			Picture picture = (Picture) media;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;

			AssetFileDescriptor fileDescriptor = null;
			try {
				fileDescriptor = getBaseContext().getContentResolver()
						.openAssetFileDescriptor(media.getUri(), "r");

				Bitmap bitmapOriginal = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
				// scaleImage(media, bitmapOriginal);
				imageViewMediaItem.setImageBitmap(bitmapOriginal);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// imageViewMediaItem.setImageURI(picture.getUri());

			Log.d("title", picture.getTitle() + "");
			Log.d("description", picture.getDescription() + "");
			Log.d("fileName", picture.getFilename() + "");
			Log.d("groupid", picture.getGroupId() + "");
			imageViewMediaItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openFullScreen();
				}
			});

		} else if (media instanceof Video) {
			imageViewMediaItem.setVisibility(View.GONE);
			// show video play icon.
			layoutVideo.setVisibility(View.VISIBLE);
			if (media.getTitle() == null) {
				setTitle(R.string.title_video_add);
			}

			final Video video = (Video) media;
			imageViewVideo.setImageBitmap(video.getBitmap());

			layoutVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openFullScreen();
				}
			});
		}

	}

	private void openFullScreen() {
		Intent intent = new Intent(MediaItemActivity.this,
				FullscreenActivity.class);
		intent.putExtra(Storage.ID, media.getId().toString());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_item, menu);

		actionSave = menu.findItem(R.id.action_save);
		actionEdit = menu.findItem(R.id.action_edit);
		actionUpload = menu.findItem(R.id.action_upload);

		// if media item is in edit mode show save button, else show edit
		// button.
		if (getIntent().getBooleanExtra(
				FloatingActionButtonFragment.NEW_MEDIA_STRING, false)) {
			showSave();
		} else {
			showEdit();
		}
		getIntent().removeExtra(FloatingActionButtonFragment.NEW_MEDIA_STRING);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch (item.getItemId()) {
		case R.id.action_edit:
			change();
			return true;
		case R.id.action_save:

			if (changeInformationfragment.setNewInformation()) {

				application.DataSource.update(media);

				if (isNew == true) {
					// getFragmentManager().popBackStack();
					//goes back to previous activity.
					finish();
					// showEdit();
				} else {
					showEdit();
					//remove fragment from stack.
					getFragmentManager().popBackStack();
				}
			}

			return true;

		case android.R.id.home:
			//amount of fragments on a stack
			int backstackCount = getFragmentManager().getBackStackEntryCount();
			//if stack is bigger than 0 remove fragment from stack else finish activity
			if (backstackCount > 0) {
				getFragmentManager().popBackStack();
			} else {
				finish();
			}
			showEdit();
			return true;
		case R.id.action_upload:
			uploadMedia();
			return true;
		case R.id.action_remove:
			removeMediaItem();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * shows save button and opens changeInformationfragment.
	 */
	private void change() {
		showSave();
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceholder, changeInformationfragment)
				.addToBackStack("edit").commit();
	}

	/**
	 * Shows save button and upload button, hides edit button.
	 */
	private void showSave() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(true);
		actionEdit.setVisible(false);
		actionUpload.setVisible(false);
	}

	/**
	 * Shows edit button and hides save and upload button.
	 */
	private void showEdit() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(false);
		actionEdit.setVisible(true);
		actionUpload.setVisible(true);
	}

	/**
	 * Starts a new upload task.
	 */
	private void uploadMedia() {		
		new UploadTask(this, media, group, application.DataSource).execute();
	}

	/**
	 * Remove media item.
	 */
	private void removeMediaItem() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Weet u zeker dat u dit bestand wilt verwijderen?")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						application.DataSource.remove(media);
						finish();
					}
				})
				.setNegativeButton("Nee",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		// Creates the popup
		builder.create().show();
	}

}
