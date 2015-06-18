/**
 * 
 */
package lhrc.group3.tjooner.models;

import java.util.Date;
import java.util.TreeSet;
import java.util.UUID;

import lhrc.group3.tjooner.storage.Storage;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Chris Class which stores the values of a media object.
 */
public abstract class Media extends DataObject {
	private UUID id;
	private String filename;
	private String title;
	private String description;
	private Date datetime;
	private boolean hasCopyright;
	private String author;
	private byte[] data;
	private String longitude;
	private String latitude;

	private UUID groupId;

	// Treeset because i want the tags to order alphabetically.
	private TreeSet<String> tags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * Create a media object with data from the database.
	 * 
	 * @param cursor
	 *            the cursor of the media from the database.
	 */
	public Media(Cursor cursor) {
		super(cursor);

		id = UUID.fromString(getString(Storage.ID));
		filename = getString(Storage.FILENAME);
		title = getString(Storage.TITLE);
		description = getString(Storage.DESCRIPTION);
		datetime = getDate(Storage.DATETIME);
		hasCopyright = getBool(Storage.HAS_COPYRIGHT);
		author = getString(Storage.AUTHOR);
		data = getData(Storage.DATA);
		latitude = getString(Storage.LATITUDE);
		longitude = getString(Storage.LONGITUDE);
		
		String stringId = getString(Storage.GROUP_ID);
		if (stringId != null) {
			groupId = UUID.fromString(stringId);
		}
	}

	/**
	 * Create a new media object with a random id (UUID).
	 */
	public Media() {
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
	 * @param filename
	 *            the filename to set
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
	 * @param title
	 *            the title to set
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
	 * @param description
	 *            the description to set
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
	 * @param datetime
	 *            the datetime to set
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
	 * @param sets
	 *            if this media object has copyright or not
	 */
	public void setCopyright(boolean hasCopyright) {
		this.hasCopyright = hasCopyright;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the tags
	 */
	public TreeSet<String> getTags() {
		return tags;
	}

	/**
	 * add a tag to this media object.
	 * 
	 * @param tag
	 *            the tag
	 */
	public void addTag(String tag) {
		tags.add(tag);
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
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
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(UUID groupId) {
		this.groupId = groupId;
	}

	/**
	 * Add tags to this media object
	 * 
	 * @param tags
	 *            the tags to add
	 */
	public void addTags(String[] tags) {
		for (String tag : tags) {
			if (tag != null && !tag.trim().isEmpty()) {
				addTag(tag.toLowerCase().trim());
			}
		}
	}

	public String getTagsString() {
		String allTags = "";
		if (getTags().size() > 0) {
			for (String tag : getTags()) {
				allTags += String.format("%s ,", tag);
			}
			// removes the comma at the end.
			allTags = allTags.substring(0, allTags.length() - 2);
		}
		return allTags;
	}
	
	/**
	 * returns the media object as a bitmap picture.
	 * @return a bitmap of the picture
	 */
	public Bitmap getBitmap(){
		return BitmapFactory.decodeByteArray(data , 0, data.length);
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
