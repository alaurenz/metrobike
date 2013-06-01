package com.HuskySoft.metrobike.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.History;
import com.HuskySoft.metrobike.ui.utility.MapSetting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * This is a home screen which will show the map, search and detail button.
 * 
 * @author Sam Wilson, Shuo Wang
 * 
 */
public class MainActivity extends FragmentActivity {

    /**
     * The tag of this class.
     */
    private static final String TAG = "MainActivity";

    /**
     * The latitude value of University of Washington.
     */
    private static final double LATITUDE = 47.65555089999999;
    /**
     * The longitude value of University of Washington.
     */
    private static final double LONGITUDE = -122.30906219999997;
    /**
     * Zoom level.<br>
     * 2.0 the highest zoom out and 21.0 the lowest zoom in
     */
    private static final float ZOOM = 15.0f;

    /**
     * The actual GoogleMap object.
     */
    private GoogleMap googleMap;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // the search button
        Button searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        // initialize the background map
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        // only initialize the map setting
        MapSetting.getInstance(googleMap);
        // onResume should be called so it can update the map
        // create a new empty history object
        History.getInstance();
        readFromHistoryFile();
        // Showing log in console for debugging. To be removed for formal
        // release.
        Log.v(TAG, "Finished launching main activity!");
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
        Log.i(TAG, "Setting menu pops up");
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
     * Update the map setting. Refresh the map.
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {
        super.onResume();
        // update or initialize the map
        MapSetting.updateStatus(googleMap);
        // try to get the current location
        // would be in version 1.0
        LatLng latLng = new LatLng(LATITUDE, LONGITUDE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
        Log.v(TAG, "Finished launching main activity--onResume!");
    }

    /**
     * Destroy Map Related parameters such as map settings.
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected final void onDestroy() {
        super.onDestroy();
        MapSetting.resetMapSetting();
        Log.v(TAG, "Destory MetroBike");
    }

    /**
     * Read the saved history file.
     */
    private void readFromHistoryFile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(History.FILENAME);
            History.readFromFile(fis);
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Cannot open history file");
        } finally {
            // close the file input stream
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Cannot close the file input stream");
            }
        }
    }

    /**
     * Override the back button to act like home button. Do this in order to
     * restore back to the current or default (UW) location.
     */
    @Override
    public final void onBackPressed() {
        // call this to act like home button
        moveTaskToBack(true);
        Log.v(TAG, "Back in here will be close the app");
    }

}
