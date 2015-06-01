/**
 * 
 */
package lhrc.group3.tjooner.adapter;

import java.util.List;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Group;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Chris
 * Adapter for the group spinner
 */
public class GroupSpinnerAdapter extends BaseAdapter {

	private List<Group> groups;
	private LayoutInflater inflater;
	
	public GroupSpinnerAdapter(List<Group> groups) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			if(inflater == null){
				inflater = LayoutInflater.from(parent.getContext());
			}
			convertView = inflater.inflate(R.layout.spinner_item_group, null,false);
		}
		
		Group group = groups.get(position);
		
		TextView textViewGroupName = (TextView)convertView.findViewById(R.id.textViewGroupName);
		
		textViewGroupName.setText(group.getDescription());
		textViewGroupName.setBackground(new ColorDrawable(Color.parseColor(group.getColor())));
		
		return convertView;
	}

}
