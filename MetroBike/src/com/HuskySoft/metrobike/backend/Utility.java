/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * @author coreyh3
 * 
 *         This class holds any utility functions that will be used across the
 *         MetroBike project.
 * 
 */
public class Utility {

    /**
     * 
     */
    private static final String TAG = "MetroBikeUtility";

    /**
     * The URL for Google Maps
     */
    private static final String GOOGLE_MAPS_BASE_URL = "http://maps.googleapis.com/maps/api/directions/json?";

    /**
     * The assignment operator for paramaterized URL requests
     */
    private static final String URL_ASSIGNMENT_OPERATOR = "=";

    /**
     * The common separator for paramaterized URL requests
     */
    private static final String URL_PARAMETER_SEPARATOR = "&";
    
    /**
     * The name of the charset to use for parameter encoding.
     * UTF-8 should give us basic URL functionality.
     */
    private static final String URL_ENCODING_CHARSET_NAME = "UTF-8";

    /**
     * The default size for what a single indent is. (For the toString Methods)
     */
    private static final String INDENT_STRING = "    ";
    
    /**
     * 
     * @author Jaylen
     * 
     */
    public enum TransitTimeMode {
        /**
         * 
         */
        ARRIVAL_TIME,

        /**
         * 
         */
        DEPARTURE_TIME;
    }

    /**
     * Converts a JSONArray into a list of strings.
     * 
     * @param jsonArray
     *            input JSONAray containing the list of strings
     * @return a list of Strings
     * @throws JSONException
     *             if the JSON isn't in the expected format
     */
    public static List<String> jsonArrayToStringList(final JSONArray jsonArray)
            throws JSONException {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    /**
     * Build the URL for querying the Google Maps web API with a bicycle
     * request.
     * 
     * @param startAddress
     *            the (unescaped) address to end at. Example: 202 E Pestle Way
     * @param endAddress
     *            the (unescaped) address to end at. Example: 1008 N Bennion St.
     * @param multipleRoutes
     *            if true, ask for more than one Route
     * @return ex:
     *         "http://maps.googleapis.com/maps/api/directions/json?origin=6504
     *         %20Latona%20Ave%20NE%2CSeattle%2CWA&destination=3801%20Brooklyn%
     *         20Ave%20NE%2CSeattle%2CWA&sensor=false&mode=bicycling&alternativ
     *         es=false"
     * @throws UnsupportedEncodingException if the charset specified by URL_ENCODING_CHARSET_NAME is not found
     */
    public static String buildBicycleQueryString(final String startAddress,
            final String endAddress, final boolean multipleRoutes) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(GOOGLE_MAPS_BASE_URL);

        // Add the key/value pairs to the url
        addKeyValuePair(url, URIKeys.ORIGIN, startAddress, false);
        addKeyValuePair(url, URIKeys.DESTINATION, endAddress, false);
        addKeyValuePair(url, URIKeys.SENSOR, URIKeys.TRUE, false);
        addKeyValuePair(url, URIKeys.MODE, URIKeys.BICYCLING, false);

        if (multipleRoutes) {
            addKeyValuePair(url, URIKeys.ALTERNATIVES, URIKeys.TRUE, true);
        } else {
            addKeyValuePair(url, URIKeys.ALTERNATIVES, URIKeys.FALSE, true);
        }

        return url.toString();
    }

    /**
     * Build the URL for querying the Google Maps web API with a transit
     * request.
     * 
     * @param startAddress
     * @param endAddress
     * @param routeTime
     * @param timeMode
     * @param multipleRoutes
     * @return ex:
     *         "http://maps.googleapis.com/maps/api/directions/json?origin=6504
     *         %20Latona%20Ave%20NE%2CSeattle%2CWA&destination=3801%20Brooklyn%
     *         20Ave%20NE%2CSeattle%2CWA&sensor=false&arrival_time=1368644400&m
     *         ode=transit&alternatives=false"
     * @throws UnsupportedEncodingException if the charset specified by URL_ENCODING_CHARSET_NAME is not found
     */
    public static String buildTransitQueryString(final String startAddress,
            final String endAddress, final long routeTime,
            final TransitTimeMode timeMode, final boolean multipleRoutes) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(GOOGLE_MAPS_BASE_URL);

        // Add the key/value pairs to the url
        addKeyValuePair(url, URIKeys.ORIGIN, startAddress, false);
        addKeyValuePair(url, URIKeys.DESTINATION, endAddress, false);
        addKeyValuePair(url, URIKeys.SENSOR, URIKeys.TRUE, false);

        switch (timeMode) {
        case ARRIVAL_TIME:
            addKeyValuePair(url, URIKeys.ARRIVAL_TIME,
                    String.valueOf(routeTime), false);
            break;
        case DEPARTURE_TIME:
            addKeyValuePair(url, URIKeys.DEPARTURE_TIME,
                    String.valueOf(routeTime), false);
            break;
        default:
            // Unreachable case
            break;
        }

        addKeyValuePair(url, URIKeys.MODE, URIKeys.TRANSIT, false);

        if (multipleRoutes) {
            addKeyValuePair(url, URIKeys.ALTERNATIVES, URIKeys.TRUE, true);
        } else {
            addKeyValuePair(url, URIKeys.ALTERNATIVES, URIKeys.FALSE, true);
        }

