package com.HuskySoft.metrobike.ui.utility.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.ui.utility.Utility;
import com.google.android.gms.maps.model.LatLng;

/**
 * This class tests the ui Utility class.
 * 
 * @author dutchscout
 * 
 */
public class UtilityTest extends TestCase {
    
    private static final double TEST_LATITUDE_ONE = 36.135;
    private static final double TEST_LONGITUDE_ONE = 73.102;
    
    /**
     * WhiteBox: This test the case of converting a null Location to a LatLng.
     */
    public void test_convertNullLocationToLatLng() {
        Location testLocation = null;//new Location(TEST_LATITUDE_ONE, TEST_LONGITUDE_ONE);
        LatLng testLatLng = Utility.convertLocation(testLocation);
        Assert.assertNull("LatLng from null Location should be null.",testLatLng);
    }
}
