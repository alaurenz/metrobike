/**
 * 
 */
package com.HuskySoft.metrobike.backend.test;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
//import org.junit.Assert;
//import org.junit.Test;

import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.Utility;
import com.HuskySoft.metrobike.backend.Utility.TransitTimeMode;

/**
 * This class tests the backend Utility class.
 * 
 * @author coreyh3
 * 
 */
public class UtilityTest extends TestCase {

    /**
     * This is a simple jsonArray to test with.
     */
    private String jsonArrayToStringListTestString = "[Small, Medium, Large]";

    /**
     * This is a bicycle query string to compare against.
     */
    private String buildBicycleQueryString_SingleRouteTestString = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "mode=bicycling&" + "alternatives=false";

    /**
     * This is a bicycle query string to compare against.
     */
    private String buildBicycleQueryString_MultipleRoutesTestString = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "mode=bicycling&" + "alternatives=true";

    /**
     * This is a transit query string to compare against.
     */
    private String buildTransitQueryString_MultipleRoutes_ArrivalTestString = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "arrival_time=100&" + "mode=transit&" + "alternatives=true";

    /**
     * This is a transit query string to compare against.
     */
    private String buildTransitQueryString_SingleRoute_DepartureTestString = "http://maps.googleapis.com/maps/api/directions/json?"
            + "origin=6504+Latona+Ave+NE%2CSeattle%2CWA&"
            + "destination=3801+Brooklyn+Ave+NE%2CSeattle%2CWA&"
            + "sensor=true&"
            + "departure_time=1000&" + "mode=transit&" + "alternatives=false";

    /**
     * This tests the jsonArrayToStringList method.
     * 
     * @throws JSONException
     */
    //@Test
    public void test_jsonArrayToStringListTest() throws JSONException {
        JSONArray testArray = new JSONArray(jsonArrayToStringListTestString);
        List<String> testValue = Utility.jsonArrayToStringList(testArray);
        Assert.assertEquals("List was not the expected size", 3, testValue.size());
    }

    /**
     * This tests the buildBicycleQueryString method with multiple routes.
     * 
     * @throws UnsupportedEncodingException
     */
    //@Test
    public void test_buildBicycleQueryStringMultipleRoutesTest() throws UnsupportedEncodingException {
        boolean multipleRoutes = true;
        String expected = buildBicycleQueryString_MultipleRoutesTestString;
        String actual = Utility.buildBicycleQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", multipleRoutes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the buildBicycleQueryString method with a single route.
     * 
     * @throws UnsupportedEncodingException
     */
    //@Test
    public void test_buildBicycleQueryStringSingleRouteTest() throws UnsupportedEncodingException {
        boolean multipleRoutes = false;
        String expected = buildBicycleQueryString_SingleRouteTestString;
        String actual = Utility.buildBicycleQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", multipleRoutes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the buildTransitQueryString method with multi-Routes and arrival set.
     * 
     * @throws UnsupportedEncodingException
     */
    //@Test
    public void test_buildTransitQueryStringMultipleRoutes_ArrivalTest()
            throws UnsupportedEncodingException {
        boolean multipleRoutes = true;
        long routeTime = 100;
        TransitTimeMode timeMode = TransitTimeMode.ARRIVAL_TIME;
        String expected = buildTransitQueryString_MultipleRoutes_ArrivalTestString;
        String actual = Utility.buildTransitQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", routeTime, timeMode, multipleRoutes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the buildTransitQueryString method with a single Routes and arrival set.
     * 
     * @throws UnsupportedEncodingException
     */
    //@Test
    public void test_buildTransitQueryStringSingleRoute_DepartureTest()
            throws UnsupportedEncodingException {
        boolean multipleRoutes = false;
        long routeTime = 1000;
        TransitTimeMode timeMode = TransitTimeMode.DEPARTURE_TIME;
        String expected = buildTransitQueryString_SingleRoute_DepartureTestString;
        String actual = Utility.buildTransitQueryString("6504 Latona Ave NE,Seattle,WA",
                "3801 Brooklyn Ave NE,Seattle,WA", routeTime, timeMode, multipleRoutes);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the listPrettyPrint method.
     */
    //@Test
    public void test_listPrettyPrintTest() {
        int indent = 1;
        String expected = "    0: first\n    1: second\n    2: third\n";
        List<String> list = new LinkedList<String>();
        list.add("first");
        list.add("second");
        list.add("third");
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the listPrettyPrint method with a null list.
     */
    //@Test
    public void test_listPrettyPrintNullTest() {
        int indent = 1;
        String expected = null;
        List<String> list = null;
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the listPrettyPrint method with a null response.
     */
    //@Test
    public void test_listPrettyPrintZeroSizeNullTest() {
        int indent = 1;
        String expected = null;
        List<String> list = new LinkedList<String>();
        String actual = Utility.listPrettyPrint(list, indent);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the getIndentString method.
     */
    //@Test
    public void test_getIndentStringTest() {
        String expected = "    ";
        String actual = Utility.getIndentString();
        Assert.assertEquals("IndentString was not the expected value", expected, actual);
    }

    /**
     * This tests the getSubstepsAsString method and passes the method a null value.
     * 
     * @throws JSONException
     */
    //@Test
    public void test_getSubstepsAsStringNullTest() throws JSONException {
        String expected = "";
        int indent = 0;
        String actual = Utility.getSubstepsAsString(null, indent);
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the getSubstepsAsString method and passes the method an empty string.
     * 
     * @throws JSONException
     */
    //@Test
    public void test_getSubstepsAsString0sizeTest() throws JSONException {
        int indent = 1;
        String expected = "";
        String actual = Utility.getSubstepsAsString(new LinkedList<Step>(), indent);
        Assert.assertEquals(expected, actual);
    }
}
