package lhrc.group3.tjooner;

import lhrc.group3.tjooner.adapter.MediaAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.Storage;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;

public class MediaGridActivity extends Activity {

	private TjoonerApplication application;
	private MediaAdapter adapter;
	private GridView gridViewMedia;
	private String groupId;
	private Group group;
	private MenuItem cancelSearch;
	private MenuItem searchMenuItem;
	private SearchView search;
	private int selectedSortOption = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_grid);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		application = (TjoonerApplication) getApplication();
		groupId = getIntent().getExtras().getString(Storage.GROUP_ID);
		Log.d("groupid", groupId+"");
		group = application.DataSource.getGroup(groupId, null, null);
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
	/**
	 * sets the media
	 * @param currentSortPosition the position the sortSpinner is in.
	 */
	private void setMediaForListView(int currentSortPosition){
		if(searchMenuItem ==null){
			return;
		}
		Group group = application.DataSource.getGroup(groupId, null, null);
		switch(currentSortPosition){
		case 0 :
			if(searchMenuItem.isActionViewExpanded() && !search.getQuery().toString().isEmpty()){
				group = application.DataSource.searchInGroup(groupId, search.getQuery().toString(), null, null);
			}
			break;
		case 1 :
			if(searchMenuItem.isActionViewExpanded() && !search.getQuery().toString().isEmpty() ){
				group = application.DataSource.searchInGroup(groupId, search.getQuery().toString(), Storage.TITLE, Storage.ASC);
				break;
			}
			group = application.DataSource.getGroup(groupId, Storage.TITLE, Storage.ASC); break;
		case 2 :
			if(searchMenuItem.isActionViewExpanded() && !search.getQuery().toString().isEmpty()){
				group = application.DataSource.searchInGroup(groupId, search.getQuery().toString(), Storage.TITLE, Storage.DESC);
				break;
			}
			group = application.DataSource.getGroup(groupId, Storage.TITLE, Storage.DESC); break;
		case 3 : 
			if(searchMenuItem.isActionViewExpanded() && !search.getQuery().toString().isEmpty()){
				group = application.DataSource.searchInGroup(groupId, search.getQuery().toString(), Storage.DATETIME, Storage.ASC);
				break;
			}
			group = application.DataSource.getGroup(groupId, Storage.DATETIME, Storage.ASC); break;
		case 4 :
			if(searchMenuItem.isActionViewExpanded() && !search.getQuery().toString().isEmpty()){
				group = application.DataSource.searchInGroup(groupId, search.getQuery().toString(), Storage.DATETIME, Storage.DESC);
				break;
			}
			
			group = application.DataSource.getGroup(groupId, Storage.DATETIME, Storage.DESC); break;
		
		}
		adapter = new MediaAdapter(group.getMediaList());
		gridViewMedia.setAdapter(adapter); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_grid, menu);
		cancelSearch = menu.findItem(R.id.inGroupcancelSearch);
		searchMenuItem = menu.findItem(R.id.searchInGroup);
		search = (SearchView) searchMenuItem.getActionView();
		 SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				String searchQuery = query.trim();
				if (searchQuery.isEmpty()) {
					Group group = application.DataSource.getGroup(groupId, null, null);
					adapter = new MediaAdapter(group.getMediaList());
					gridViewMedia.setAdapter(adapter);
					return false;
				}

				Group searchGroup = application.DataSource.searchInGroup(groupId,
						searchQuery, null, null);
				Log.d("size van de searchquery", searchGroup.getMediaList()
						.size() + "");
				adapter = new MediaAdapter(searchGroup.getMediaList());
				gridViewMedia.setAdapter(adapter);
				search.clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.isEmpty()) {
					Group group = application.DataSource.getGroup(groupId, null, null);
					adapter = new MediaAdapter(group.getMediaList());
					gridViewMedia.setAdapter(adapter);
				}
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will.
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		case R.id.searchInGroup:
			cancelSearch.setVisible(true);
			selectedSortOption = 0;
			return true;
		case R.id.inGroupcancelSearch:
			cancelSearch.setVisible(false);
			selectedSortOption = 0;
			Group group = application.DataSource.getGroup(groupId, null, null);
			adapter = new MediaAdapter(group.getMediaList());
			gridViewMedia.setAdapter(adapter);
			searchMenuItem.collapseActionView();
		case R.id.menuItemUnsorted:
			selectedSortOption = 0;
			setMediaForListView(selectedSortOption);
			break;
		case R.id.menuItemSortByNameAscending:
			selectedSortOption = 1;
			setMediaForListView(selectedSortOption);
			break;
		case R.id.menuItemSortByNameDescending:
			selectedSortOption = 2;
			setMediaForListView(selectedSortOption);
			break;
		case R.id.menuItemSortByDateOldToNew:
			selectedSortOption = 3;
			setMediaForListView(selectedSortOption);
			break;
		case R.id.menuItemSortByDateNewToOld:
			selectedSortOption = 4;
			setMediaForListView(selectedSortOption);
			break;
			

		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(search != null && search.getQuery().toString().isEmpty()){
			
			Log.d("resume aangeroepen", "resume");
			setMediaForListView(selectedSortOption);
		}

	}
	
	@Override
	public void onBackPressed() {
		Group group = application.DataSource.getGroup(groupId, null, null);
		adapter = new MediaAdapter(group.getMediaList());
		gridViewMedia.setAdapter(adapter);
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
