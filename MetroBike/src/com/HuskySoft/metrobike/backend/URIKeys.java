/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import android.annotation.SuppressLint;

/**
 * Common URI keys for working with Google Maps web API. These are meant to
 * mirror the keywords described here:
 * https://developers.google.com/maps/documentation/directions/
 * 
 * @author dutchscout
 */
public enum URIKeys {

    /**
     * If true, asks for more than one solution.
     */
    ALTERNATIVES,

    /**
     * When to arrive at the destination. Cannot be used with DEPARTURE_TIME.
     */
    ARRIVAL_TIME,

    /**
     * Places to avoid.
     */
    AVOID,

    /**
     * Indicates bicycling, used with MODE.
     */
    BICYCLING,

    /**
     * When to leave the origin. Cannot be used with ARRIVAL_TIME.
     */
    DEPARTURE_TIME,

    /**
     * The location to get directions to.
     */
    DESTINATION,

    /**
     * Indicates driving a car, used with MODE.
     */
    DRIVING,

    /**
     * Means false (yes, really!).
     */
    FALSE,

    /**
     * Refers to highways, used with AVOID.
     */
    HIGHWAYS,

    /**
     * An option to indicate the desired directions language.
     */
    LANGUAGE,

    /**
     * The travel mode to use (BICYCLING/DRIVING/TRANSIT/WALKING).
     */
    MODE,

    /**
     * The location to get directions from.
     */
    ORIGIN,

    /**
     * Indicates a region code for getting direction information.
     */
    REGION,

    /**
     * Required for some queries, indicate whether the request comes from a
     * device with a location sensor onboard. For mobile apps, this should
     * generally be TRUE.
     * 
     */
    SENSOR,

    /**
     * Indicates road use tolls, used with AVOID.
     */
    TOLLS,

    /**
     * Indicates taking public transit such as busses, trains, etc. Used with
     * MODE.
     */
    TRANSIT,

    /**
     * Means true (yes, really!).
     */
    TRUE,

    /**
     * The units to use.
     */
    UNITS,

    /**
     * Indicates walking, including along sidewalks. Used with MODE.
     */
    WALKING,

    /**
     * Indicates points to stop at during travel.
     */
    WAYPOINTS;

    /**
     * Returns the lower-case string matching the enum name. For use in building
     * actual queries.
     * 
     * @return the lower-case string matching the enum name. For use in building
     *         actual queries
     */
    @SuppressLint("DefaultLocale")
    public String getLowerCase() {
        return this.toString().toLowerCase();
    }
}
