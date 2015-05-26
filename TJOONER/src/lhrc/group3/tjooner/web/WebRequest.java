/**
 * 
 */
package lhrc.group3.tjooner.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Chris
 *
 */
public class WebRequest {
	
	private static final String GROUPS_URL = "http://setup.tjooner.tv/JCC/Saxion/TJOONER/REST/api/Category";
	private RequestType type;
	
	private DataSource dataSource;
	
	public WebRequest(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void getGroups(){
		type = RequestType.GROUP;
		new WebTask().execute(GROUPS_URL);
	}
	
	private enum RequestType{
		GROUP
	}
	
	private class WebTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();	
			HttpGet httpGet = new HttpGet(params[0]);	
				
			ResponseHandler<String> handler
			= new BasicResponseHandler();	

			String result = null;
			try {
				result = client.execute(httpGet, handler);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (type) {
			case GROUP:
				
				List<Group> groups = new ArrayList<Group>();
				
				try {
					JSONArray jsonGroups = new JSONArray(result);
					
					for (int i = 0; i < jsonGroups.length(); i++) {
						Group group = new Group(jsonGroups.getJSONObject(i));
						groups.add(group);
						dataSource.insert(group);
					}
					
				} catch (JSONException e) {
					 
				}
				
				break;

			default:
				break;
			}
		}
		
		
		
	}	
}
