/**
 * 
 */
package com.HuskySoft.metrobike.backend;

/**
 * Common URI keys for working with Google Maps web API.
 * @author dutchscout
 */
public enum URIKeys {
    
    ALTERNATIVES,
    
    ARRIVAL_TIME,
    
    AVOID,
    
    BICYCLING,
    
    DEPARTURE_TIME,
    
    DESTINATION,
    
    DRIVING,
    
    FALSE,
    
    HIGHWAYS,
    
    LANGUAGE,
    
    MODE,
    
    ORIGIN,
    
    REGION,
    
    SENSOR,
    
    TOLLS,
    
    TRANSIT,
    
    TRUE,
    
    UNITS,
    
    WALKING,
    
    WAYPOINTS;
    
    public String getLowerCase() {
        return this.toString().toLowerCase();
    }
}
