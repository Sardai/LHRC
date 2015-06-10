/**
 * 
 */
package lhrc.group3.tjooner.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import lhrc.group3.tjooner.storage.Storage;

/**
 * @author Chris
 *	Helper class for date object with a few handy methods.
 */
public class Date extends java.util.Date {

	public static final String DATE_TIME_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS";
	
	/**
	 * Creates a new date object.
	 */
	public Date(){
		
	}
	
	/**
	 * Creates a dateobject from a string with the format YYYY-MM-DD HH:MM:SS.SSS.
	 * @param date the date string
	 */
	public Date(String date){
		DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH);
		try {
			setTime(format.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the date in a formatted the string.	
	 * @param format the format of the string in which the date is returned
	 * @return the string of the date in the specified format
	 */
	public String toString(String format){
		DateFormat df = new SimpleDateFormat(format,Locale.US);
		return df.format(this);
	}
		
}
