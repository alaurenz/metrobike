
package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the go to Navigate button
	 */
	public void goToNavigate(View view) {
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
	 * onClick function of the go to details button
	 */
	public void goToDetail(View view) {
        // Do something in response to button
	   	Intent intent = new Intent(this, NavigateActivity.class);
	   	startActivity(intent);
    }
}