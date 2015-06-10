package lhrc.group3.tjooner;

import java.util.List;

import lhrc.group3.tjooner.adapter.MediaAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.Storage;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MediaGridActivity extends Activity {

	private TjoonerApplication application;
	private MediaAdapter adapter;
	private GridView gridViewMedia;
	private String groupId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_grid);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		application = (TjoonerApplication) getApplication();

		groupId = getIntent().getExtras().getString(Storage.GROUP_ID);

		Group group = application.DataSource.getGroup(groupId);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(group.getColor()));

		
		setTitle(group.getDescription());

		Log.i("MediaGridActivity", group.getMediaList().size() + "");
		gridViewMedia = (GridView) findViewById(R.id.gridViewMedia);
		adapter = new MediaAdapter(group.getMediaList());
		gridViewMedia.setAdapter(adapter);
		gridViewMedia.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Media media = (Media) gridViewMedia.getAdapter().getItem(
						position);

				Intent intent = new Intent(MediaGridActivity.this,
						MediaItemActivity.class);

				intent.putExtra(Storage.ID, media.getId().toString());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_grid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("resume aangeroepen","resume");
		Group group = application.DataSource.getGroup(groupId);
		adapter = new MediaAdapter(group.getMediaList());
		gridViewMedia.setAdapter(adapter);
		
	}
	
}
