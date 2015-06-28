package lhrc.group3.tjooner.fragments;

import java.util.Calendar;

import lhrc.group3.tjooner.models.Media;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Datepicker fragment
 * @author Chris
 *
 */
public class DatePickerFragment extends DialogFragment implements
		OnDateSetListener {

	private OnDateSetListener dateSetListener;
	private Media media;
	
	public DatePickerFragment(Media media) {
		this.media = media;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		
		if(media != null && media.getDatetime() != null){
			c.setTime(media.getDatetime());
		}
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		if(dateSetListener != null){
			//Notify listener that the date has been set.
			dateSetListener.onDateSet(view, year, month, day);
		}
	}
	
	/**
	 * Set a listener for when the date is set.
	 * @param dateSetListener the listener.
	 */
	public void setOnDateSetListener(OnDateSetListener dateSetListener){
		this.dateSetListener = dateSetListener;
	}
}