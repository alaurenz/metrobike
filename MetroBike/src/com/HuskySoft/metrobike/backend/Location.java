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
    private static final long serialVersionUID = 1L;

    /**
     * TAG for logging statements.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: Location.java: ";

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
     * The amount to indent the toString call.
     */
    private int indent = 0;

    /**
     * The actual indented string.
     */
    private String indentString = "";

    /**
     * Constructs a new (immutable) Location with the given coordinates.
     * 
     * @param lat
     *            the latitude for the new Location
     * @param lng
     *            the longitude for the new Location
     */
    public Location(final double lat, final double lng) {
        //System.out.println(TAG + "Location()->lat: " + lat);
        //System.out.println(TAG + "Location()->lng: " + lng);
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
    public static Location makeNorthEastBound(final Location one, final Location two) {
        // TODO: Consider the case where two locations are on opposite sides of
        // the Prime Meridian.

        //System.out.println(TAG + "makeNorthEastBound()->one: " + one);
        //System.out.println(TAG + "makeNorthEastBound()->two: " + two);
        // Return two if one is null (even if two is also null)
        if (one == null) {
            //System.out.println(TAG + "makeNorthEastBound()->Returning two.");
            return two;
        }

        // Return one if two is null
        if (two == null) {
            //System.out.println(TAG + "makeNorthEastBound()->Returning one.");
            return one;
        }

        //System.out.println(TAG + "makeNorthEastBound()->Creating a new Location object.");
        return new Location(Math.max(one.latitude, two.latitude), Math.max(one.longitude,
                two.longitude));
    }

    /**
     * Getter for the latitude field.
     * 
     * @return the latitude
     */
    public double getLatitude() {
        //System.out.println(TAG + "getLatitude()->latitude: " + latitude);
        return latitude;
    }

    /**
     * Getter for the longitude field.
     * 
     * @return the longitude
     */
    public double getLongitude() {
        //System.out.println(TAG + "getLongitude()->longitude: " + longitude);
        return longitude;
    }

    /**
     * Returns a string representation of this location.
     * 
     * @return this location in format: "latitude,longitude".
     */
    public String getLocationAsString() {
        return latitude + "," + longitude;
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
    public static Location makeSouthWestBound(final Location one, final Location two) {
        // TODO: Consider the case where two locations are on opposite sides of
        // the Prime Meridian.

        //System.out.println(TAG + "makeSouthWestBound()->one: " + one);
        //System.out.println(TAG + "makeSouthWestBound()->two: " + two);
        // Return two if one is null (even if two is also null)
        if (one == null) {
            //System.out.println(TAG + "makeSouthWestBound()->Returning two.");
            return two;
        }

        // Return one if two is null
        if (two == null) {
            //System.out.println(TAG + "makeSouthWestBound()->Returning one.");
            return one;
        }

        //System.out.println(TAG + "makeSouthWestBound()->Creating a new Location object.");
        return new Location(Math.min(one.latitude, two.latitude), Math.min(one.longitude,
                two.longitude));
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
        for (int i = 0; i < newIndent; i++) {
            indentString = Utility.getIndentString();
        }
    }

    /**
     * This is the getter for the indent field.
     * 
     * @return the amount to indent.
     */
    public int getIndent() {
        ////System.out.println(TAG + "getIndent()->indent: " + indent);
        return indent;
    }

    @Override
    public String toString() {
        StringBuilder locationString = new StringBuilder();
        String extraIndent = indentString + Utility.getIndentString();
        locationString.append(indentString + "Location:\n");
        locationString.append(extraIndent + "Latitude: " + latitude + "\n");
        locationString.append(extraIndent + "Longitude: " + longitude + "\n");
        return locationString.toString();
    }

    @Override
    public boolean equals(final Object other) {
        // We can use instanceof because Location is 'final'
        if (other instanceof Location) {
            Location oth = (Location) other;
            return (this.latitude == oth.latitude) && (this.longitude == oth.longitude);
        }
        //System.out.println(TAG + "equals()->other was not an instanceOf Location");
        return false;
    }

    @Override
    public int hashCode() {
        int latInt = (int) Math.floor(HASH_SCALE * latitude);
        int longInt = (int) Math.floor(HASH_SCALE * longitude);
        int hash = (latInt * longInt);
        //System.out.println(TAG + "hashCode()->hash: " + hash);
        return hash;
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
        out.writeInt(indent);
        out.writeObject(indentString);
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
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read each field from the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the writing order in writeObject()
        latitude = in.readDouble();
        longitude = in.readDouble();
        indent = in.readInt();
        indentString = (String) in.readObject();
    }
}
