package lhrc.group3.tjooner;

import java.util.ArrayList;

import lhrc.group3.tjooner.adapter.GroupAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.storage.Storage;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * @author Chris Rötter, Luuk Wellink
 * @author Hugo van der Geest, Rene Bisperink
 * 
 * 
 */
public class MainActivity extends Activity {
	private Button ButtonCameraPhoto;
	private Button ButtonCameraVideo;
	private TjoonerApplication application;
	private ImageView newImage;
	private VideoView newVideo;
	private MediaController mediaControls;


	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = (TjoonerApplication) getApplication();

		gridView = (GridView) findViewById(R.id.gridViewGroups);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Group group = (Group) ((GroupAdapter) gridView.getAdapter())
						.getItem(position);
				Intent intent = new Intent(MainActivity.this,MediaGridActivity.class);
				intent.putExtra(Storage.GROUP_ID, group.getId().toString());
				startActivity(intent);
			}

		});

		gridView.setVerticalSpacing(15);
		gridView.setHorizontalSpacing(15);

		WebRequest webRequest = new WebRequest(application.DataSource);
		webRequest.setOnGroupRequestListener(new OnGroupRequestListener() {

			@Override
			public void Completed(ArrayList<Group> groups) {
				Log.i("WebRequest", "success");
				application.setGroups(groups);
				gridView.setAdapter(new GroupAdapter(groups));
			}
		});
		webRequest.getGroups();
	}

	/**
	 * OnActivityResult used for taking new pictures.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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