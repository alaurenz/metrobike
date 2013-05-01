/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.util.List;

/**
 * @author coreyh3
 * @author dutchscout
 * Represents a series of related steps to complete a portion of a route.
 */
public class Leg {
	/**
	 * The distance of the leg in meters.
	 */
	private int distanceInMeters;
	/**
	 * The duration of the leg in seconds.
	 */
	private int durationInSeconds;
	/**
	 * The starting location in this leg.
	 */
	private Location startLocation;
	/**
	 * The ending location in this leg.
	 */
	private Location endLocation;
	/**
	 * The starting address (human-readable) in this leg.
	 */
	private String startAddress;
	/**
	 * The ending address (human-readable) in this leg.
	 */
	private String endAddress;
	/**
	 * The list of steps to complete this leg.
	 */
	private List<Step> stepList;
	
	/**
	 * @author coreyh3
	 * @author dutchscout
	 * Represents the shortest portion of a route.
	 */
	public class Step {
		/**
		 * The distance of this step in meters.
		 */
		private int distanceInMeters;
		/**
		 * The duration of this step in seconds.
		 */
		private int durationInSeconds;
		/**
		 * The staring location of this step.
		 */
		private Location startLocation;
		/**
		 * The ending location of this step.
		 */
		private Location endLocation;
		/**
		 * The travel mode for this step (ex: BICYCLING).
		 */
		private TravelMode travelMode;
		/**
		 * Human-readable direction for this step.
		 */
		private String htmlInstruction;
		/**
		 * String-stored list of points for plotting the step on a map.
		 */
		private String polyLinePoints;
	}
}
