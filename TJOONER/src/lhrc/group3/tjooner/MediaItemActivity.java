package lhrc.group3.tjooner;

import lhrc.group3.tjooner.fragments.ChangeInformationFragment;
import lhrc.group3.tjooner.fragments.FloatingActionButtonFragment;
import lhrc.group3.tjooner.fragments.InformationFragment;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract.Colors;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class MediaItemActivity extends Activity {

	private Media media;
	private boolean wijzigMedia;
	private ChangeInformationFragment changeInformationfragment;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private MenuItem actionSave, actionEdit;

	private TjoonerApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		application = (TjoonerApplication) getApplication();

		String id = getIntent().getExtras().getString(Storage.ID);
		media = application.DataSource.getMedia(id);

		if (media.getGroupId() != null) {
			Group group = application.getGroup(media.getGroupId());
			ActionBar bar = getActionBar();
			bar.setBackgroundDrawable(new ColorDrawable(group.getColor()));
		}

		ImageView imageViewMediaItem = (ImageView) findViewById(R.id.imageViewMediaItem);
		VideoView videoViewMediaItem = (VideoView) findViewById(R.id.videoViewMediaItem);

		Intent intent = getIntent();
		wijzigMedia = intent.getBooleanExtra(FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false);

		if (getIntent().hasExtra(Storage.GROUP_ID)) {
			String groupId = getIntent().getStringExtra(Storage.GROUP_ID);
			changeInformationfragment = new ChangeInformationFragment(media, groupId);
		} else {
			changeInformationfragment = new ChangeInformationFragment(media);
		}

		InformationFragment fragment = new InformationFragment(media);

		getFragmentManager().beginTransaction().replace(R.id.fragmentPlaceholder, fragment).commit();
		if (wijzigMedia) {
			change();
		}

		if (media.getTitle() != null) {
			setTitle(media.getTitle());
		}

		if (media instanceof Picture) {
			videoViewMediaItem.setVisibility(View.GONE);
			if (media.getTitle() == null) {
				setTitle(R.string.title_picture_add);
			}

			Picture picture = (Picture) media;
			imageViewMediaItem.setImageBitmap(picture.getBitmap());

			Log.d("title", picture.getTitle() + "");
			Log.d("description", picture.getDescription() + "");
			Log.d("fileName", picture.getFilename() + "");
			Log.d("groupid", picture.getGroupId() + "");

		} else if (media instanceof Video) {
			imageViewMediaItem.setVisibility(View.GONE);
			if (media.getTitle() == null) {
				setTitle(R.string.title_video_add);
			}

			Video video = (Video) media;
			videoViewMediaItem.setVideoPath(video.getPath());
			videoViewMediaItem.start();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_item, menu);

		actionSave = menu.findItem(R.id.action_save);
		actionEdit = menu.findItem(R.id.action_edit);

		if (getIntent().getBooleanExtra(FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false)) {
			showSave();
		} else {
			showEdit();
		}
		getIntent().removeExtra(FloatingActionButtonFragment.NIEUWE_MEDIA_STRING);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_edit :
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

			case R.id.action_save :
				if (changeInformationfragment.setNewInformation()) {
					application.DataSource.update(media);
				}
				getFragmentManager().popBackStack();
				showEdit();
				return true;

			case android.R.id.home :
				int backstackCount = getFragmentManager().getBackStackEntryCount();
				if (backstackCount > 0) {
					getFragmentManager().popBackStack();
				} else {
					finish();
				}
				showEdit();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void change() {
		showSave();
		getFragmentManager().beginTransaction().replace(R.id.fragmentPlaceholder, changeInformationfragment).addToBackStack("edit").commit();
	}

	private void showSave() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(true);
		actionEdit.setVisible(false);
	}

	private void showEdit() {
		if (actionSave == null || actionEdit == null)
			return;
		actionSave.setVisible(false);
		actionEdit.setVisible(true);
	}
}
