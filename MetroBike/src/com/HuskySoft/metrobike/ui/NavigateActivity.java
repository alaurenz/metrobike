package com.HuskySoft.metrobike.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.TravelMode;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * 
 * @author Xinyun Chen
 * 
 */

public class NavigateActivity extends Activity {

    /**
     * Results from the search.
     */
    private ArrayList<Route> routes;
    
    /**
     * duration of the animated camera in the map.
     */
    private static final int ANIMATED_CAMERA_DURATION_IN_MILLISECOND = 3000;
    
    /**
     * Current Device's screen height.
     */
    private float DP_HEIGHT;
    
    /**
     * Current Device's screen width.
     */
    private float DP_WIDTH;
    
    private GoogleMap mMap;
    private List<Leg> legs;
    private int currentLeg = 0;
    private int currentStep = 0;
    private TextView instr;
    private Button next;
    private Button prev;

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

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        DP_HEIGHT = outMetrics.heightPixels / density;
        DP_WIDTH = outMetrics.widthPixels / density;
        
        @SuppressWarnings("unchecked")
        List<Route> recievedRoutes = (ArrayList<Route>) getIntent().getSerializableExtra(
                "List of Routes");
        setContentView(R.layout.activity_navigate);
        instr = (TextView) findViewById(R.id.direction_instr);
        next = (Button)findViewById(R.id.button_next);
        prev = (Button)findViewById(R.id.button_previous);
        prev.setEnabled(false);
        if (recievedRoutes != null) {
            routes = (ArrayList<Route>) recievedRoutes;
            currRoute = (Integer) getIntent().getSerializableExtra("Current Route Index");
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            drawRoute();
        }
        
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	updateNext();
            	drawRoute();
            }
        });
        
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	updatePrev();
            	drawRoute();
            }
        });
    }
    
    private void updatePrev() {
    	// re-enable the "next" button
    	if (currentStep + 1 == legs.get(currentLeg).getStepList().size()
    			&& currentLeg + 1 == legs.size()) {
    		next.setEnabled(true);
    	}
    	if (currentStep > 0) {
    		currentStep--;
    	} else if (currentLeg > 0) {
    		currentLeg--;
    		currentStep = legs.get(currentLeg).getStepList().size()-1;
    	}
    	// disable the "previous" button when at the start of the steps 
    	if (currentStep == 0 && currentLeg == 0) {
    		prev.setEnabled(false);
    	}
    }
    
    private void updateNext() {
    	int legsize = legs.size();
    	int stepsize = legs.get(currentLeg).getStepList().size();
    	// re-enable the "previous" button
    	if (currentStep == 0 && currentLeg == 0) {
    		prev.setEnabled(true);
    	}
    	if (stepsize > currentStep + 1) {
    		currentStep++;
    	} else if (currentLeg < legsize - 1) {
    		currentStep = 0;
    		currentLeg++;
    	}
    	// disable the "next" button when get to the end of the steps 
    	if (currentStep + 1 == legs.get(currentLeg).getStepList().size()
    			&& currentLeg + 1 == legs.size()) {
    		next.setEnabled(false);
    	}
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
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("List of Routes", (Serializable) routes);
        intent.putExtra("Current Route Index", currRoute);
        startActivity(intent);
    }

    /**
     * draw the current route on the map.
     */
    private void drawRoute() {
        if (currRoute >= 0 && currRoute < routes.size()) {
            //clear the map drawing first
            mMap.clear();
            
            //get the source and destination
            legs = routes.get(currRoute).getLegList();
            Location start = legs.get(0).getStartLocation();
            Location end = legs.get(legs.size() - 1).getStepList()
                    .get(legs.get(legs.size() - 1).getStepList().size() - 1).getEndLocation();

            //draw Markers for starting and ending points
            mMap.addMarker(new MarkerOptions()
                    .position(com.HuskySoft.metrobike.ui.utility.Utility.convertLocation(start))
                    .title("Start Here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.starting)));

            mMap.addMarker(new MarkerOptions()
                    .position(com.HuskySoft.metrobike.ui.utility.Utility.convertLocation(end)).title("End Here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ending)));
            drawSteps(); 
            //set the camera to focus on the route
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                 com.HuskySoft.metrobike.ui.utility.Utility.getCameraCenter(routes.get(currRoute)), 
                 com.HuskySoft.metrobike.ui.utility.Utility.getCameraZoomLevel(routes.get(currRoute), DP_HEIGHT, DP_WIDTH)),
                 ANIMATED_CAMERA_DURATION_IN_MILLISECOND, null);
        }
    }
    
    private void drawSteps() {
    	// get rid of the HTML tags
    	String direction = legs.get(currentLeg).getStepList().get(currentStep).
    			getHtmlInstruction().replaceAll("\\<.*?>","");
    	instr.setText(direction);
    	for (int i = 0; i < legs.size(); i++) {
        	Leg l = legs.get(i);
        	for (int j = 0; j < l.getStepList().size(); j ++) {
        		Step s = l.getStepList().get(j);
        		PolylineOptions polylineOptions = new PolylineOptions();
        		for (LatLng ll : com.HuskySoft.metrobike.ui.utility.Utility.convertLocationList(s.getPolyLinePoints())) {
        			polylineOptions = polylineOptions.add(ll);
        		}
        		
        		if(i == currentLeg && j == currentStep) {
        			if (s.getTravelMode() == TravelMode.TRANSIT) {
            			mMap.addPolyline(polylineOptions.color(Color.argb(200, 255, 0, 0)).width(12f));
            		} else {
            			mMap.addPolyline(polylineOptions.color(Color.argb(200, 0, 0, 255)).width(8f).zIndex(1));
            		}
        		} else {
        		
        			if (s.getTravelMode() == TravelMode.TRANSIT) {
        				mMap.addPolyline(polylineOptions.color(Color.argb(200, 105, 105, 105)).width(12f));
        			} else {
        				mMap.addPolyline(polylineOptions.color(Color.argb(200, 105, 105, 105)).width(8f).zIndex(1));
        			}
        		}
        		mMap.addCircle(new CircleOptions()
        		.center(com.HuskySoft.metrobike.ui.utility.Utility.convertLocation(s.getEndLocation()))
        		.radius(4)
        		.strokeColor(Color.BLACK).strokeWidth(3)
        		.fillColor(Color.WHITE).zIndex(2));
        	}
        }
    }
}