package com.HuskySoft.metrobike.ui.utility;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * This class holds the setting of the display map.
 * 
 * @author Sam Wilson
 * 
 */
public final class MapSetting {
    /**
     * The actual google map object.
     */
    private GoogleMap map;

    /**
     * This object which stores all the setting.
     */
    private static MapSetting mapSetting;

    /**
     * private constructor. This class is singleton class.
     * 
     * @param googleMap
     *            the actual google map object
     */
    private MapSetting(final GoogleMap googleMap) {
        map = googleMap;
    }

    /**
     * Initialize this class.
     * 
     * @param googleMap
     *            the actual google map object
     * @return this object
     */
    public static MapSetting getInstance(final GoogleMap googleMap) {
        if (mapSetting == null) {
            mapSetting = new MapSetting(googleMap);
        }
        return mapSetting;
    }

    /**
     * Toggles the traffic layer on or off.
     * 
     * @param show
     *            true if show
     */
    public void setTraffic(final boolean show) {
        map.setTrafficEnabled(show);

    }

    /**
     * Sets the type of map tiles that should be displayed.
     * 
     * @param mapType
     *            the value of the map type
     */
    public void setMapDisplay(final int mapType) {
        map.setMapType(mapType);
    }

    /**
     * Gets the type of map that's currently displayed.
     * 
     * @return the map types in integer
     */
    public int getMapDisplay() {
        return map.getMapType();
    }

    /**
     * Enables or disables the my-location layer.
     * 
     * @param display
     *            true if display
     */
    public void setCurrentLocationButton(final boolean display) {
        map.setMyLocationEnabled(display);
    }

    /**
     * Repositions the camera according to the instructions defined in the
     * update.
     * 
     * @param update
     *            The change that should be applied to the camera
     */
    public void moveCameraTo(final CameraUpdate update) {
        map.moveCamera(update);
    }

    /**
     * Animates the movement of the camera from the current position to the
     * position defined in the update.
     * 
     * @param update
     *            The change that should be applied to the camera
     */
    public void cameraAnimation(CameraUpdate update, int durationMs,
            GoogleMap.CancelableCallback callback) {
        map.animateCamera(update, durationMs, callback);
    }

    /**
     * Removes all markers, polylines, polygons, overlays, etc from the map.
     */
    public void clean() {
        map.clear();
    }

    /**
     * Adds a polyline to this map.
     * 
     * @param options
     *            A polyline options object that defines how to render the
     *            Polyline.
     * @return The Polyline object that was added to the map.
     */
    public final Polyline addPolyline(PolylineOptions options) {
        return map.addPolyline(options);
    }

    /**
     * Add a circle to this map.
     * 
     * @param options
     *            A circle options object that defines how to render the Circle
     * @return The Circle object that is added to the map
     */
    public final Circle addCircle(CircleOptions options) {
        return map.addCircle(options);
    }

    /**
     * Adds a marker to this map. The marker's icon is rendered on the map at
     * the location Marker.position.
     * 
     * @param options
     *            the details of the marker options
     * @return the added marker
     */
    public Marker addMarkerOptions(MarkerOptions options) {
        return map.addMarker(options);
    }
}
