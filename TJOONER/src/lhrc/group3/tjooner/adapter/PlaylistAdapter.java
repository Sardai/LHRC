/**
 * 
 */
package lhrc.group3.tjooner.adapter;

import java.util.ArrayList;
import java.util.List;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Media;

import com.terlici.dragndroplist.DragNDropAdapter;
import com.terlici.dragndroplist.DragNDropListView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @author Chris
 *	Adapter to show media in a listview for the creation of a playlist.
 */
public class PlaylistAdapter extends BaseAdapter implements DragNDropAdapter {

	private static final int HANDLER_ID = R.id.handler;
	private List<Media> list;
	private List<Media> selectedItems = new ArrayList<Media>();

	/**
	 * Creates a new playlist adapter with a medialist
	 * @param list the list with media items.
	 */
	public PlaylistAdapter(List<Media> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.list_item_handler, null, false);
		}

		final Media item = list.get(position);

		TextView textView = (TextView) convertView.findViewById(R.id.text);
		CheckBox checkBoxSelectedItem = (CheckBox) convertView
				.findViewById(R.id.checkBoxSelectedItem);

		textView.setText(item.getTitle());
		checkBoxSelectedItem
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						//if checkbox is checked add media item to the selected items list, else remove from selected items list.
						if (isChecked) {
							selectedItems.add(item);
						} else {
							selectedItems.remove(item);
						}						
					}
				});

		//if selected items list contains media item check the checkbox.
		checkBoxSelectedItem.setChecked(selectedItems.contains(item));

		return convertView;
	}
	
	

	@Override
	public void onItemDrag(DragNDropListView parent, View view, int position,
			long id) {

	}

	@Override
	public void onItemDrop(DragNDropListView parent, View view,
			int startPosition, int endPosition, long id) {
		//if media item is moved, change position in the list.
		Media temp = list.get(startPosition);
		list.remove(startPosition);
		list.add(endPosition, temp);

	}

	@Override
	public int getDragHandler() {
		//the id of the view which should begin the dragging.
		return HANDLER_ID;
	}

}
