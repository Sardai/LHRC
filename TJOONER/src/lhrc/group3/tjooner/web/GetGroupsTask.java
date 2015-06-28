/**
 * 
 */
package lhrc.group3.tjooner.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Chris Asynctask to get groups from the online TJOONER environment.
 */
public class GetGroupsTask extends AsyncTask<Void, Void, String> {

	private static final String GROUPS_URL = "http://setup.tjooner.tv/JCC/Saxion/TJOONER/REST/api/Category";

	/**
	 * Initialize a web request.
	 * 
	 * @param dataSource
	 *            the data source to store the groups.
	 */
	public GetGroupsTask(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private DataSource dataSource;

	@Override
	protected String doInBackground(Void... params) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(GROUPS_URL);

		ResponseHandler<String> handler = new BasicResponseHandler();

		String result = null;
		try {
			result = client.execute(httpGet, handler);
		} catch (ClientProtocolException e) {
			Log.e("WebRequest", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("WebRequest", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {

		ArrayList<Group> groups = new ArrayList<Group>();
		if (result == null) {
			if (onGroupRequestListener != null) {
				onGroupRequestListener.Completed(groups);
			}
			return;
		}

		try {
			JSONArray jsonGroups = new JSONArray(result);

			for (int i = 0; i < jsonGroups.length(); i++) {
				Group group = new Group(jsonGroups.getJSONObject(i));
				groups.add(group);
			}

			dataSource.upsert(groups);

			if (onGroupRequestListener != null) {
				onGroupRequestListener.Completed(groups);
			}

		} catch (JSONException e) {
			Log.e("WebRequest", e.getMessage());
		}

	}
	

	/**
	 * EventListener for group requests.
	 * 
	 * @author Chris
	 *
	 */
	public interface OnGroupRequestListener extends EventListener {
		public void Completed(ArrayList<Group> groups);
	}

	private OnGroupRequestListener onGroupRequestListener;

	/**
	 * Set the ongrouprequest listener.
	 * 
	 * @param onGroupRequestListener
	 */
	public void setOnGroupRequestListener(
			OnGroupRequestListener onGroupRequestListener) {
		this.onGroupRequestListener = onGroupRequestListener;
	}
}
