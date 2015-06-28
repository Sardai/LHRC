/**
 * 
 */
package lhrc.group3.tjooner.models;

import android.database.Cursor;

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
