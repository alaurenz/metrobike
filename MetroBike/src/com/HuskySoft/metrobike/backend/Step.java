package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author coreyh3
 * @author dutchscout Represents the shortest portion of a route.
 */
public final class Step implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Step objects (ex:
     * from the log) being made into new-version Step objects.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The distance of this step in meters.
     */
    private long distanceInMeters;

    /**
     * The duration of this step in seconds.
     */
    private long durationInSeconds;

    /**
     * The staring location of this step.
     */
    private Location startLocation;

    /**
     * The ending location of this step.
     */
    private Location endLocation;

    /**
     * The travel mode for this step (ex: BICYCLING).
     */
    private TravelMode travelMode;

    /**
     * Human-readable direction for this step.
     */
    private String htmlInstruction;

    /**
     * String-stored list of points for plotting the step on a map.
     */
    private String polyLinePoints;

    /**
     * A List of substeps. If there are no substeps for the current step then
     * this will be null.
     */
    private List<Step> substeps;

    /**
     * Constructs an empty Step.
     */
    public Step() {
        distanceInMeters = 0L;
        durationInSeconds = 0L;
        startLocation = null;
        endLocation = null;
        travelMode = TravelMode.UNKNOWN;
        htmlInstruction = "";
        polyLinePoints = "";
        substeps = null;
    }

    /**
     * Returns a new Step based on the passed json_src.
     * 
     * @param jsonStep
     *            the JSON to parse into a Step object
     * @return A Step based on the passed json_src
     * @throws JSONException
     * 
     */
    public static Step buildStepFromJSON(final JSONObject jsonStep)
            throws JSONException {
        Step newStep = new Step();

        // Set the distance.
        JSONObject distance = jsonStep
                .getJSONObject(WebRequestJSONKeys.DISTANCE.getLowerCase());
        newStep.setDistanceInMeters(distance.getLong(WebRequestJSONKeys.VALUE
                .getLowerCase()));

        // Set the duration.
        JSONObject duration = jsonStep
                .getJSONObject(WebRequestJSONKeys.DURATION.getLowerCase());
        newStep.setDurationInSeconds(duration.getLong(WebRequestJSONKeys.VALUE
                .getLowerCase()));

        // Set the start location.
        JSONObject tempStartLocation = jsonStep
                .getJSONObject(WebRequestJSONKeys.START_LOCATION
                        .getLowerCase());
        double startLat = tempStartLocation.getDouble(WebRequestJSONKeys.LAT
                .getLowerCase());
        double startLng = tempStartLocation.getDouble(WebRequestJSONKeys.LNG
                .getLowerCase());
        newStep.setStartLocation(new Location(startLat, startLng));

        // Set the end location.
        JSONObject tempEndLocation = jsonStep
                .getJSONObject(WebRequestJSONKeys.END_LOCATION.getLowerCase());
        double endLat = tempEndLocation.getDouble(WebRequestJSONKeys.LAT
                .getLowerCase());
        double endLng = tempEndLocation.getDouble(WebRequestJSONKeys.LNG
                .getLowerCase());
        newStep.setEndLocation(new Location(endLat, endLng));

        // Set the substeps if they exist.
        if (jsonStep.has(WebRequestJSONKeys.STEPS.getLowerCase())) {
            List<Step> substeps = new ArrayList<Step>();

            JSONArray substepsArray = jsonStep
                    .getJSONArray(WebRequestJSONKeys.STEPS.getLowerCase());
            for (int i = 0; i < substepsArray.length(); i++) {
                Step currentSubstep = Step.buildStepFromJSON(substepsArray
                        .getJSONObject(i));
                substeps.add(currentSubstep);
            }

            newStep.setSubsteps(substeps);
        }

        // Set the travel mode.
        String stringTravelMode = jsonStep
                .getString(WebRequestJSONKeys.TRAVEL_MODE.getLowerCase());
        newStep.setTravelMode(TravelMode.valueOf(stringTravelMode));

        // Set the HTMLInstructions
        if (jsonStep.has(WebRequestJSONKeys.HTML_INSTRUCTIONS.getLowerCase())){
            String tempHtmlInstruction = jsonStep
                    .getString(WebRequestJSONKeys.HTML_INSTRUCTIONS.getLowerCase());
            newStep.setHtmlInstruction(tempHtmlInstruction);
        } else {
            Log.w("","No HTML instructions in this step!");
        }

        // Set the PolyLine Points
        JSONObject tempPolyLine = jsonStep
                .getJSONObject(WebRequestJSONKeys.POLYLINE.getLowerCase());
        String tempPoints = tempPolyLine.getString(WebRequestJSONKeys.POINTS
                .getLowerCase());
        newStep.setPolyLinePoints(tempPoints);

        return newStep;
    }

    /**
     * @return the distanceInMeters
     */
    public long getDistanceInMeters() {
        return distanceInMeters;
    }

    /**
     * Set the distance (in meters) for the step.
     * 
     * @param newDistanceInMeters
     *            the distanceInMeters to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setDistanceInMeters(final long newDistanceInMeters) {
        this.distanceInMeters = newDistanceInMeters;
        return this;
    }

    /**
     * @return the durationInSeconds
     */
    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * Set the duration (in seconds) for the step.
     * 
     * @param newDurationInSeconds
     *            the durationInSeconds to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setDurationInSeconds(final long newDurationInSeconds) {
        this.durationInSeconds = newDurationInSeconds;
        return this;
    }

    /**
     * @return the startLocation
     */
    public Location getStartLocation() {
        return startLocation;
    }

    /**
     * Set the start location for the step.
     * 
     * @param newStartLocation
     *            the startLocation to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setStartLocation(final Location newStartLocation) {
        this.startLocation = newStartLocation;
        return this;
    }

    /**
     * @return the endLocation
     */
    public Location getEndLocation() {
        return endLocation;
    }

    /**
     * Set the end location for the step.
     * 
     * @param newEndLocation
     *            the endLocation to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setEndLocation(final Location newEndLocation) {
        this.endLocation = newEndLocation;
        return this;
    }

    /**
     * @return the travelMode
     */
    public TravelMode getTravelMode() {
        return travelMode;
    }

    /**
     * Set the travel mode (ex: BICYCLING) for the step.
     * 
     * @param newTravelMode
     *            the travelMode to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setTravelMode(final TravelMode newTravelMode) {
        this.travelMode = newTravelMode;
        return this;
    }

    /**
     * @return the htmlInstruction
     */
    public String getHtmlInstruction() {
        return htmlInstruction;
    }

    /**
     * Set new instructions for the step.
     * 
     * @param newHtmlInstruction
     *            the htmlInstruction to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setHtmlInstruction(final String newHtmlInstruction) {
        this.htmlInstruction = newHtmlInstruction;
        return this;
    }

    /**
     * @return the polyLinePoints
     */
    public String getPolyLinePoints() {
        return polyLinePoints;
    }

    /**
     * Set the polyline points for the step.
     * 
     * @param newPolyLinePoints
     *            the polyLinePoints to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setPolyLinePoints(final String newPolyLinePoints) {
        this.polyLinePoints = newPolyLinePoints;
        return this;
    }

    /**
     * Get the List of substeps. If there are no substeps then this returns
     * null.
     * 
     * @return the list of substeps. If there are no substeps then this returns
     *         null.
     */
    public List<Step> getSubsteps() {
        return substeps;
    }

    /**
     * Set the list of substeps.
     * 
     * @param newSubsteps
     *            the list of of substeps to set.
     * @return the modified Step, for Builder pattern purposes.
     */
    public Step setSubsteps(final List<Step> newSubsteps) {
        this.substeps = newSubsteps;
        return this;
    }

    @Override
    public String toString() {
        // TODO: Make this toString meaningful and easy to read (if possible).
        return super.toString();
    }

    /**
     * Implements a custom serialization of a Step object.
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
        out.writeLong(distanceInMeters);
        out.writeLong(durationInSeconds);
        out.writeObject(startLocation);
        out.writeObject(endLocation);
        out.writeObject(travelMode);
        out.writeObject(htmlInstruction);
        out.writeObject(polyLinePoints);
    }

    /**
     * Implements a custom deserialization of a Step object.
     * 
     * @param in
     *            the ObjectInputStream to read from
     * @throws IOException
     *             if the stream fails
     * @throws ClassNotFoundException
     *             if a class is not found
     */
    private void readObject(final ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        // Read each field from the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the writing order in writeObject()
        distanceInMeters = in.readLong();
        durationInSeconds = in.readLong();
        startLocation = (Location) in.readObject();
        endLocation = (Location) in.readObject();
        travelMode = (TravelMode) in.readObject();
        htmlInstruction = (String) in.readObject();
        polyLinePoints = (String) in.readObject();
    }
}