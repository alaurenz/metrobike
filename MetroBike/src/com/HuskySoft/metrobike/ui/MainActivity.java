package com.HuskySoft.metrobike.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.HuskySoft.metrobike.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button searchButton = (Button) findViewById(R.id.buttonSearch);
		searchButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent intent = new Intent(v.getContext(), SearchActivity.class);
		    	startActivity(intent);
		    }
		});
		
		Button DetailsButton = (Button) findViewById(R.id.buttonDetails);
		DetailsButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent intent = new Intent(v.getContext(), DetailsActivity.class);
		    	startActivity(intent);
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	
}
