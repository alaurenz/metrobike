package com.jeffreysambells.polyline;

import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.Location;

/**
 * This class allows decoding GoogleMaps-style polylines into lists of LatLong
 * points. The main code was copied from
 * http://jeffreysambells.com/2010/05/27/decoding
 * -polylines-from-google-maps-direction-api-with-java
 * 
 * @author Jeffrey Sambells
 * @author dutchscout (fit the code into Utility class format and converted to
 *         LatLong)
 */
public final class Utility {

    /**
     * Factoring this out for checkstyle.
     */
    private static final int CHAR_OFFSET = 63;
    
    /**
     * Factoring this out for checkstyle.
     */
    private static final int TEST_CONDITION = 0x20;
    
    /**
     * Factoring this out for checkstyle.
     */
    private static final int SHIFT_VALUE = 5;
    
    /**
     * Factoring this out for checkstyle.
     */
    private static final double FLOAT_ERROR_RANGE = 1E5;
    
    /**
     * Factoring this out for checkstyle.
     */
    private static final int BITSHIFT_CONSTANT = 0x1f;
    /**
     * Tag for determining which class is generating the logging messages.
     */
    private static final String TAG = "com.jeffreysambells.polyline: Utility.java: ";
    
    /**
     * A private constructor that throws an error to deter instantiation of this
     * utility class.
     */
    private Utility() {
        /*
         * Based on a suggestion here
         * http://stackoverflow.com/questions/7766277/
         * why-am-i-getting-this-warning-about-utility-classes-in-java about
         * utility classes, throw an error here to prevent instatiation.
         */
        throw new AssertionError("Never instantiate utility classes!");
    }

    /**
     * Converts a GoogleMaps polyline into a list of Location objects. This code
     * from http://jeffreysambells.com/2010/05/27/decoding-polylines-from
     * -google-maps-direction-api-with-java.
     * 
     * @param encoded
     *            the polyline string to decode
     * @return the list of Location objects in the line
     */
    public static List<Location> decodePoly(final String encoded) {
        System.out.println(TAG + "decodePoly()->Encoded String is: " + encoded);
        List<Location> poly = new ArrayList<Location>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - CHAR_OFFSET;
                result |= (b & BITSHIFT_CONSTANT) << shift;
                shift += SHIFT_VALUE;
            } while (b >= TEST_CONDITION);
            int dlat = 0;
            if ((result & 1) != 0) {
                dlat = ~(result >> 1);
            } else {
                dlat = (result >> 1);
            }
            
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - CHAR_OFFSET;
                result |= (b & BITSHIFT_CONSTANT) << shift;
                shift += SHIFT_VALUE;
            } while (b >= TEST_CONDITION);
            int dlng = 0;
            if ((result & 1) != 0) { 
                dlng = ~(result >> 1);
            } else {
                dlng = (result >> 1);
            }
            
            lng += dlng;

            System.out.println(TAG + "decodePoly()->Lat: " + lat);
            System.out.println(TAG + "decodePoly()->Lng: " + lng + "\n");
            Location p =
                    new Location(((double) lat / FLOAT_ERROR_RANGE), 
                            ((double) lng / FLOAT_ERROR_RANGE));
            // OLD PROBLEMATIC CODE 
            //Location p =
            //        new Location((int) (((double) lat / 1E5) * 1E6),
            //                (int) (((double) lng / 1E5) * 1E6));*/
            poly.add(p);
            
        }

        return poly;
    }
}
