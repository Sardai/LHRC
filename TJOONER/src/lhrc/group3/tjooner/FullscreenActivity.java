package lhrc.group3.tjooner;

import java.io.File;

import lhrc.group3.tjooner.helpers.FileUtils;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class FullscreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		final VideoView videoViewMedia = (VideoView) findViewById(R.id.videoViewMedia);
		ImageView imageViewMedia = (ImageView) findViewById(R.id.imageViewMedia);

		if (getIntent().hasExtra(Storage.ID)) {
			TjoonerApplication app = (TjoonerApplication) getApplication();
			Media media = app.DataSource.getMedia(getIntent().getExtras()
					.getString(Storage.ID));

			if (media instanceof Picture) {
				imageViewMedia.setImageURI(media.getUri());
			} else if (media instanceof Video) {
				imageViewMedia.setVisibility(View.GONE);

				videoViewMedia.setOnPreparedListener(new OnPreparedListener() {
					
					@Override
					public void onPrepared(MediaPlayer mp) {
						videoViewMedia.start();
						
					}
				});
				File file = new File(FileUtils.getVideoPath(this, media.getUri()));
				videoViewMedia.setMediaController(new MediaController(this));
				videoViewMedia.setVideoURI(media.getUri());
				videoViewMedia.start();
			}
		}

	}

}
