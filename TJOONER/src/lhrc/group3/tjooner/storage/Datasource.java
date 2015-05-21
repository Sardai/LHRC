package lhrc.group3.tjooner.storage;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.models.Video;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class to insert, update ,remove and get object from the database.
 * @author Chris
 *
 */
public class Datasource {
	
      private static final String WHERE = String.format("%s = ?", Storage.ID);
	
	  private SQLiteDatabase database;
	  private Storage storage;
	  
	  /**
	   * Initialize a datasource.
	   * @param context
	   */
	  public Datasource(Context context){
		  storage = new Storage(context);
	  }
	  
	  /**
	   * Opens database
	   */
	  public void open(){
		  database = storage.getWritableDatabase();
	  }
	  
	  /**
	   * closes database
	   */
	  public void close(){
		  storage.close();
	  }
	  
	  /**
	   * Insert a media object in the database.
	   * @param media the media object to insert
	   */
	  public void insert(Media media){
		  ContentValues values = getMediaValues(media);
		  values.put(Storage.ID,media.getId().toString());
		  int datatype = 1;
		  if(media instanceof Video){
			  datatype = 2;
		  }
		  values.put(Storage.DATA_TYPE, datatype);
		  values.put(Storage.DATA,media.getData());
		  database.insert(Storage.MEDIA_TABLE_NAME, null, values);
	  }
	  
	  /**
	   * Insert a group in the database.
	   * @param group the group to insert
	   */
	  public void insert(Group group){
		  ContentValues values = new ContentValues();		  
		  values.put(Storage.DESCRIPTION,group.getDescription());
		  values.put(Storage.COLOR, group.getColor());
		  database.insert(Storage.GROUP_TABLE_NAME, null, values);
	  }
	  
	  /**
	   * Insert a tag in the database.
	   * @param tag the tag to insert
	   */
	  public void insert(String tag){
		  String where = String.format("%s = %s",Storage.WORD,tag);
		  Cursor cursor = database.query(Storage.TAG_TABLE_NAME,new String[]{Storage.WORD},where,null,null,null,null);
		  if(cursor.getCount() == 0){
			ContentValues values = new ContentValues();
			values.put(Storage.WORD, tag);
			database.insert(Storage.TAG_TABLE_NAME, null, values);
		}
	  }
	  
	  /**
	   * update an existing media object in the database. 
	   * @param media the media object to update
	   */
	  public void update(Media media){
		  String[] args = {media.getId().toString()};
		  database.update(Storage.MEDIA_TABLE_NAME, getMediaValues(media), WHERE,args );
	  }
	  
	  /**
	   * Remove a media object from the database.
	   * @param media the media object to remove
	   */
	  public void remove(Media media){
		  	String[] args = {media.getId().toString()};
	        database.delete(Storage.MEDIA_TABLE_NAME, WHERE, args);
	  }
	  
	  /**
	   * get all groups with media object and tags from the database 
	   * @return an map with all groups and media object and tags
	   */
	  public HashMap<UUID,Group> getAll(){
		  Cursor cursor = database.query(Storage.GROUP_TABLE_NAME,Storage.GROUP_COLUMNS,null,null,null,null,null);
		  
		  HashMap<UUID,Group> groups = new HashMap<UUID, Group>();
		  
		  if(cursor.getCount() == 0){
			  return groups;
		  }
		  
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()){
			  Group group = new Group(cursor);			  
			  groups.put(group.getId(), group);
		  }
		  
		  cursor = database.query(Storage.MEDIA_TABLE_NAME,Storage.MEDIA_COLUMNS,null,null,null,null,null);
		  
		  if(cursor.getCount() == 0){
			  return groups;
		  }
		  
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()){
			  int dataType = cursor.getInt(cursor.getColumnIndex(Storage.DATA_TYPE));
			  
			  
			  Media media = null;
			  switch (dataType) {
			case 1:
				  media = new Picture(cursor);
				break;
			case 2:
				media = new Video(cursor);
				break;
			}
			  String[] columns = {Storage.WORD};
			  String where = String.format("%s = %s", Storage.MEDIA_ID,media.getId().toString());
			  Cursor cursorTag = database.query(Storage.MEDIA_TAG_TABLE_NAME,columns,where,null,null,null,null);
			  
			  if(cursorTag.getCount()> 0){
				  cursorTag.moveToFirst();
				  while(!cursorTag.isAfterLast()){
					media.addTag(cursor.getString(0));  
				  }				  
			  }
			  			  
			  groups.get(media.getGroupId()).addMedia(media);
			  
		  }
		  
		  return groups;
	  }
	  
	  /**
	   * get all tags from the database.
	   * @return all tags
	   */
	  public TreeSet<String> getTags(){
		  String[] columns = {Storage.WORD};
		  Cursor cursor = database.query(Storage.TAG_TABLE_NAME,columns,null,null,null,null,null);
		  TreeSet<String> tags = new TreeSet<String>();
		  
		  if(cursor.getCount() == 0){
			  return tags;
		  }
		  
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()){
			  tags.add(cursor.getString(0));
		  }
		  return tags;
	  }
	  
	  
	  private ContentValues getMediaValues(Media media){
		  ContentValues values = new ContentValues();
		  values.put(Storage.FILENAME, media.getFilename());
		  values.put(Storage.TITLE, media.getTitle());
		  values.put(Storage.DESCRIPTION, media.getDescription());
		  values.put(Storage.DATE_TIME_FORMAT, media.getDatetime().toString(Storage.DATE_TIME_FORMAT));
		  values.put(Storage.HAS_COPYRIGHT, media.hasCopyright());
		  values.put(Storage.COPYRIGHT_HOLDER, media.getCopyrightHolder());
		  return values;
	  }	  
}

