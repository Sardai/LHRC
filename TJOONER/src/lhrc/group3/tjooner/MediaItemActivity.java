package lhrc.group3.tjooner;

import java.util.HashMap;

import lhrc.group3.tjooner.fragments.ChangeInformationFragment;
import lhrc.group3.tjooner.fragments.FloatingActionButtonFragment;
import lhrc.group3.tjooner.fragments.InformationFragment;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MediaItemActivity extends Activity {

	private Media media;
	private boolean wijzigMedia;
	private ChangeInformationFragment changeInformationfragment;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	
	private TjoonerApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_item);
		
		application = (TjoonerApplication)getApplication();
		
		String id = getIntent().getExtras().getString(Storage.ID);
		media = application.DataSource.getMedia(id);
		
		ImageView imageViewMediaItem  = (ImageView) findViewById(R.id.imageViewMediaItem);
		VideoView videoViewMediaItem = (VideoView) findViewById(R.id.videoViewMediaItem);
		
		
		Intent intent = getIntent();
		wijzigMedia = intent.getBooleanExtra(
				FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false);
		
		manager = getFragmentManager();
		transaction = manager.beginTransaction();
		changeInformationfragment = new ChangeInformationFragment(media);
		InformationFragment fragment = new InformationFragment(media);
		if(wijzigMedia){
			transaction.replace(R.id.fragmentPlaceholder, changeInformationfragment);
		}
		else {
			
			transaction.replace(R.id.fragmentPlaceholder, fragment);
		}
		
		transaction.commit();
		
		
		
		
		if(media.getTitle() != null){
			setTitle(media.getTitle());
		}
		
		if(media instanceof Picture){
			videoViewMediaItem.setVisibility(View.INVISIBLE);
			if(media.getTitle() == null){
				setTitle(R.string.title_picture_add);
			}
			
			Picture picture = (Picture) media;
			imageViewMediaItem.setImageBitmap(picture.getBitmap());
			
			Log.d("title",	picture.getTitle()+"");
			Log.d("description", picture.getDescription()+"");
			Log.d("fileName", picture.getFilename()+"");
			Log.d("groupid", picture.getGroupId()+"");
		
		}
		else if(media instanceof Video){
			imageViewMediaItem.setVisibility(View.INVISIBLE);
			if(media.getTitle() == null){
				setTitle(R.string.title_video_add);
			}
			
			Video video = (Video)media;
		}
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.media_item, menu);
		if(getIntent().getBooleanExtra(FloatingActionButtonFragment.NIEUWE_MEDIA_STRING, false)){
		menu.findItem(R.id.change_save_media_button).setTitle("Save!");
		}
		getIntent().removeExtra(FloatingActionButtonFragment.NIEUWE_MEDIA_STRING);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.change_save_media_button) {
			if(wijzigMedia){
				if(changeInformationfragment.setNewInformation()){
					application.DataSource.update(media);
					transaction = manager.beginTransaction();
					InformationFragment fragment = new InformationFragment(media);
					transaction.replace(R.id.fragmentPlaceholder, fragment);
					transaction.commit();
					item.setTitle("Modify");
					wijzigMedia = false;
					
				}
			}
			else {
				changeInformationfragment = new ChangeInformationFragment(media);
				transaction = manager.beginTransaction();
				transaction.replace(R.id.fragmentPlaceholder, changeInformationfragment);
				transaction.commit();
				item.setTitle("Save!");
				wijzigMedia = true;
			}
			
		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
