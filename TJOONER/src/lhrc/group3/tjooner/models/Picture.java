/**
 * 
 */
package lhrc.group3.tjooner.models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Chris
 *
 */
public class Picture extends Media {

	/**
	 * Initializes a empty picture.
	 */
	public Picture(){
		
	}
	
	/**
	 * Initializes a picture from data from the database.
	 * @param cursor the data from the database
	 */
	public Picture(Cursor cursor){
		super(cursor);
	}
	
}
