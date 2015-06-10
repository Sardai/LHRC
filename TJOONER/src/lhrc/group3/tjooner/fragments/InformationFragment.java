package lhrc.group3.tjooner.fragments;

import lhrc.group3.tjooner.R;
import lhrc.group3.tjooner.TjoonerApplication;
import lhrc.group3.tjooner.models.Media;
import lhrc.group3.tjooner.storage.DataSource;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Luuk
 */
public class InformationFragment extends Fragment{
	private TextView title, description, dateTime, filename;
	private TjoonerApplication app;
	private DataSource source;
	private Media media;
	
	public InformationFragment(Media media) {
		this.media = media;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.information_fragment, container, false);
		
		source = app.DataSource;
		app = (TjoonerApplication) getActivity().getApplication();
		
		title = (TextView) view.findViewById(R.id.textViewTitle);
		description = (TextView) view.findViewById(R.id.textViewDescription);
		dateTime = (TextView) view.findViewById(R.id.textViewDatetime);
		filename = (TextView) view.findViewById(R.id.textViewFilename);
		
		title.setText(media.getTitle());
		
		if(media.getDescription() != null) {
		description.setText(media.getDescription());
		description.setVisibility(View.VISIBLE);
		} else {
			description.setVisibility(View.INVISIBLE);
		}
		if(media.getDatetime() != null) {
		dateTime.setText( media.getDatetime().toString());
		dateTime.setVisibility(View.VISIBLE);
		} else {
			dateTime.setVisibility(View.INVISIBLE);
		}
		if(media.getFilename() != null) {
		filename.setText(media.getFilename());
		filename.setVisibility(View.VISIBLE);
		} else {
			filename.setVisibility(View.INVISIBLE);
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	
}
