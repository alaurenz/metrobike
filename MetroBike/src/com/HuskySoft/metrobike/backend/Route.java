package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author coreyh3
 * @author dutchscout
 * 
 *         Represents an entire route from the user's indicated start to end
 *         addresses. A Route object can be created by parsing json or by
 *         creating a blank route and adding to it.
 */
public final class Route implements Serializable, Comparable<Route>{
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Route objects (ex:
     * from the log) being made into new-version Route objects.
     */
    private static final long serialVersionUID = 1L;

    /**
     * TAG for logging statements.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: Route.java: ";
    
    /**
     * Default value for the route summary.
     */
    private static final String DEFAULT_ROUTE_SUMMARY = "no summary";

    /**
     * For displaying the route, this is the location of the Northeast corner of
     * the viewing window.
     */
    private Location neBound;

    /**
     * For displaying the route, this is the location of the Southwest corner of
     * the viewing window.
     */
    private Location swBound;

    /**
     * The list of legs that make up this route.
     */
    private List<Leg> legList;

    /**
     * A short summary-descriptor for the route.
     */
    private String summary;

    /**
     * Google-Maps-indicated warnings that must be displayed for this route.
     */
    private List<String> warnings;
    
    /**
     * Constructs an empty Route.
     */
    public Route() {
        neBound = null;
        swBound = null;
        legList = new ArrayList<Leg>();
        summary = DEFAULT_ROUTE_SUMMARY;
    }

    /**
     * Returns a new Route based on the passed jsonRoute.
     * 
     * @param jsonRoute
     *            the JSON to parse into a route object
     * @return A route based on the passed json_src
     * @throws JSONException If parsing the JSON fails
     */
    public static Route buildRouteFromJSON(final JSONObject jsonRoute) throws JSONException {
        System.out.println(TAG + "buildRouteFromJSON()->Entering this method.");
        Route newRoute = new Route();

        JSONArray legsArray = jsonRoute.getJSONArray(WebRequestJSONKeys.LEGS.getLowerCase());

        for (int i = 0; i < legsArray.length(); i++) {
            Leg currentLeg = Leg.buildLegFromJSON(legsArray.getJSONObject(i));
            newRoute.addLeg(currentLeg);
        }

        JSONArray warningsArray =
                jsonRoute.getJSONArray(WebRequestJSONKeys.WARNINGS.getLowerCase());

        newRoute.setWarnings(Utility.jsonArrayToStringList(warningsArray));

        System.out.println(TAG + "buildRouteFromJSON()->Exiting this method.");
        return newRoute;
    }

    /**
     * Add a new leg to this route (at the end of the route).
     * 
     * @param toAdd
     *            the leg to add
     */
    public void addLeg(final Leg toAdd) {
        System.out.println(TAG + "addLeg()->toAdd: " + toAdd);

        // Update neBound
        //neBound = Location.makeNorthEastBound(neBound, toAdd.getStartLocation());
        //neBound = Location.makeNorthEastBound(neBound, toAdd.getEndLocation());
        neBound = Location.makeNorthEastBound(neBound, toAdd.getNeBound());

        // Update swBound
        //swBound = Location.makeSouthWestBound(swBound, toAdd.getStartLocation());
        //swBound = Location.makeSouthWestBound(swBound, toAdd.getEndLocation());
        swBound = Location.makeSouthWestBound(swBound, toAdd.getSwBound());
        
        // Add the leg to our list
        legList.add(toAdd);
        
        // TODO: consider if/how we should build the new polyline here.
    }
    
    /**
     * Add a new leg to this route at the beginning of route.
     * 
     * @param toAdd
     *            the leg to add
     */
    public void addLegBeginning(final Leg toAdd) {
        System.out.println(TAG + "addLegBeginning()->toAdd: " + toAdd);
        // Update neBound
        //neBound = Location.makeNorthEastBound(neBound, toAdd.getStartLocation());
        //neBound = Location.makeNorthEastBound(neBound, toAdd.getEndLocation());
        neBound = Location.makeNorthEastBound(neBound, toAdd.getNeBound());

        // Update swBound
        //swBound = Location.makeSouthWestBound(swBound, toAdd.getStartLocation());
        //swBound = Location.makeSouthWestBound(swBound, toAdd.getEndLocation());
        swBound = Location.makeSouthWestBound(swBound, toAdd.getSwBound());
        
        // Add the leg to our list
        legList.add(0, toAdd);
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        System.out.println(TAG + "getSummary()->summary: " + summary);
        return summary;
    }

    /**
     * Sets the route summary to the passed String.
     * 
     * @param newSummary
     *            the new summary
     * @return the modified Route, for Builder pattern purposes
     */
    public Route setSummary(final String newSummary) {
        System.out.println(TAG + "setSummary()->newSummary: " + newSummary);
        this.summary = newSummary;
        return this;
    }

    /**
     * Returns the list of warnings that must be displayed on the map.
     * 
     * @return the list of warnings that must be displayed on the map.
     */
    public List<String> getWarnings() {
        System.out.println(TAG + "getWarnings()->warnings: " + warnings);
        return warnings;
    }

