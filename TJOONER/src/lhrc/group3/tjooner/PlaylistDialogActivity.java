package lhrc.group3.tjooner;

import java.util.ArrayList;
import java.util.List;

import lhrc.group3.tjooner.adapter.GroupSelectionAdapter;
import lhrc.group3.tjooner.models.Group;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
/**
 * Dialog activity to add a title and select groups for the creation of a playlist.
 * @author Chris
 *
 */
public class PlaylistDialogActivity extends Activity {

	public static final String GROUPS = "groups";
	public static final String TITLE = "title";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist_dialog);
		
		TjoonerApplication application = (TjoonerApplication)getApplication();
		
		final EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
		ListView listViewGroups = (ListView) findViewById(R.id.listViewGroups);
		
		final GroupSelectionAdapter adapter = new GroupSelectionAdapter(application.getGroups());
		listViewGroups.setAdapter(adapter);
		
		Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreate);
		
		
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		buttonCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//put all ids from the selected grops in an array and put them in an intent for PlaylistActivity.
				String[] groupIds = new String[adapter.getSelectedGroups().size()];
				int count = 0;
				for (Group group : adapter.getSelectedGroups()) {
					groupIds[count] = group.getId().toString();
					count++;
				}
				Intent intent = new Intent(PlaylistDialogActivity.this,PlaylistActivity.class);
				intent.putExtra(GROUPS,groupIds);
				intent.putExtra(TITLE, editTextTitle.getText().toString());
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playlist_dialog, menu);
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
