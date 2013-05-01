/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.util.List;

/**
 * @author coreyh3
 * @author dutchscout
 * Represents an entire route from the user's indicated start to end addresses.
 */
public class Route {
	/**
	 * For displaying the route, this is the location of the Northeast corner
	 * of the viewing window.
	 */
	private Location ne_Bound;
	/**
	 * For displaying the route, this is the location of the Southwest corner
	 * of the viewing window.
	 */
	private Location sw_Bound;
	/**
	 * The list of legs that make up this route.
	 */
	private List<Leg> legList;
	/**
	 * String-stored list of points for plotting the entire route on a map.
	 */
	private String polylinePoints;
	/**
	 * A short summary-descriptor for the route.
	 */
	private String summary;
	/**
	 * Google-Maps-indicated warnings that must be displayed for this route.
	 */
	private List<String> warnings;
	
}
