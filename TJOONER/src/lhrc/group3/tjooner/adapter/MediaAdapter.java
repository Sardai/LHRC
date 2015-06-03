/**
 * 
 */
package lhrc.group3.tjooner.adapter;

import java.util.List;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
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
		
		if(convertView == null){
			if(inflater == null)
			{
				inflater = LayoutInflater.from(parent.getContext());
			}
			convertView = inflater.inflate(R.layout.grid_item_media, null,false);
		}
		
		ImageView imageViewMedia = (ImageView) convertView.findViewById(R.id.imageViewMediaItem);
		
		Media media = mediaList.get(position);
		
		if(media instanceof Picture){
			imageViewMedia.setVisibility(View.VISIBLE);
			Picture picture = (Picture) media;
			imageViewMedia.setImageBitmap(picture.getBitmap());
		}else if(media instanceof Video){
			imageViewMedia.setVisibility(View.INVISIBLE);
			Video video = (Video) media;
		}
		
		return convertView;
	}

}
