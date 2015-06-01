package lhrc.group3.tjooner;

import lhrc.group3.tjooner.adapter.GroupSpinnerAdapter;
import lhrc.group3.tjooner.storage.DataSource;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

public class HugoTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hugo_test);
		
		final DataSource source = new DataSource(this);
		source.open();
		
		final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
		
		 
		
		  	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, source.getTags());
	         textView.setAdapter(adapter);
	         textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	         
	         Button button = (Button)findViewById(R.id.button1);
	         button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String text = textView.getText().toString();
					
					source.insert(text.split(","));
					
				}
			});
	         
	         
	        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	        GroupSpinnerAdapter sadapter = new GroupSpinnerAdapter(source.getGroups());
	        spinner.setAdapter(sadapter); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hugo_test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
