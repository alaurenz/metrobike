package com.HuskySoft.metrobike.ui.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Sam Wilson, Shuo Wang, Qinyuan Wan This class holds any utility
 *         functions that will be used across the ui of the MetroBike project.
 */
public final class Utility {

    /**
     * Tag string for logging.
     */
    private static final String TAG = "com.HuskySoft.metrobike.ui.utility: Utility.java: ";

    /**
     * naxus7's screen height.
     */
    private static final float NEXUS7_SCREEN_HEIGHT = 905.16425f;

    /**
     * naxus7's screen width.
     */
    private static final float NEXUS7_SCREEN_WIDTH = 600.93896f;

    /**
     * zoom level constant helps to get the appropriate zoom level of the
     * camera.
     */
    private static final double NEXUS7_ZOOM_CONSTANT = 259.6656;

    /**
     * vehicle icon size.
     */
    private static final int VEHICLE_ICON_SIZE = 40;

    /**
     * Minimum two digit number.
     */
    private static final int MIN_TWO_DIGIT_NUMBER = 10;

    /**
     * A private constructor that throws an error to deter instantiation of this
     * utility class.
     */
    private Utility() {
        /*
         * Based on a suggestion here
         * http://stackoverflow.com/questions/7766277/
         * why-am-i-getting-this-warning-about-utility-classes-in-java about
         * utility classes, throw an error here to prevent instantiation.
         */
        throw new AssertionError("Never instantiate utility classes!");
    }

    /**
     * Converts MetroBike's Location type to Android's LatLng type.
     * 
     * @param toConvert
     *            the Location to convert
     * @return the new LatLng object, or null if the passed Location is null
     */
    public static LatLng convertLocation(final Location toConvert) {
        if (toConvert == null) {
            System.out.println(TAG + "convertLocation()->toConvert was null.");
            return null;
        }

        System.out.println(TAG + "convertLocation()->toConvert.getLatitude(): "
                + toConvert.getLatitude());
        System.out.println(TAG + "convertLocation()->toConvert.getLongitude(): "
                + toConvert.getLongitude());
        return new LatLng(toConvert.getLatitude(), toConvert.getLongitude());
    }

    /**
     * Converts a list of MetroBike's Location type to a list of Android's
     * LatLng type.
     * 
     * @param toConvert
     *            the list of Locations to convert
     * @return the new list of LatLng objects, or null if the passed list is
     *         null
     */
    public static List<LatLng> convertLocationList(final List<Location> toConvert) {
        if (toConvert == null) {
            System.out.println(TAG + "convertLocationList()->toConvert() was null.");
            return null;
        }

        System.out.println(TAG + "convertLocationList()->toConvert.size(): " + toConvert.size());
        List<LatLng> toReturn = new ArrayList<LatLng>();
        for (Location l : toConvert) {
            toReturn.add(convertLocation(l));
        }
        return toReturn;
    }

    /**
     * Get the center of the route to let the camera center there.
     * 
     * @param route
     *            : route to be process
     * @return a LatLng representation of the geographical point on the map.
     */
    public static LatLng getCameraCenter(final Route route) {
        if (route == null) {
            return null;
        }
        double latitude = (route.getNeBound().getLatitude() + route.getSwBound().getLatitude()) / 2;
        double longitude = (route.getNeBound().getLongitude() + route.getSwBound().getLongitude()) / 2;

        System.out.println(TAG + "getCameraCenter()->latitude: " + latitude);
        System.out.println(TAG + "getCameraCenter()->longitude: " + longitude);
        return new LatLng(latitude, longitude);
    }

    /**
     * Get the appropriate zoom level of the given route.
     * 
     * @param route
     *            : the route object to be process, require not null
     * @param screenHeight
     *            : the screen height of the device
     * @param screenWidth
     *            : the screen width of the device
     * @return a float number representation of the level
     */
    public static float getCameraZoomLevel(final Route route, final float screenHeight,
            final float screenWidth) {
        double latitudeDif = route.getNeBound().getLatitude() - route.getSwBound().getLatitude();
        double longitudeDif = route.getNeBound().getLongitude() - route.getSwBound().getLongitude();
        double comparedLatitudeDif = latitudeDif * NEXUS7_SCREEN_HEIGHT / screenHeight;
        double comparedLongitudeDif = longitudeDif * NEXUS7_SCREEN_WIDTH / screenWidth;
        double maxOfTwo = Math.max(comparedLatitudeDif, comparedLongitudeDif * NEXUS7_SCREEN_HEIGHT
                / NEXUS7_SCREEN_WIDTH);

        System.out.println(TAG + "getCameraZoomLevel()->latitudeDif: " + latitudeDif);
        System.out.println(TAG + "getCameraZoomLevel()->longitudeDif: " + longitudeDif);
        System.out.println(TAG + "getCameraZoomLevel()->comparedLatitudeDif: "
                + comparedLatitudeDif);
        System.out.println(TAG + "getCameraZoomLevel()->comparedLongitudeDif: "
                + comparedLongitudeDif);
        System.out.println(TAG + "getCameraZoomLevel()->maxOfTwo: " + maxOfTwo);

        return Math.round(Math.log(NEXUS7_ZOOM_CONSTANT / maxOfTwo) / Math.log(2) + 1);
    }

    /**
     * Build a bit map from the given URL.
     * 
     * @param src
     *            : the src string of the url.
     * @return a Bitmap object
     */
    public static Bitmap getBitmapFromURL(final String src) {
        try {
            System.out.println(TAG + "getBitmapFromURL()->src: " + src);
            URL url = new URL("http:" + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return Bitmap.createScaledBitmap(myBitmap, VEHICLE_ICON_SIZE, VEHICLE_ICON_SIZE, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            // Null pointer here may happen inside of connection.connect();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert Android system representation of date to natural form.
     * 
     * @param systemYear
     *            : year in Android
     * @param systemMonth
     *            : month in Android
     * @param systemDay
     *            : day in Android
     * @return a natural formatted date string
     */
    public static String convertAndroidSystemDateToFormatedDateString(final int systemYear,
            final int systemMonth, final int systemDay) {

        // formatted month string
        String monthString = "";

        // System uses month from 0 to 11 to represent January to December
        int monthUIStyle = systemMonth + 1;
        if (monthUIStyle < MIN_TWO_DIGIT_NUMBER) {
            monthString += "0";
        }
        monthString += monthUIStyle;

        // formatted day string
        String dayString = "";
        if (systemDay < MIN_TWO_DIGIT_NUMBER) {
            dayString += "0";
        }
        dayString += systemDay;
        return monthString + "/" + dayString + "/" + systemYear;
    }

    /**
     * Convert Android system representation of time to natural form.
     * 
     * @param systemHour
     *            : year in Android
     * @param systemMinute
     *            : month in Android
     * @return a natural formatted time string
     */
    public static String convertAndroidSystemTimeToFormatedTimeString(final int systemHour,
            final int systemMinute) {
        String hourString = "";
        if (systemHour < MIN_TWO_DIGIT_NUMBER) {
            hourString += "0";
        }
        hourString += systemHour;

        String minuteString = "";
        if (systemMinute < MIN_TWO_DIGIT_NUMBER) {
            minuteString += "0";
        }
        minuteString += systemMinute;
        return hourString + ":" + minuteString;
    }
}
