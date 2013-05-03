
package com.HuskySoft.metrobike.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * 
 * @author mengwan
 *
 */

public class ResultsActivity extends Activity {
	
	/**
	 * duration of the animated camera in the map
	 */
	private static final int animatedCameraDurationInMilliSecond = 3000;
	
	/**
	 * GoogleMap object stored here to be modified
	 */
	private GoogleMap mMap;
	
	/**
	 * results from the search
	 */
	private ArrayList<Route> routes = null;
	
	
	/**
	 * current route that should be displayed on the map
	 */
	private Route currRoute = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Result");
		
		//get the solution from the search activity
		routes = (ArrayList<Route>) getIntent().getSerializableExtra("List of Routes");
		
		//set the default route to be the first route of the solution
		if (routes.size() > 0) {
			currRoute = routes.get(0);
		}
		setContentView(R.layout.activity_results);	
		mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		drawRoute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	/**
	 * 
	 * @param view: the view of the button
	 * onClick function of the go to Navigate button
	 */
	public void goToNavigate(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, NavigateActivity.class);
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
	 * onClick function of the go to details button
	 */
	public void goToDetail(View view) {
        // Do something in response to button
	   	Intent intent = new Intent(this, DetailsActivity.class);
	   	startActivity(intent);
    }
	
	/**
	 * draw the current route on the map
	 */
	private void drawRoute() {
		if (currRoute != null) {
			
			List<Leg> legs = currRoute.getLegList();
			Location start = legs.get(0).getStartLocation();
			Location end = legs.get(legs.size() - 1).getStepList().get(legs.get(legs.size() - 1).getStepList().size() - 1).getEndLocation();
			
			mMap.addMarker(new MarkerOptions()
	        .position(new LatLng(start.getLatitude() , start.getLongitude()))
	        .title("Start Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.starting)));
			
			mMap.addMarker(new MarkerOptions()
	        .position(new LatLng(end.getLatitude() , end.getLongitude()))
	        .title("End Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ending)));
			
			double cameraLatitude = (start.getLatitude() + end.getLatitude()) / 2;
			double cameraLongitude = (start.getLongitude() + end.getLongitude()) / 2;
			
			PolylineOptions polylineOptions = new PolylineOptions();
			for (Leg l: legs) {
				for (Step s: l.getStepList()) {
					polylineOptions = polylineOptions.add(new LatLng(s.getStartLocation().getLatitude() , s.getStartLocation().getLongitude() ));
					polylineOptions = polylineOptions.add(new LatLng(s.getEndLocation().getLatitude() , s.getEndLocation().getLongitude() ));
				}
			}
			Polyline polyline = mMap.addPolyline(polylineOptions.color(Color.RED));
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cameraLatitude, cameraLongitude), (float) 13.0), animatedCameraDurationInMilliSecond, null);
		}
	}
}