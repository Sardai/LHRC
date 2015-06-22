/**
 * 
 */
package lhrc.group3.tjooner.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;

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

	public static String getVideoPath(Context context, Uri uri) {
		String[] filePathColumn = { MediaStore.Video.Media.DATA };

		return getPath(context, uri, filePathColumn);
	}
	
	public static String getPicturePath(Context context, Uri uri) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		return getPath(context, uri, filePathColumn);
	}

	private static String getPath(Context context, Uri uri, String[] filePathColumn) {
		Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
				null, null, null);

		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		return cursor.getString(columnIndex);
	}
}
