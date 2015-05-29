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

 
	public Picture(){
		
	}
	
	public Picture(Cursor cursor){
		super(cursor);
	}
	
	/**
	 * returns the media object as a bitmap picture.
	 * @return a bitmap of the picture
	 */
	public Bitmap getBitmap(){
		return BitmapFactory.decodeByteArray(super.getData() , 0, super.getData().length);
	}
	
}
