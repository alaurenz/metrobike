package com.HuskySoft.metrobike.backend.test;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.HuskySoft.metrobike.backend.Location;

/**
 * This class tests the Location class.
 * 
 * @author coreyh3
 *
 */
public class LocationTest {
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
    @Before
    public void setUp() throws Exception {
        // Seattle
        loc = new Location(latitude, longitude);
    }

    /**
     * This tests the getLatitude method.
     */
    @Test
    public void getLatitudeTest() {
        double expected = latitude;
        double actual = loc.getLatitude();
        double delta = .001;

        Assert.assertEquals(expected, actual, delta);
    }

    /**
     * This tests the getLongitude method.
     */
    @Test
    public void getLongitudeTest() {
        double expected = longitude;
        double actual = loc.getLongitude();
        double delta = .001;

        Assert.assertEquals(expected, actual, delta);
    }

    /**
     * This tests the hashCode method.
     */
    @Test
    public void hashCodeTest() {
        int expected = -1529164004;
        int actual = loc.hashCode();
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the equals method.
     */
    @Test
    public void equalsTest() {
        Assert.assertTrue(loc.equals(loc));
    }

    /**
     * This tests that the equals method returns false on different objects.
     */
    @Test
    public void equalsDiffObjectTest() {
        Assert.assertFalse(loc.equals(new LinkedList<String>()));
    }

    /**
     * This tests that the equals method returns false when the longitude is incorrect.
     */
    @Test
    public void equalsWrongLatTest() {
        Assert.assertFalse(loc.equals(new Location(latitude, 20.0)));
    }

    /**
     * This tests that the equals method returns false when the latitude is incorrect.
     */
    @Test
    public void equalsWrongLongTest() {
        Assert.assertTrue(!loc.equals(new Location(20.00, longitude)));
    }

    /**
     * This tests the toString method.
     */
    @Test
    public void toStringTest() {
        String expected = "    Location:\n        Latitude: 47.61\n        Longitude: -122.33\n";
        loc.setIndent(2);
        String actual = loc.toString();
        Assert.assertEquals(expected, actual);
    }

    /**
     * This tests the getIndent method.
     */
    @Test
    public void getIndentTest() {
        int expected = 2;
        loc.setIndent(expected);
        int actual = loc.getIndent();

        Assert.assertEquals(expected, actual);
    }
}
