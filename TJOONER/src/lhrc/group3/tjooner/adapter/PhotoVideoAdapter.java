package lhrc.group3.tjooner.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PhotoVideoAdapter extends BaseAdapter{

	
	/**
	 * ArrayList of photo/video objects to be displayed in this gridview
	 * 
	 */
	ArrayList<PhotoVideoAdapter> photovideoadapter;
	
	@Override
	public int getCount() {	
		return photovideoadapter.size();
	}

	@Override
	public Object getItem(int position) {	
		return photovideoadapter.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public long getItemId(int position) {
	return position;
	}

}
