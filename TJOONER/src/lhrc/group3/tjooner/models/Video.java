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
	}

	
		
	
	
 }
