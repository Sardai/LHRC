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

import android.net.Uri;

/**
 * @author Chris
 *
 */
public class FileUtils {
 
		/**
		 * creates a byte array from a inputstream.
		 * @param inputStream the inputstream
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
}
