/**
 * 
 */
package lhrc.group3.tjooner;

import java.util.HashSet;

import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.web.WebRequest;
import android.app.Application;

/**
 * @author Chris
 *
 */
public class TjoonerApplication extends Application {
	
	private WebRequest webRequest;	
	private HashSet<Group> groups;
	private final DataSource dataSource;
	
	public TjoonerApplication(){
		dataSource = new DataSource(getApplicationContext());
		webRequest = new WebRequest(dataSource);
		webRequest.getGroups();
	}
	
}
