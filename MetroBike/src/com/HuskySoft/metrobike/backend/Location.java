package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author coreyh3
 * @author dutchscout Represents a location as an immutable latitude and
 *         longitude pair (in decimal degrees). Note that the member fields are
 *         not final because this would be incompatible with readObject, which
 *         is used here for Serializability.
 */
public final class Location implements Serializable {
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
     * A scaling factor used in computing the hashCode. It determines how much
     * precision to use in the hashCode calculation.
     */
    private static final int HASH_SCALE = 1000;

    /**
     * The latitude in decimal degrees.
     */
    private double latitude;

    /**
     * The longitude in decimal degrees.
     */
    private double longitude;

    /**
     * Constructs a new (immutable) Location with the given coordinates.
     * 
     * @param lat
     *            the latitude for the new Location
     * @param lng
     *            the longitude for the new Location
     */
    public Location(final double lat, final double lng) {
        latitude = lat;
        longitude = lng;
    }

    /**
     * Returns a Location that is a North-East bound on the passed Locations. If
     * one location is null, the non-null location will be returned. If both
     * locations are null, null will be returned.
     * 
     * @param one
     *            the first Location to bound
     * @param two
     *            the second Location to bound
     * @return a Location that is a North-East bound on the passed Locations
     */
    public static Location makeNorthEastBound(final Location one,
            final Location two) {
        // TODO: Consider the case where two locations are on opposite sides of
        // the Prime Meridian.

        // Return two if one is null (even if two is also null)
        if (one == null) {
            return two;
        }

        // Return one if two is null
        if (two == null) {
            return one;
        }

        return new Location(Math.max(one.latitude, two.latitude), Math.max(
                one.longitude, two.longitude));
    }

    /**
     * Getter for the latitude field.
     * 
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * Getter for the longitude field.
     * 
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Returns a Location that is a South-West bound on the passed Locations. If
     * one location is null, the non-null location will be returned. If both
     * locations are null, null will be returned.
     * 
     * @param one
     *            the first Location to bound
     * @param two
     *            the second Location to bound
     * @return a Location that is a South-West bound on the passed Locations
     */
    public static Location makeSouthWestBound(final Location one,
            final Location two) {
        // TODO: Consider the case where two locations are on opposite sides of
        // the Prime Meridian.

        // Return two if one is null (even if two is also null)
        if (one == null) {
            return two;
        }

        // Return one if two is null
        if (two == null) {
            return one;
        }

        return new Location(Math.min(one.latitude, two.latitude), Math.min(
                one.longitude, two.longitude));
    }
    
    @Override
    public String toString() {
        // TODO: Make this toString meaningful and easy to read (if possible).
        return super.toString();
    }

    @Override
    public boolean equals(final Object other) {
        // We can use instanceof because Location is 'final'
        if (other instanceof Location) {
            Location oth = (Location) other;
            return (this.latitude == oth.latitude)
                    && (this.longitude == oth.latitude);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO: double check if this is the best code for hashCode;
        int latInt = (int) Math.floor(HASH_SCALE * latitude);
        int longInt = (int) Math.floor(HASH_SCALE * longitude);
        return (latInt * longInt);
    }

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
