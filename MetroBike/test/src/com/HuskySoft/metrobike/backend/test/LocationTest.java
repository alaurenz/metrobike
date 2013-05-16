package com.HuskySoft.metrobike.backend.test;

import java.util.LinkedList;

import junit.framework.TestCase;
import junit.framework.Assert;

import com.HuskySoft.metrobike.backend.Location;

/**
 * This class tests the Location class.
 * 
 * @author coreyh3
 *
 */
public class LocationTest extends TestCase {
    /**
     * This field holds a location object for use by the test methods.
     */
    private Location loc = null;
    
    /**
     * The latitude of Seattle.
     */
    private double latitude = 47.61;
    
    /**
     * The longitude of Seattle.
     */
    private double longitude = -122.33;

    /**
     * This initializes the Location field for use by the test methods.
     * 
     * @throws Exception
     */
    //@Before
    public void setUp() {
        // Seattle
        loc = new Location(latitude, longitude);
    }

    /**
     * WhiteBox: This tests the getLatitude method.
     * @throws Exception 
     */
    //@Test
    public void test_getLatitudeTest() throws Exception {
        setUp();
        double expected = latitude;
        double actual = loc.getLatitude();
        double delta = .001;

        Assert.assertEquals("Actual value of loc.getLatitude() was: " + actual, expected, actual, delta);
    }

    /**
     * WhiteBox: This tests the getLongitude method.
     */
    //@Test
    public void test_getLongitudeTest() {
        setUp();
        double expected = longitude;
        double actual = loc.getLongitude();
        double delta = .001;

        Assert.assertEquals("Actual value of loc.getLongitude() was: " + actual, expected, actual, delta);
    }

    /**
     * WhiteBox: This tests the hashCode method.
     */
    //@Test
    public void test_hashCodeTest() {
        setUp();
        int expected = -1529164004;
        int actual = loc.hashCode();
        Assert.assertEquals("Actual value of loc.hashCode() was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the equals method.
     */
    //@Test
    public void test_equalsTest() {
        setUp();
        Assert.assertTrue("Actual value of loc.equals(loc) was: false", loc.equals(loc));
    }

    /**
     * WhiteBox: This tests that the equals method returns false on different objects.
     */
    //@Test
    public void test_equalsDiffObjectTest() {
        setUp();
        Assert.assertFalse("Actual value of loc.equals(new LinkedList<String>()) was: true", loc.equals(new LinkedList<String>()));
    }

    /**
     * WhiteBox: This tests that the equals method returns false when the longitude is incorrect.
     */
    //@Test
    public void test_equalsWrongLatTest() {
        setUp();
        Assert.assertFalse("Actual value of loc.equals(new Location(latitude, latitude)) was: true", loc.equals(new Location(latitude, latitude)));
    }

    /**
     * WhiteBox: This tests that the equals method returns false when the latitude is incorrect.
     * https://github.com/alaurenz/metrobike/issues/94
     */
    //@Test
    public void test_equalsWrongLongTest() {
        setUp();
        Assert.assertTrue("Actual value of loc.equals(new Location(longitude, longitude)) was: false", !loc.equals(new Location(longitude, longitude)));
    }

    /**
     * WhiteBox: This tests the toString method.
     */
    //@Test
    public void test_toStringTest() {
        setUp();
        String expected = "    Location:\n        Latitude: 47.61\n        Longitude: -122.33\n";
        loc.setIndent(2);
        String actual = loc.toString();
        Assert.assertEquals("Actual value of loc.toString() was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the getIndent method.
     */
    //@Test
    public void test_getIndentTest() {
        setUp();
        int expected = 2;
        loc.setIndent(expected);
        int actual = loc.getIndent();

        Assert.assertEquals("Actual value of loc.getIndent() was: " + actual, expected, actual);
    }
}
