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


	public Video(){
		
	}
	
	public Video(Cursor cursor){
		super(cursor);
	}
		
 }
