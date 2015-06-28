/**
 * 
 */
package lhrc.group3.tjooner.adapter;

import java.util.ArrayList;
import java.util.List;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Group;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * Adapter for group selection.
 * @author Chris
 *
 */
public class GroupSelectionAdapter extends BaseAdapter {

	private List<Group> groups;
	private List<Group> selectedGroups = new ArrayList<Group>();
	/**
	 * instantiate a new group adapter.
	 * @param groups the TJOONER groups
	 */
	public GroupSelectionAdapter(List<Group> groups) {
		this.groups = groups;
	}
	
	/**
	 * get the selected groups.
	 * @return the selected groups
	 */
	public List<Group> getSelectedGroups(){
		return selectedGroups;
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_select_group	, null,false);
		}
		
		TextView textView = (TextView) convertView.findViewById(R.id.textViewGroupName);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxSelectedGroup);
		
		final Group group = groups.get(position);
		
		textView.setText(group.getDescription());
		//if selectedGroups contains the specified group than the checkbox gets selected.
		checkBox.setSelected(selectedGroups.contains(group));
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//if checkbox is selected add group to the selectedGroups list else remove it from the list.
				if(isChecked){
					selectedGroups.add(group);
				}
				else{
					selectedGroups.remove(groups.indexOf(group));
				}
				
			}
		});
		return convertView;
	}

}
