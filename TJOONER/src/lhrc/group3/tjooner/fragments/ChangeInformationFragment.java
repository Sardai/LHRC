package lhrc.group3.tjooner.fragments;

import java.util.Calendar;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.helpers.Date;
import lhrc.group3.tjooner.models.Media;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompatApi21;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;



public class ChangeInformationFragment extends Fragment {
	
	private EditText fileName;
	private EditText titleEditText;
	private EditText descriptionEditText;
	private DatePicker datePicker;
	private Media media;
	private CheckBox hasCopyRight;
	private EditText authorEditText;
	private LinearLayout authorInformationLayout;
	private TimePicker mediaTimePicker;
	
	public ChangeInformationFragment(Media media) {
		super();
		this.media = media;
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.change_information_fragment,
				container, false);
		setRetainInstance(true);
		
		//get all the components
		fileName = (EditText) view.findViewById(R.id.fileNameEditText);
		titleEditText = (EditText) view.findViewById(R.id.titleEditText);
		descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
		datePicker = (DatePicker) view.findViewById(R.id.mediaDatePicker);
		hasCopyRight = (CheckBox) view.findViewById(R.id.isAuterCheckBox);
		authorEditText = (EditText) view.findViewById(R.id.authorNameEditText);
		authorInformationLayout = (LinearLayout) view.findViewById(R.id.authordetails);
		mediaTimePicker = (TimePicker) view.findViewById(R.id.mediaTimePicker);
		
		//sets the existing information
		setExistingInformationInTextViews();
		
		hasCopyRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(hasCopyRight.isChecked()){
					authorInformationLayout.setVisibility(LinearLayout.VISIBLE);
				}
				else {
					authorInformationLayout.setVisibility(LinearLayout.GONE);
				}
				
			}
		});
		
		

		return view;
	}

	public boolean setNewInformation() {
		boolean geldigeInfo = true;
		if (titleEditText.getText().toString().equals("")) {
			geldigeInfo = false;
		}
		//todo group and other requered information

		if (!geldigeInfo) {
			Toast.makeText(getActivity().getBaseContext(),
					"you haven't filled in all the required information",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		media.setFilename(fileName.getText().toString());
		media.setTitle(titleEditText.getText().toString());
		media.setDescription(descriptionEditText.getText().toString());
		if((hasCopyRight.isChecked()) && authorEditText.getText().toString().equals("")){
			Toast.makeText(getActivity().getBaseContext(),
					"if the checkbox has copy right is checked you have to give up an author name",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if( hasCopyRight.isChecked()) {
			media.setCopyright(true);
			media.setCopyrightHolder(authorEditText.getText().toString()+"");
		} else {
			media.setCopyright(false);
		}
		
		//"YYYY-MM-DD HH:MM:SS.SSS"
		//setting the month and day to the right format.
		String month = formatToTwoDigits(datePicker.getMonth());
		String day = formatToTwoDigits(datePicker.getDayOfMonth());	
		String hour = formatToTwoDigits(mediaTimePicker.getCurrentHour());
		String minutes = formatToTwoDigits(mediaTimePicker.getCurrentMinute());
		
		String dateTime = datePicker.getYear()+"-"+month+"-"+day+" "+
		hour+":"+minutes+":"+"00.000";
		Log.d("de date formaat", dateTime);
		//media.setDatetime(new Date(dateTime));
		//application.DataSource.update(media);
		//finish();
		Toast.makeText(getActivity().getBaseContext(),
				"The Information has been changed/saved!",
				Toast.LENGTH_SHORT).show();
		return true;

	}
	
	private String formatToTwoDigits(int s){
		if(s <= 9){
			return "0"+ s;
		}
		return s+ "";
	}

	public void setExistingInformationInTextViews() {
		authorInformationLayout.setVisibility(LinearLayout.GONE);
		if (media.getTitle() != null) {
			titleEditText.setText(media.getTitle() + "");
		}
		if (media.getDescription() != null) {
			descriptionEditText.setText(media.getDescription()+"");
		}
		if(media.getFilename() != null){
			fileName.setText(media.getFilename()+"");
		}
		if(media.getDatetime() != null){
			datePicker.updateDate(media.getDatetime().getYear(), media.getDatetime().getMonth(), media.getDatetime().getDay());
			mediaTimePicker.setCurrentHour(media.getDatetime().getHours());
			mediaTimePicker.setCurrentMinute(media.getDatetime().getMinutes());
		}
		else{
			Calendar cal = Calendar.getInstance();
			datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			mediaTimePicker.setCurrentHour(cal.get(Calendar.HOUR));
			mediaTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
			
		}
		
		if(media.getCopyrightHolder() != null){
			hasCopyRight.setChecked(media.hasCopyright());
			if(media.hasCopyright()){
				authorInformationLayout.setVisibility(LinearLayout.VISIBLE);
			}
		}
		

	}

}
