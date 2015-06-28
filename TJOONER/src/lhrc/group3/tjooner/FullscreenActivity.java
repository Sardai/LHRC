package lhrc.group3.tjooner;

import java.io.FileNotFoundException;

import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Activity to show media fullscreen.
 * @author Chris
 *
 */
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
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;

				AssetFileDescriptor fileDescriptor = null;
				try {
					fileDescriptor = getBaseContext().getContentResolver()
							.openAssetFileDescriptor(media.getUri(), "r");

					Bitmap bitmapOriginal = BitmapFactory.decodeFileDescriptor(
							fileDescriptor.getFileDescriptor(), null, options);
				//	scaleImage(media, bitmapOriginal);
					imageViewMedia.setImageBitmap(bitmapOriginal);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//imageViewMedia.setImageURI(media.getUri());
			} else if (media instanceof Video) {
				imageViewMedia.setVisibility(View.GONE);

				videoViewMedia.setOnPreparedListener(new OnPreparedListener() {
					
					@Override
					public void onPrepared(MediaPlayer mp) {
						videoViewMedia.start();
						
					}
				});
//				File file = new File(FileUtils.getVideoPath(this, media.getUri()));
				videoViewMedia.setMediaController(new MediaController(this));
				videoViewMedia.setVideoURI(media.getUri());
				videoViewMedia.start();
			}
		}

	}

}