        return url.toString();
    }

    /**
     * Adds a String of the form "key=value(&)" to a url.
     * @param url the StringBuilder for the url to add to
     * @param key the key to add
     * @param value the value to add
     * @param isFinalParam true if this is the final parameter
     */
    private static void addKeyValuePair(final StringBuilder url,
            final URIKeys key, final URIKeys value,
            final boolean isFinalParam) {
        url.append(key.getLowerCase());
        url.append(URL_ASSIGNMENT_OPERATOR);
        url.append(value.getLowerCase());
        if (!isFinalParam) {
            url.append(URL_PARAMETER_SEPARATOR);
        }
    }

    /**
     * Adds a String of the form "key=value(&)" to a url.
     * @param url the StringBuilder for the url to add to
     * @param key the key to add
     * @param value the value to add
     * @param isFinalParam true if this is the final parameter
     * @throws UnsupportedEncodingException if the charset specified by URL_ENCODING_CHARSET_NAME is not found
     */
    private static void addKeyValuePair(final StringBuilder url,
            final URIKeys key, final String value,
            final boolean isFinalParam) throws UnsupportedEncodingException {
        url.append(key.getLowerCase());
        url.append(URL_ASSIGNMENT_OPERATOR);
        url.append(encodeURLParameter(value));
        if (!isFinalParam) {
            url.append(URL_PARAMETER_SEPARATOR);
        }
    }

    /**
     * Formats a parameter for inclusion as part of a URL.
     * @param toFormat the parameter to encode
     * @return the parameter formatted for URL inclusion
     * @throws UnsupportedEncodingException if the charset specified by URL_ENCODING_CHARSET_NAME is not found
     */
    private static String encodeURLParameter(final String toFormat) throws UnsupportedEncodingException {
        /*
         * // TODO Auto-generated method stub validateRequestParameters();
         * StringBuilder requestString = new StringBuilder("?origin=" +
         * startAddress + "&"); requestString.append("destination=" + endAddress
         * + "&");
         * 
         * //TODO: This should probably be set to true if we are using the
         * sensor //to determine the location. //Hardcoding it to false for now.
         * requestString.append("sensor=false" + "&");
         * 
         * //TODO: I'm hardcoding this to transit for now since that is the more
         * //complicated of the two cases. We should really be checking if it is
         * //MIXED, TRANSIT,BICYCLING, WALKING, or UNKNOWN.
         * requestString.append("mode=transit" +"&");
         * 
         * 
         * requestString = new StringBuilder(googleMapsUrl +
         * URLEncoder.encode(requestString.toString(), "UTF-8"));
         */
        return URLEncoder.encode(toFormat, URL_ENCODING_CHARSET_NAME);
    }

    /**
     * This method takes in a URL as a String and returns a JSON String as a
     * response.
     * 
     * @param theURL
     *            the URL to send to the server.
     * @return Returns what the server sends back (should be a JSON String)
     */
    public static String doQuery(final String theURL) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(theURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            try {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                byte[] thisResponse = new byte[2048];
                int bytesThisTime = 0;
                do {
                    bytesThisTime = in.read(thisResponse);
                    if (bytesThisTime < 0)
                        break;
                    String stringResponse = new String(thisResponse, 0,
                            bytesThisTime);
                    response.append(stringResponse);
                } while (bytesThisTime > 0);
            } finally {
                urlConnection.disconnect();
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad url..." + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IO Exception..." + e.getMessage());
        }
        return response.toString();
    }

    /**
     * This pretty prints out a list as a String
     * 
     * @param list the list to print.
     * @return a String representation of the list.
     */
    public static String listPrettyPrint(List<?> list, int indent) {        
        String indentString = "";
        for(int i = 0; i < indent; i++) {
            indentString += INDENT_STRING;
        }
        
        StringBuilder listAsString = new StringBuilder();
        for(int i = 0; i < list.size(); i++) {
            listAsString.append(indentString + i + ": " + list.get(i) + "\n");
        }
        return listAsString.toString();
    }
    
    /**
     * This recursively converts a list of substeps into a String 
     * representation
     * 
     * @param substeps A list of substeps.
     * @return A String representation of the substeps.
     */
    public static String getSubstepsAsString(List<Step> substeps, int indent) {
        if(substeps == null || substeps.size() == 0) {
            return "";
        }
        
        StringBuilder substepString = new StringBuilder();
        for(int i = 0; i < substeps.size(); i++) {
            substeps.get(i).setIndent(indent);
            substepString.append(substeps.get(i).toString());
        }
        
        return substepString.toString();
    }
    
    /**
     * This recursively converts a list of substeps into a String 
     * representation
     * 
     * @param legs A list of substeps.
     * @return A String representation of the substeps.
     */
    public static String getLegsAsString(List<Leg> legs) {
        if(legs == null || legs.size() == 0) {
            return "";
        }
        
        StringBuilder substepString = new StringBuilder();
        for(int i = 0; i < legs.size(); i++) {
            substepString.append(legs.get(i).toString());
        }
        
        return substepString.toString();
    }
    
    public static String getIndentString() {
        return INDENT_STRING;
    }

}
