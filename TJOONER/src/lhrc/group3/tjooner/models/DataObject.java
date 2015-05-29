/**
 * 
 */
package lhrc.group3.tjooner.models;

import lhrc.group3.tjooner.helpers.Date;
import android.database.Cursor;

/**
 * @author Chris Super class with a few helper methods to retrieve data from
 *         database
 */
public abstract class DataObject {

	Cursor cursor;

	/**
	 * Initializes empty dataobject.
	 */
	public DataObject() {

	}

	/**
	 * Initializes a dataobject with data from database.
	 * 
	 * @param cursor
	 *            the cursor from the database
	 */
	public DataObject(Cursor cursor) {
		this.cursor = cursor;
	}

	/**
	 * get an string from the cursor.
	 * 
	 * @param field
	 *            the name of the field
	 * @return the string from the cursor
	 */
	protected String getString(String field) {
		return cursor.getString(cursor.getColumnIndex(field));
	}

	/**
	 * Get an integer from the cursor.
	 * 
	 * @param field
	 *            the name of the field
	 * @return the integer from the cursor
	 */
	protected int getInt(String field) {
		return cursor.getInt(cursor.getColumnIndex(field));
	}

	/**
	 * Get an boolean from the cursor.
	 * 
	 * @param field
	 *            the name of the field
	 * @return the boolean from the cursor
	 */
	protected boolean getBool(String field) {
		return cursor.getInt(cursor.getColumnIndex(field)) > 1;
	}

	/**
	 * Get an date from the cursor.
	 * 
	 * @param field
	 *            the name of the field
	 * @return the date from the cursor
	 */
	protected Date getDate(String field) {
		int index = cursor.getColumnIndex(field);
		if (cursor.isNull(index)) {
			return null;
		}
		return new Date(cursor.getString(index));
	}

	/**
	 * Get data from the cursor.
	 * 
	 * @param field
	 *            the name of the field
	 * @return the data from the cursor
	 */
	protected byte[] getData(String field) {
		return cursor.getBlob(cursor.getColumnIndex(field));

	}
}
