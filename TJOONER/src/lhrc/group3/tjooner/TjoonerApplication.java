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
	public DataSource dataSource;
	
	public TjoonerApplication(){
		dataSource = new DataSource(this);
		webRequest = new WebRequest(dataSource);
	//s	webRequest.getGroups();
	}
	
}
