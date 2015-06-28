/**
 * 
 */
package lhrc.group3.tjooner;

import java.util.List;
import java.util.UUID;

import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Application class to store TJOONER groups and the datasource.
 * @author Chris
 *
 */
public class TjoonerApplication extends Application {

	 private List<Group> groups;
	 public DataSource DataSource;
	 
	@Override
	public void onCreate() {
		DataSource = new DataSource(this);
		DataSource.open();	 
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	/**
	 * Get group from list.
	 * @param groupId the id of the group
	 * @return the group
	 */
	public Group getGroup(UUID groupId){
		for (int i = 0; i < groups.size(); i++) {
			if(groups.get(i).getId().equals(groupId)){
				return groups.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Checks if a network is available.
	 * @return if a network is available.
	 */
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
