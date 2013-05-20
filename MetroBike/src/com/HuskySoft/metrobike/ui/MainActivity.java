package com.HuskySoft.metrobike.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
        
//        Set ActionBar to be translucent and overlaying the map
//        Currently not using this. 
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        ActionBar actionBar = getActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99000000")));
        
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

        // Showing log in console for debugging. To be removed for formal
        // release.
        Log.v("MetroBike", "Finished launching main activity!");
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
        Location loc = googleMap.getMyLocation();
        LatLng latLng = null;
        if (loc == null) {
            latLng = new LatLng(LATITUDE, LONGITUDE);
        } else {
            latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
        Log.v("MetroBike", "Finished launching main activity--onResume!");
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
    }

    /**
     * Override the back button to act like home button. Do this in order to
     * restore back to the current or default (UW) location.
     */
    @Override
    public final void onBackPressed() {
        // call this to act like home button
        moveTaskToBack(true);
    }

}
