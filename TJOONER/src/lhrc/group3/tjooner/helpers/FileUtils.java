/**
 * 
 */
package lhrc.group3.tjooner.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author Chris
 *
 */
public class FileUtils {

	/**
	 * creates a byte array from a inputstream.
	 * 
	 * @param inputStream
	 *            the inputstream
	 * @return the byte array of the inputstream
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	/**
	 * Get video path from uri.
	 * @param context the context
	 * @param uri the uri of the video
	 * @return the video path
	 */
	public static String getVideoPath(Context context, Uri uri) {
		String[] filePathColumn = { MediaStore.Video.Media.DATA };

		return getPath(context, uri, filePathColumn);
	}
	
	/**
	 * Get picture path from uri.
	 * @param context the context
	 * @param uri the uri of the picture
	 * @return the picture path
	 */
	public static String getPicturePath(Context context, Uri uri) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		return getPath(context, uri, filePathColumn);
	}

	/**
	 * get path from uri.
	 * @param context the context
	 * @param uri the uri
	 * @param filePathColumn the type of path
	 * @return the path
	 */
	private static String getPath(Context context, Uri uri, String[] filePathColumn) {
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
				null, null, null);
		if(cursor == null)
		{
			return uri.toString();
		}
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		return cursor.getString(columnIndex);
	}
	
	/**
	 * Get content uri from image.
	 * @param context the context
	 * @param filePath the path of the file
	 * @return the content uri
	 */
	public static Uri getImageContentUri(Context context, String filePath) {
	    Cursor cursor = context.getContentResolver().query(
	            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	            new String[] { MediaStore.Images.Media._ID },
	            MediaStore.Images.Media.DATA + "=? ",
	            new String[] { filePath }, null);

	    if (cursor != null && cursor.moveToFirst()) {
	        int id = cursor.getInt(cursor
	                .getColumnIndex(MediaStore.MediaColumns._ID));
	        Uri baseUri = Uri.parse("content://media/external/images/media");
	        return Uri.withAppendedPath(baseUri, "" + id);
	    } else {
	        
	            ContentValues values = new ContentValues();
	            values.put(MediaStore.Images.Media.DATA, filePath);
	            return context.getContentResolver().insert(
	                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        
	    }
	}
}
