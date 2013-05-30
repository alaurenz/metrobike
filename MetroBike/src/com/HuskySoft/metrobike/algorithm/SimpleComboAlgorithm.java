package com.HuskySoft.metrobike.algorithm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.DirectionsRequest.RequestParameters;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Utility;
import com.HuskySoft.metrobike.backend.Utility.TransitTimeMode;

/**
 * The simplest "combo" algorithm, this class takes a transit Route and replaces
 * the walking steps with bicycling directions.
 *
 * @author Adrian Laurenzi
 */
public final class SimpleComboAlgorithm extends AlgorithmWorker {
	
	/**
	 * Max combo routes to generate (by replacing walking steps in
	 * transit routes with bicycling)
	 */
	private static final int MAX_COMBO_ROUTES_TO_GENERATE = 1;
	
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

        List<Route> unsortedTransitRoutes;
        try {
        	unsortedTransitRoutes = getTransitResults(toProcess.getStartAddress(),
                    toProcess.getEndAddress(), routeTime, timeMode);
        } catch (UnsupportedEncodingException e) {
            return addError(DirectionsStatus.UNSUPPORTED_CHARSET);
        }

        if(unsortedTransitRoutes != null) {
        	List<Route> transitRoutes =
        			Utility.sortRoutesByTransitDuration(unsortedTransitRoutes);
        	
            List<Route> comboResults = new ArrayList<Route>();
            int i = 0;
            while(i < transitRoutes.size() && i < MAX_COMBO_ROUTES_TO_GENERATE) {
            	Route curRoute = transitRoutes.get(i);
            	
            	Route comboRoute = null;
                if(timeMode.equals(TransitTimeMode.DEPARTURE_TIME)) {
                    comboRoute = replaceWalkingWithBicyclingDeparture(
                        curRoute, routeTime);
                } else if(timeMode.equals(TransitTimeMode.ARRIVAL_TIME)) {
                    comboRoute = replaceWalkingWithBicyclingArrival(
                        curRoute, routeTime);
                }
    
                if (comboRoute != null && comboRoute.getLegList().size() > 0) {
                    comboResults.add(comboRoute);
                }
                i++;
            }
            addResults(comboResults);
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

}
