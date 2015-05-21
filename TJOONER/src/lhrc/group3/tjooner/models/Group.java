/**
 * 
 */
package lhrc.group3.tjooner.models;

import java.util.TreeMap;
import java.util.UUID;

import lhrc.group3.tjooner.storage.Storage;
import android.database.Cursor;

/**
 * @author Chris
 * Class which stores the values of a TJOONER group. 
 * An instance of a TJOONER group can only be created with an existing group from the database
 */
public class Group extends DataObject{

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