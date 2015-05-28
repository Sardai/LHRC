package lhrc.group3.tjooner.fragments;

import lhrc.group3.tjooner.R;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingActionButtonFragment extends Fragment {
	public static final int REQUEST_CODE_PICK_FROM_GALLERY = 120;
	private String imgString;
	private ImageView expandImageView;
	private ImageView selectPictureFromGalImageView;
	private ImageView selectPictureFromCamImageVIew;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.floating_action_button_fragment, container, false);
		expandImageView = (ImageView) view.findViewById(R.id.imageView1);
		selectPictureFromGalImageView = (ImageView) view.findViewById(R.id.imageView2);
		selectPictureFromCamImageVIew = (ImageView) view.findViewById(R.id.imageView3);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
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
		
		return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
		     
	        if (requestCode == REQUEST_CODE_PICK_FROM_GALLERY && resultCode == getActivity().RESULT_OK
	                && null != data) {
	           

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

	        } else {
	            Toast.makeText(getActivity(), "You haven't picked an Image",
	                    Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        Toast.makeText(getActivity(), "Something went wrong, sorry!", Toast.LENGTH_LONG)
	                .show();
	    }
		super.onActivityResult(requestCode, resultCode, data);
	}

}
