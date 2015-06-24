/**
 * 
 */
package lhrc.group3.tjooner.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * @author Chris
 *
 */
public class MediaAdapter extends BaseAdapter {

	private static int SCALE_SIZE = 8;

	private List<Media> mediaList;
	private LayoutInflater inflater;

	public MediaAdapter(List<Media> mediaList) {
		this.mediaList = mediaList;
	}

	@Override
	public int getCount() {
		return mediaList.size();
	}

	@Override
	public Object getItem(int position) {
		return mediaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			if (inflater == null) {
				inflater = LayoutInflater.from(parent.getContext());
			}
			convertView = inflater.inflate(R.layout.grid_item_media, parent,
					false);
		}

		ImageView imageViewMedia = (ImageView) convertView
				.findViewById(R.id.imageViewMediaItem);
		ImageView imageViewPlay = (ImageView) convertView
				.findViewById(R.id.imageViewPlay);
		imageViewMedia.setImageResource(R.drawable.ic_launcher);
		imageViewPlay.setVisibility(View.GONE);
		Media media = mediaList.get(position);
		if(media instanceof Picture){
			imageViewPlay.setVisibility(View.GONE);
		} else {
			imageViewPlay.setVisibility(View.VISIBLE);
		}
		ImageLoader loader = new ImageLoader(media, imageViewMedia, parent.getContext());
		loader.execute(convertView);
		

		return convertView;
	}

	private void scaleImage(Media media, Bitmap bitmapOriginal) {
		if (media.getSmallImage() != null || bitmapOriginal == null)
			return;
		Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal,
				bitmapOriginal.getWidth() / SCALE_SIZE,
				bitmapOriginal.getHeight() / SCALE_SIZE, true);
		bitmapOriginal.recycle();
		media.setSmallImage(bitmapsimplesize);

	}
	
	public class ImageLoader extends AsyncTask<Object, Double, Media>{
		private Media media;
		private ImageView imageViewMedia;
		private Context context;
		private View view;
		private Bitmap bitmap = null;
		public ImageLoader(Media media, ImageView imageViewMedia, Context context) {
			this.media = media;
			this.imageViewMedia = imageViewMedia;
			this.context = context;
		}

		@Override
		protected Media doInBackground(Object... params) {
			this.view = (View) params[0];
			if (media instanceof Picture) {
				
				//imageViewPlay.setVisibility(View.GONE);
				// imageViewMedia.setImageURI(media.getUri());

				// Bitmap bitmapOriginal =
				// BitmapFactory.decodeFile(media.getPath());
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 15;

				AssetFileDescriptor fileDescriptor = null;
				try {
					fileDescriptor = context.getContentResolver()
							.openAssetFileDescriptor(media.getUri(), "r");

					Bitmap bitmapOriginal = BitmapFactory.decodeFileDescriptor(
							fileDescriptor.getFileDescriptor(), null, options);
					this.bitmap = bitmapOriginal;
				//	scaleImage(media, bitmapOriginal);
					//imageViewMedia.setImageBitmap(bitmapOriginal);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (media instanceof Video) {
				scaleImage(media, media.getBitmap());
				//imageViewMedia.setImageBitmap(media.getSmallImage());
				//imageViewPlay.setVisibility(View.VISIBLE);
			}
			return media;
		}
		@Override
		protected void onPostExecute(Media result) {
			if(result instanceof Video){
				if(view != null && result.getSmallImage() != null){
					imageViewMedia.setImageBitmap(media.getSmallImage());
				}
			} else {
				if(view != null && bitmap != null){
					imageViewMedia.setImageBitmap(bitmap);
				}
				
			}
			super.onPostExecute(result);
		}
		
	}
}
