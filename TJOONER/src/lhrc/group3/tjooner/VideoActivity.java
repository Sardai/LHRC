package lhrc.group3.tjooner;

import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		if(!getIntent().hasExtra(Storage.PATH)){
			return;
		}
		
		String path = getIntent().getExtras().getString(Storage.PATH);
		
		VideoView videoViewMedia = (VideoView) findViewById(R.id.videoViewMedia);
		videoViewMedia.setVideoPath(path);
		videoViewMedia.start();
		
		
		
	}
 
}
