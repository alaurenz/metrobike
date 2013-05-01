package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author coreyh3
 * @author dutchscout Represents a location as a latitude and longitude pair (in
 *         decimal degrees).
 */
public class Location implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version Location objects
     * (ex: from the log) being made into new-version Location objects.
     */
    private static final long serialVersionUID = 0L;

    /**
     * The latitude in decimal degrees.
     */
    private double latitude;

    /**
     * The longitude in decimal degrees.
     */
    private double longitude;

    /**
     * Implements a custom serialization of a Location object.
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
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }

    /**
     * Implements a custom deserialization of a Location object.
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
        latitude = in.readDouble();
        longitude = in.readDouble();
    }
}
