package lhrc.group3.tjooner.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Storage extends SQLiteOpenHelper {

	public static final String MEDIA_TABLE_NAME = "media";
	public static final String ID = "id";
	public static final String FILENAME = "filename";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String GROUP_ID = "groupId";
	public static final String DATETIME = "datetime";
	public static final String HAS_COPYRIGHT = "hasCopyright";
	public static final String COPYRIGHT_HOLDER = "copyrightHolder" ;
	public static final String DATA = "data";
	public static final String DATA_TYPE = "dataType";
	
	public static final String[] MEDIA_COLUMNS = {
		ID,FILENAME,TITLE,DESCRIPTION,GROUP_ID,DATETIME,HAS_COPYRIGHT,COPYRIGHT_HOLDER,DATA,DATA_TYPE
	};
	
	public static final String DATE_TIME_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS";
	
	public static final String GROUP_TABLE_NAME = "groups";
	public static final String COLOR = "color";
	
	public static final String[] GROUP_COLUMNS = {
		DESCRIPTION,COLOR
	};
	
	public static final String TAG_TABLE_NAME = "tags";
	public static final String WORD = "word";
	
	public static final String MEDIA_TAG_TABLE_NAME = "media_tag";
	public static final String MEDIA_ID = "mediaId";
	
    private static final String MEDIA_DATABASE_NAME = "tjooner.db";
    private static final int DATABASE_VERSION = 1;
	
	//TODO add not null to columns wich are required -> chris. 
	private static final String MEDIA_TABLE_CREATE = String.format(
			"create table %s (%s string primary key, %s text, %s text, %s text, %s text, %s text,%s integer,%s text, %s blob, %s integer)",
			MEDIA_TABLE_NAME,ID,FILENAME,TITLE, DESCRIPTION,GROUP_ID,DATETIME,HAS_COPYRIGHT,COPYRIGHT_HOLDER,DATA,DATA_TYPE );
	
	private static final String GROUP_TABLE_CREATE = String.format(
			"create table %s (%s text primary key, %s text,%s text)",
				GROUP_TABLE_NAME,ID,DESCRIPTION,COLOR
			);
	
	private static final String TAG_TABLE_CREATE = String.format(
			"create table %s (%s text primary key)",
			TAG_TABLE_NAME,WORD
			);
	
	private static final String MEDIA_TAG_TABLE_CREATE = String.format(
			"create table %s (%s string, %s string PRIMARY KEY (column1, column2))",
				MEDIA_TAG_TABLE_NAME, MEDIA_ID, Storage.WORD
			);
	
	public Storage(Context context) {
		super(context, MEDIA_DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL("DROP TABLE IF EXISTS " + TAG_TABLE_NAME);
		
	}
	
	

}