    /**
     * Sets the list of warnings that must be displayed on the map.
     * 
     * @param newWarnings
     *            the warnings to set
     * @return the modified Route, for Builder pattern purposes
     */
    public Route setWarnings(final List<String> newWarnings) {
        System.out.println(TAG + "setWarnings()->newWarnings: " + newWarnings);
        this.warnings = newWarnings;
        return this;
    }

    /**
     * Returns the North-East bound for the route display area.
     * 
     * @return the North-East bound for the route display area.
     */
    public Location getNeBound() {
        System.out.println(TAG + "getNeBound()->neBound: " + neBound);
        return neBound;
    }

    /**
     * Returns the South-West bound for the route display area.
     * 
     * @return the South-West bound for the route display area.
     */
    public Location getSwBound() {
        System.out.println(TAG + "getSwBound()->swBound: " + swBound);
        return swBound;
    }

    /**
     * Returns the Route's current total distance in meters.
     * 
     * @return the Route's current total distance in meters
     */
    public long getDistanceInMeters() {
        long myDistance = 0;
        for (Leg l : legList) {
            myDistance += l.getDistanceInMeters();
        }
        System.out.println(TAG + "getDistanceInMeters()->myDistance: " + myDistance);
        return myDistance;
    }

    /**
     * Returns the Route's current total duration in seconds.
     * 
     * @return the Route's current total duration in seconds
     */
    public long getDurationInSeconds() {
        long myDuration = 0;
        for (Leg l : legList) {
            myDuration += l.getDurationInSeconds();
        }
        System.out.println(TAG + "getDurationInSeconds()->myDuration: " + myDuration);
        return myDuration;
    }
    
    /**
     * Returns the Route's total duration in the following format:
     * "XX days, XX hours, XX minutes"
     * 
     * @return the Route's total duration as a human-readable 
     *          String
     */
    public String getDurationHumanReadable() {
        long totalDurationSeconds = getDurationInSeconds();
        String humanReadable = Utility.secondsToHumanReadableDuration(totalDurationSeconds);
        System.out.println(TAG + "getDurationHumanReadable()->humanReadable: " + humanReadable);
        return humanReadable;
    }
    
    /**
     * Returns an unmodifiable list of Legs to complete this Route.
     * 
     * @return an unmodifiable list of Legs to complete this Route
     */
    public List<Leg> getLegList() {
        return Collections.unmodifiableList(legList);
    }

    /**
     * Returns a list of step-by-step text directions for this Route.
     * 
     * @return a list of step-by-step text directions for this Route.
     */
    public List<String> directionStepsText() {
        List<String> toReturn = new ArrayList<String>();

        if (legList == null || legList.size() == 0) {
            return toReturn;
        }

        for (Leg l : legList) {
            toReturn.addAll(l.directionStepsText());
        }

        return toReturn;
    }

    /**
     * Returns the full polyline for this Route.
     * 
     * @return the full polyline for this Route
     */
    public List<Location> getPolyLinePoints() {
        List<Location> toReturn = new ArrayList<Location>();

        if (legList == null || legList.size() == 0) {
            return toReturn;
        }

        for (Leg l : legList) {
            List<Location> legPolyline = l.getPolyLinePoints();
            if (legPolyline != null) {
                toReturn.addAll(legPolyline);
            }
        }
        return toReturn;
    }
    
    @Override
    public String toString() {
        StringBuilder routeString = new StringBuilder("Route:\n");
        routeString.append(Utility.getIndentString() + "neBound: " + neBound + "\n");
        routeString.append(Utility.getIndentString() + "swBound: " + swBound + "\n");
        routeString.append(Utility.getIndentString() + "summary: " + summary + "\n");
        routeString.append(Utility.getIndentString() + "Warnings:\n");
        routeString.append(Utility.listPrettyPrint(warnings, 2));
        routeString.append(Utility.getIndentString() + "legList:\n");
        routeString.append(Utility.getLegsAsString(legList));
        return routeString.toString();
    }

    /**
     * Implements a custom serialization of a Route object.
     * 
     * @param out
     *            the ObjectOutputStream to write to
     * @throws IOException
     *             if the stream fails
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        // Write each field to the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the read order in readObject()
        out.writeObject(neBound);
        out.writeObject(swBound);
        out.writeObject(legList);
        out.writeObject(summary);
        out.writeObject(warnings);
    }

    /**
     * Implements a custom deserialization of a Route object.
     * 
     * @param in
     *            the ObjectInputStream to read from
     * @throws IOException
     *             if the stream fails
     * @throws ClassNotFoundException
     *             if a class is not found
     */
    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read each field from the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the writing order in writeObject()
        neBound = (Location) in.readObject();
        swBound = (Location) in.readObject();
        legList = (List<Leg>) in.readObject();
        summary = (String) in.readObject();
        warnings = (List<String>) in.readObject();
    }
    
    /**
     * Overwrite compareTo method
     * @param other
     * @return
     */
    public int compareTo(Route other) {
        System.out.println(TAG + "compareTo()->Entering this method.");
        if (this.getDurationInSeconds() < other.getDurationInSeconds()) {
            System.out.println(TAG + "compareTo()->Exiting this method.");
            return -1;
        } else if (this.getDurationInSeconds()
        		== other.getDurationInSeconds()) {
            System.out.println(TAG + "compareTo()->Exiting this method.");
            return 0;
        } else {
            System.out.println(TAG + "compareTo()->Exiting this method.");
            return 1;
        }
    }
}
