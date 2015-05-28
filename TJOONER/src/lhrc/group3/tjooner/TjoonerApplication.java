/**
 * 
 */
package lhrc.group3.tjooner;

import java.util.HashSet;
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
	 private HashSet<Group> groups;
	 private DataSource dataSource;

	@Override
	public void onCreate() {
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		WebRequest webRequest = new WebRequest(dataSource);
		webRequest.setOnGroupRequestListener(new OnGroupRequestListener() {
			
			@Override
			public void Completed(Map<UUID, Group> groups) {
			  Log.i("WebRequest", "success");
				
			}
		});
		webRequest.getGroups();
	}

}
