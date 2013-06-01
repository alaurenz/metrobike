package com.HuskySoft.metrobike.ui.utility;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

/**
 * This class holds the setting of the display map.
 * 
 * @author Sam Wilson, Shuo Wang
 * 
 */
public final class MapSetting {
    
    /**
     * Tag string for logging.
     */
    private static final String TAG = "com.HuskySoft.metrobike.ui.utility: MapSetting.java: ";
    
    /**
     * The actual google map object.
     */
    private static GoogleMap mMap;

    /**
     * This object which stores all the setting.
     */
    private static MapSetting mSet;

    /**
     * private constructor. This class is singleton class.
     * 
     * @param googleMap
     *            the actual google map object
     */
    private MapSetting(final GoogleMap googleMap) {
        System.out.println(TAG + "MapSetting()->googleMap: " + googleMap);
        mMap = googleMap;
        // default value
        // we will need to read the saved value in verion 1
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * Initialize this class.
     * 
     * @param googleMap
     *            the actual google map object
     * @return this object
     */
    public static MapSetting getInstance(final GoogleMap googleMap) {
        if (mSet == null && googleMap != null) {
            System.out.println(TAG + "getInstance()->googleMap: " + googleMap);
            mSet = new MapSetting(googleMap);
        }
        return mSet;
    }

    /**
     * Toggles the traffic layer on or off.
     * 
     * @param show
     *            true if show
     */
    public void setTraffic(final boolean show) {
        System.out.println(TAG + "setTraffic()->show: " + show);
        mMap.setTrafficEnabled(show);
    }

    /**
     * Update the traffic availability.
     * 
     * @return true if traffic is enable.
     */
    public boolean getTrafficDisplay() {
        System.out.println(TAG + "getTraffic()->mMap.isTrafficEnabled(): " 
                + mMap.isTrafficEnabled());
        return mMap.isTrafficEnabled();
    }

    /**
     * Sets the type of map tiles that should be displayed.
     * 
     * @param mapType
     *            the value of the map type
     */
    public void setMapDisplay(final int mapType) {
        System.out.println(TAG + "setMapDisplay()->mapType: " + mapType);
        mMap.setMapType(mapType);
    }

    /**
     * Gets the type of map that's currently displayed.
     * 
     * @return the map types in integer
     */
    public int getMapDisplay() {
        System.out.println(TAG + "getMapDisplay()->mMap.getMapType(): " + mMap.getMapType());
        return mMap.getMapType();
    }

    /**
     * Enables or disables the my-location layer.
     * 
     * @param display
     *            true if display
     */
    public void setCurrentLocationButton(final boolean display) {
        System.out.println(TAG + "setCurrentLocationButton()->display: " + display);
        mMap.setMyLocationEnabled(display);
    }

    /**
     * Check if the current location is enable and return that.
     * 
     * @return the current location if enable.
     */
    public Location getCurrentLocation() {
        try {
            return mMap.getMyLocation();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if the current location button available.
     * 
     * @return true if my current location enable.
     */
    public boolean getMyCurrentLocation() {
        System.out.println(TAG + "getMyCurrentLocation()->mMap.isMyLocationEnabled(): " 
                + mMap.isMyLocationEnabled());
        return mMap.isMyLocationEnabled();
    }

    /**
     * Static method in order to update the settings status to all maps.
     * 
     * @param googleMap
     *            the map that need to be updated.
     */
    public static void updateStatus(final GoogleMap googleMap) {
        System.out.println(TAG + "updateStatus()->googleMap: " + googleMap);
        if (mMap == null || googleMap == null) {
            return;
        }
        googleMap.setMapType(mMap.getMapType());
        googleMap.setMyLocationEnabled(mMap.isMyLocationEnabled());
        googleMap.setTrafficEnabled(mMap.isTrafficEnabled());
    }

    /**
     * Reset MapSettings to be null. Useful when the Google Map this setting
     * attached to is destroyed. WARNING: this method resets MapSettings to
     * null. Use with caution.
     */
    public static void resetMapSetting() {
        mSet = null;
        System.out.println(TAG + "resetMapSetting()->setting mSet to null.");
    }
}
