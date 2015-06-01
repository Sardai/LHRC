package lhrc.group3.tjooner.fragments;

import java.util.Calendar;

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
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		if(dateSetListener != null){
			dateSetListener.onDateSet(view, year, month, day);
		}
	}
	
	public void setOnDateSetListener(OnDateSetListener dateSetListener){
		this.dateSetListener = dateSetListener;
	}
}