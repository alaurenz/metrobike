/**
 * 
 */
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
public final class Route implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Route objects (ex:
     * from the log) being made into new-version Route objects.
     */
    private static final long serialVersionUID = 0L;

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
     * String-stored list of points for plotting the entire route on a map.
     */
    private String polylinePoints;

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
        polylinePoints = "";
        summary = DEFAULT_ROUTE_SUMMARY;
    }

    /**
     * Returns a new Route based on the passed jsonRoute.
     * 
     * @param jsonRoute
     *            the JSON to parse into a route object
     * @return A route based on the passed json_src
     * @throws JSONException 
     */
    public static Route buildRouteFromJSON(final JSONObject jsonRoute) throws JSONException {  
        Route newRoute = new Route();
        
        JSONArray legsArray = jsonRoute.getJSONArray(WebRequestJSONKeys.LEGS.getLowerCase());
        
        for(int i = 0; i < legsArray.length(); i++) {
            Leg currentLeg = Leg.buildLegFromJSON(legsArray.getJSONObject(i));
            newRoute.addLeg(currentLeg);
        }     
        
        JSONArray warningsArray = jsonRoute.getJSONArray(WebRequestJSONKeys.WARNINGS.getLowerCase());
        
        newRoute.setWarnings(Utility.jsonArrayToStringList(warningsArray)); 
        
        return newRoute;
    }


    
    /**
     * Add a new leg to this route.
     * 
     * @param toAdd
     *            the leg to add
     */
    public void addLeg(final Leg toAdd) {

        // Update neBound
        neBound = Location
                .makeNorthEastBound(neBound, toAdd.getStartLocation());
        neBound = Location.makeNorthEastBound(neBound, toAdd.getEndLocation());

        // Update swBound
        swBound = Location
                .makeSouthWestBound(swBound, toAdd.getStartLocation());
        swBound = Location.makeSouthWestBound(swBound, toAdd.getEndLocation());

        // Add the leg to our list
        legList.add(toAdd);

        // TODO: consider if/how we should build the new polyline here.
    }

    /**
     * @return the summary
     */
    public String getSummary() {
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
        this.summary = newSummary;
        return this;
    }

    /**
     * Returns the list of warnings that must be displayed on the map.
     * 
     * @return the list of warnings that must be displayed on the map.
     */
    public List<String> getWarnings() {
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
        this.warnings = newWarnings;
        return this;
    }

    /**
     * Returns the North-East bound for the route display area.
     * 
     * @return the North-East bound for the route display area.
     */
    public Location getNeBound() {
        return neBound;
    }

    /**
     * Returns the South-West bound for the route display area.
     * 
     * @return the South-West bound for the route display area.
     */
    public Location getSwBound() {
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
        return myDuration;
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
     * Returns the polyline points string for plotting the Route on the map.
     * 
     * @return the polyline points string for plotting the Route on the map.
     */
    public String getPolylinePoints() {
        return polylinePoints;
    }

    @Override
    public String toString() {
        StringBuilder routeString = new StringBuilder("Route:\n");
        routeString.append(Utility.getIndentString() + "neBound: " + neBound + "\n");
        routeString.append(Utility.getIndentString() + "swBound: " + swBound + "\n");
        routeString.append(Utility.getIndentString() + "polylinePoints: " + polylinePoints + "\n");
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
        out.writeObject(polylinePoints);
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
    private void readObject(final ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // Read each field from the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the writing order in writeObject()
        neBound = (Location) in.readObject();
        swBound = (Location) in.readObject();
        legList = (List<Leg>) in.readObject();
        polylinePoints = (String) in.readObject();
        summary = (String) in.readObject();
        warnings = (List<String>) in.readObject();
    }
}
