package com.HuskySoft.metrobike.algorithm.test;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.HuskySoft.metrobike.backend.DirectionsRequest;
import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.TravelMode;
import com.HuskySoft.metrobike.backend.Utility.TransitTimeMode;

/**
 * This class tests the Algorithm Worker class.
 * 
 * @author mengwan
 * 
 */
public class MixAlgorithmTest extends TestCase {

    /**
     * This holds a directionsRequest object for use by other testing methods.
     */
    private DirectionsRequest request = null;

    /**
     * static value of the time stamp that is needed for setting the request up.
     */
    private static final int TIME_STAMP_FOR_TESTING_ONE = 1371427200;
    
    /**
     * static value of the time stamp that is needed for setting the request up.
     */
    private static final int TIME_STAMP_FOR_TESTING_TWO = 1371168000;
    
    /**
     * This sets up the test class to use a new directions request object in
     * future tests.
     * @param timeMode : departure time or arrival time.
     */
    // @Before
    public final void setUpAdrainToStevens(final TransitTimeMode timeMode) {
        request = new DirectionsRequest();

        String startAddress = "302 NE 50th St,Seattle,WA";
        String endAddress = "3801 Brooklyn Ave NE,Seattle,WA";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        if (timeMode.equals(TransitTimeMode.ARRIVAL_TIME)) {
            request.setArrivalTime(TIME_STAMP_FOR_TESTING_ONE);
        } else {
            request.setDepartureTime(TIME_STAMP_FOR_TESTING_ONE);
        }
        request.setTravelMode(TravelMode.MIXED);
    }

    /**
     * This sets up the test class to use a new directions request object in
     * future tests.
     * 
     * @param timeMode : departure time or arrival time.
     */
    // @Before
    public final void setUpStevensToQinyuan(final TransitTimeMode timeMode) {
        request = new DirectionsRequest();

        String startAddress = "3801 Brooklyn Ave NE,Seattle,WA";
        String endAddress = "2310 48th Street NE, Seattle, WA";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        if (timeMode.equals(TransitTimeMode.ARRIVAL_TIME)) {
            request.setArrivalTime(TIME_STAMP_FOR_TESTING_ONE);
        } else {
            request.setDepartureTime(TIME_STAMP_FOR_TESTING_ONE);
        }
        request.setTravelMode(TravelMode.MIXED);
    }
    
    /**
     * This sets up the test class to use a new directions request object in
     * a failure tranvel mode.
     * 
     */
    // @Before
    public final void setUpStevensToQinyuanBadMode() {
        request = new DirectionsRequest();

        String startAddress = "3801 Brooklyn Ave NE,Seattle,WA";
        String endAddress = "2310 48th Street NE, Seattle, WA";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        request.setDepartureTime(TIME_STAMP_FOR_TESTING_ONE);
        request.setTravelMode(TravelMode.TRANSIT);
    }

    /**
     * Setup a case that is too long for the algorithm to return a combo route.
     * @param timeMode : departure time or arrival time.
     */
    // @Before
    public final void setUpQinyuanToMountRainer(final TransitTimeMode timeMode) {
        request = new DirectionsRequest();

        String startAddress = "2310 48th Street NE, Seattle, WA";
        String endAddress = "Mount Rainier National Park, WA, United States";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        if (timeMode.equals(TransitTimeMode.ARRIVAL_TIME)) {
            request.setArrivalTime(TIME_STAMP_FOR_TESTING_TWO);
        } else {
            request.setDepartureTime(TIME_STAMP_FOR_TESTING_TWO);
        }
        request.setTravelMode(TravelMode.MIXED);
    }

