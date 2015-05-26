package lhrc.group3.tjooner;

import java.util.Map;
import java.util.UUID;

import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Chris Rötter, Luuk Wellink
 * @author Hugo van der Geest, Rene Bisperink
 * 
 * 
 */
public class MainActivity extends Activity {
	private Button ButtonCameraPhoto;
	private Button ButtonCameraVideo;
	private TjoonerApplication application;
	private ImageView image;
	public static int TAKE_PICTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
 
		application = (TjoonerApplication) getApplication();
		
		ButtonCameraPhoto = (Button) findViewById(R.id.buttonCameraPhoto);
		ButtonCameraVideo = (Button) findViewById(R.id.buttonCameraVideo);
		image = (ImageView) findViewById(R.id.imageViewPhoto);
 

		ButtonCameraPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				

				// start camera activity
				startActivityForResult(intentPicture, TAKE_PICTURE);
				
			}
			

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && data != null){
			 Bitmap bitmap;
			 OutputStream output;
			 
			 Bundle extras = data.getExtras();
			 
			 bitmap = (Bitmap) extras.get("data");
			 image.setImageBitmap(bitmap);
			 
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			 byte[] byteArray = stream.toByteArray();
			 
//			 Media media = new Media();
//			 media.setData(byteArray);
//			 application.dataSource.insert(media);
			
			 
			 Toast.makeText(MainActivity.this, "Afbeelding opgeslagen", Toast.LENGTH_SHORT).show();; 
		 }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}