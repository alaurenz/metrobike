/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @author coreyh3
 * @author dutchscout Represents a series of related steps to complete a portion
 *         of a route.
 */
public class Leg implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Leg objects (ex:
     * from the log) being made into new-version Leg objects.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The distance of the leg in meters.
     */
    private long distanceInMeters;

    /**
     * The duration of the leg in seconds.
     */
    private long durationInSeconds;

    /**
     * The starting location in this leg.
     */
    private Location startLocation;

    /**
     * The ending location in this leg.
     */
    private Location endLocation;

    /**
     * The starting address (human-readable) in this leg.
     */
    private String startAddress;

    /**
     * The ending address (human-readable) in this leg.
     */
    private String endAddress;

    /**
     * The list of steps to complete this leg.
     */
    private List<Step> stepList;

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
        out.writeLong(distanceInMeters);
        out.writeLong(durationInSeconds);
        out.writeObject(startLocation);
        out.writeObject(endLocation);
        out.writeObject(startAddress);
        out.writeObject(endAddress);
        out.writeObject(stepList);
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
        startAddress = (String) in.readObject();
        endAddress = (String) in.readObject();
        stepList = (List<Step>) in.readObject();
    }

    /**
     * @author coreyh3
     * @author dutchscout Represents the shortest portion of a route.
     */
    public class Step implements Serializable {
        /**
         * Part of serializability, this id tracks if a serialized object can be
         * deserialized using this version of the class.
         * 
         * NOTE: Please add 1 to this number every time you change the
         * readObject() or writeObject() methods, so we don't have old-version
         * Step objects (ex: from the log) being made into new-version Step
         * objects.
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
         * Implements a custom serialization of a Step object.
         * 
         * @param out
         *            the ObjectOutputStream to write to
         * @throws IOException
         *             if the stream fails
         */
        private void writeObject(final ObjectOutputStream out)
                throws IOException {
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
}
