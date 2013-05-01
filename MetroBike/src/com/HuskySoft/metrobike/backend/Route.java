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
 * @author dutchscout Represents an entire route from the user's indicated start
 *         to end addresses.
 */
public class Route implements Serializable {
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
