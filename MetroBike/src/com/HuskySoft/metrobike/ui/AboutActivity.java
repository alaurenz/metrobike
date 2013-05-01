package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * This activity shows the HuskySoft logo.
 * @author Sam Wilson
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
	}

}
