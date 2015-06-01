/**
 * 
 */
package lhrc.group3.tjooner.models;

import lhrc.group3.tjooner.storage.Storage;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author Chris
 *
 */
public class Video extends Media {

	private String path;
	
	/**
	 * Initializes a empty video
	 */
	public Video(){
		
	}
	
	/**
	 * Initializes a video with data from the database.
	 * @param cursor the data from the database
	 */
	public Video(Cursor cursor){
		super(cursor);
		path = getString(Storage.PATH);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return Uri the uri of the video
	 */
	public Uri getUri(){
		return Uri.parse(path);
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
		
	
	
 }
