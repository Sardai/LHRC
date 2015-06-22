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

public class FullscreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		VideoView videoViewMedia = (VideoView) findViewById(R.id.videoViewMedia);
		ImageView imageViewMedia = (ImageView) findViewById(R.id.imageViewMedia);

		if (getIntent().hasExtra(Storage.ID)) {
			TjoonerApplication app = (TjoonerApplication) getApplication();
			videoViewMedia.setVisibility(View.GONE);
			Media media = app.DataSource.getMedia(getIntent().getExtras()
					.getString(Storage.ID));

			if (media instanceof Picture) {
				imageViewMedia.setImageURI(media.getUri());
			} else if (media instanceof Video) {
				imageViewMedia.setVisibility(View.GONE);

				videoViewMedia.setVideoPath(media.getPath());
				videoViewMedia.start();
			}
		}

	}

}
