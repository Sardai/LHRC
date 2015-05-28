package lhrc.group3.tjooner.fragments;

import java.io.ByteArrayOutputStream;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.models.Picture;
import lhrc.group3.tjooner.storage.DataSource;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class FloatingActionButtonFragment extends Fragment {
	public static final int REQUEST_CODE_PICK_IMAGE_FROM_GALLERY = 120;
	public static final int REQUEST_CODE_PICK_VIDEO_FROM_GALLERY = 121;
	private String imgString;
	private ImageView expandImageView;
	private ImageView selectPictureFromGalImageView;
	private ImageView selectVideoFromGalImageView;
	private ImageView makeNewVideo;
	private ImageView makeNewPicture;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.floating_action_button_fragment, container, false);
		expandImageView = (ImageView) view.findViewById(R.id.expandMenuImageView);
		selectPictureFromGalImageView = (ImageView) view.findViewById(R.id.ImageFromGalleryImageView);
		selectVideoFromGalImageView = (ImageView) view.findViewById(R.id.selectVideoFromGalleryImageView);
		makeNewVideo = (ImageView) view.findViewById(R.id.newVideoImageView);
		makeNewPicture = (ImageView) view.findViewById(R.id.newPictureImageView);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		double widthPercentage = 0.22;
		
		//set all the images arcordingly to the screen size
		expandImageView.getLayoutParams().width = (int) (widthPercentage * width);
		expandImageView.getLayoutParams().height = (int) (widthPercentage * width);
		
		selectPictureFromGalImageView.getLayoutParams().width = (int) (widthPercentage * width);
		selectPictureFromGalImageView.getLayoutParams().height = (int) (widthPercentage * width);
		
		makeNewPicture.getLayoutParams().width = (int) (widthPercentage * width);
		makeNewPicture.getLayoutParams().height = (int) (widthPercentage * width);
		
		selectVideoFromGalImageView.getLayoutParams().height = (int)(widthPercentage* width);
		selectVideoFromGalImageView.getLayoutParams().width = (int)(widthPercentage* width);
		
		makeNewVideo.getLayoutParams().height = (int)(widthPercentage* width);
		makeNewVideo.getLayoutParams().width = (int)(widthPercentage* width);
		
		selectPictureFromGalImageView.setVisibility(ImageView.GONE);
		selectVideoFromGalImageView.setVisibility(ImageView.GONE);
		makeNewPicture.setVisibility(ImageView.GONE);
		makeNewVideo.setVisibility(ImageView.GONE);
		
		expandImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int visibility = makeNewPicture.getVisibility();
				if(visibility == ImageView.VISIBLE){
					selectPictureFromGalImageView.setVisibility(ImageView.GONE);
					selectVideoFromGalImageView.setVisibility(ImageView.GONE);
					makeNewPicture.setVisibility(ImageView.GONE);
					makeNewVideo.setVisibility(ImageView.GONE);
				}
				else {
					selectPictureFromGalImageView.setVisibility(ImageView.VISIBLE);
					selectVideoFromGalImageView.setVisibility(ImageView.VISIBLE);
					makeNewPicture.setVisibility(ImageView.VISIBLE);
					makeNewVideo.setVisibility(ImageView.VISIBLE);
				}
				
			}
		});
		selectPictureFromGalImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
				        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_FROM_GALLERY);
				
				
			}
		});
		
		selectVideoFromGalImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Use MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO_FROM_GALLERY);
			}
		});
		
		return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//pick picture from gallery
		
		try {
	        if (requestCode == REQUEST_CODE_PICK_IMAGE_FROM_GALLERY && resultCode == getActivity().RESULT_OK
	                && null != data) {
	        	
	           Log.d("in de if met de request code", "tot hier werkt het nog 1");

	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };


	            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	       
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            imgString = cursor.getString(columnIndex);
	            cursor.close();
	            
	            //hier de bitmap die je moet opslaan in de db
	            Bitmap pictureBitmap = BitmapFactory
	                    .decodeFile(imgString);
	            Picture pic = new Picture();
	            Log.d("in de if met de request code", "tot hier werkt het nog 2");
	         ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			 byte[] byteArray = stream.toByteArray();
			 Log.d("in de if met de request code", "tot hier werkt het nog 3");
	            pic.setData(byteArray);
	            Log.d("in de if met de request code", "tot hier werkt het nog 4");
	            DataSource source = new DataSource(getActivity().getBaseContext());
	            Log.d("in de if met de request code", "tot hier werkt het nog 5");
	            source.open();
	            Log.d("in de if met de request code", "tot hier werkt het nog 6");
	            source.insert(pic);
	            Log.d("in de if met de request code", "tot hier werkt het nog 7");

	        } else if(requestCode == REQUEST_CODE_PICK_IMAGE_FROM_GALLERY) {
	            Toast.makeText(getActivity(), "You haven't picked an Image",
	                    Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        Toast.makeText(getActivity(), "Something went wrong, sorry!", Toast.LENGTH_LONG)
	                .show();
	    }
		
		//pick video from gallery
		try {
		     
	        if (requestCode == REQUEST_CODE_PICK_VIDEO_FROM_GALLERY && resultCode == getActivity().RESULT_OK
	                && null != data) {
	        	Uri selectedVideo = data.getData();
	        	   	
	        	
	         

	        } else {
	            Toast.makeText(getActivity(), "You haven't picked an Video",
	                    Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        Toast.makeText(getActivity(), "Something went wrong, sorry!", Toast.LENGTH_LONG)
	                .show();
	    }
		
		super.onActivityResult(requestCode, resultCode, data);
	}

}
