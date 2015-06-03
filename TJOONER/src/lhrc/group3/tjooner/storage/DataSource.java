package lhrc.group3.tjooner.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lhrc.group3.tjooner.helpers.DateUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class to insert, update ,remove and get object from the database.
 * 
 * @author Chris
 *
 */
public class DataSource {
	private static final String WHERE = String.format("%s = ?", Storage.ID);

	private SQLiteDatabase database;
	private Storage storage;

	/**
	 * Initialize a datasource.
	 * 
	 * @param context
	 */
	public DataSource(Context context) {
		storage = new Storage(context);
	}

	/**
	 * Opens database
	 */
	public void open() {
		database = storage.getWritableDatabase();
	}

	/**
	 * closes database
	 */
	public void close() {
		storage.close();
	}

	/**
	 * Insert a media object in the database.
	 * 
	 * @param media
	 *            the media object to insert
	 */
	public void insert(Media media) {
		ContentValues values = getContentValues(media);
		values.put(Storage.ID, media.getId().toString());
		int datatype = 0;
		if (media instanceof Video) {
			Video video = (Video) media;
			datatype = Storage.DATA_TYPE_VIDEO;
			values.put(Storage.PATH, video.getPath());
		} else if (media instanceof Picture) {
			datatype = Storage.DATA_TYPE_PICTURE;
		}
		values.put(Storage.DATA_TYPE, datatype);
		values.put(Storage.DATA, media.getData());
		database.insert(Storage.MEDIA_TABLE_NAME, null, values);
		Log.i("storage", "media item saved");
	}

	/**
	 * Insert a group in the database.
	 * 
	 * @param group
	 *            the group to insert
	 */
	public void insert(Group group) {
		ContentValues values = new ContentValues();
		values.put(Storage.ID, group.getId().toString());
		values.put(Storage.DESCRIPTION, group.getDescription());
		values.put(Storage.COLOR, group.getColor());
		database.insert(Storage.GROUP_TABLE_NAME, null, values);
	}

	/**
	 * updates or insert a set of groups in the database. when a group is not
	 * removed than it is marked inactive.
	 * 
	 * @param groups
	 *            the set of groups to insert.
	 */
	public void upsert(ArrayList<Group> groups) {

		ContentValues values = new ContentValues();
		values.put(Storage.INACTIVE, 1);
		database.update(Storage.GROUP_TABLE_NAME, values, null, null);

		String where = String.format("%s = ?", Storage.ID);

		for (Group group : groups) {

			String id = group.getId().toString();
			Cursor cursor = database.query(Storage.GROUP_TABLE_NAME, new String[]{Storage.ID}, where, new String[]{id}, null, null, null);
			if (cursor.getCount() == 0) {
				insert(group);
			} else {
				update(group);
			}

		}

	}

	/**
	 * Insert a tag in the database.
	 * 
	 * @param tag
	 *            the tag to insert
	 */
	public void insert(String tag) {
		String where = String.format("%s = '%s'", Storage.WORD, tag);
		Cursor cursor = database.query(Storage.TAG_TABLE_NAME, new String[]{Storage.WORD}, where, null, null, null, null);
		if (cursor.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put(Storage.WORD, tag);
			database.insert(Storage.TAG_TABLE_NAME, null, values);
		}
	}
	
	/**
	 * add a tag to a media object in the database.
	 * 
	 * @param mediaId the id of the media object
	 * @param tag
	 *            the tag to insert
	 */
	public void insert(UUID mediaId,String tag){
		insert(tag);
		ContentValues values = new ContentValues();
		values.put(Storage.MEDIA_ID,mediaId.toString());
		values.put(Storage.WORD,tag);
		database.insert(Storage.MEDIA_TAG_TABLE_NAME, null, values);
	}

	/**
	 * Insert an array of tags in the database.
	 * 
	 * @param tags
	 *            the tags to insert
	 */
	public void insert(String[] tags) {
		for (String tag : tags) {
			insert(tag);
		}
	}
	
 

	/**
	 * update an existing media object in the database.
	 * 
	 * @param media
	 *            the media object to update
	 */
	public void update(Media media) {
		String[] args = {media.getId().toString()};
		database.update(Storage.MEDIA_TABLE_NAME, getContentValues(media), WHERE, args);
		
		String where = String.format("%s = '%s'",Storage.MEDIA_ID,media.getId());
		
		//remove all current tags of this media object.
		database.delete(Storage.MEDIA_TAG_TABLE_NAME, where, null);		
		for (String tag : media.getTags()) {
			insert(media.getId(),tag);			
		}
		
		
	}
	/**
	 * update an existing group object in the database.
	 * 
	 * @param group
	 *            the group object to update
	 */
	public void update(Group group) {
		String[] args = {group.getId().toString()};
		database.update(Storage.GROUP_TABLE_NAME, getContentValues(group), WHERE, args);
	}

	/**
	 * Remove a media object from the database.
	 * 
	 * @param media
	 *            the media object to remove
	 */
	public void remove(Media media) {
		String[] args = {media.getId().toString()};
		database.delete(Storage.MEDIA_TABLE_NAME, WHERE, args);
	}