    /**
     * Black Box test: Test if all the steps in the result are in either bicycle
     * mode or transit mode given a departure time.
     */
    // @Test
    public final void testAllStepModeAdrainToStevens() {
        setUpAdrainToStevens(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();
        List<Route> routes = request.getSolutions();
        Assert.assertTrue(routes.size() > 0);

        Assert.assertTrue(allStepsTransitBicycling(routes));
    }

    /**
     * White Box test: Test if all the steps in the result are in either bicycle
     * mode or transit mode given an arrival time.
     */
    // @Test
    public final void testAllStepModeArrivalAdrainToStevens() {
        setUpAdrainToStevens(TransitTimeMode.ARRIVAL_TIME);
        DirectionsStatus result = request.doRequest();

        List<Route> routes = request.getSolutions();
        Assert.assertNotNull(
                "Null routes means getting directions failed.  Errors: "
                        + request.getVerboseErrorMessages() + " return status: "
                        + result.getMessage(), routes);

        Assert.assertTrue(allStepsTransitBicycling(routes));
    }

    /**
     * Black Box test: Test if all the steps in the result are in either bicycle
     * mode or transit mode given a departure time.
     */
    // @Test
    public final void testAllStepModeStevensToQinyuan() {
        setUpStevensToQinyuan(TransitTimeMode.DEPARTURE_TIME);

        DirectionsStatus result = request.doRequest();
        List<Route> routes = request.getSolutions();

        // Multiple asserts here so we get good error messages instead of just
        // exceptions
        Assert.assertNotNull(
                "Null routes means getting directions failed.  Errors: "
                        + request.getVerboseErrorMessages() + " return status: "
                        + result.getMessage(), routes);

        Assert.assertTrue("We didn't get any routes!  (# of routes: " + routes.size()
                + "), status: " + result.getMessage(), routes.size() > 0);

        Assert.assertTrue("Some of our steps aren't bicycle steps!",
                allStepsTransitBicycling(routes));
    }

    /**
     * Returns true if all Steps of all given Routes are either transit or
     * bicycling, false otherwise.
     * 
     * @param routesToCheck
     *            list of routes to check
     * @return true if all steps are transit or bicycling, false otherwise
     */
    private boolean allStepsTransitBicycling(final List<Route> routesToCheck) {
        for (Route r : routesToCheck) {
            for (Leg l : r.getLegList()) {
                for (Step s : l.getStepList()) {
                    boolean allTransitBicycle = s.getTravelMode() == TravelMode.TRANSIT
                            || s.getTravelMode() == TravelMode.BICYCLING;
                    if (!allTransitBicycle) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Black Box test: Test if all the steps started with the same location as
     * the previous steps.
     */
    // @Test
    public final void testAllStepStartEndAdrainToStevens() {
        setUpAdrainToStevens(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();

        List<Route> routes = request.getSolutions();
        for (Route r : routes) {
            Location legStart = null;
            Location stepStart = null;
            Location stepEnd = null;
            for (int i = 0; i < r.getLegList().size(); i++) {
                legStart = r.getLegList().get(i).getStartLocation();
                for (int j = 0; j < r.getLegList().get(i).getStepList().size(); j++) {
                    stepStart = r.getLegList().get(i).getStepList().get(j).getStartLocation();
                    if (j == 0) {
                        Assert.assertEquals(stepStart.getLatitude(), legStart.getLatitude());
                        Assert.assertEquals(stepStart.getLongitude(), legStart.getLongitude());
                    }
                    if (j != 0 && i != 0) {
                        Assert.assertEquals(stepStart.getLatitude(), stepEnd.getLatitude());
                        Assert.assertEquals(stepStart.getLongitude(), stepEnd.getLongitude());
                    }
                    stepEnd = r.getLegList().get(i).getStepList().get(j).getEndLocation();
                }
            }
        }
    }

    /**
     * Black Box test: Test if all the steps started with the same location as
     * the previous steps.
     */
    // @Test
    public final void testAllStepStartEndStevensToQinyuan() {
        setUpStevensToQinyuan(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();

        List<Route> routes = request.getSolutions();

        for (Route r : routes) {
            Location legStart = null;
            Location stepStart = null;
            Location stepEnd = null;
            for (int i = 0; i < r.getLegList().size(); i++) {
                legStart = r.getLegList().get(i).getStartLocation();

                for (int j = 0; j < r.getLegList().get(i).getStepList().size(); j++) {
                    stepStart = r.getLegList().get(i).getStepList().get(j).getStartLocation();
                    if (j == 0) {
                        Assert.assertEquals(stepStart.getLatitude(), legStart.getLatitude());
                        Assert.assertEquals(stepStart.getLongitude(), legStart.getLongitude());
                    }
                    if (j != 0 && i != 0) {
                        Assert.assertEquals(stepStart.getLatitude(), stepEnd.getLatitude());
                        Assert.assertEquals(stepStart.getLongitude(), stepEnd.getLongitude());
                    }
                    stepEnd = r.getLegList().get(i).getStepList().get(j).getEndLocation();
                }
            }
        }
    }

    /**
     * Black Box test: test if every route's duration consists the sum of its
     * legs and steps.
     */
    // @Test
    public final void testTimeAdrainToStevens() {
        setUpAdrainToStevens(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();

        List<Route> routes = request.getSolutions();

        for (Route r : routes) {
            long routeTimeForLeg = r.getDurationInSeconds();
            long routeTimeForSteps = r.getDurationInSeconds();
            for (Leg l : r.getLegList()) {
                long legTimeForSteps = l.getDurationInSeconds();
                routeTimeForLeg -= l.getDurationInSeconds();
                for (Step s : l.getStepList()) {
                    routeTimeForSteps -= s.getDurationInSeconds();
                    legTimeForSteps -= s.getDurationInSeconds();
                }
                Assert.assertEquals(0, legTimeForSteps);
            }
            Assert.assertEquals(0, routeTimeForLeg);
            Assert.assertEquals(0, routeTimeForSteps);
        }
    }

    /**
     * Black Box test: test if every route's duration consists the sum of its
     * legs and steps.
     */
    // @Test
    public final void testTimeStevensToQinyuan() {
        setUpStevensToQinyuan(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();

        List<Route> routes = request.getSolutions();

        for (Route r : routes) {
            long routeTimeForLeg = r.getDurationInSeconds();
            long routeTimeForSteps = r.getDurationInSeconds();
            for (Leg l : r.getLegList()) {
                long legTimeForSteps = l.getDurationInSeconds();
                routeTimeForLeg -= l.getDurationInSeconds();
                for (Step s : l.getStepList()) {
                    routeTimeForSteps -= s.getDurationInSeconds();
                    legTimeForSteps -= s.getDurationInSeconds();
                }
                Assert.assertEquals(0, legTimeForSteps);
            }
            Assert.assertEquals(0, routeTimeForLeg);
            Assert.assertEquals(0, routeTimeForSteps);
        }
    }

    /**
     * Black Box test: the result will be all bicycling if the route is too
     * long.
     */
    // @Test
    public final void testNoTransitQinyuanToMountRainer() {
        setUpQinyuanToMountRainer(TransitTimeMode.DEPARTURE_TIME);

        request.doRequest();

        List<Route> routes = request.getSolutions();

        for (Route r : routes) {
            for (Leg l : r.getLegList()) {
                for (Step s : l.getStepList()) {
                    Assert.assertEquals(TravelMode.BICYCLING, s.getTravelMode());
                }
            }
        }
    }
    
    /**
     * Black Box test: the result will be all bicycling if the route is too
     * long.
     */
    // @Test
    public final void testBadModeStevensToQinyuan() {
        setUpStevensToQinyuanBadMode();

        DirectionsStatus actual = request.doRequest();
        DirectionsStatus expected = DirectionsStatus.UNSUPPORTED_TRAVEL_MODE_ERROR;
        Assert.assertEquals(
                "Actual status for request.doRequest() call was: " + actual.getMessage(), expected,
                actual);
    }
}
