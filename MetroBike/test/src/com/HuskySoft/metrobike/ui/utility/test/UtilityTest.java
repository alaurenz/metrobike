package com.HuskySoft.metrobike.ui.utility.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import android.graphics.Bitmap;

import com.HuskySoft.metrobike.backend.DirectionsRequest;
import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.TravelMode;
import com.HuskySoft.metrobike.ui.utility.Utility;
import com.google.android.gms.maps.model.LatLng;

/**
 * This class tests the ui Utility class.
 * 
 * @author dutchscout
 * 
 */
public class UtilityTest extends TestCase {

    /**
     * Fix value 1. Need this in order to pass the checkstyle.
     */
    private static final int SIZE = 100;

    /**
     * Fix value 2. Need this in order to pass the checkstyle.
     */
    private static final int D_TIME = 4000000;

    /**
     * Fix value 3. Need this in order to pass the checkstyle.
     */
    private static final int BUS_TRSF = 5;

    /**
     * Fix value 4. Need this in order to pass the checkstyle.
     */
    private static final float ZOOM_1 = 600f;

    /**
     * Fix value 5. Need this in order to pass the checkstyle.
     */
    private static final float ZOOM_2 = 250f;
    
    /**
     * Fix value 6. Need this in order to pass the checkstyle.
     */
    private static final int YEAR_2013 = 2013;
    
    /**
     * Fix value 7. Need this in order to pass the checkstyle.
     */
    private static final int INT_3 = 3;
    
    /**
     * Fix value 8. Need this in order to pass the checkstyle.
     */
    private static final int INT_5 = 5;
    
    /**
     * Fix value 9. Need this in order to pass the checkstyle.
     */
    private static final int INT_8 = 8;
    
    /**
     * Fix value 10. Need this in order to pass the checkstyle.
     */
    private static final int INT_9 = 9;
    
    /**
     * Fix value 11. Need this in order to pass the checkstyle.
     */
    private static final int INT_11 = 11;
    
    /**
     * Fix value 12. Need this in order to pass the checkstyle.
     */
    private static final int INT_15 = 15;
    
    /**
     * Fix value 13. Need this in order to pass the checkstyle.
     */
    private static final int INT_31 = 31;

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
     * WhiteBox: This test the case of converting a null Location to a LatLng.
     */
    public final void testConvertNullLocationToLatLng() {
        Location testLocation = null;
        LatLng testLatLng = Utility.convertLocation(testLocation);
        Assert.assertNull("LatLng from null Location should be null.", testLatLng);
    }

    /**
     * BlackBox test: Test if LatLng consists with the Location.
     */
    public final void testConvertListOfLocation() {
        List<Location> toTest = new ArrayList<Location>();
        for (int i = 1; i <= SIZE; i++) {
            toTest.add(new Location(i * 1.0, (SIZE + 1 - i) * 1.0));
        }
        List<LatLng> result = Utility.convertLocationList(toTest);
        Assert.assertEquals(SIZE, result.size());
        HashSet<LatLng> set = new HashSet<LatLng>();
        for (int i = 0; i < SIZE; i++) {
            set.add(result.get(i));
        }
        for (int i = 0; i < SIZE; i++) {
            set.remove(new LatLng(toTest.get(i).getLatitude(), toTest.get(i).getLongitude()));
        }
        Assert.assertEquals(0, set.size());
    }

    /**
     * WhiteBox: This test the case of processing null list of locations.
     */
    public final void testConvertNullList() {
        Assert.assertNull(Utility.convertLocationList(null));
    }

    /**
     * BlackBox: this test if the center is always within the route region.
     */
    public final void testCameraCenter() {
        DirectionsRequest request = new DirectionsRequest();

        String startAddress = "The Space Needle";
        String endAddress = "University of Washington, Seattle";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        request.setDepartureTime(D_TIME);
        request.setTravelMode(TravelMode.MIXED);
        request.setMinNumberBusTransfers(0);
        request.setMaxNumberBusTransfers(BUS_TRSF);

        DirectionsStatus expected = DirectionsStatus.REQUEST_SUCCESSFUL;

        DirectionsStatus actual = request.doDummyRequest();
        Assert.assertEquals(
                "Actual status for request.doRequest() call was: " + actual.getMessage(), expected,
                actual);
        List<Route> routes = request.getSolutions();
        for (Route r : routes) {
            LatLng center = Utility.getCameraCenter(r);
            Assert.assertTrue(center.latitude < r.getNeBound().getLatitude());
            Assert.assertTrue(center.longitude < r.getNeBound().getLongitude());
            Assert.assertTrue(center.latitude > r.getSwBound().getLatitude());
            Assert.assertTrue(center.longitude > r.getSwBound().getLongitude());
        }
    }

