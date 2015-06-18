package lhrc.group3.tjooner.fragments;

import java.util.Date;

import lhrc.group3.tjooner.GPSTracker;
import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.TjoonerApplication;
import lhrc.group3.tjooner.helpers.DateUtils;
import lhrc.group3.tjooner.models.Group;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.DataSource;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Luuk,Chris
 */
public class InformationFragment extends PreferenceFragment {

	private TextView title, description, dateTime, location, filename, author,
			tjoonerCategory, tags;
	private LinearLayout layoutFilename, layoutDescription, layoutDateTime,
			layoutLocation, layoutAuthor, layoutCopyright;
	private TjoonerApplication app;
	private Media media;

	public InformationFragment(Media media) {
		this.media = media;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.information_fragment, container,
				false);
		setRetainInstance(true);

		app = (TjoonerApplication) getActivity().getApplication();

		title = (TextView) view.findViewById(R.id.textViewTitle);
		description = (TextView) view.findViewById(R.id.textViewDescription);
		dateTime = (TextView) view.findViewById(R.id.textViewDatetime);
		location = (TextView) view.findViewById(R.id.textViewLocation);
		filename = (TextView) view.findViewById(R.id.textViewFilename);
		author = (TextView) view.findViewById(R.id.textViewAuthor);
		tjoonerCategory = (TextView) view
				.findViewById(R.id.textViewTjoonerCategory);
		tags = (TextView) view.findViewById(R.id.textViewTags);

		layoutFilename = (LinearLayout) view.findViewById(R.id.layoutFilename);
		layoutAuthor = (LinearLayout) view.findViewById(R.id.layoutAuthor);
		layoutDateTime = (LinearLayout) view.findViewById(R.id.layoutDateTime);
		layoutLocation = (LinearLayout) view.findViewById(R.id.layoutLocation);
		layoutDescription = (LinearLayout) view
				.findViewById(R.id.layoutDescription);
		layoutCopyright = (LinearLayout) view
				.findViewById(R.id.layoutCopyright);

		// title is a required field, it always has a value.
		title.setText(media.getTitle());
		setText(media.getDescription(), description, layoutDescription);
		setText(media.getDatetime(), dateTime, layoutDateTime);
		setText(media.getFilename(), filename, layoutFilename);
		setText(media.getAuthor(), author, layoutAuthor);
		setText("lat: " + media.getLatitude() + " long: "
				+ media.getLongitude(), location, layoutLocation);

		if (media.getFilename() != null) {
			filename.setText(media.getFilename());
			layoutFilename.setVisibility(view.VISIBLE);
		} else {
			layoutFilename.setVisibility(view.GONE);
		}

		if (media.hasCopyright()) {
			layoutCopyright.setVisibility(View.VISIBLE);
		} else {
			layoutCopyright.setVisibility(View.GONE);
		}

		if (media.getGroupId() != null) {
			Group group = app.getGroup(media.getGroupId());
			tjoonerCategory.setText(group.getDescription());
		}

		tags.setText(media.getTagsString());

		return view;
	}

	private void setText(Date date, TextView textView, LinearLayout layout) {
		String dateString = null;
		if (date != null) {
			dateString = DateUtils.dateToString(date, "dd-MM-yyyy HH:mm");
		}
		setText(dateString, textView, layout);
	}

	private void setText(String text, TextView textView, LinearLayout layout) {
		if (text == null || text.isEmpty()) {
			layout.setVisibility(View.GONE);
		} else {
			textView.setText(text);
			layout.setVisibility(View.VISIBLE);
		}
	}

}
