package lhrc.group3.tjooner.fragments;

import java.util.Calendar;

import lhrc.group3.tjooner.R;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class GoogleMapFragment extends DialogFragment  {
	private WebView webView;
	private String longitude;
	private String latitude;
	
	public GoogleMapFragment(String longitude, String latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.google_maps_fragment, container, false);
		webView = (WebView) view.findViewById(R.id.webViewGoogleMaps);
		String url = "http://maps.google.com/maps?&z=10&q="+latitude+"+"+longitude+"&ll="+latitude+"+"+longitude;
		webView.loadUrl(url);
		return view;
	}

}
