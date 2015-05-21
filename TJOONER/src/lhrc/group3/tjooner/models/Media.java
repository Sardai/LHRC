/**
 * 
 */
package lhrc.group3.tjooner.models;



import java.util.TreeSet;
import java.util.UUID;

import lhrc.group3.tjooner.helpers.Date;
import lhrc.group3.tjooner.storage.Storage;
import android.database.Cursor;

/**
 * @author Chris
 * Class which stores the values of a media object.
 */
public class Media extends DataObject {
	private UUID id;
	private String filename;
	private String title;
	private String description;
	private Date datetime;
	private boolean hasCopyright;
	private String copyrightHolder;
	private byte[] data;
	
	private UUID groupId;
	
	//Treeset because i want the tags to order alphabetically.	
	private TreeSet<String> tags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * Create a media object with data from the database.
	 * @param cursor the cursor of the media from the database.
	 */
	public Media(Cursor cursor){
		super(cursor);
		
		id = UUID.fromString(getString(Storage.ID));
	 	filename = getString(Storage.FILENAME); 
	 	title = getString(Storage.TITLE);
	 	description = getString(Storage.DESCRIPTION);
	 	datetime = getDate(Storage.DATETIME);
	 	hasCopyright = getBool(Storage.HAS_COPYRIGHT);
	 	copyrightHolder = getString(Storage.COPYRIGHT_HOLDER);
	 	data = getData(Storage.DATA);
	 	
	 	groupId = UUID.fromString(getString(Storage.GROUP_ID));
	}
	
	/**
	 * Create a new media object with a random id (UUID).
	 */
	public Media(){
		id = UUID.randomUUID();
	}

	
	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}
	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	/**
	 * @return if this object has copyright
	 */
	public boolean hasCopyright() {
		return hasCopyright;
	}
	/**
	 * @param sets if this media object has copyright or not
	 */
	public void setCopyright(boolean hasCopyright) {
		this.hasCopyright = hasCopyright;
	}
	/**
	 * @return the copyrightHolder
	 */
	public String getCopyrightHolder() {
		return copyrightHolder;
	}
	/**
	 * @param copyrightHolder the copyrightHolder to set
	 */
	public void setCopyrightHolder(String copyrightHolder) {
		this.copyrightHolder = copyrightHolder;
	}	
	
	/**
	 * @return the tags
	 */
	public TreeSet<String> getTags() {
		return tags;
	}
	
	/**
	 * add a tag to this media object.
	 * @param tag the tag
	 */
	public void addTag(String tag){
		tags.add(tag);
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the groupId
	 */
	public UUID getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}
	
	
}
