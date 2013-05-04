package com.HuskySoft.metrobike.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Route;

/**
 * @author mengwan
 *
 */
public class DetailsActivity extends Activity {

	/**
     * Results from the search.
     */
    private ArrayList<Route> routes = null;

    /**
     * Current route that should be displayed on the map.
     */
    private int currRoute = -1;
	
	/**
	 * 
	 * onCreate function of DetailsActivity class
	 * Display the details of metroBike search
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setDetails();
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Details");
		@SuppressWarnings("unchecked")
		List<Route> recievedRoutes = (ArrayList<Route>) getIntent()
                .getSerializableExtra("List of Routes");
        if (recievedRoutes != null) {
            routes = (ArrayList<Route>) recievedRoutes;
            currRoute = (Integer) getIntent().
            getSerializableExtra("Current Route Index");
        }
		setContentView(R.layout.activity_details);
	}
	
	/**
	 * 
	 * onCreate menu option of DetailsActivity
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
	
	 /**
     * Invoked when user click setting button in the menu.
     * 
     * @param menuItem
     *            the items in the menu bar
     */
    public final void goToSettingsPage(final MenuItem menuItem) {
        // start the settings activity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the go to Navigate button
	 */
	public void goToNavigate(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, NavigateActivity.class);
    	intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
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
	 * onClick function for the return to result page button
	 */
	
	public void goToResults(View view) {
		// Do something in response to button
    	Intent intent = new Intent(this, ResultsActivity.class);
    	intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
    	startActivity(intent);
	}
	
	/**
	 * 
	 * helper function that sets up the details of a route
	 */
	private void setDetails() {
		//sets up details
	}
}
