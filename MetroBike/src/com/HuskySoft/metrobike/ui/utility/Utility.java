package com.HuskySoft.metrobike.ui.utility;

import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.Location;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author sw11 This class holds any utility functions that will be used across
 *         the ui of the MetroBike project.
 */
public final class Utility {

    /**
     * A private constructor that throws an error to deter instantiation of this
     * utility class.
     * 
     * @throws AssertionError
     *             if the constructor is called
     */
    private Utility() throws AssertionError {
        /*
         * Based on a suggestion here
         * http://stackoverflow.com/questions/7766277/
         * why-am-i-getting-this-warning-about-utility-classes-in-java about
         * utility classes, throw an error here to prevent instatiation.
         */
        throw new AssertionError("Never instantiate utility classes!");
    }

    /**
     * Converts MetroBike's Location type to Android's LatLng type.
     * 
     * @param toConvert
     *            the Location to convert
     * @return the new LatLng object
     */
    public static LatLng convertLocation(final Location toConvert) {
        return new LatLng(toConvert.getLatitude(), toConvert.getLongitude());
    }

    /**
     * Converts a list of MetroBike's Location type to a list of Android's
     * LatLng type.
     * 
     * @param toConvert
     *            the list of Locations to convert
     * @return the new list of LatLng objects
     */
    public static List<LatLng> convertLocationList(final List<Location> toConvert) {
        List<LatLng> toReturn = new ArrayList<LatLng>();
        for (Location l : toConvert) {
            toReturn.add(convertLocation(l));
        }
        return toReturn;
    }
}
