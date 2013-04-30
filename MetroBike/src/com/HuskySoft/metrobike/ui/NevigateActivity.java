package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.R.layout;
import com.HuskySoft.metrobike.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NevigateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nevigate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_nevigate, menu);
		return true;
	}

}
