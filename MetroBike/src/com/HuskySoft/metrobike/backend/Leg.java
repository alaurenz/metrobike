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
 * @author dutchscout Represents a series of related steps to complete a portion
 *         of a route.
 */
public final class Leg implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Leg objects (ex:
     * from the log) being made into new-version Leg objects.
     */
    private static final long serialVersionUID = 2L;

    /**
     * A default address for the start and end of a leg.
     */
    private static final String DEFAULT_LEG_ADDRESS = "no address";

    /**
     * The starting address (human-readable) in this leg.
     */
    private String startAddress;

    /**
     * The ending address (human-readable) in this leg.
     */
    private String endAddress;

    /**
     * For displaying this leg, this is the location of the Northeast corner of
     * the viewing window.
     */
    private Location neBound;

    /**
     * For displaying this leg, this is the location of the Southwest corner of
     * the viewing window.
     */
    private Location swBound;
    
    /**
     * The list of steps to complete this leg.
     */
    private List<Step> stepList;

    /**
     * The amount to indent.
     */
    private int indent = 2;

    /**
     * The actual indented string.
     */
    private String indentString = Utility.getIndentString() + Utility.getIndentString();

    /**
     * Constructs an empty Leg.
     */
    public Leg() {
        startAddress = DEFAULT_LEG_ADDRESS;
        endAddress = DEFAULT_LEG_ADDRESS;
        stepList = new ArrayList<Step>();
    }

    /**
     * Returns a new Leg based on the passed json_src.
     * 
     * @param jsonLeg
     *            the JSON to parse into a Leg object
     * @return A Leg based on the passed json_src
     * @throws JSONException If parsing the JSON fails
     */
    public static Leg buildLegFromJSON(final JSONObject jsonLeg) throws JSONException {
        Leg newLeg = new Leg();

        // Set the start address
        newLeg.setStartAddress(jsonLeg.getString(WebRequestJSONKeys.START_ADDRESS.getLowerCase()));

        // Set the end address
        newLeg.setEndAddress(jsonLeg.getString(WebRequestJSONKeys.END_ADDRESS.getLowerCase()));

        // Set the steps list
        JSONArray stepsArray = jsonLeg.getJSONArray(WebRequestJSONKeys.STEPS.getLowerCase());

        for (int i = 0; i < stepsArray.length(); i++) {
            Step currentStep = Step.buildStepFromJSON(stepsArray.getJSONObject(i));
            newLeg.addStep(currentStep);
        }

        return newLeg;
    }

    /**
     * Adds a new step to the leg and updates the leg parameters.
     * 
     * @param newStep
     *            the step to add
     * @return the modified Leg, for Builder pattern purposes
     */
    public Leg addStep(final Step newStep) {
        
        // Update neBound
        neBound = Location.makeNorthEastBound(neBound, newStep.getStartLocation());
        neBound = Location.makeNorthEastBound(neBound, newStep.getEndLocation());

        // Update swBound
        swBound = Location.makeSouthWestBound(swBound, newStep.getStartLocation());
        swBound = Location.makeSouthWestBound(swBound, newStep.getEndLocation());
        
        stepList.add(newStep);
        return this;
    }

    /**
     * @return the startAddress
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     * Set the start address of the leg.
     * 
     * @param newStartAddress
     *            the startAddress to set
     * @return the modified Leg, for Builder pattern purposes
     */
    public Leg setStartAddress(final String newStartAddress) {
        this.startAddress = newStartAddress;
        return this;
    }

    /**
     * @return the endAddress
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     * Set the end address of the leg.
     * 
     * @param newEndAddress
     *            the endAddress to set
     * @return the modified Leg, for Builder pattern purposes
     */
    public Leg setEndAddress(final String newEndAddress) {
        this.endAddress = newEndAddress;
        return this;
    }

    /**
     * Returns the Leg's current starting location.
     * 
     * @return the Leg's current starting location
     */
    public Location getStartLocation() {
        if (!stepList.isEmpty()) {
            return stepList.get(0).getStartLocation();
        }
        return null;
    }

    /**
     * Returns the Leg's current ending location.
     * 
     * @return the Leg's current ending location
     */
    public Location getEndLocation() {
        if (!stepList.isEmpty()) {
            return stepList.get(stepList.size() - 1).getEndLocation();
        }
        return null;
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
     * Returns the Leg's current total distance in meters.
     * 
     * @return the Leg's current total distance in meters
     */
    public long getDistanceInMeters() {
        long myDistance = 0;
        for (Step s : stepList) {
            myDistance += s.getDistanceInMeters();
        }
        return myDistance;
    }

    /**
     * Returns the Leg's current total duration in seconds.
     * 
     * @return the Leg's current total duration in seconds
     */
    public long getDurationInSeconds() {
        long myDuration = 0;
        for (Step s : stepList) {
            myDuration += s.getDurationInSeconds();
        }
        return myDuration;
    }

    /**
     * Returns an unmodifiable list of Steps necessary to complete this Leg.
     * 
     * @return an unmodifiable list of Steps necessary to complete this Leg
     */
    public List<Step> getStepList() {
        return Collections.unmodifiableList(stepList);
    }

    /**
     * Returns a list of step-by-step text directions for this Leg.
     * 
     * @return a list of step-by-step text directions for this Leg.
     */
    public List<String> directionStepsText() {
        List<String> toReturn = new ArrayList<String>();

        if (stepList == null || stepList.size() == 0) {
            return toReturn;
        }

        for (Step s : stepList) {
            String stepText = s.getHtmlInstruction();
            if (stepText == null) {
                toReturn.add("<step missing directions>");
            } else {
                toReturn.add(stepText);
            }
        }
        return toReturn;
    }
    
    /**
     * Returns the full polyline for this Leg.
     * 
     * @return the full polyline for this Leg
     */
    public List<Location> getPolyLinePoints() {
        List<Location> toReturn = new ArrayList<Location>();

        if (stepList == null || stepList.size() == 0) {
            return toReturn;
        }

        for (Step s : stepList) {
            List<Location> stepPolyline = s.getPolyLinePoints();
            if (stepPolyline != null) {
                toReturn.addAll(stepPolyline);
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        String extraIndent = indentString + Utility.getIndentString();
        StringBuilder legString = new StringBuilder(Utility.getIndentString() + "Leg:\n");
        legString.append(extraIndent + "startAddress: " + startAddress + "\n");
        legString.append(extraIndent + "endAddress: " + endAddress + "\n");
        legString.append(extraIndent + "stepList:\n");
        legString.append(Utility.getSubstepsAsString(stepList, indent + 2));
        return legString.toString();
    }

    /**
     * Implements a custom serialization of a Leg object.
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
        out.writeObject(startAddress);
        out.writeObject(endAddress);
        out.writeObject(stepList);
        out.writeInt(indent);
        out.writeObject(indentString);
    }

    /**
     * Implements a custom deserialization of a Leg object.
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
        startAddress = (String) in.readObject();
        endAddress = (String) in.readObject();
        stepList = (List<Step>) in.readObject();
        indent = in.readInt();
        indentString = (String) in.readObject();
    }
}
