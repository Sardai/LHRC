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
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
			convertView = inflater.inflate(R.layout.grid_item_media, null,
					false);
		}

		ImageView imageViewMedia = (ImageView) convertView
				.findViewById(R.id.imageViewMediaItem);
		ImageView imageViewPlay = (ImageView) convertView
				.findViewById(R.id.imageViewPlay);
		Media media = mediaList.get(position);

		if (media instanceof Picture) {
			imageViewPlay.setVisibility(View.GONE);
			// imageViewMedia.setImageURI(media.getUri());

			// Bitmap bitmapOriginal =
			// BitmapFactory.decodeFile(media.getPath());
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;

			AssetFileDescriptor fileDescriptor = null;
			try {			
				fileDescriptor = parent.getContext().getContentResolver()
						.openAssetFileDescriptor(media.getUri(), "r");

				Bitmap bitmapOriginal = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
			//	scaleImage(media, bitmapOriginal);
				imageViewMedia.setImageBitmap(bitmapOriginal);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (media instanceof Video) {
			scaleImage(media, media.getBitmap());
			imageViewMedia.setImageBitmap(media.getSmallImage());
			imageViewPlay.setVisibility(View.VISIBLE);
		}

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
}
