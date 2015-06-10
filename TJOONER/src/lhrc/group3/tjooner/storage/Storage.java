package lhrc.group3.tjooner.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * SqliteOpenHelper for storage of media, groups and tags.
 * @author Chris
 *
 */
public class Storage extends SQLiteOpenHelper {

	public static final String MEDIA_TABLE_NAME = "media";
	public static final String ID = "id";
	public static final String FILENAME = "filename";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String GROUP_ID = "groupId";
	public static final String DATETIME = "datetime";
	public static final String HAS_COPYRIGHT = "hasCopyright";
	public static final String AUTHOR = "author" ;
	public static final String DATA = "data";
	public static final String DATA_TYPE = "dataType";
	public static final String PATH = "path";
	public static final int DATA_TYPE_PICTURE = 1;
	public static final int DATA_TYPE_VIDEO = 2;
	
	public static final String[] MEDIA_COLUMNS = {
		ID,FILENAME,TITLE,DESCRIPTION,GROUP_ID,DATETIME,HAS_COPYRIGHT,AUTHOR,DATA,DATA_TYPE,PATH
	};
		
	public static final String GROUP_TABLE_NAME = "groups";
	public static final String COLOR = "color";
	public static final String INACTIVE = "inactive";
	
	public static final String[] GROUP_COLUMNS = {
		ID,DESCRIPTION,COLOR,INACTIVE
	};
	
	public static final String TAG_TABLE_NAME = "tags";
	public static final String WORD = "word";
	
	public static final String MEDIA_TAG_TABLE_NAME = "media_tag";
	public static final String MEDIA_ID = "mediaId";
		
	private static final String MEDIA_TABLE_CREATE = String.format(
			"create table %s (%s string primary key, %s text, %s text, %s text, %s text, %s text,%s integer,%s text, %s blob, %s integer, %s text)",
			MEDIA_TABLE_NAME,ID,FILENAME,TITLE, DESCRIPTION,GROUP_ID,DATETIME,HAS_COPYRIGHT,AUTHOR,DATA,DATA_TYPE,PATH );
	
	private static final String GROUP_TABLE_CREATE = String.format(
			"create table %s (%s text primary key, %s text,%s text, %s integer)",
				GROUP_TABLE_NAME,ID,DESCRIPTION,COLOR,INACTIVE
			);
	
	private static final String TAG_TABLE_CREATE = String.format(
			"create table %s (%s text primary key)",
			TAG_TABLE_NAME,WORD
			);
	
	private static final String MEDIA_TAG_TABLE_CREATE = String.format(
			"create table %s (%s string, %s string, PRIMARY KEY (%s, %s))",
				MEDIA_TAG_TABLE_NAME, MEDIA_ID, WORD,MEDIA_ID,WORD
			);
	
	private static final String DATABASE_NAME = "tjooner.db";
    private static final int DATABASE_VERSION = 4;
	
	public Storage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase database) {
		 	database.execSQL(MEDIA_TABLE_CREATE);
	        database.execSQL(GROUP_TABLE_CREATE);
	        database.execSQL(TAG_TABLE_CREATE);
	        database.execSQL(MEDIA_TAG_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("Database",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + MEDIA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEDIA_TAG_TABLE_NAME);
		onCreate(db);
	}
	
	

}
