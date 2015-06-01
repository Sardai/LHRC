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
		setRetainInstance(true);
		
		//app = (TjoonerApplication) getActivity().getApplication();
		//source = app.DataSource;
		
		title = (TextView) view.findViewById(R.id.textViewTitle);
		description = (TextView) view.findViewById(R.id.textViewDescription);
		dateTime = (TextView) view.findViewById(R.id.textViewDatetime);
		filename = (TextView) view.findViewById(R.id.textViewFilename);
		
		title.setText(media.getTitle());
		if(media.getTitle() != null) {
			title.setText(media.getTitle());
			title.setVisibility(View.VISIBLE);
			} else {
				title.setVisibility(view.GONE);
			}
		if(media.getDescription() != null) {
		description.setText(media.getDescription());
		description.setVisibility(view.VISIBLE);
		} else {
			description.setVisibility(view.GONE);
		}
		if(media.getDatetime() != null) {
		dateTime.setText( media.getDatetime().toString());
		dateTime.setVisibility(view.VISIBLE);
		} else {
			dateTime.setVisibility(view.GONE);
		}
		if(media.getFilename() != null) {
		filename.setText(media.getFilename());
		filename.setVisibility(view.VISIBLE);
		} else {
			filename.setVisibility(view.GONE);
		}
		return view;
	}
	
	
	
}
