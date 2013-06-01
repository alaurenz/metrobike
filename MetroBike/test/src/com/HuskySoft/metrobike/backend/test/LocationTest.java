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
 * check style: Sam Wilson
 */
public class LocationTest extends TestCase {
    /**
     * The expected hashing value.
     */
    private static final int HASHING_VALUE = -1529164004;

    /**
     * The dalta value.
     */
    private static final double DELTA = .001;

    /**
     * This field holds a location object for use by the test methods.
     */
    private Location loc = null;

    /**
     * The latitude of Seattle.
     */
    private static final double LATITUDE = 47.61;

    /**
     * The longitude of Seattle.
     */
    private static final double LONGITUDE = -122.33;

    /**
     * This initializes the Location field for use by the test methods.
     * 
     * @throws Exception
     */
    // @Before
    public final void setUp() {
        // Seattle
        loc = new Location(LATITUDE, LONGITUDE);
    }

    /**
     * WhiteBox: This tests the getLatitude method.
     * 
     * @throws Exception
     *             prevent any exceptions.
     */
    // @Test
    public final void testGetLatitudeTest() throws Exception {
        setUp();
        double expected = LATITUDE;
        double actual = loc.getLatitude();
        double delta = DELTA;

        Assert.assertEquals("Actual value of loc.getLatitude() was: " + actual, expected, actual,
                delta);
    }

    /**
     * WhiteBox: This tests the getLongitude method.
     */
    // @Test
    public final void testGetLongitudeTest() {
        setUp();
        double expected = LONGITUDE;
        double actual = loc.getLongitude();
        double delta = DELTA;

        Assert.assertEquals("Actual value of loc.getLongitude() was: " + actual, expected, actual,
                delta);
    }

    /**
     * WhiteBox: This tests the hashCode method.
     */
    // @Test
    public final void testHashCodeTest() {
        setUp();
        int expected = HASHING_VALUE;
        int actual = loc.hashCode();
        Assert.assertEquals("Actual value of loc.hashCode() was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the equals method.
     */
    // @Test
    public final void testEqualsTest() {
        setUp();
        Assert.assertTrue("Actual value of loc.equals(loc) was: false", loc.equals(loc));
    }

    /**
     * WhiteBox: This tests that the equals method returns false on different
     * objects.
     */
    // @Test
    public final void testEqualsDiffObjectTest() {
        setUp();
        Assert.assertFalse("Actual value of loc.equals(new LinkedList<String>()) was: true",
                loc.equals(new LinkedList<String>()));
    }

    /**
     * WhiteBox: This tests that the equals method returns false when the
     * longitude is incorrect.
     */
    // @Test
    public final void testEqualsWrongLatTest() {
        setUp();
        Assert.assertFalse(
                "Actual value of loc.equals(new Location(latitude, latitude)) was: true",
                loc.equals(new Location(LATITUDE, LATITUDE)));
    }

    /**
     * WhiteBox: This tests that the equals method returns false when the
     * latitude is incorrect. https://github.com/alaurenz/metrobike/issues/94
     */
    // @Test
    public final void testEqualsWrongLongTest() {
        setUp();
        Assert.assertTrue(
                "Actual value of loc.equals(new Location(longitude, longitude)) was: false",
                !loc.equals(new Location(LONGITUDE, LONGITUDE)));
    }

    /**
     * WhiteBox: This tests the toString method.
     */
    // @Test
    public final void testToStringTest() {
        setUp();
        String expected = "    Location:\n        Latitude: 47.61\n        Longitude: -122.33\n";
        loc.setIndent(2);
        String actual = loc.toString();
        Assert.assertEquals("Actual value of loc.toString() was: " + actual, expected, actual);
    }

    /**
     * WhiteBox: This tests the getIndent method.
     */
    // @Test
    public final void testGetIndentTest() {
        setUp();
        int expected = 2;
        loc.setIndent(expected);
        int actual = loc.getIndent();

        Assert.assertEquals("Actual value of loc.getIndent() was: " + actual, expected, actual);
    }
}
