package lhrc.group3.tjooner;

import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		VideoView videoViewMedia = (VideoView) findViewById(R.id.videoViewMedia);
		ImageView imageViewMedia = (ImageView) findViewById(R.id.imageViewMedia);
		if(getIntent().hasExtra(Storage.PATH)){
			imageViewMedia.setVisibility(View.GONE);	
			String path = getIntent().getExtras().getString(Storage.PATH);
			
			videoViewMedia.setVideoPath(path);
			videoViewMedia.start();
		
		}else if(getIntent().hasExtra(Storage.ID)){
			TjoonerApplication app = (TjoonerApplication)getApplication();
			videoViewMedia.setVisibility(View.GONE);
			Media media = app.DataSource.getMedia(getIntent().getExtras().getString(Storage.ID));
			imageViewMedia.setImageBitmap(media.getBitmap());
			
		}
		
	}
 
}
