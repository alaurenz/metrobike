package com.HuskySoft.metrobike.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.History;
import com.HuskySoft.metrobike.ui.utility.MapSetting;
import com.HuskySoft.metrobike.ui.utility.Utility;
import com.HuskySoft.metrobike.ui.utility.Utility.Language;
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
        
        setSystemLocate();
        
        setContentView(R.layout.activity_main);
        
        // Reload localized title: only needed for localization
        getActionBar().setTitle(R.string.app_name);
        
        // the search button
        Button searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        initGoogleMap();
        // onResume should be called so it can update the map
        // create a new empty history object
        History.getInstance();
        readFromHistoryFile();
        
        // Showing log in console for debugging. To be removed for formal
        // release.
        Log.v(TAG, "Finished launching main activity!");
    }

    /**
     * Initialize the language preference.
     */
    private void setSystemLocate() {
        SharedPreferences settings = getSharedPreferences(Utility.LANGUAGE_NAME, 0);
        String lang = settings.getString("language", "NO_LANG");
        
        if (!lang.equals("NO_LANG")) {
            Resources resources = getResources(); 
            Configuration config = resources.getConfiguration(); 
            if (lang.equals("CN")) {
                Log.e(TAG, "Chinese in main");
                config.locale = Locale.SIMPLIFIED_CHINESE;
                Utility.setCurrentLocale(Language.SIMPLIFIED_CHINESE);
            } else {
                Log.e(TAG, "English in main");
                config.locale = Locale.ENGLISH;
                Utility.setCurrentLocale(Language.ENGLISH);
            }
            DisplayMetrics dm = resources.getDisplayMetrics(); 
            resources.updateConfiguration(config, dm);
        } else {
            if (Locale.getDefault().getLanguage().equals("zh") && 
                Locale.getDefault().getCountry().equals("CN")) {
                Log.e(TAG, "No Language set. Use Chinese as default");
                Utility.setCurrentLocale(Language.SIMPLIFIED_CHINESE);
            } else {
                Log.e(TAG, "No Language set. Use English as default");
                Utility.setCurrentLocale(Language.ENGLISH);
            }
        }
    }

    /**
     * Initialize the Google Map.
     */
    private void initGoogleMap() {
        // initialize the background map
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        // only initialize the map setting
        MapSetting.getInstance(googleMap);

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
        case R.id.action_search:
            // user click the search button, start the search activity
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        case R.id.action_settings:
            // user click the setting button, start the settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
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
        try {
            if (googleMap == null) {
                initGoogleMap();
            }
            // update or initialize the map
            MapSetting.updateStatus(googleMap);
            // try to get the current location
            Location currLoc = googleMap.getMyLocation();
            LatLng latLng;
            if (currLoc == null) {
                latLng = new LatLng(LATITUDE, LONGITUDE);
            } else {
                latLng = new LatLng(currLoc.getLatitude(), currLoc.getLongitude());
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
            Log.v(TAG, "Finished launching main activity--onResume!");
        } catch (Exception e) {
            Log.e(TAG, "Fail to move camera, google play service needed to be updated");
        }
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
            History.closeFileStream(fis, null);
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
