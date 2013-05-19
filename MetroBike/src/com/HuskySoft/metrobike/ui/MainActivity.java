package com.HuskySoft.metrobike.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.MapSetting;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * This is a home screen which will show the map, search and detail button.
 * 
 * @author Sam Wilson
 * 
 */
public class MainActivity extends Activity {

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
     * GoogleMap object stored here to be modified.
     */
    private MapSetting mMap;
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
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        createMap();
        // Showing log in console for debugging. To be removed for formal
        // release.
        Log.v("MetroBike", "Finished launching main activity!");
    }

    /**
     * Create the Google map as a background.
     */
    private void createMap() {
        // Noted: mMap.getMyLocation() return null
        mMap = MapSetting.getInstance(googleMap);
        // the top right hand button
        mMap.setCurrentLocationButton(true);
        // the latlng of university of Washington
        LatLng ll = new LatLng(LATITUDE, LONGITUDE);
        mMap.moveCameraTo(CameraUpdateFactory.newLatLngZoom(ll, ZOOM));
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
     * Update the map setting.
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {
        mMap = MapSetting.getInstance(googleMap);
        super.onResume();
    }
}
