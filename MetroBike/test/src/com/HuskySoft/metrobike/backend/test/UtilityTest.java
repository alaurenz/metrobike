/**
 * 
 */
package com.HuskySoft.metrobike.backend.test;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;

import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.Utility;
import com.HuskySoft.metrobike.backend.Utility.TransitTimeMode;

/**
 * This class tests the backend Utility class.
 * 
 * @author coreyh3
 * 
 * check style: Sam Wilson
 */
public class UtilityTest extends TestCase {

    /**
     * The route time.
     */
    private static final int ROUTE_TIME_2 = 1000;

    /**
     * The route time.
     */
    private static final int ROUTE_TIME_1 = 100;

    /**
     * Human readable duration.
     */
    private static final int SEC_HUMAN_READABLE_DURATION_4 = 20;

    /**
     * Human readable duration.
     */
    private static final int SEC_HUMAN_READABLE_DURATION_3 = 1000;

    /**
     * Human readable duration.
     */
    private static final int SEC_HUMAN_READABLE_DURATION_2 = 6100;

    /**
     * Human readable duration.
     */
    private static final int SEC_HUMAN_READABLE_DURATION_1 = 300000;

    /**
     * The time stamp value.
     */
    private static final int TIME_STAMP = 1368996421;

    /**
     * This is a simple jsonArray to test with.
     */
    private String jsonArrayToStringListTestString = "[Small, Medium, Large]";

    /**
     * This is a bicycle query string to compare against.
     */
    private String buildBicycleQueryStringSingleRouteTestString 
        = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "mode=bicycling&" + "alternatives=false";

    /**
     * This is a bicycle query string to compare against.
     */
    private String buildBicycleQueryStringMultipleRoutesTestString 
        = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "mode=bicycling&" + "alternatives=true";

    /**
     * This is a transit query string to compare against.
     */
    private String buildTransitQueryStringMultipleRoutesArrivalTestString 
        = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "arrival_time=100&" + "mode=transit&" + "alternatives=true";

    /**
     * This is a transit query string to compare against.
     */
    private String buildTransitQueryStringSingleRouteDepartureTestString 
        = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "departure_time=1000&" + "mode=transit&" + "alternatives=false";

    /**
     * WhiteBox: This tests the jsonArrayToStringList method.
     * 
     * @throws JSONException
     *             json exception
     */
    // @Test
    public final void testJsonArrayToStringListTest() throws JSONException {
        JSONArray testArray = new JSONArray(jsonArrayToStringListTestString);
        List<String> testValue = Utility.jsonArrayToStringList(testArray);
        Assert.assertEquals("List's actual size was: " + testValue.size(), 1 + 1 + 1,
                testValue.size());
    }

