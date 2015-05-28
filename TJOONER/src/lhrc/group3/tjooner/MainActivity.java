package lhrc.group3.tjooner;

import java.util.Map;
import java.util.UUID;

import lhrc.group3.tjooner.adapter.CustomAdapter;
import lhrc.group3.tjooner.models.Group;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
	private ImageView newImage;
	private VideoView newVideo;
	private MediaController mediaControls;
	public static int TAKE_PICTURE = 1;
	public static int TAKE_VIDEO = 2;

	GridView gv;
	Context context;
	
	public String [] groupNames={"group1", "group2", "group3","group4","group5","group6"};
	public int [] groupPreview ={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
 
		gv = (GridView) findViewById(R.id.gridView1); 
		gv.setAdapter(new CustomAdapter(this, groupNames, groupPreview));
		gv.setVerticalSpacing(15);
		gv.setHorizontalSpacing(15);
		
		newVideo = (VideoView) findViewById(R.id.videoViewNewVideo);
		newImage = (ImageView) findViewById(R.id.imageViewNewPhoto);
		
		application = (TjoonerApplication) getApplication();
		
		newVideo.setMediaController(mediaControls);
		
		ButtonCameraPhoto = (Button) findViewById(R.id.buttonCameraPhoto);
		ButtonCameraVideo = (Button) findViewById(R.id.buttonCameraVideo);
	
		ButtonCameraVideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);	
				
				startActivityForResult(intentVideo, TAKE_VIDEO);
			}
		});
		
		ButtonCameraPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				

				// start camera activity
				startActivityForResult(intentPicture, TAKE_PICTURE);
				
			}
			

		});
	}
	/**
	 * OnActivityResult used for taking new pictures.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && data != null){
			 Bitmap bitmap;
			 OutputStream output;
			 
			 Bundle extras = data.getExtras();
			 
			 bitmap = (Bitmap) extras.get("data");
			 newImage.setImageBitmap(bitmap);
			 
			 ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			 byte[] byteArray = stream.toByteArray();
			 
//			 Media media = new Media();
//			 media.setData(byteArray);
//			 application.dataSource.insert(media);
			
			 
			 Toast.makeText(MainActivity.this, "Afbeelding opgeslagen", Toast.LENGTH_SHORT).show();; 
		 }
		 
		 if (requestCode == TAKE_VIDEO && resultCode== RESULT_OK && data != null){
			 newVideo.setVideoURI(data.getData());
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