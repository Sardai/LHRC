/**
 * 
 */
package lhrc.group3.tjooner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;
import android.app.Application;
import android.util.Log;

/**
 * @author Chris
 *
 */
public class TjoonerApplication extends Application {

	 private WebRequest webRequest;
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
	
	public Group getGroup(UUID groupId){
		for (int i = 0; i < groups.size(); i++) {
			if(groups.get(i).getId().equals(groupId)){
				return groups.get(i);
			}
		}
		return null;
	}
	
 

}
