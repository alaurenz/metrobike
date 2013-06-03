package com.HuskySoft.metrobike.backend;

/**
 * @author coreyh3 / dutchscout Represents the mode of travel for a specific leg
 *         or as an indication of desired travel options in a directions
 *         request.
 */
public enum TravelMode {

    /**
     * Indicates bicycling.
     */
    BICYCLING,
    
    /**
     * Indicates driving. Needed for parsing some search results.
     */
    DRIVING,

    /**
     * Indicates some combination of travel modes, but most typically bicycling
     * and transit.
     */
    MIXED,

    /**
     * Indicates transit (includes train/bus/ferry/etc).
     */
    TRANSIT,

    /**
     * Indicates walking.
     */
    WALKING,

    /**
     * Indicates that the travel mode is not known.
     */
    UNKNOWN;
}
