package lhrc.group3.tjooner.web;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetAddressAsnycTask extends AsyncTask<String, Double, List<Address>> {
	private TextView addressTextView;
	private String longitude;
	private String latitude;
	private Activity activity;
	private LinearLayout layout;
	
	public SetAddressAsnycTask(TextView addressTextView, String longitude, String latitude, Activity activity, LinearLayout layout) {
		this.addressTextView = addressTextView;
		this.longitude = longitude;
		this.latitude = latitude;
		this.activity = activity;
		this.layout = layout;
	}

	@Override
	protected List<Address> doInBackground(String... params) {
		Geocoder coder = new Geocoder(activity);
		try {
			return coder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(List<Address> result) {
		layout.setVisibility(View.VISIBLE);
		if(result != null && result.size() > 0){
			Address address = result.get(0);
			String addressString = "";
			if(address.getThoroughfare() != null){
				addressString = addressString + address.getThoroughfare() + ", ";
			}
			if(address.getPostalCode() != null){
				addressString = addressString + address.getPostalCode() + ", ";
			}
			
			if(address.getLocality() != null){
				addressString = addressString + address.getLocality() + ", ";
			}
			if(address.getCountryName() != null){
				addressString = addressString + address.getCountryName();
			}
			
			addressTextView.setText(addressString);
		} else {
			addressTextView.setText("Click here to show location on google maps");
		}
		super.onPostExecute(result);
	}

}
