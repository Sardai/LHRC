/**
 * 
 */
package lhrc.group3.tjooner.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
	
	/**
	 * Initialize a web request.
	 * @param dataSource the datasource to store data from the webrequest.
	 */
	public WebRequest(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	/**
	 * get groups from the online TJOONER enviroment.
	 */
	public void getGroups(){
		type = RequestType.GROUP;
		new WebTask().execute(GROUPS_URL);
	}
	
	/**
	 * enum for all request types.
	 * @author Chris
	 *
	 */
	private enum RequestType{
		GROUP
	}
	
	/**
	 * EventListener for group requests. 
	 * @author Chris
	 *
	 */
	public interface OnGroupRequestListener extends EventListener {
	        public void Completed(ArrayList<Group> groups);
    }

	private OnGroupRequestListener onGroupRequestListener;

	/**
	 * Set the ongrouprequest listener.
	 * @param onGroupRequestListener
	 */
    public void setOnGroupRequestListener(OnGroupRequestListener onGroupRequestListener){
        this.onGroupRequestListener = onGroupRequestListener;
    }
	
    /**
     * The webtask to get data from the online TJOONER enviroment
     * @author Chris
     *
     */
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
			
			switch (type) {
			case GROUP:
				
				ArrayList<Group> groups = new ArrayList<Group>();
				if(result == null){
					if(onGroupRequestListener != null){
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
					
					if(onGroupRequestListener != null){
						onGroupRequestListener.Completed(groups);
					}
					
				} catch (JSONException e) {
					 Log.e("WebRequest", e.getMessage());
				}
				
				break;

			default:
				break;
			}
		}
		
		
		
	}	
}
