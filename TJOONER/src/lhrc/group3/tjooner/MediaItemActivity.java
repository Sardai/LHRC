package lhrc.group3.tjooner;

import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class MediaItemActivity extends Activity {

	private TjoonerApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_item);
		
		application = (TjoonerApplication)getApplication();
		
		String id = getIntent().getExtras().getString(Storage.ID);
		Media media = application.DataSource.getMedia(id);
		
		ImageView imageViewMediaItem  = (ImageView) findViewById(R.id.imageViewMediaItem);
		VideoView videoViewMediaItem = (VideoView) findViewById(R.id.videoViewMediaItem);
		
		if(media.getTitle() != null){
			setTitle(media.getTitle());
		}
		
		if(media instanceof Picture){
			videoViewMediaItem.setVisibility(View.GONE);
			if(media.getTitle() == null){
				setTitle(R.string.title_picture_add);
			}
			
			Picture picture = (Picture) media;
			imageViewMediaItem.setImageBitmap(picture.getBitmap());
		}
		else if(media instanceof Video){
			imageViewMediaItem.setVisibility(View.GONE);
			if(media.getTitle() == null){
				setTitle(R.string.title_video_add);
			}
			
			Video video = (Video)media;
			videoViewMediaItem.setVideoPath(video.getPath());
			videoViewMediaItem.start();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
