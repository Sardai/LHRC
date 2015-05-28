package lhrc.group3.tjooner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import lhrc.group3.tjooner.adapter.GroupAdapter;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.storage.DataSource;
import lhrc.group3.tjooner.web.WebRequest;
import lhrc.group3.tjooner.web.WebRequest.OnGroupRequestListener;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
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

	private GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
		application = (TjoonerApplication) getApplication();
 
		gridView = (GridView) findViewById(R.id.gridView1); 
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Group group = (Group) ((GroupAdapter)gridView.getAdapter()).getItem(position);
				Toast.makeText(MainActivity.this, "You Clicked " + group.getDescription(),
						Toast.LENGTH_LONG).show();				
			}
			
		});
		
		gridView.setVerticalSpacing(15);
		gridView.setHorizontalSpacing(15);
		
		DataSource dataSource = new DataSource(this);
		dataSource.open();
		WebRequest webRequest = new WebRequest(dataSource);
		webRequest.setOnGroupRequestListener(new OnGroupRequestListener() {
			
			@Override
			public void Completed(ArrayList<Group> groups) {
			  Log.i("WebRequest", "success");
			  gridView.setAdapter(new GroupAdapter(groups));			
			}
		});
		webRequest.getGroups();
		
		
		ButtonCameraPhoto = (Button) findViewById(R.id.buttonCameraPhoto);
		ButtonCameraVideo = (Button) findViewById(R.id.buttonCameraVideo);
	
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
			 
			 Picture picture = new Picture();
			 picture.setData(byteArray);
			 
			 
			 
//			 Media media = new Media();
//			 media.setData(byteArray);
			 application.DataSource.insert(picture);
			
			 
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