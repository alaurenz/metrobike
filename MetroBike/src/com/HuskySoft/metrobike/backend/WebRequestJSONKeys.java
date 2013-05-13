/**
 * 
 */
package com.HuskySoft.metrobike.backend;

/**
 * @author coreyh3
 * 
 * 
 *
 */
public enum WebRequestJSONKeys {
    
    ROUTES,
    BOUNDS,
    NORTHEAST,
    SOUTHWEST,
    LAT,
    LNG,

    COPYRIGHTS,
    
    LEGS,
    ARRIVAL_TIME,
    DEPARTURE_TIME,
    DISTANCE,
    DURATION,
    END_ADDRESS,
    START_ADDRESS,
    START_LOCATION,
    END_LOCATION,
    
    TIME_ZONE,
    TEXT,
    VALUE,
    
    STEPS,
    HTML_INSTRUCTIONS,
    POLYLINE,
    POINTS,
    
    TRAVEL_MODE,
    
    TRANSIT_DETAILS,
    ARRIVAL_STOP,
    DEPARTURE_STOP,
    NAME,
    
    HEADSIGN,
    LINE,
    AGENCIES,
    PHONE,
    URL,
    SHORT_NAME,
    VEHICLE,
    ICON,
    TYPE,
    NUM_STOPS,
    OVERVIEW_POLYLINE,
    WARNINGS,
    STATUS,
    
    
    
    
    /**
     * Indicates that the travel mode is not known.
     */
    UNKNOWN;
    
    public String getLowerCase() {
        return this.toString().toLowerCase();
    }
}
