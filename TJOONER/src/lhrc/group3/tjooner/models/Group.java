/**
 * 
 */
package lhrc.group3.tjooner.models;

import java.util.TreeMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import lhrc.group3.tjooner.storage.Storage;
import android.database.Cursor;

/**
 * @author Chris
 * Class which stores the values of a TJOONER group. 
 * An instance of a TJOONER group can only be created with an existing group from the database
 */
public class Group extends DataObject{

	private static String ID = "Id";
	private static String BACKGROUND_COLOR = "BackgroundColor"; 
	private static String DESCRIPTION = "Description";
	
	private UUID id;
	private String description;
	private String color;
	
	private TreeMap<UUID, Media> mediaList = new TreeMap<UUID, Media>();
	
	/**
	 * Create a group object with data from the database.
	 * @param cursor the cursor of the group from the database.
	 */
	public Group(Cursor cursor){
		super(cursor);
		
		description = getString(Storage.DESCRIPTION);
		color = getString(Storage.COLOR);		
	}
	
	/**
	 * Create a group object with data from a json string.
	 * @param json the json object with the values
	 * @throws JSONException if the json is malformed throws an jsonexception
	 */
	public Group(JSONObject json) throws JSONException{
		id = UUID.fromString(json.getString(ID));
		description = json.getString(DESCRIPTION);
		color = json.getString(BACKGROUND_COLOR);
	}
	
	/**
	 * Create a new group object with a random id (UUID).
	 */
	public Group(){
		id = UUID.randomUUID();
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return the media
	 */
	public TreeMap<UUID, Media> getMediaList() {
		return mediaList;
	}
	
	public void addMedia(Media media){
		mediaList.put(media.getId(), media);
	}
	
	
}
