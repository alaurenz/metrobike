package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.R.layout;
import com.HuskySoft.metrobike.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/**
 * 
 * @author mengwan
 *
 */

public class NavigateActivity extends Activity {

	/**
	 * Oncreate function of NavigateActivity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Navigate");
		setContentView(R.layout.activity_navigate);
	}

	/**
	 * onCreate function of Menu on NavigateActivity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_navigate, menu);
		return true;
	}

	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the go to details button
	 */
	public void goToDetail(View view) {
       // Do something in response to button
	   	Intent intent = new Intent(this, NavigateActivity.class);
	   	startActivity(intent);
    }
	
	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the return to search page button
	 */
	public void goToSearchPage(View view) {
       // Do something in response to button
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
    }
	
	/**
	 * 
	 * @param view: the view of the button
	 * onClick funtion for the return to result page button
	 */
	
	public void goToResults(View view) {
		// Do something in response to button
	   	Intent intent = new Intent(this, ResultsActivity.class);
	   	startActivity(intent);
	}
	
}
