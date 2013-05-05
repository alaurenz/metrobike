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
    public static List<Location> decodePoly(String encoded) {

        List<Location> poly = new ArrayList<Location>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            Location p =
                    new Location((int) (((double) lat / 1E5) * 1E6),
                            (int) (((double) lng / 1E5) * 1E6));
            poly.add(p);
        }

        return poly;
    }
}
