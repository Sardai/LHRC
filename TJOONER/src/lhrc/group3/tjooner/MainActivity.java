package lhrc.group3.tjooner;

import java.util.ArrayList;

import lhrc.group3.tjooner.adapter.GroupAdapter;
import lhrc.group3.tjooner.adapter.MediaAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.storage.Storage;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SearchView;
import android.widget.Spinner;
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
	private MenuItem cancelSearch;
	private MenuItem searchMenuItem;
	private SearchView search;

	private GridView gridView;
	private Spinner sortSpinner;
	private MediaAdapter mediaAdapter;
	private GroupAdapter groupAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = (TjoonerApplication) getApplication();

		gridView = (GridView) findViewById(R.id.gridViewGroups);
		sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
	
		String[] spinnerArray = {"Sorteren", "Title oplopend", "Title aflopend", "Datum oud-nieuw", "Datum nieuw-oud"};
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.sort_spinner_layout, spinnerArray);
		sortSpinner.setAdapter(spinnerAdapter);
		sortSpinner.setVisibility(Spinner.GONE);
		setSpinnerOnItemSelectedListener();
		setOngroupClickListener();

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
		cancelSearch = menu.findItem(R.id.cancelSearch);
		searchMenuItem = menu.findItem(R.id.searchInAllMedia);
		search = (SearchView) searchMenuItem.getActionView();
		   SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				String searchQuery = query.trim();
				if (searchQuery.isEmpty()) {
					groupAdapter = new GroupAdapter((ArrayList<Group>) application.DataSource.getGroups());
					gridView.setAdapter(groupAdapter);
					setOngroupClickListener();
					sortSpinner.setVisibility(Spinner.GONE);
					return false;
				}
	
				sortSpinner.setVisibility(Spinner.VISIBLE);
				sortSpinner.setSelection(0);
				mediaAdapter = new MediaAdapter(application.DataSource.search(searchQuery, null, null).getMediaList());
				gridView.setAdapter(mediaAdapter);
				setOnMediaClickListener();
				search.clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.isEmpty()) {
					sortSpinner.setVisibility(Spinner.GONE);
					groupAdapter = new GroupAdapter((ArrayList<Group>) application.DataSource.getGroups());
					gridView.setAdapter(groupAdapter);
					setOngroupClickListener();
				}
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		/*case android.R.id.home:
			finish();
			return true;*/
		case R.id.searchInAllMedia:
			cancelSearch.setVisible(true);
			return true;
		case R.id.cancelSearch:
			cancelSearch.setVisible(false);
			searchMenuItem.collapseActionView();
			groupAdapter = new GroupAdapter((ArrayList<Group>) application.DataSource.getGroups());
			gridView.setAdapter(groupAdapter);
			sortSpinner.setSelection(0);
			sortSpinner.setVisibility(Spinner.GONE);
			setOngroupClickListener();

		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setOngroupClickListener(){
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Group group = (Group) ((GroupAdapter) gridView.getAdapter())
						.getItem(position);
				Intent intent = new Intent(MainActivity.this,
						MediaGridActivity.class);
				intent.putExtra(Storage.GROUP_ID, group.getId().toString());
				startActivity(intent);
			}

		});
	}
	private void setOnMediaClickListener(){
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Media media = (Media) gridView.getAdapter().getItem(
						position);

				Intent intent = new Intent(MainActivity.this,
						MediaItemActivity.class);

				intent.putExtra(Storage.ID, media.getId().toString());
				startActivity(intent);
			}
		});
	}
	
	private void setSpinnerOnItemSelectedListener(){
		sortSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Group group = application.DataSource.search(search.getQuery().toString(), null, null);
				switch(position){
				case 0 : break;
				case 1 :
					group = application.DataSource.search(search.getQuery().toString(), Storage.TITLE, Storage.ASC); break;
				case 2 :
					group = application.DataSource.search(search.getQuery().toString(), Storage.TITLE, Storage.DESC); break;
				case 3 :
					group = application.DataSource.search(search.getQuery().toString(), Storage.DATETIME, Storage.ASC); break;
				case 4 :
					group = application.DataSource.search(search.getQuery().toString(), Storage.DATETIME, Storage.DESC); break;
					
				
				}
				mediaAdapter = new MediaAdapter(group.getMediaList());
				gridView.setAdapter(mediaAdapter);
				setOnMediaClickListener();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		GroupAdapter adapter = new GroupAdapter((ArrayList<Group>) application.DataSource.getGroups());
		gridView.setAdapter(adapter);
		setOngroupClickListener();
		super.onBackPressed();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	Log.d("zoeken", "ik zoek");
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        
	        search.setQuery(query+"", false);
	       
	    }
	}

	
}