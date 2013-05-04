package com.HuskySoft.metrobike.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.HuskySoft.metrobike.R;

/**
 * @author mengwan
 * 
 */
public class DetailsActivity extends Activity {

    /**
     * onCreate function of DetailsActivity class Display the details of
     * metroBike search
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setDetails();
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("Details");
        setContentView(R.layout.activity_details);
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
     * 
     * @param view
     *            : the view of the button onClick function of the go to
     *            Navigate button
     */
    public void goToNavigate(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, NavigateActivity.class);
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
     *            : the view of the button onClick function for the return to
     *            result page button
     */

    public void goToResults(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }

    /**
     * 
     * helper function that sets up the details of a route
     */
    private void setDetails() {
        // sets up details
    }
}
