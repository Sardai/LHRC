/**
 * 
 */
package lhrc.group3.tjooner.fragments;

import java.util.Calendar;

import lhrc.group3.tjooner.models.Media;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * timepicker fragment
 * @author Chris
 *
 */
public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {

	private OnTimeSetListener timeSetListener;
	private Media media;
	
	public TimePickerFragment(Media media) {
	  this.media = media;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		
		if(media != null && media.getDatetime() != null){
			c.setTime(media.getDatetime());
		}
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if(timeSetListener != null){
			//Notify listener that the time has been set.
			timeSetListener.onTimeSet(view, hourOfDay, minute);
		}
	}
	
	/**
	 * Set a listener for when the time is set.
	 * @param timeSetListener the listener.
	 */
	public void setOnTimeSetListener(OnTimeSetListener timeSetListener){
		this.timeSetListener = timeSetListener;
	}
}