    /**
     * BlackBox: this test if the cameraZoom level is legal.
     */
    public final void testCameraZoomLevel() {
        DirectionsRequest request = new DirectionsRequest();

        String startAddress = "The Space Needle";
        String endAddress = "University of Washington, Seattle";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        request.setDepartureTime(D_TIME);
        request.setTravelMode(TravelMode.MIXED);
        request.setMinNumberBusTransfers(0);
        request.setMaxNumberBusTransfers(BUS_TRSF);

        DirectionsStatus expected = DirectionsStatus.REQUEST_SUCCESSFUL;

        DirectionsStatus actual = request.doDummyRequest();
        Assert.assertEquals(
                "Actual status for request.doRequest() call was: " + actual.getMessage(), expected,
                actual);
        List<Route> routes = request.getSolutions();
        for (Route r : routes) {
            float f = Utility.getCameraZoomLevel(r, ZOOM_1, ZOOM_2);
            Assert.assertTrue(f > 0);
        }
    }

    /**
     * BlackBox: Test if the url icon method.
     */
    public final void testUrlIcon() {
        Bitmap resultBitmap = Utility
                .getBitmapFromURL("//www.helpinghomelesscats.com/images/cat1.jpg");
        Assert.assertNotNull(resultBitmap);
    }

    /**
     * WhiteBox: Test if the url icon method returns null if a bad url is given.
     */
    public final void testBadUrlIcon() {
        Bitmap badResultBitmap = Utility
                .getBitmapFromURL("helpinghomelesscats.com/images/cat1.jpg");
        Assert.assertNull(badResultBitmap);
    }
    
    /**
     * WhiteBox: Test get camera center for passing null.
     */
    public final void testNullGetCameraCenter() {
        Assert.assertNull(Utility.getCameraCenter(null));
    }
    
    /**
     * BlackBox: Test if the converting method of the location is working properly.
     */
    public final void testLocationToLatLng() {
        Location toConvert = new Location(0.00, 1.00);
        LatLng result = Utility.convertLocation(toConvert);
        Assert.assertEquals(toConvert.getLatitude(), result.latitude);
        Assert.assertEquals(toConvert.getLongitude(), result.longitude);
    }
    
    /**
     * BlackBox: Test if the converting method of the date is working properly.
     */
    public final void testConvertAndroidSystemDateToFormatedDateString() {
        Assert.assertEquals("06/03/2013", 
                Utility.convertAndroidSystemDateToFormatedDateString(YEAR_2013, INT_5, INT_3));
        Assert.assertEquals("12/31/2013", 
                Utility.convertAndroidSystemDateToFormatedDateString(YEAR_2013, INT_11, INT_31));
    }
    
    /**
     * BlackBox: Test if the converting method of the time is working properly.
     */
    public final void testConvertAndroidSystemTimeToFormatedTimeString() {
        Assert.assertEquals("09:08", 
                Utility.convertAndroidSystemTimeToFormatedTimeString(INT_9, INT_8));
        Assert.assertEquals("00:00", 
                Utility.convertAndroidSystemTimeToFormatedTimeString(0, 0));
        Assert.assertEquals("15:11", 
                Utility.convertAndroidSystemTimeToFormatedTimeString(INT_15, INT_11));
    }
    
    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration given
     * a very short duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationVeryShort() {
        String expected = "0 minutes";
        String actual = com.HuskySoft.metrobike.ui.utility.Utility.
                secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_4);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(20) was: "
                + actual, expected, actual);
    }

    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration given
     * a short duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationShort() {
        String expected = "17 minutes";
        String actual = com.HuskySoft.metrobike.ui.utility.Utility.
                secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_3);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(1000) was: "
                + actual, expected, actual);
    }

    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration given
     * a medium duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationMedium() {
        String expected = "1 hour, 42 minutes";
        String actual = com.HuskySoft.metrobike.ui.utility.Utility.
                secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_2);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(6100) was: "
                + actual, expected, actual);
    }

    /**
     * Test-Driven Development: This tests secondsToHumanReadableDuration given
     * a long duration.
     * 
     */
    // @Test
    public final void testSecondsToHumanReadableDurationLong() {
        String expected = "3 days, 11 hours, 20 minutes";
        String actual = com.HuskySoft.metrobike.ui.utility.Utility.
                secondsToHumanReadableDuration(SEC_HUMAN_READABLE_DURATION_1);
        Assert.assertEquals("Actual value of Utility.secondsToHumanReadableDuration(300000) was: "
                + actual, expected, actual);
    }
}
