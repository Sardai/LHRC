package lhrc.group3.tjooner.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lhrc.group3.tjooner.GPSTracker;
import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.TjoonerApplication;
import lhrc.group3.tjooner.adapter.GroupSpinnerAdapter;
import lhrc.group3.tjooner.helpers.DateUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.Storage;
import android.app.Application;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompatApi21;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ChangeInformationFragment extends Fragment implements OnClickListener, OnFocusChangeListener {

	private Media media;

	private EditText fileName, titleEditText, descriptionEditText, authorEditText, editTextDate, editTextTime;
	private CheckBox hasCopyRight;
	private MultiAutoCompleteTextView textViewTags;
	private TjoonerApplication application;

	private TimePickerFragment timeFragment;
	private DatePickerFragment dateFragment;

	private java.util.Date dateTime;

	private Spinner spinnerGroup;

	private UUID groupId;

	private List<Group> groups;
	private GPSTracker gps;
	private String longitude;
	private String latitude;
	

	public ChangeInformationFragment(Media media, String groupId) {
		super();
		this.media = media;
		if (groupId != null) {
			this.groupId = UUID.fromString(groupId);
		}
	}

	public ChangeInformationFragment(Media media) {
		this(media, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.change_information_fragment, container, false);
		setRetainInstance(true);

		application = (TjoonerApplication) getActivity().getApplication();
		gps = new GPSTracker(getActivity());
		// get all the components
		fileName = (EditText) view.findViewById(R.id.fileNameEditText);
		descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
		hasCopyRight = (CheckBox) view.findViewById(R.id.isAuterCheckBox);
		authorEditText = (EditText) view.findViewById(R.id.authorNameEditText);
		titleEditText = (EditText) view.findViewById(R.id.titleEditText);
		// datePicker = (DatePicker) view.findViewById(R.id.mediaDatePicker);
		// mediaTimePicker = (TimePicker)
		// view.findViewById(R.id.mediaTimePicker);
		spinnerGroup = (Spinner) view.findViewById(R.id.spinnerTjoonerGroup);
		textViewTags = (MultiAutoCompleteTextView) view.findViewById(R.id.multiAutoCompleteTextViewTags);
		editTextDate = (EditText) view.findViewById(R.id.editTextDate);
		editTextTime = (EditText) view.findViewById(R.id.editTextTime);

		groups = application.DataSource.getGroups();
		GroupSpinnerAdapter spinnerAdapter = new GroupSpinnerAdapter(groups);
		spinnerGroup.setAdapter(spinnerAdapter);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, application.DataSource.getTags());
		textViewTags.setAdapter(adapter);
		textViewTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		// sets the existing information
		setExistingInformationInTextViews();

		editTextTime.setOnClickListener(this);
		editTextDate.setOnClickListener(this);
		editTextTime.setOnFocusChangeListener(this);
		editTextDate.setOnFocusChangeListener(this);
		if(gps.canGetLocation()) {
			longitude = gps.getLongtitude()+"";
			latitude = gps.getLatitude()+"";
		} else {
			longitude = "-";
			latitude = "-";
		}
		
		
		return view;
	}

	public boolean setNewInformation() {
		boolean geldigeInfo = true;
		if (titleEditText.getText().toString().equals("")) {
			geldigeInfo = false;
			titleEditText.setError("This field is required");
		}
		else{
			titleEditText.setError(null);
		}
		// todo group and other requered information

		if (!geldigeInfo) {
			Toast.makeText(getActivity().getBaseContext(), "you haven't filled in all the required information", Toast.LENGTH_SHORT).show();
			return false;
		}
		media.setFilename(fileName.getText().toString());
		media.setTitle(titleEditText.getText().toString());
		media.setDescription(descriptionEditText.getText().toString());
		media.setCopyright(hasCopyRight.isChecked());
		media.setAuthor(authorEditText.getText().toString());
		media.setDatetime(dateTime);
		Group selectedGroup = (Group) spinnerGroup.getSelectedItem();
		media.setGroupId(selectedGroup.getId());
		media.setLongitude(longitude);
		media.setLatitude(latitude);

		String[] tags = textViewTags.getText().toString().split(",");
		media.addTags(tags);
		
		return true;
	}

	public void setExistingInformationInTextViews() {
		titleEditText.setText(media.getTitle());
		descriptionEditText.setText(media.getDescription());
		fileName.setText(media.getFilename());
		authorEditText.setText(media.getAuthor());
		hasCopyRight.setChecked(media.hasCopyright());

		if (media.getDatetime() != null) {
			dateTime = media.getDatetime();
			editTextDate.setText(DateUtils.dateToString(dateTime, "dd-MM-yyyy"));
			editTextTime.setText(DateUtils.dateToString(dateTime, "HH:mm"));
		}

		if (media.getGroupId() != null) {
			groupId = media.getGroupId();
		}
		if (groupId != null) {
			for (int i = 0; i < groups.size(); i++) {
				if (groups.get(i).getId().equals(groupId)) {
					spinnerGroup.setSelection(i);
				}

			}
		}
		
		textViewTags.setText(media.getTagsString());
		
		// if(media.getDatetime() != null){
		// datePicker.updateDate(media.getDatetime().getYear(),
		// media.getDatetime().getMonth(), media.getDatetime().getDay());
		// mediaTimePicker.setCurrentHour(media.getDatetime().getHours());
		// mediaTimePicker.setCurrentMinute(media.getDatetime().getMinutes());
		// }
		// else{
		// Calendar cal = Calendar.getInstance();
		// datePicker.updateDate(cal.get(Calendar.YEAR),
		// cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		// mediaTimePicker.setCurrentHour(cal.get(Calendar.HOUR));
		// mediaTimePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
		//
		// }

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.editTextTime :
				showTimePicker();
				break;
			case R.id.editTextDate :
				showDatePicker();
				break;
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		int id = v.getId();
		if (!hasFocus)
			return;

		switch (id) {
			case R.id.editTextTime :
				showTimePicker();
				break;
			case R.id.editTextDate :
				showDatePicker();
				break;
		}

	}

	private void showDatePicker() {
		if (isDialogVisible()) {
			return;
		}
		dateFragment = new DatePickerFragment(media);
		dateFragment.show(getActivity().getFragmentManager(), "datepicker");
		dateFragment.setOnDateSetListener(new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if (dateTime == null) {
					dateTime = new Date();
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateTime);
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				dateTime = cal.getTime();

				editTextDate.setText(DateUtils.dateToString(dateTime, "dd-MM-yyyy"));
			}
		});
	}

	private void showTimePicker() {
		if (isDialogVisible()) {
			return;
		}
		timeFragment = new TimePickerFragment(media);
		timeFragment.show(getActivity().getFragmentManager(), "timepicker");
		timeFragment.setOnTimeSetListener(new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				if (dateTime == null) {
					dateTime = new Date();
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(dateTime);
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				dateTime = cal.getTime();

				editTextTime.setText(DateUtils.dateToString(dateTime, "HH:mm"));
			}
		});
	}

	private boolean isDialogVisible() {
		return (dateFragment != null && dateFragment.isVisible()) || (timeFragment != null && timeFragment.isVisible());
	}

}
