/**
 * 
 */
package lhrc.group3.tjooner.models;

import android.database.Cursor;

/**
 * @author Chris
 *
 */
public class Picture extends Media {

 
	public Picture(){
		
	}
	
	public Picture(Cursor cursor){
		super(cursor);
	}
	
}
