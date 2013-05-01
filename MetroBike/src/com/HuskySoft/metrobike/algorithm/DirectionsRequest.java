/**
 * 
 */
package com.HuskySoft.metrobike.algorithm;

import java.util.List;

import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.TravelMode;

/**
 * @author coreyh3
 * @author dutchscout
 * Represents a request for directions to the MetroBike backend.
 */
public class DirectionsRequest {
	/**
	 * The starting location that the user wants directions from.
	 */
	private String startAddress;
	/**
	 * The ending location that the user wants directions to.
	 */
	private String endAddress;
	/**
	 * Time the user would like to arrive at their destination.  
	 * If this is set, then departure time should not be set.
	 */
	private long arrivalTime;
	/**
	 * Time the user would like to depart from their starting location.  
	 * If this is set, then arrival time should not be set.
	 */
	private long departureTime;
	/**
	 * The travel mode the user is requesting directions for.  Default is "mixed."
	 */
	private TravelMode travelMode;
	/**
	 * An optional field.  It sets a minimum distance the user would like to bike.
	 */
	private int minDistanceToBikeInMeters;
	/**
	 * An optional field.  It sets the maximum distance the user would like to bike. 
	 * (In case they are exercise averse)
	 */
	private int maxDistanceToBikeInMeters;
	/**
	 * An optional field.  The minimum number of bus transfers to have.
	 * In case they like multiple transfers.
	 */
	private int minNumberBusTransfers;
	/**
	 * An optional field.  Setting the maximum number of bus transfers in case
	 * the user doesn't want routes with lots of transfers.
	 */
	private int maxNumberBusTransfers;
	/**
	 * When the request is complete, this holds a list of all of the chosen routes.
	 */
	private List<Route> solutions;
	/**
	 * Initiates the request calculation.  This is a blocking call.
	 */
	public void doRequest() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Set the starting address for the trip.
	 * @param startAddress the address to start from.
	 * @return the modified DirectionsRequest object.
	 * Used as part of the builder pattern.
	 */
	public DirectionsRequest setStartAddress(String startAddress){
		this.startAddress = startAddress;
		return this;
	}
}
