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
 * The simplest "combo" algorithm, this class takes a transit Route and replaces
 * the walking steps with bicycling directions.
 *
 * @author Adrian Laurenzi
 */
public final class SimpleComboAlgorithm extends AlgorithmWorker {

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectionsStatus findRoutes(final RequestParameters toProcess) {
        clearErrors();
        clearResults();

        // NOTE: this method assumes:
        // toProcess.getTravelMode() == TravelMode.MIXED

        // NOTE: This assumes a valid toProcess object (arrival time is set XOR
        // departure time is set). If both the arrival and
        // departure times are set, we'll use the arrival time.
        long routeTime;
        Utility.TransitTimeMode timeMode;
        if (toProcess.getArrivalTime() != 0) {
            timeMode = TransitTimeMode.ARRIVAL_TIME;
            routeTime = toProcess.getArrivalTime();

            // TODO: fix
            // NOTE: currently unsupported
            System.err.println("ERROR: arrival time not yet supported");
            return addError(DirectionsStatus.INVALID_REQUEST_PARAMS);
        } else {
            timeMode = TransitTimeMode.DEPARTURE_TIME;
            routeTime = toProcess.getDepartureTime();
        }

        List<Route> transitRoutes;
        try {
            transitRoutes = getTransitResults(toProcess.getStartAddress(),
                    toProcess.getEndAddress(), routeTime, timeMode);
        } catch (UnsupportedEncodingException e) {
            return addError(DirectionsStatus.UNSUPPORTED_CHARSET);
        }

        // TODO uncomment for comparing combo to normal transit
        // addResults(transitRoutes);
        
        if(transitRoutes != null) {
            List<Route> comboResults = new ArrayList<Route>();
            for (Route curRoute : transitRoutes) {
    
                Route comboRoute = replaceWalkingWithBicycling(
                        curRoute, toProcess.getDepartureTime());
    
                if (comboRoute != null && comboRoute.getLegList().size() > 0) {
                    comboResults.add(comboRoute);
                }
    
                // TODO make this more elegant?
                // For preventing exceeding query request limit
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    /**
     * Replaces all walking Steps within given Route (assumed to be a transit
     * Route) with bicycling directions Steps.
     *
     * NOTE: this method only works for TransitTimeMode.DEPARTURE_TIME
     *
     * @param transitRoute
     *            the transit Route for which walking Steps will be replaced
     *            departureTime the departure time for given Route
     * @param departureTime initial departure time for the given Route
     * @return given Route with walking Steps replaced by bicycling Steps return
     *         null if Route could not be generated
     */
    private Route replaceWalkingWithBicycling(final Route transitRoute,
            final long departureTime) {
        Location curStretchStartLocation = null;
        long curStretchDepartTime = departureTime;
        Step prevStep = null;

        Route comboRoute = new Route();

        Step curStep;
        List<Leg> curLegs = transitRoute.getLegList();
        for (int l = 0; l < curLegs.size(); l++) {
            List<Step> curSteps = curLegs.get(l).getStepList();
            for (int s = 0; s <= curSteps.size(); s++) {
                if (s == curSteps.size()) {
                    curStep = prevStep;
                } else {
                    curStep = curSteps.get(s);
                }

                if (curStretchStartLocation == null) {
                    curStretchStartLocation = curStep.getStartLocation();
                }

                if ((l == (curLegs.size() - 1) && s == curSteps.size())
                        || (prevStep != null
                        && !prevStep.getTravelMode().equals(
                                curStep.getTravelMode()))) {
                    // we reached the [very] last step OR are switching travel
                    // modes

                    List<Route> subRoutes = null;

                    // NOTE: this is needed to deal with end-of-route edge case
                    TravelMode travelModeToCheck = prevStep.getTravelMode();
                    Location curStretchEndLocation = prevStep.getEndLocation();
                    if (l == (curLegs.size() - 1) && s == curSteps.size()) {
                        travelModeToCheck = curStep.getTravelMode();
                        curStretchEndLocation = curStep.getEndLocation();
                    }

                    if (travelModeToCheck.equals(TravelMode.WALKING)) {
                        // ask for bicycling directions for prev stretch
                        try {
                            subRoutes = getBicycleResults(
                                  curStretchStartLocation.getLocationAsString(),
                                  curStretchEndLocation.getLocationAsString());
                        } catch (UnsupportedEncodingException e) {
                            // TODO have this error get "pushed up"
                            // return
                            // addError(DirectionsStatus.UNSUPPORTED_CHARSET);
                            addError(DirectionsStatus.UNSUPPORTED_CHARSET);
                        }

                    } else if (travelModeToCheck.equals(TravelMode.TRANSIT)) {
                        // ask for transit directions for prev stretch
                        try {
                            subRoutes = getTransitResults(
                                  curStretchStartLocation.getLocationAsString(),
                                  curStretchEndLocation.getLocationAsString(),
                                  curStretchDepartTime,
                                  TransitTimeMode.DEPARTURE_TIME);

                        } catch (UnsupportedEncodingException e) {
                            // TODO have this error get "pushed up"
                            // return
                            // addError(DirectionsStatus.UNSUPPORTED_CHARSET);
                            addError(DirectionsStatus.UNSUPPORTED_CHARSET);
                        }

                        // TODO: check if there are any walking steps in each
                        // subRoute
                        // (due to different transit route being returned)
                        // -> if so, replace w/ bicycling by recursively running
                        // this method

                    } else {
                        // TODO: throw exception or something...
                        // unexpected travel mode for this step (not walking or
                        // transit)
                        System.err.println("ERROR: unexpected travel mode");
                        // addError(...)
                        return null;
                    }

                    if (subRoutes == null || subRoutes.size() == 0) {
                        System.err.println("ERROR: no subroutes found");
                        // addError(...)
                        return null;
                    } else {
                        // TODO: use more than just first result
                        Route subRoute = subRoutes.get(0);
                        curStretchDepartTime += subRoute.getDurationInSeconds();
                        for (Leg newLeg : subRoute.getLegList()) {
                            comboRoute.addLeg(newLeg);
                        }
                    }

                    curStretchStartLocation = curStep.getStartLocation();
                }
                prevStep = curStep;
            }
        }
        return comboRoute;
    }

    /**
     * Retrieves transit directions between given
     * start and end addresses.
     *
     * @param startAddress describing the search to make
     * @param endAddress describing the search to make
     * @param routeTime departure or arrival time for search
     * @param timeMode
     *             denoting if searching by departure or arrival time
     * @throws UnsupportedEncodingException
     *             if there is a problem with the default charset
     * @return list of retrieved Routes
     */
    private List<Route> getTransitResults(
            final String startAddress, final String endAddress,
            final long routeTime, final TransitTimeMode timeMode)
            throws UnsupportedEncodingException {

        String queryString = Utility.buildTransitQueryString(
                startAddress, endAddress, routeTime, timeMode, true);

        // Fetch the query results
        String jsonResult = doQueryWithHandling(queryString);

        if (jsonResult != null) {
            // Parse the results
            // TODO remove
            // System.err.println("got JSON (transit):\n " + jsonResult);
            List<Route> result = buildRouteListFromJSONString(jsonResult);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Retrieves bicycle directions between given
     * start and end addresses.
     *
     * @param startAddress describing the search to make
     * @param endAddress describing the search to make
     * @throws UnsupportedEncodingException
     *             if there is a problem with the default charset
     * @return list of retrieved Routes
     */
    private List<Route> getBicycleResults(final String startAddress,
            final String endAddress) throws UnsupportedEncodingException {
        // Build the query string
        String queryString;
        queryString = Utility.buildBicycleQueryString(
                startAddress, endAddress, true);

        // Fetch the query results
        String jsonResult = doQueryWithHandling(queryString);

        if (jsonResult != null) {
            // Parse the results
            List<Route> result = buildRouteListFromJSONString(jsonResult);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
