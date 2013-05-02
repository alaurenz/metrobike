package com.HuskySoft.metrobike.algorithm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.Leg;
import com.HuskySoft.metrobike.backend.Location;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.Step;
import com.HuskySoft.metrobike.backend.TravelMode;

/**
 * @author coreyh3
 * @author dutchscout Represents a request for directions to the MetroBike
 *         backend.
 */
public final class DirectionsRequest implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version DirectionsRequest
     * objects (ex: from the log) being made into new-version DirectionsRequest
     * objects.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The starting location that the user wants directions from.
     */
    private String startAddress;

    /**
     * The ending location that the user wants directions to.
     */
    private String endAddress;

    /**
     * Time the user would like to arrive at their destination. If this is set
     * (ie. it equals a nonzero value), then departure time should not be set.
     */
    private long arrivalTime = 0;

    /**
     * Time the user would like to depart from their starting location. If this
     * is set (ie. it equals a nonzero value), then arrival time should not be
     * set.
     */
    private long departureTime = 0;

    /**
     * The travel mode the user is requesting directions for. Default is
     * "MIXED."
     */
    private TravelMode travelMode = TravelMode.MIXED;

    /**
     * An optional field. It sets a minimum distance the user would like to
     * bike.
     */
    private long minDistanceToBikeInMeters;

    /**
     * An optional field. It sets the maximum distance the user would like to
     * bike. (In case they are exercise averse)
     */
    private long maxDistanceToBikeInMeters;

    /**
     * An optional field. The minimum number of bus transfers to have. In case
     * they like multiple transfers.
     */
    private int minNumberBusTransfers;

    /**
     * An optional field. Setting the maximum number of bus transfers in case
     * the user doesn't want routes with lots of transfers.
     */
    private int maxNumberBusTransfers;

    /**
     * When the request is complete, this holds a list of all of the chosen
     * routes.
     */
    private List<Route> solutions;

    /**
     * Initiates the request calculation. This is a blocking call.
     */
    public void doRequest() {
        // TODO: Clean up this method and make it more general-purpose.
        // TODO: Consider clearing the solutions field whenever any change is
        // made to the query.
        // For now, just generate dummy results

        solutions = new ArrayList<Route>();

        Route dummyRoute = new Route();

        dummyRoute.setSummary("Roosevelt Way NE");

        List<String> warnings = new ArrayList<String>();
        warnings.add("Bicycling directions are in beta. Use caution: "
                + "This route may contain streets that aren't suited "
                + "for bicycling.");

        dummyRoute.setWarnings(warnings);

        Leg dummyLeg = new Leg();

        dummyLeg.setEndAddress("3801 Brooklyn Avenue Northeast, "
                + "University of Washington, Seattle, WA 98105, USA");

        dummyLeg.setStartAddress("6504 Latona Avenue Northeast, "
                + "Seattle, WA 98115, USA");

        // Add Step 1
        dummyLeg.addStep((new Step())
                .setDistanceInMeters(18)
                .setDurationInSeconds(6)
                .setEndLocation(new Location(47.675910, -122.325690))
                .setHtmlInstruction(
                        "Head southeast on Latona Ave NE toward NE 65th St")
                .setPolyLinePoints("gv~aHjwriVXY")
                .setStartLocation(new Location(47.67604, -122.32582))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 2
        dummyLeg.addStep((new Step()).setDistanceInMeters(252)
                .setDurationInSeconds(63)
                .setEndLocation(new Location(47.67588, -122.32233))
                .setHtmlInstruction("Turn left onto NE 65th St")
                .setPolyLinePoints("mu~aHpvriV@s@@qE@gE?w@?u@?cB")
                .setStartLocation(new Location(47.67591, -122.32569))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 3
        dummyLeg.addStep((new Step())
                .setDistanceInMeters(593)
                .setDurationInSeconds(142)
                .setEndLocation(new Location(47.67201, -122.31736))
                .setHtmlInstruction("Turn right onto NE Ravenna Blvd")
                .setPolyLinePoints(
                        "gu~aHpariVZYn@m@bA{@x@s@POFI`@]XWBCBEZYNOh@e@dB"
                                + "{ALMPOjC}BZYN]Jq@|@}G")
                .setStartLocation(new Location(47.67588, -122.32233))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 4
        dummyLeg.addStep((new Step())
                .setDistanceInMeters(1775)
                .setDurationInSeconds(378)
                .setEndLocation(new Location(47.65609, -122.31788))
                .setHtmlInstruction("Turn right onto Roosevelt Way NE")
                .setPolyLinePoints(
                        "a}}aHnbqiVnA?X?nC?jC?V?f@?|@?VApC@fA@~E@~B?hA@P?"
                                + "r@?xB@b@?^?`A@R?jCAjB@~GDj@F^PRLHDHBFBJ@L?tC?~"
                                + "@BpA@r@@`DBlD@|BB\\?^@r@?L@@?PDHB")
                .setStartLocation(new Location(47.67201, -122.31736))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 5
        dummyLeg.addStep((new Step()).setDistanceInMeters(348)
                .setDurationInSeconds(114)
                .setEndLocation(new Location(47.65598, -122.31325))
                .setHtmlInstruction("Turn left onto NE Campus Pkwy")
                .setPolyLinePoints("qyzaHveqiVDS@I@IAkAByB?OBkE@sB?C?qB@wB@qB")
                .setStartLocation(new Location(47.65609, -122.31788))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 6
        dummyLeg.addStep((new Step()).setDistanceInMeters(268)
                .setDurationInSeconds(31)
                .setEndLocation(new Location(47.65358, -122.31334))
                .setHtmlInstruction("Turn right onto University Way NE")
                .setPolyLinePoints("{xzaHxhpiV^B|A@hCDlB@L@zA@")
                .setStartLocation(new Location(47.65598, -122.31325))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 7
        dummyLeg.addStep((new Step()).setDistanceInMeters(113)
                .setDurationInSeconds(21)
                .setEndLocation(new Location(47.65422, -122.31447))
                .setHtmlInstruction("Turn right onto Burke-Gilman Trail")
                .setPolyLinePoints("{izaHjipiVM`@Sh@EHCDEBIDGDEDINEJOb@Un@")
                .setStartLocation(new Location(47.65358, -122.31334))
                .setTravelMode(TravelMode.BICYCLING));

        // Add Step 8
        dummyLeg.addStep((new Step())
                .setDistanceInMeters(35)
                .setDurationInSeconds(6)
                .setEndLocation(new Location(47.6539, -122.31448))
                .setHtmlInstruction(
                        "Turn left onto Brooklyn Ave NE.  Destination will "
                                + "be on the right")
                .setPolyLinePoints("{mzaHlppiVp@@L?")
                .setStartLocation(new Location(47.65422, -122.31447))
                .setTravelMode(TravelMode.BICYCLING));

        // Add the leg to the route
        dummyRoute.addLeg(dummyLeg);
        
        // Add the route to our solution
        solutions.add(dummyRoute);

        /*
         * // TODO Auto-generated method stub validateRequestParameters();
         * //@Jaylen, I think we can use this to do the proper encoding for the
         * //http request. -> java.net.URLEncoder; //URLEncoder.encode("", "");
         * //TODO: Modify the string builder appends to not use String
         * //concatenation in them. Leaving them in there now for readability.
         * StringBuilder requestString = new StringBuilder("http://maps." +
         * "googleapis.com/maps/api/directions/json?");
         * requestString.append("origin=" + startAddress + "&");
         * requestString.append("destination=" + endAddress + "&"); //TODO: This
         * should probably be set to true if we are using the sensor //to
         * determine the location. //Hardcoding it to false for now.
         * requestString.append("sensor=false" + "&"); //TODO: I'm hardcoding
         * this to transit for now since that is the more //complicated of the
         * two cases. We should really be checking if it is //MIXED, TRANSIT,
         * BICYCLING, WALKING, or UNKNOWN. requestString.append("mode=transit" +
         * "&");
         * 
         * // + "origin=6504%20Latona%20Ave%20NE%2CSeattle%2CWA&" // +
         * "destination=3801%20Brooklyn%20Ave%20NE%2CSeattle%2CWA&" // +
         * "sensor=false&" + "arrival_time=1368644400&" // + "mode=transit"; //
         */
    }

    /**
     * Is responsible for validating all of the input parameters needed by the
     * doRequest call.
     */
    public void validateRequestParameters() {

    }

    /**
     * Set the starting address for the trip.
     * 
     * @param newStartAddress
     *            the address to start from.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setStartAddress(final String newStartAddress) {
        this.startAddress = newStartAddress;
        return this;
    }

    /**
     * Set the ending address for the trip.
     * 
     * @param newEndAddress
     *            the address to end at.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setEndAddress(final String newEndAddress) {
        this.endAddress = newEndAddress;
        return this;
    }

    /**
     * Set the arrival time. If this is set then departure time should not be
     * set.
     * 
     * @param newArrivalTime
     *            the time the user would like to arrive at their destination.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setArrivalTime(final long newArrivalTime) {
        if (this.departureTime != 0) {
            throw new IllegalArgumentException("departureTime was already "
                    + "set.");
        }
        this.arrivalTime = newArrivalTime;
        return this;
    }

    /**
     * Set the departure time. If this is set then the arrival time should not
     * be set.
     * 
     * @param newDepartureTime
     *            the time the user would like to depart from their starting
     *            location.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setDepartureTime(final long newDepartureTime) {
        if (this.arrivalTime != 0) {
            throw new IllegalArgumentException("departureTime was "
                    + "already set.");
        }
        this.departureTime = newDepartureTime;
        return this;
    }

    /**
     * Set the travel mode that the user would like to arrive at their
     * destination. The default travel mode is MIXED.
     * 
     * @param newTravelMode
     *            the TravelMode that the user would like to arrive at their
     *            destination.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setTravelMode(final TravelMode newTravelMode) {
        this.travelMode = newTravelMode;
        return this;
    }

    /**
     * Set the minimum distance the user would like to bike in meters. This
     * parameter is optional.
     * 
     * @param newMinDistanceToBikeInMeters
     *            the minimum distance to bike in mneters
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMinDistanceToBikeInMeters(
            final long newMinDistanceToBikeInMeters) {
        this.minDistanceToBikeInMeters = newMinDistanceToBikeInMeters;
        return this;
    }

    /**
     * Set the maximum distance to bike in meters. This parameter is optional.
     * 
     * @param newMaxDistanceToBikeInMeters
     *            the max distance to bike in meters.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMaxDistanceToBikeInMeters(
            final long newMaxDistanceToBikeInMeters) {
        this.maxDistanceToBikeInMeters = newMaxDistanceToBikeInMeters;
        return this;
    }

    /**
     * Set the minimum number of bus transfers. This parameter is optional.
     * 
     * @param newMinNumberBusTransfers
     *            the minimum number of bus transfers.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMinNumberBusTransfers(
            final int newMinNumberBusTransfers) {
        this.minNumberBusTransfers = newMinNumberBusTransfers;
        return this;
    }

    /**
     * Set the maximum number of bus transfers. This parameter is optional.
     * 
     * @param newMaxNumberBusTransfers
     *            the maximum number of bus transfers.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMaxNumberBusTransfers(
            final int newMaxNumberBusTransfers) {
        this.maxNumberBusTransfers = newMaxNumberBusTransfers;
        return this;
    }

    /**
     * Return the solutions to the request for directions. This field is null
     * before doRequest() is called.
     * 
     * @return the solutions
     */
    public List<Route> getSolutions() {
        return solutions;
    }

    @Override
    public String toString() {
        // TODO: Make this toString meaningful and easy to read (if possible).
        return super.toString();
    }
    
    /**
     * Implements a custom serialization of a DirectionsRequest object.
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
        out.writeLong(arrivalTime);
        out.writeLong(departureTime);
        out.writeObject(travelMode);
        out.writeLong(minDistanceToBikeInMeters);
        out.writeLong(maxDistanceToBikeInMeters);
        out.writeInt(minNumberBusTransfers);
        out.writeInt(maxNumberBusTransfers);
        out.writeObject(solutions);
    }

    /**
     * Implements a custom deserialization of a DirectionsRequest object.
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
        startAddress = (String) in.readObject();
        endAddress = (String) in.readObject();
        arrivalTime = in.readLong();
        departureTime = in.readLong();
        travelMode = (TravelMode) in.readObject();
        minDistanceToBikeInMeters = in.readLong();
        maxDistanceToBikeInMeters = in.readLong();
        minNumberBusTransfers = in.readInt();
        maxNumberBusTransfers = in.readInt();
        solutions = (List<Route>) in.readObject();
    }
}
