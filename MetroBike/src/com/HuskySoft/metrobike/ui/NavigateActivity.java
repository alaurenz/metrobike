package com.HuskySoft.metrobike.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Route;

import android.os.Bundle;
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
    private ArrayList<Route> routes;

    /**
     * Current route that should be displayed on the map.
     */
    private int currRoute = -1;

    /**
     * onCreate function of DetailsActivity class Display the details of
     * metroBike search.
     * 
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressWarnings("unchecked")
        List<Route> recievedRoutes = (ArrayList<Route>) getIntent().getSerializableExtra(
                "List of Routes");
        if (recievedRoutes != null) {
            routes = (ArrayList<Route>) recievedRoutes;
            currRoute = (Integer) getIntent().getSerializableExtra("Current Route Index");
        }
        setContentView(R.layout.activity_navigate);
    }

    /**
     * Show the menu bar when the setting button is clicked.
     * 
     * @param menu
     *            The options menu in which you place your items.
     * @return true if the menu to be displayed.
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
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
     * Direct to detail page.
     * 
     * @param view
     *            the view of the button onClick function of the go to details
     *            button
     */
    public final void goToDetail(final View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
        startActivity(intent);
    }

    /**
     * Direct to search page.
     * 
     * @param view
     *            the view of the button onClick function of the return to
     *            search page button
     */
    public final void goToSearchPage(final View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Direct to result page.
     * 
     * @param view
     *            the view of the button onClick funtion for the return to
     *            result page button
     */

    public final void goToResults(final View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
        startActivity(intent);
    }

}