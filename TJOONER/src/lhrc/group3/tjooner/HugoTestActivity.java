package lhrc.group3.tjooner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HugoTestActivity extends Activity {
	public static final int REQUEST_CODE_PICK_FROM_GALLERY = 120;
	private String imgString;
	private ImageView expandImageView;
	private ImageView selectPictureFromGalImageView;
	private ImageView selectPictureFromCamImageVIew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hugo_test);
		expandImageView = (ImageView) findViewById(R.id.imageView1);
		selectPictureFromGalImageView = (ImageView) findViewById(R.id.imageView2);
		selectPictureFromCamImageVIew = (ImageView) findViewById(R.id.imageView3);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		double widthPercentage = 0.22;
		expandImageView.getLayoutParams().width = (int) (widthPercentage * width);
		expandImageView.getLayoutParams().height = (int) (widthPercentage * width);
		selectPictureFromGalImageView.getLayoutParams().width = (int) (widthPercentage * width);
		selectPictureFromGalImageView.getLayoutParams().height = (int) (widthPercentage * width);
		selectPictureFromCamImageVIew.getLayoutParams().width = (int) (widthPercentage * width);
		selectPictureFromCamImageVIew.getLayoutParams().height = (int) (widthPercentage * width);
		selectPictureFromGalImageView.setVisibility(ImageView.GONE);
		selectPictureFromCamImageVIew.setVisibility(ImageView.GONE);
		
		expandImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int visibility = selectPictureFromCamImageVIew.getVisibility();
				if(visibility == ImageView.VISIBLE){
					selectPictureFromGalImageView.setVisibility(ImageView.GONE);
					selectPictureFromCamImageVIew.setVisibility(ImageView.GONE);
				}
				else {
					selectPictureFromGalImageView.setVisibility(ImageView.VISIBLE);
					selectPictureFromCamImageVIew.setVisibility(ImageView.VISIBLE);
				}
				
			}
		});
		selectPictureFromGalImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
				        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_CODE_PICK_FROM_GALLERY);
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hugo_test, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	try {
     
        if (requestCode == REQUEST_CODE_PICK_FROM_GALLERY && resultCode == RESULT_OK
                && null != data) {
           

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };


            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
       
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgString = cursor.getString(columnIndex);
            cursor.close();
            ImageView imgView = (ImageView) findViewById(R.id.imageView4);
            
            imgView.setImageBitmap(BitmapFactory
                    .decodeFile(imgString));

        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    } catch (Exception e) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                .show();
    }

	}
}
