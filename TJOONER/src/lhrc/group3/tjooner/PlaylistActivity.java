package lhrc.group3.tjooner;

import java.util.ArrayList;

import lhrc.group3.tjooner.adapter.PlaylistAdapter;
import lhrc.group3.tjooner.models.Media;

import com.terlici.dragndroplist.DragNDropListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PlaylistActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		
		DragNDropListView list = (DragNDropListView)findViewById(R.id.DragNDropListView);	
		
		TjoonerApplication application = (TjoonerApplication)getApplication();
		
		ArrayList<Media> media = new ArrayList<Media>();
		for (String groupId :(String[]) getIntent().getExtras().get(PlaylistDialogActivity.GROUPS)) {
			media.addAll(application.DataSource.getGroup(groupId).getMediaList());
		}
				
		PlaylistAdapter adapter = new PlaylistAdapter(media);
		list.setDragNDropAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playlist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