    /**
     * WhiteBox: This tests the buildBicycleQueryString method with multiple
     * routes.
     * 
     * @throws UnsupportedEncodingException
     *             the unsupported encodeing exception
     */
    // @Test
    public final void testBuildBicycleQueryStringMultipleRoutesTest()
            throws UnsupportedEncodingException {
        boolean multipleRoutes = true;
        String expected = buildBicycleQueryStringMultipleRoutesTestString;
        String actual = Utility.buildBicycleQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", multipleRoutes);
        Assert.assertEquals("Actual value of Utility.buildBicycleQueryString() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the buildBicycleQueryString method with a single
     * route.
     * 
     * @throws UnsupportedEncodingException
     *             the unsupported encodeing exception
     */
    // @Test
    public final void testBuildBicycleQueryStringSingleRouteTest()
            throws UnsupportedEncodingException {
        boolean multipleRoutes = false;
        String expected = buildBicycleQueryStringSingleRouteTestString;
        String actual = Utility.buildBicycleQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", multipleRoutes);
        Assert.assertEquals("Actual value of Utility.buildBicycleQueryString() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the buildTransitQueryString method with multi-Routes
     * and arrival set.
     * 
     * @throws UnsupportedEncodingException
     *             the unsupported encodeing exception
     */
    // @Test
    public final void testBuildTransitQueryStringMultipleRoutesArrivalTest()
     throws UnsupportedEncodingException {
        boolean multipleRoutes = true;
        long routeTime = ROUTE_TIME_1;
        TransitTimeMode timeMode = TransitTimeMode.ARRIVAL_TIME;
        String expected = buildTransitQueryStringMultipleRoutesArrivalTestString;
        String actual = Utility.buildTransitQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", routeTime, timeMode, multipleRoutes);
        Assert.assertEquals("Actual value of Utility.buildTransitQueryString() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the buildTransitQueryString method with a single
     * Routes and arrival set.
     * 
     * @throws UnsupportedEncodingException
     *             the unsupported encodeing exception
     */
    // @Test
    public final void testBuildTransitQueryStringSingleRouteDepartureTest()
            throws UnsupportedEncodingException {
        boolean multipleRoutes = false;
        long routeTime = ROUTE_TIME_2;
        TransitTimeMode timeMode = TransitTimeMode.DEPARTURE_TIME;
        String expected = buildTransitQueryStringSingleRouteDepartureTestString;
        String actual = Utility.buildTransitQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", routeTime, timeMode, multipleRoutes);
        Assert.assertEquals("Actual value of Utility.buildTransitQueryString() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the listPrettyPrint method.
     */
    // @Test
    public final void testListPrettyPrintTest() {
        int indent = 1;
        String expected = "    0: first\n    1: second\n    2: third\n";
        List<String> list = new LinkedList<String>();
        list.add("first");
        list.add("second");
        list.add("third");
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals("Actual value of Utility.listPrettyPrint() was: " + actual, expected,
                actual);
    }

    /**
     * WhiteBox: This tests the listPrettyPrint method with a null list.
     */
    // @Test
    public final void testListPrettyPrintNullTest() {
        int indent = 1;
        String expected = null;
        List<String> list = null;
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals("Actual value of Utility.listPrettyPrint() was: " + actual, expected,
                actual);
    }

    /**
     * WhiteBox: This tests the listPrettyPrint method with a null response.
     */
    // @Test
    public final void testListPrettyPrintZeroSizeNullTest() {
        int indent = 1;
        String expected = null;
        List<String> list = new LinkedList<String>();
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals("Actual value of Utility.listPrettyPrint() was: " + actual, expected,
                actual);
    }

    /**
     * WhiteBox: This tests the getIndentString method.
     */
    // @Test
    public final void testGetIndentStringTest() {
        String expected = "    ";
        String actual = Utility.getIndentString();
        Assert.assertEquals("Actual value of Utility.getIndentString() was: " + actual, expected,
                actual);
    }

    /**
     * WhiteBox: This tests the getSubstepsAsString method and passes the method
     * a null value.
     * 
     * @throws JSONException
     *             the json exception.
     */
    // @Test
    public final void testGetSubstepsAsStringNullTest() throws JSONException {
        String expected = "";
        int indent = 0;
        String actual = Utility.getSubstepsAsString(null, indent);
        Assert.assertEquals("Actual value of Utility.getSubstepsAsString() was: " + actual,
                expected, actual);
    }

    /**
     * WhiteBox: This tests the getSubstepsAsString method and passes the method
     * an empty string.
     * 
     * @throws JSONException the json exception.
     */
    // @Test
    public final void testGetSubstepsAsString0sizeTest() throws JSONException {
        int indent = 1;
        String expected = "";
        String actual = Utility.getSubstepsAsString(new LinkedList<Step>(), indent);
        Assert.assertEquals("Actual value of Utility.getSubstepsAsString() was: " + actual,
                expected, actual);
    }
    
    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration
     * given a very short duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationVeryShort() {
        String expected = "0 minutes";
        String actual = Utility.secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_4);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(20) was: "
                + actual, expected, actual);
    }
    
    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration
     * given a short duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationShort() {
        String expected = "17 minutes";
        String actual = Utility.secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_3);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(1000) was: "
                + actual, expected, actual);
    }
    
    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration
     * given a medium duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationMedium() {
        String expected = "1 hour, 42 minutes";
        String actual = Utility.secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_2);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(6100) was: "
                + actual, expected, actual);
    }
    
    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration
     * given a long duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationLong() {
        String expected = "3 days, 11 hours, 20 minutes";
        String actual = Utility.secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_1);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(300000) was: "
                + actual, expected, actual);
    }
    
    /**
     * Whitebox: This tests the timestampTo12HourTime.
     * NOTE: this test assumes the time is GMT-7
     * See https://github.com/alaurenz/metrobike/issues/154
     */
    // @Test
    public final void testTimestampTo12HourTime() {
        String expected = "1:47 PM";
        String actual = Utility.timestampTo12HourTime(TIME_STAMP,
                TimeZone.getTimeZone("America/Los_Angeles"));
        Assert.assertEquals("Actual value of Utility.timestampTo12HourTime(1368996421) was: "
                + actual, expected, actual);
    }
}
