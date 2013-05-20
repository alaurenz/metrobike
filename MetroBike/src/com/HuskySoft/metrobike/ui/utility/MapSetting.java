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
        mMap.setTrafficEnabled(show);
    }

    /**
     * Sets the type of map tiles that should be displayed.
     * 
     * @param mapType
     *            the value of the map type
     */
    public void setMapDisplay(final int mapType) {
        mMap.setMapType(mapType);
    }

    /**
     * Gets the type of map that's currently displayed.
     * 
     * @return the map types in integer
     */
    public int getMapDisplay() {
        return mMap.getMapType();
    }

    /**
     * Enables or disables the my-location layer.
     * 
     * @param display
     *            true if display
     */
    public void setCurrentLocationButton(final boolean display) {
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
     * Static method in order to update the settings status to all maps.
     * 
     * @param googleMap
     *            the map that need to be updated.
     */
    public static void updateStatus(final GoogleMap googleMap) {
        googleMap.setMapType(mMap.getMapType());
        googleMap.setMyLocationEnabled(mMap.isMyLocationEnabled());
        googleMap.setTrafficEnabled(mMap.isTrafficEnabled());
    }
    
    /**
     * Reset MapSettings to be null.
     * Useful when the Google Map this setting attached to
     * is destroyed.
     * WARNING: this method resets MapSettings to null.
     * Use with caution.
     */
    public static void resetMapSetting() {
        mSet = null;
    }
}
