package lhrc.group3.tjooner.adapter;

import java.util.List;
import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Group;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter for TJOONER groups.
 * @author Chris
 *
 */
public class GroupAdapter extends BaseAdapter {

	
	private List<Group> groups;	
	private static LayoutInflater inflater = null;
	
	public GroupAdapter(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if(convertView == null){
		
			if(inflater == null){
				inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
		
			convertView = inflater.inflate(R.layout.grid_item_group, null);
			//set height of view to the width of the view
			//convertView.setLayoutParams(new LayoutParams(convertView.getWidth(),convertView.getWidth()));
		}
		
		Group group = groups.get(position);
		
		TextView textViewGroup = (TextView) convertView.findViewById(R.id.textViewGroup);		

		textViewGroup.setText(group.getDescription());
		textViewGroup.setBackground(new ColorDrawable(group.getColor()));
		
		return convertView;
	}

	 
	
}