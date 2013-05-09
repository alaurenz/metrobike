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
import android.widget.TextView;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;

/**
 * @author mengwan, Xinyun Chen
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
    private TextView directions;
    private TextView start;
    private TextView destnation;
	
	/**
	 * 
	 * onCreate function of DetailsActivity class
	 * Display the details of metroBike search
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		directions = (TextView) findViewById(R.id.directions);
		start = (TextView) findViewById(R.id.directionsStart);
		destnation = (TextView) findViewById(R.id.directionsDest);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Details");
		@SuppressWarnings("unchecked")
		List<Route> recievedRoutes = (ArrayList<Route>) getIntent()
                .getSerializableExtra("List of Routes");
        if (recievedRoutes != null) {
            routes = (ArrayList<Route>) recievedRoutes;
            currRoute = (Integer) getIntent().
            getSerializableExtra("Current Route Index");
            // if there exists route, show the details
            this.setDetails();
        }
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
     * this method will be called when user click buttons in the setting menu.
     * 
     * @param item
     *            the menu item that user will click
     * @return true if user select an item
     */
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            // user click the setting button, start the settings activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the go to Navigate button
	 */
	public void goToNavigate(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, NavigateActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		Route displayRoute = routes.get(currRoute);
		List<Leg> legs = displayRoute.getLegList();
		for (int i = 0; i < legs.size(); i++) {
			Leg curLeg = legs.get(i);
			List<Step> steps = curLeg.getStepList();
			start.append(legs.get(i).getStartAddress());
			for (int j = 0; j < steps.size(); j++) {
				Step s = steps.get(j);
				directions.append("\nStep " + (j+1) + "   " + s.getHtmlInstruction());
			}
			destnation.append(legs.get(i).getEndAddress());
		}
	}
}
