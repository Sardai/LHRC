package lhrc.group3.tjooner;

import java.util.ArrayList;
import java.util.List;

import lhrc.group3.tjooner.adapter.GroupAdapter;
import lhrc.group3.tjooner.adapter.MediaAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.storage.Storage;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
	private MenuItem sortMenuItem;
	private SearchView search;

	private GridView gridView;
	private MediaAdapter mediaAdapter;
	private GroupAdapter groupAdapter;
	private int selectedSortOption = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = (TjoonerApplication) getApplication();

		gridView = (GridView) findViewById(R.id.gridViewGroups);
		setOngroupClickListener();

		gridView.setVerticalSpacing(15);
		gridView.setHorizontalSpacing(15);

		if (application.isNetworkAvailable()) {
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
		else{
			List<Group> groups = application.DataSource.getGroups();
			application.setGroups(groups);
			gridView.setAdapter(new GroupAdapter(groups));
		}
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
		sortMenuItem = menu.findItem(R.id.menuSorItem);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		search.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				String searchQuery = query.trim();
				if (searchQuery.isEmpty()) {
					groupAdapter = new GroupAdapter(
							(ArrayList<Group>) application.DataSource
									.getGroups());
					gridView.setAdapter(groupAdapter);
					setOngroupClickListener();
					sortMenuItem.setVisible(false);
					return false;
				}

				sortMenuItem.setVisible(true);
				selectedSortOption = 0;
				mediaAdapter = new MediaAdapter(application.DataSource.search(
						searchQuery, null, null).getMediaList());
				gridView.setAdapter(mediaAdapter);
				setOnMediaClickListener();
				search.clearFocus();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.isEmpty()) {
					sortMenuItem.setVisible(false);
					groupAdapter = new GroupAdapter(
							(ArrayList<Group>) application.DataSource
									.getGroups());
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
		/*
		 * case android.R.id.home: finish(); return true;
		 */
		case R.id.searchInAllMedia:
			cancelSearch.setVisible(true);
			sortMenuItem.setVisible(true);
			return true;
		case R.id.cancelSearch:
			cancelSearch.setVisible(false);
			sortMenuItem.setVisible(false);
			searchMenuItem.collapseActionView();
			groupAdapter = new GroupAdapter(
					(ArrayList<Group>) application.DataSource.getGroups());
			gridView.setAdapter(groupAdapter);
			selectedSortOption = 0;
			sortMenuItem.setVisible(false);
			setOngroupClickListener();
			return true;
		case R.id.action_playlist:
			Intent intent = new Intent(this, PlaylistDialogActivity.class);
			startActivity(intent);
			return true;

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

	private void setOngroupClickListener() {
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

	private void setOnMediaClickListener() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Media media = (Media) gridView.getAdapter().getItem(position);

				Intent intent = new Intent(MainActivity.this,
						MediaItemActivity.class);

				intent.putExtra(Storage.ID, media.getId().toString());
				startActivity(intent);
			}
		});
	}

	private void setMediaForListView(int position) {
		Group group = null;
		if (!search.getQuery().toString().isEmpty()) {
			group = application.DataSource.search(search.getQuery().toString(),
					null, null);
		} else {
			return;
		}
		switch (position) {
		case 0:
			break;
		case 1:
			if (!search.getQuery().toString().isEmpty()) {
				group = application.DataSource.search(search.getQuery()
						.toString(), Storage.TITLE, Storage.ASC);
			}
			break;
		case 2:
			if (!search.getQuery().toString().isEmpty()) {
				group = application.DataSource.search(search.getQuery()
						.toString(), Storage.TITLE, Storage.DESC);
			}
			break;
		case 3:
			if (!search.getQuery().toString().isEmpty()) {
				group = application.DataSource.search(search.getQuery()
						.toString(), Storage.DATETIME, Storage.ASC);
			}
			break;
		case 4:
			if (!search.getQuery().toString().isEmpty()) {
				group = application.DataSource.search(search.getQuery()
						.toString(), Storage.DATETIME, Storage.DESC);
			}
			break;

		}
		mediaAdapter = new MediaAdapter(group.getMediaList());
		gridView.setAdapter(mediaAdapter);
		setOnMediaClickListener();

	}

	@Override
	public void onBackPressed() {
		GroupAdapter adapter = new GroupAdapter(
				(ArrayList<Group>) application.DataSource.getGroups());
		gridView.setAdapter(adapter);
		setOngroupClickListener();
		super.onBackPressed();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			Log.d("zoeken", "ik zoek");
			String query = intent.getStringExtra(SearchManager.QUERY);

			search.setQuery(query + "", false);

		}
	}

}