	/**
	 * Get an media object from it's id.
	 * 
	 * @param id
	 *            the id of the media object
	 * @return the media object
	 */
	public Media getMedia(String id) {
		String where = String.format("%s = ?", Storage.ID);
		String[] args = {id};
		Cursor cursor = database.query(Storage.MEDIA_TABLE_NAME, Storage.MEDIA_COLUMNS, where, args, null, null, null);

		if (cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();

		return getMedia(cursor);
	}

	/**
	 * Get an media object from it's id.
	 * 
	 * @param id
	 *            the id of the media object
	 * @return the media object
	 */
	public Media getMedia(UUID id) {
		return getMedia(id.toString());
	}

	public List<Group> getGroups() {
		Cursor cursor = database.query(Storage.GROUP_TABLE_NAME, Storage.GROUP_COLUMNS, null, null, null, null, null);

		List<Group> groups = new ArrayList<Group>();

		if (cursor.getCount() == 0) {
			return groups;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Group group = new Group(cursor);
			groups.add(group);
			cursor.moveToNext();
		}
		return groups;
	}

	public Group getGroup(String groupId) {

		String groupWhere = String.format("%s = '%s'", Storage.ID, groupId);
		Cursor cursorGroup = database.query(Storage.GROUP_TABLE_NAME, Storage.GROUP_COLUMNS, groupWhere, null, null, null, null);

		if (cursorGroup.getCount() == 0) {
			return null;
		}
		cursorGroup.moveToFirst();
		Group group = new Group(cursorGroup);

		String where = String.format("%s = '%s'", Storage.GROUP_ID, groupId);
		Cursor cursor = database.query(Storage.MEDIA_TABLE_NAME, Storage.MEDIA_COLUMNS, where, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			Media media = getMedia(cursor);

			String[] columns = {Storage.WORD};
			where = String.format("%s = '%s'", Storage.MEDIA_ID, media.getId().toString());
			Cursor cursorTag = database.query(Storage.MEDIA_TAG_TABLE_NAME, columns, where, null, null, null, null);

			if (cursorTag.getCount() > 0) {
				cursorTag.moveToFirst();
				while (!cursorTag.isAfterLast()) {
					media.addTag(cursor.getString(0));
					cursorTag.moveToNext();
				}
			}
			cursorTag.close();
			group.addMedia(media);
			cursor.moveToNext();
		}
		return group;
	}

	/**
	 * get all groups with media object and tags from the database
	 * 
	 * @return an map with all groups and media object and tags
	 */
	public HashMap<UUID, Group> getAll() {
		Cursor cursor = database.query(Storage.GROUP_TABLE_NAME, Storage.GROUP_COLUMNS, null, null, null, null, null);

		HashMap<UUID, Group> groups = new HashMap<UUID, Group>();

		if (cursor.getCount() == 0) {
			return groups;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Group group = new Group(cursor);
			groups.put(group.getId(), group);
		}

		cursor = database.query(Storage.MEDIA_TABLE_NAME, Storage.MEDIA_COLUMNS, null, null, null, null, null);

		if (cursor.getCount() == 0) {
			return groups;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			Media media = getMedia(cursor);

			String[] columns = {Storage.WORD};
			String where = String.format("%s = %s", Storage.MEDIA_ID, media.getId().toString());
			Cursor cursorTag = database.query(Storage.MEDIA_TAG_TABLE_NAME, columns, where, null, null, null, null);

			if (cursorTag.getCount() > 0) {
				cursorTag.moveToFirst();
				while (!cursorTag.isAfterLast()) {
					media.addTag(cursor.getString(0));
				}
			}

			groups.get(media.getGroupId()).addMedia(media);

		}

		return groups;
	}

	/**
	 * get all tags from the database.
	 * 
	 * @return all tags
	 */
	public List<String> getTags() {
		String[] columns = {Storage.WORD};
		Cursor cursor = database.query(Storage.TAG_TABLE_NAME, columns, null, null, null, null, null);
		List<String> tags = new ArrayList<String>();

		if (cursor.getCount() == 0) {
			return tags;
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			tags.add(cursor.getString(0));
			cursor.moveToNext();
		}
		return tags;
	}

	/**
	 * Get the content values from a media object
	 * 
	 * @param media
	 *            the media object with values
	 * @return the content values from the media object
	 */
	private ContentValues getContentValues(Media media) {
		ContentValues values = new ContentValues();
		values.put(Storage.FILENAME, media.getFilename());
		values.put(Storage.TITLE, media.getTitle());
		values.put(Storage.DESCRIPTION, media.getDescription());
		if (media.getDatetime() != null) {
			values.put(Storage.DATETIME, DateUtils.dateToString(media.getDatetime(), DateUtils.DATE_TIME_FORMAT));
		}
		values.put(Storage.HAS_COPYRIGHT, media.hasCopyright() ? 1 : 0);
		values.put(Storage.AUTHOR, media.getAuthor());
		if (media.getGroupId() != null) {
			values.put(Storage.GROUP_ID, media.getGroupId().toString());
		}
		return values;
	}

	/**
	 * Get the content values from a group object
	 * 
	 * @param group
	 *            the group object with values
	 * @return the content values from the group object
	 */
	private ContentValues getContentValues(Group group) {
		ContentValues values = new ContentValues();
		values.put(Storage.DESCRIPTION, group.getDescription());
		values.put(Storage.COLOR, group.getColorString());
		values.put(Storage.INACTIVE, 0);
		return values;
	}

	/**
	 * Get a media object from a cursor.
	 * 
	 * @param cursor
	 *            the cursor from the database
	 * @return the media object
	 */
	private Media getMedia(Cursor cursor) {
		int dataType = cursor.getInt(cursor.getColumnIndex(Storage.DATA_TYPE));
		Media media = null;
		switch (dataType) {
			case Storage.DATA_TYPE_PICTURE :
				media = new Picture(cursor);
				break;
			case Storage.DATA_TYPE_VIDEO :
				media = new Video(cursor);
				break;
		}
		return media;
	}
}
