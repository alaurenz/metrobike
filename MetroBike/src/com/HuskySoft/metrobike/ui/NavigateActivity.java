package com.HuskySoft.metrobike.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Route;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * 
 * @author mengwan
 * 
 */

public class NavigateActivity extends Activity {
	
	/**
     * Results from the search.
     */
    private ArrayList<Route> routes = null;

    /**
     * Current route that should be displayed on the map.
     */
    private int currRoute = -1;

    /**
     * Oncreate function of NavigateActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("Navigate");
        
        @SuppressWarnings("unchecked")
		List<Route> recievedRoutes = (ArrayList<Route>) getIntent()
                .getSerializableExtra("List of Routes");
        if (recievedRoutes != null) {
            routes = (ArrayList<Route>) recievedRoutes;
            currRoute = (Integer) getIntent().
            getSerializableExtra("Current Route Index");
        }
        setContentView(R.layout.activity_navigate);
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
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
     * @param view
     *            : the view of the button onClick function of the go to details
     *            button
     */
    public void goToDetail(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
        startActivity(intent);
    }

    /**
     * 
     * @param view
     *            : the view of the button onClick function of the return to
     *            search page button
     */
    public void goToSearchPage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * @param view
     *            : the view of the button onClick funtion for the return to
     *            result page button
     */

    public void goToResults(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
        startActivity(intent);
    }

}
