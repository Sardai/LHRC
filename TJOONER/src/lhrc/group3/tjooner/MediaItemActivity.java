package lhrc.group3.tjooner;

import java.io.File;
import java.io.FileInputStream;
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

public class MediaItemActivity extends Activity {

	private Media media;
	private boolean wijzigMedia;
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

		// Checks if a media item is a new item
		if (media.getTitle() == null) {
			isNew = true;
		} else {
			isNew = false;
		}

		if (media.getGroupId() != null) {
			group = application.getGroup(media.getGroupId());
			ActionBar bar = getActionBar();
			bar.setBackgroundDrawable(new ColorDrawable(group.getColor()));
		}

		imageViewMediaItem = (ImageView) findViewById(R.id.imageViewMediaItem);
		ImageView imageViewVideo = (ImageView) findViewById(R.id.imageViewVideo);
		Intent intent = getIntent();
		wijzigMedia = intent.getBooleanExtra(
				FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false);

		if (getIntent().hasExtra(Storage.GROUP_ID)) {
			String groupId = getIntent().getStringExtra(Storage.GROUP_ID);
			changeInformationfragment = new ChangeInformationFragment(media,
					groupId);
		} else {
			changeInformationfragment = new ChangeInformationFragment(media);
		}

		InformationFragment fragment = new InformationFragment(media);

		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceholder, fragment).commit();
		if (wijzigMedia) {
			change();
		}

		if (media.getTitle() != null) {
			setTitle(media.getTitle());
		}

		RelativeLayout layoutVideo = (RelativeLayout) findViewById(R.id.layoutVideo);
		if (media instanceof Picture) {
			layoutVideo.setVisibility(View.GONE);
			imageViewMediaItem.setVisibility(View.VISIBLE);
			if (media.getTitle() == null) {
				setTitle(R.string.title_picture_add);
			}

			Picture picture = (Picture) media;
			imageViewMediaItem.setImageURI(picture.getUri());

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
		
		if (getIntent().getBooleanExtra(
				FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false)) {
			showSave();
		} else {
			showEdit();
		}
		getIntent().removeExtra(
				FloatingActionButtonFragment.NIEUWE_MEDIA_STRING);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_edit:
			change();

			// if (wijzigMedia) {
			// if (changeInformationfragment.setNewInformation()) {
			// application.DataSource.update(media);
			// transaction = manager.beginTransaction();
			// InformationFragment fragment = new InformationFragment(
			// media);
			// transaction.replace(R.id.fragmentPlaceholder, fragment);
			// transaction.commit();
			// item.setTitle("Modify");
			// wijzigMedia = false;
			//
			// }
			// } else {
			// changeInformationfragment = new
			// ChangeInformationFragment(media);
			// transaction = manager.beginTransaction();
			// transaction.replace(R.id.fragmentPlaceholder,
			// changeInformationfragment);
			// transaction.commit();
			// item.setTitle("Save!");
			// wijzigMedia = true;
			// }

			return true;

		case R.id.action_save:

			if (changeInformationfragment.setNewInformation()) {

				application.DataSource.update(media);

				if (isNew == true) {
					// getFragmentManager().popBackStack();
					finish();
					// showEdit();
				} else {
					showEdit();
					getFragmentManager().popBackStack();
				}
			}

			return true;

		case android.R.id.home:
			int backstackCount = getFragmentManager().getBackStackEntryCount();
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

	private void change() {
		showSave();
		getFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceholder, changeInformationfragment)
				.addToBackStack("edit").commit();
	}

	private void showSave() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(true);
		actionEdit.setVisible(false);
		actionUpload.setVisible(false);
	}

	private void showEdit() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(false);
		actionEdit.setVisible(true);
		actionUpload.setVisible(true);
	}

	private void uploadMedia() {
		// File file = new File(FileUtils.getPicturePath(this, media.getUri()));
		// RandomAccessFile raf = new RandomAccessFile(file, "r");
		// // length of 8 kb.
		// int chunkSize = 1024;
		// long bytesRead = 0;
		// int totalRead = 0;
		// StringBuffer fileContent = new StringBuffer("");
		//
		// FileInputStream is = new FileInputStream(file);
		// int n = 0;
		// String base64String = "";
		// String id = emptyUUID;
		//
		// byte[] buffer = new byte[chunkSize];
		//
		// while ((n = is.read(buffer)) != -1) {
		// if (!base64String.isEmpty()) {
		// // JSONObject obj = getJsonObject(id, base64String, false);
		// }
		//
		// base64String = Base64.encodeToString(buffer, Base64.NO_CLOSE);
		// fileContent.append(base64String);
		// }
		//
		// byte[] decodedString = Base64.decode(fileContent.toString(),
		// Base64.NO_PADDING);
		// fileContent = null;
		// Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
		// decodedString.length);
		// imageViewMediaItem.setImageBitmap(decodedByte);

		new UploadTask(this, media, group).execute();
	}

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
								// User cancelled the popup, returning to
								// editscreen
							}
						});
		// Creates the popup
		builder.create().show();
	}

}
