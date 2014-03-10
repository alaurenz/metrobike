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
    private static final long serialVersionUID = 3L;

    /**
     * TAG for logging statements.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: Step.java: ";

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
     * String encoding points for plotting the step on a map.
     */
    private String polyLine;

    /**
     * A List of substeps. If there are no substeps for the current step then
     * this will be null.
     */
    private List<Step> substeps;

    /**
     * Transit details for this step (if this is a transit step) If there are no
     * transit details for the current step then this will be null.
     */
    private TransitDetails transitDetails;

    /**
     * The amount to indent.
     */
    private int indent = 0;

    /**
     * The actual indented string.
     */
    private String indentString = "";

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
        polyLine = "";
        substeps = null;
        transitDetails = null;
    }

    /**
     * Returns a new Step based on the passed json_src.
     * 
     * @param jsonStep
     *            the JSON to parse into a Step object
     * @return A Step based on the passed json_src
     * @throws JSONException
     *             if there is a problem parsing the JSON
     */
    public static Step buildStepFromJSON(final JSONObject jsonStep) throws JSONException {
        Step newStep = new Step();
        //System.out.println(TAG + "buildStepFromJSON()->Entering this method.");
        // Set the distance.
        JSONObject distance = jsonStep.getJSONObject(WebRequestJSONKeys.DISTANCE.getLowerCase());
        newStep.setDistanceInMeters(distance.getLong(WebRequestJSONKeys.VALUE.getLowerCase()));

        // Set the duration.
        JSONObject duration = jsonStep.getJSONObject(WebRequestJSONKeys.DURATION.getLowerCase());
        newStep.setDurationInSeconds(duration.getLong(WebRequestJSONKeys.VALUE.getLowerCase()));

        // Set the start location.
        JSONObject tempStartLocation =
                jsonStep.getJSONObject(WebRequestJSONKeys.START_LOCATION.getLowerCase());
        double startLat = tempStartLocation.getDouble(WebRequestJSONKeys.LAT.getLowerCase());
        double startLng = tempStartLocation.getDouble(WebRequestJSONKeys.LNG.getLowerCase());
        newStep.setStartLocation(new Location(startLat, startLng));

        // Set the end location.
        JSONObject tempEndLocation =
                jsonStep.getJSONObject(WebRequestJSONKeys.END_LOCATION.getLowerCase());
        double endLat = tempEndLocation.getDouble(WebRequestJSONKeys.LAT.getLowerCase());
        double endLng = tempEndLocation.getDouble(WebRequestJSONKeys.LNG.getLowerCase());
        newStep.setEndLocation(new Location(endLat, endLng));

        // Set the substeps if they exist.
        if (jsonStep.has(WebRequestJSONKeys.STEPS.getLowerCase())) {
            List<Step> substeps = new ArrayList<Step>();

            JSONArray substepsArray =
                    jsonStep.getJSONArray(WebRequestJSONKeys.STEPS.getLowerCase());
            for (int i = 0; i < substepsArray.length(); i++) {
                Step currentSubstep = Step.buildStepFromJSON(substepsArray.getJSONObject(i));
                substeps.add(currentSubstep);
            }

            newStep.setSubsteps(substeps);
        }

        // Set the travel mode.
        String stringTravelMode = jsonStep.getString(WebRequestJSONKeys.TRAVEL_MODE.getLowerCase());
        newStep.setTravelMode(TravelMode.valueOf(stringTravelMode));

        // Set the HTMLInstructions
        if (jsonStep.has(WebRequestJSONKeys.HTML_INSTRUCTIONS.getLowerCase())) {
            String tempHtmlInstruction =
                    jsonStep.getString(WebRequestJSONKeys.HTML_INSTRUCTIONS.getLowerCase());
            newStep.setHtmlInstruction(tempHtmlInstruction);
        } else {
            System.err.println("No HTML instructions in this step!");
        }

        // Set the PolyLine Points
        JSONObject tempPolyLine =
                jsonStep.getJSONObject(WebRequestJSONKeys.POLYLINE.getLowerCase());
        String tempPoints = tempPolyLine.getString(WebRequestJSONKeys.POINTS.getLowerCase());
        newStep.setPolyLine(tempPoints);

        // Parse the polyline (OLD CODE)
        // TODO think about the kinds of errors we could get here
        // TODO figure out how to handle the sheer size of this
        // List<Location> polyList =
        // com.jeffreysambells.polyline.Utility.decodePoly(tempPoints);
        // newStep.setPolyLinePoints(new ArrayList<Location>());

        // Set transit details (but only if this is a transit step)
        if (newStep.getTravelMode().equals(TravelMode.TRANSIT)) {
            if (jsonStep.has(WebRequestJSONKeys.TRANSIT_DETAILS.getLowerCase())) {
                TransitDetails newStepTransitDetails = new TransitDetails();

                JSONObject tempTransitDetails =
                        jsonStep.getJSONObject(WebRequestJSONKeys.TRANSIT_DETAILS.getLowerCase());

                JSONObject tempArrivalStop =
                        tempTransitDetails.getJSONObject(WebRequestJSONKeys.ARRIVAL_STOP
                                .getLowerCase());
                JSONObject tempArrivalStopLocation =
                        tempArrivalStop.getJSONObject(WebRequestJSONKeys.LOCATION.getLowerCase());
                newStepTransitDetails.setArrivalStop(
                        tempArrivalStopLocation.getDouble(WebRequestJSONKeys.LAT.getLowerCase()),
                        tempArrivalStopLocation.getDouble(WebRequestJSONKeys.LNG.getLowerCase()));
                JSONObject tempDepartureStop =
                        tempTransitDetails.getJSONObject(WebRequestJSONKeys.DEPARTURE_STOP
                                .getLowerCase());
                JSONObject tempDepartureStopLocation =
                        tempDepartureStop.getJSONObject(WebRequestJSONKeys.LOCATION.getLowerCase());
                newStepTransitDetails.setDepartureStop(
                        tempDepartureStopLocation.getDouble(WebRequestJSONKeys.LAT.getLowerCase()),
                        tempDepartureStopLocation.getDouble(WebRequestJSONKeys.LNG.getLowerCase()));

                JSONObject tempArrivalTime =
                        tempTransitDetails.getJSONObject(WebRequestJSONKeys.ARRIVAL_TIME
                                .getLowerCase());
                newStepTransitDetails.setArrivalTime(tempArrivalTime
                        .getString(WebRequestJSONKeys.TEXT.getLowerCase()));
                JSONObject tempDepartureTime =
                        tempTransitDetails.getJSONObject(WebRequestJSONKeys.DEPARTURE_TIME
                                .getLowerCase());
                newStepTransitDetails.setDepartureTime(tempDepartureTime
                        .getString(WebRequestJSONKeys.TEXT.getLowerCase()));

                String tempHeadsign =
                        tempTransitDetails.getString(WebRequestJSONKeys.HEADSIGN.getLowerCase());
                newStepTransitDetails.setHeadsign(tempHeadsign);

                JSONObject tempTransitLine =
                        tempTransitDetails.getJSONObject(WebRequestJSONKeys.LINE.getLowerCase());
                newStepTransitDetails.setAgencyName(tempTransitLine
                        .getString(WebRequestJSONKeys.AGENCIES.getLowerCase()));

                // NOTE: uses the first agency only
                JSONArray tempAgenciesArray =
                        tempTransitLine.getJSONArray(WebRequestJSONKeys.AGENCIES.getLowerCase());
                // TODO: if(tempAgenciesArray.length() > 1) { ... }
                JSONObject tempFirstAgency = tempAgenciesArray.getJSONObject(0);
                newStepTransitDetails.setAgencyName(tempFirstAgency
                        .getString(WebRequestJSONKeys.NAME.getLowerCase()));

                newStepTransitDetails.setLineShortName(tempTransitLine
                        .getString(WebRequestJSONKeys.SHORT_NAME.getLowerCase()));

                JSONObject tempVehicle =
                        tempTransitLine.getJSONObject(WebRequestJSONKeys.VEHICLE.getLowerCase());
                newStepTransitDetails.setVehicleType(tempVehicle.getString(WebRequestJSONKeys.NAME
                        .getLowerCase()));
                newStepTransitDetails.setVehicleIconURL(tempVehicle
                        .getString(WebRequestJSONKeys.ICON.getLowerCase()));

                int tempNumStops =
                        tempTransitDetails.getInt(WebRequestJSONKeys.NUM_STOPS.getLowerCase());
                newStepTransitDetails.setNumStops(tempNumStops);

                newStep.setTransitDetails(newStepTransitDetails);
            } else {
                System.err.println("No transit details for this step "
                        + "(however, they were expected)!");
            }
        }

        //System.out.println(TAG + "buildStepFromJson()->Exiting this method.");
        return newStep;
    }

    /**
     * throws IllegalStateException if the step is not a transit step.
     * 
     * @return the transitDetails
     */
    public TransitDetails getTransitDetails() {
        //System.out.println(TAG + "getTransitDetails()->transitDetails: " + transitDetails);
        if (!this.getTravelMode().equals(TravelMode.TRANSIT)) {
            throw new IllegalStateException("Cannot get transit details from a non-transit step!");
        }
        return transitDetails;
    }

    /**
     * @return the distanceInMeters
     */
    public long getDistanceInMeters() {
        //System.out.println(TAG + "getDistanceInMeters()->distanceInMeters: " + distanceInMeters);
        return distanceInMeters;
    }

    /**
     * Set the transit details for the step.
     * 
     * @param newTransitDetails
     *            the newTransitDetails to set.
     * @return the modified Step, for Builder pattern purposes.
     */
    public Step setTransitDetails(final TransitDetails newTransitDetails) {
        //System.out.println(TAG + "setTransitDetails()->newTransitDetails: " + newTransitDetails);
        if (!this.getTravelMode().equals(TravelMode.TRANSIT)) {
            throw new IllegalStateException("Cannot add transit details to a non-transit step!");
        }
        this.transitDetails = newTransitDetails;
        return this;
    }

    /**
     * Set the distance (in meters) for the step.
     * 
     * @param newDistanceInMeters
     *            the distanceInMeters to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setDistanceInMeters(final long newDistanceInMeters) {
        //System.out.println(TAG + "setDistanceInMeters()->newDistanceInMeters: "
        //        + newDistanceInMeters);
        this.distanceInMeters = newDistanceInMeters;
        return this;
    }

    /**
     * @return the durationInSeconds
     */
    public long getDurationInSeconds() {
        //System.out.println(TAG + "getDurationInSeconds()->durationInSeconds: " + durationInSeconds);
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
        //System.out.println(TAG + "setDurationInSeconds()->newDurationInSeconds: "
        //        + newDurationInSeconds);
        this.durationInSeconds = newDurationInSeconds;
        return this;
    }

    /**
     * @return the startLocation
     */
    public Location getStartLocation() {
        //System.out.println(TAG + "getStartLocation()->startLocation: " + startLocation);
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
        //System.out.println(TAG + "setStartLocation()->newStartLocation: " + newStartLocation);
        this.startLocation = newStartLocation;
        return this;
    }

    /**
     * @return the endLocation
     */
    public Location getEndLocation() {
        //System.out.println(TAG + "getEndLocation()->endLocation: " + endLocation);
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
        //System.out.println(TAG + "setEndLocation()->newEndLocation: " + newEndLocation);
        this.endLocation = newEndLocation;
        return this;
    }

    /**
     * @return the travelMode
     */
    public TravelMode getTravelMode() {
        //System.out.println(TAG + "getTravelMode()->travelMode.name(): " + travelMode.name());
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
        //System.out.println(TAG + "setTravelMode()->newTravelMode.name(): " + newTravelMode.name());
        this.travelMode = newTravelMode;
        return this;
    }

    /**
     * @return the htmlInstruction
     */
    public String getHtmlInstruction() {
        //System.out.println(TAG + "getHtmlInstruction()->htmlInstruction: " + htmlInstruction);
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
        //System.out.println(TAG + "setHtmlInstruction()->newHtmlInstruction: " + newHtmlInstruction);
        this.htmlInstruction = newHtmlInstruction;
        return this;
    }

    /**
     * @return list of Locations for this step obtained by decoding its polyLine
     */
    public List<Location> getPolyLinePoints() {
        //System.out.println(TAG + "getPolyLinePoints()->Entering this method.");
        List<Location> polyLinePoints = com.jeffreysambells.polyline.Utility.decodePoly(polyLine);
        //System.out.println(TAG + "getPolyLinePoints()->Exiting this method.");
        return polyLinePoints;
    }

    /**
     * @return the polyLine
     */
    public String getPolyLine() {
        //System.out.println(TAG + "getPolyLine()->polyLine: " + polyLine);
        return polyLine;
    }

    /**
     * Set the polyline String for the step.
     * 
     * @param newPolyLine
     *            the polyLine to set
     * @return the modified Step, for Builder pattern purposes
     */
    public Step setPolyLine(final String newPolyLine) {
        //System.out.println(TAG + "setPolyLine()->newPolyLine: " + newPolyLine);
        this.polyLine = newPolyLine;
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
        String extraIndent = indentString + Utility.getIndentString();
        StringBuilder stepString = new StringBuilder();
        stepString.append(indentString + "Step\n");
        stepString.append(extraIndent + "distanceInMeters: " + distanceInMeters + "\n");
        stepString.append(extraIndent + "durationInSeconds: " + durationInSeconds + "\n");
        startLocation.setIndent(indent + 1);
        stepString.append(extraIndent + "startLocation: " + startLocation.toString() + "\n");
        endLocation.setIndent(indent + 1);
        stepString.append(extraIndent + "endLocation: " + endLocation.toString() + "\n");
        stepString.append(extraIndent + "travelMode: " + travelMode.toString() + "\n");
        stepString.append(extraIndent + "htmlInstruction: " + htmlInstruction + "\n");
        stepString.append(extraIndent + "polyLine: " + polyLine + "\n");
        stepString.append(extraIndent + "substepList:\n");
        stepString.append(Utility.getSubstepsAsString(substeps, indent + 2));
        if (transitDetails != null) {
            stepString.append(extraIndent + "transitDetails:" + transitDetails.toString() + "\n");
        }
        return stepString.toString();
    }

    /**
     * Setter for the indent field. Affects the amount of indentation used in
     * the toString() method.
     * 
     * @param newIndent
     *            the new indent value.
     */
    public void setIndent(final int newIndent) {
        //System.out.println(TAG + "setIndent()->newIndent: " + newIndent);
        this.indent = newIndent;
        indentString = "";
        for (int i = 0; i < indent; i++) {
            indentString = Utility.getIndentString();
        }
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
        out.writeObject(polyLine);
        out.writeObject(substeps);
        out.writeInt(indent);
        out.writeObject(indentString);
        out.writeObject(transitDetails);
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
    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
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
        polyLine = (String) in.readObject();
        substeps = (List<Step>) in.readObject();
        indent = in.readInt();
        indentString = (String) in.readObject();
        transitDetails = (TransitDetails) in.readObject();
    }
}