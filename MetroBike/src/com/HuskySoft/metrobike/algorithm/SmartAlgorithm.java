package com.HuskySoft.metrobike.algorithm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.DirectionsRequest.RequestParameters;
import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.TravelMode;
import com.HuskySoft.metrobike.backend.Utility;
import com.HuskySoft.metrobike.backend.Utility.TransitTimeMode;

/**
 * The "smart" algorithm calculates locations along the diagonal 
 * between the input start and end locations to query the transit API 
 * with and then swaps in bicycling directions with most promising 
 * transit routes.
 *
 * @author Adrian Laurenzi
 */
public final class SmartAlgorithm extends AlgorithmWorker {
	
	/**
     * The TAG to use in this file for Android Log messages.
     */
    private static final String TAG = "SmartAlgorithm ";
	
	/**
	 * Distance (in degrees) to move along diagonal between
	 * start and end locations
	 * NOTE: 0.015 is approximately 1 mile
	 */
	private static final double BIAS_DIST_DEG = 0.02; //0.015;
	
    /**
     * {@inheritDoc}
     */
    @Override
    public DirectionsStatus findRoutes(final RequestParameters toProcess) {
        clearErrors();

        // NOTE: this method assumes:
        // toProcess.getTravelMode() == TravelMode.MIXED

        // NOTE: This assumes a valid toProcess object (arrival time is set XOR
        // departure time is set). If both the arrival and
        // departure times are set, we'll use the arrival time.
        long routeTime;
        Utility.TransitTimeMode timeMode;
        if (toProcess.getArrivalTime() != RequestParameters.DONT_CARE) {
            timeMode = TransitTimeMode.ARRIVAL_TIME;
            routeTime = toProcess.getArrivalTime();
        } else {
            timeMode = TransitTimeMode.DEPARTURE_TIME;
            routeTime = toProcess.getDepartureTime();
        }
        
        // extract start and end locations (lat/long rather than addresses) 
        // from some existing solution route 
        Route tmpRoute = getReferencedRoute();
        if (tmpRoute == null) {
        	System.out.println(TAG + "No reference route found to use " +
        			"for getting lat/long for route start and end locations." +
        			" Ensure another algorithm was run before this one.");
        	addError(DirectionsStatus.NO_RESULTS_FOUND);
        	return getMostRecentStatus();
        }
        List<Leg> tmpLegList = tmpRoute.getLegList();
        Location startLocation = tmpLegList.get(0).getStartLocation();
        Location endLocation = tmpLegList.get(tmpLegList.size() - 1).getEndLocation();
        
        
        // TODO dont run this algorithm if deg between start and end < (BIAS_DIST_DEG * 4)
        
        Location startBiasedLocation = getLocationAlongDiagonal(startLocation,
        		endLocation,BIAS_DIST_DEG);
        Location endBiasedLocation = getLocationAlongDiagonal(endLocation,
        		startLocation, BIAS_DIST_DEG);
        
        // run 2 queries: startBiasedLoc -> end and start -> endBiasedLoc 
        // (maybe also: startBiased -> endBiased)
        // store all results in potentialTransitRoutes
        List<Route> potentialTransitRoutes = new ArrayList<Route>();
        try {
        	List<Route> startBiasedRoutes = getTransitResults(
        			startBiasedLocation.getLocationAsString(),
        			endLocation.getLocationAsString(), routeTime, timeMode);
        	if(startBiasedRoutes != null) {
        		potentialTransitRoutes.addAll(startBiasedRoutes);
        	}
        	
        	List<Route> endBiasedRoutes = getTransitResults(
        			startLocation.getLocationAsString(),
        			endBiasedLocation.getLocationAsString(), routeTime, timeMode);
            if(endBiasedRoutes != null) {
            	potentialTransitRoutes.addAll(endBiasedRoutes);
            }
        } catch (UnsupportedEncodingException e) {
        	return addError(DirectionsStatus.UNSUPPORTED_CHARSET);
        }
        
        Route smartRoute = null;
        if(potentialTransitRoutes.size() == 0) {
        	System.err.println(TAG + "No potential transit routes.");
        } else {
	        Route shortestTimeInTransit =
	        		Utility.sortRoutesByTransitDuration(potentialTransitRoutes).get(0);
	        
	        // remove start and/or end walking steps/legs
	        Route preliminarySmartRoute = removeStartEndNonTransitSteps(shortestTimeInTransit);
	        
	        // add bicycling steps from biased start/end to target start/end
	        List<Step> preliminarySmartSteps = preliminarySmartRoute.getLegList()
	        		.get(0).getStepList();
	        if(preliminarySmartSteps.size() == 0) {
	        	System.err.println(TAG + "Preliminary smart route had 0 steps.");
	        } else {
		        Location smartStartLocation = preliminarySmartSteps.get(0).getStartLocation();
		        Location smartEndLocation = preliminarySmartSteps.get(
		        		preliminarySmartSteps.size() - 1).getEndLocation();
		        if(!smartStartLocation.equals(startLocation)) {
		        	List<Route> bicyclingSubRoutes = null;
		        	try {
		        		bicyclingSubRoutes = getBicycleResults(
		        				startLocation.getLocationAsString(),
		        				smartStartLocation.getLocationAsString());
		            } catch (UnsupportedEncodingException e) {
		                addError(DirectionsStatus.UNSUPPORTED_CHARSET);
		            }
		        	if (bicyclingSubRoutes == null || bicyclingSubRoutes.size() == 0) {
		                System.err.println(TAG + "No bicycling subroutes found");
		                preliminarySmartRoute = null;
		            } else {
		            	for(Leg l : bicyclingSubRoutes.get(0).getLegList()) {
		            		preliminarySmartRoute.addLegBeginning(l);
		            	}
		            }
		        }
		        if(!smartEndLocation.equals(endLocation)) {
		        	List<Route> bicyclingSubRoutes = null;
		        	try {
		        		bicyclingSubRoutes = getBicycleResults(
		        				smartEndLocation.getLocationAsString(),
		        				endLocation.getLocationAsString());
		            } catch (UnsupportedEncodingException e) {
		                addError(DirectionsStatus.UNSUPPORTED_CHARSET);
		            }
		        	if (bicyclingSubRoutes == null || bicyclingSubRoutes.size() == 0) {
		                System.err.println(TAG + "No bicycling subroutes found");
		                preliminarySmartRoute = null;
		            } else {
		            	for(Leg l : bicyclingSubRoutes.get(0).getLegList()) {
		            		preliminarySmartRoute.addLeg(l);
		            	}
		            }
		        }
		        
		        if(preliminarySmartRoute != null) {
			        if (timeMode.equals(TransitTimeMode.ARRIVAL_TIME)) {
			        	smartRoute =
			        			replaceWalkingWithBicyclingArrival(preliminarySmartRoute, routeTime);			
			        } else if (timeMode.equals(TransitTimeMode.DEPARTURE_TIME)) {
			        	smartRoute =
			        			replaceWalkingWithBicyclingDeparture(preliminarySmartRoute, routeTime);
			        }
		        }
	        }
        }
        
        if (smartRoute != null && smartRoute.getLegList().size() > 0) {
            // add result to solutions
            List<Route> smartRoutes = new ArrayList<Route>();
        	smartRoutes.add(smartRoute);
        	addResults(smartRoutes);
        }

        // If we got no results, return the appropriate status code
        if (getResults() == null || getResults().size() == 0) {
            if (!hasErrors()) {
                // If we didn't notice not getting results somehow, add this
                // error manually.
                addError(DirectionsStatus.NO_RESULTS_FOUND);
            }
            return getMostRecentStatus();
        }

        return markSuccessful();
    }
    
