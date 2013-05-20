package com.HuskySoft.metrobike.ui.utility.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

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
     * WhiteBox: This test the case of converting a null Location to a LatLng.
     */
    public void test_convertNullLocationToLatLng() {
        Location testLocation = null;//new Location(TEST_LATITUDE_ONE, TEST_LONGITUDE_ONE);
        LatLng testLatLng = Utility.convertLocation(testLocation);
        Assert.assertNull("LatLng from null Location should be null.",testLatLng);
    }
    
    /**
     * BlackBox test: Test if LatLng consists with the Location.
     */
    public void test_convertListOfLocation() {
        List<Location> toTest = new ArrayList<Location>();
        for(int i = 1; i <= 100 ; i ++) {
            toTest.add(new Location (i * 1.0, (101 - i) * 1.0));
        }
        List<LatLng> result = Utility.convertLocationList(toTest);
        Assert.assertEquals(100, result.size());
        HashSet<LatLng> set = new HashSet<LatLng>();
        for (int i = 0; i < 100; i ++) {
            set.add(result.get(i));
        }
        for (int i = 0; i < 100; i ++) {
            set.remove(new LatLng(toTest.get(i).getLatitude(), toTest.get(i).getLongitude()));
        }
        Assert.assertEquals(0, set.size());
    }
    
    /**
     * WhiteBox: This test the case of processing null list of locations
     */
    public void test_convertNullList() {
        Assert.assertNull(Utility.convertLocationList(null));
    }
    
    /**
     * BlackBox: this test if the center is always within the route region
     */
    public void test_cameraCenter() {
        DirectionsRequest request = new DirectionsRequest();
        
        String startAddress = "The Space Needle";
        String endAddress = "University of Washington, Seattle";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        request.setDepartureTime(4000000);
        request.setTravelMode(TravelMode.MIXED);
        request.setMinNumberBusTransfers(0);
        request.setMaxNumberBusTransfers(5);
        
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
     * BlackBox: this test if the cameraZoom level is legal
     */
    public void test_cameraZoomLevel() {
        DirectionsRequest request = new DirectionsRequest();
        
        String startAddress = "The Space Needle";
        String endAddress = "University of Washington, Seattle";
        request.setStartAddress(startAddress);
        request.setEndAddress(endAddress);
        request.setDepartureTime(4000000);
        request.setTravelMode(TravelMode.MIXED);
        request.setMinNumberBusTransfers(0);
        request.setMaxNumberBusTransfers(5);
        
        DirectionsStatus expected = DirectionsStatus.REQUEST_SUCCESSFUL;

        DirectionsStatus actual = request.doDummyRequest();
        Assert.assertEquals(
                "Actual status for request.doRequest() call was: " + actual.getMessage(), expected,
                actual);
        List<Route> routes = request.getSolutions();
        for (Route r : routes) {
            float f = Utility.getCameraZoomLevel(r, 600f, 350f);
            Assert.assertTrue(f > 0);
        }
    }
}