    /**
     * Returns a location along the diagonal between given start
     * and end locations that is distanceInDeg away from start.
     * 
     * @param start location (of diagonal)
     * @param end location (of diagonal)
     * @param distanceInDeg distance in miles along diagonal 
     * 		   from start location
     *
     */
    private Location getLocationAlongDiagonal(Location start,
    		Location end, double distanceInDeg) {
    	double latDegDiff = end.getLatitude() - start.getLatitude();
    	double longDegDiff = end.getLongitude() - start.getLongitude();
    	
    	// these variables are used to determine which way to move 
    	// along diagonal 
    	int latDirectionCoeff = 1;
    	if(latDegDiff < 0) {
    		latDirectionCoeff = -1;
    	}
    	int longDirectionCoeff = 1;
    	if(longDegDiff < 0) {
    		longDirectionCoeff = -1;
    	}
    	
    	double theta = Math.atan2(
                Math.abs(longDegDiff), Math.abs(latDegDiff));
        double newLat = (latDirectionCoeff 
                * distanceInDeg * Math.cos(theta)) + start.getLatitude();
        double newLong = (longDirectionCoeff 
                * distanceInDeg * Math.sin(theta)) + start.getLongitude();
    	
    	return new Location(newLat, newLong);
    }

    
    /**
     * Removes non-transit steps from beginning and end of
     * given transit route.
     * 
     * @param route to remove steps from
     * 
     * @return route made up of one leg that includes
     * 			all steps not removed from given route
     */
    private Route removeStartEndNonTransitSteps(Route transitRoute) {
    	Route result = new Route();
    	List<Step> tmpSteps = new ArrayList<Step>();
    	boolean addSteps = false;
    	for (Leg curLeg : transitRoute.getLegList()) {
    		for (Step curStep : curLeg.getStepList()) {
    			System.err.println(curStep.getStartLocation().getLocationAsString()
            			+ " to " + curStep.getEndLocation().getLocationAsString());
        		
    			if (curStep.getTravelMode().equals(TravelMode.TRANSIT)) {
    				addSteps = true;
    			}
    			if(addSteps) {
    				tmpSteps.add(curStep);
    			}
    		}
    	}
    	
    	for(int i = tmpSteps.size() - 1; i >= 0; i--) {
    		Step curStep = tmpSteps.get(i);
    		if (!curStep.getTravelMode().equals(TravelMode.TRANSIT)) {
    			tmpSteps.remove(i);
			} else {
				break;
			}
    	}
    	
    	Leg tmpLeg = new Leg();
    	for(Step s : tmpSteps) {
    		tmpLeg.addStep(s);
    	}
    	
    	result.addLeg(tmpLeg);
    	return result;
    }
}
