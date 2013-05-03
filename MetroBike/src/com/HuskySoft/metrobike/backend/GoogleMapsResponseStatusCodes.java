package com.HuskySoft.metrobike.backend;

/**
 * 
 * @author coreyh3
 * 
 * Values that the "status" field in the JSON response can take on. I'm not 
 * sure if we need this but I thought it might be nice to have.
 *
 */
public enum GoogleMapsResponseStatusCodes {
    /**
     * indicates the response contains a valid result.
     */
    OK,
    /**
     * indicates at least one of the locations specified in the requests's 
     * origin, destination, or waypoints could not be geocoded.
     */
    NOT_FOUND,
    /**
     * indicates no route could be found between the origin and destination.
     */
    ZERO_RESULTS,
    /**
     * indicates that too many waypointss were provided in the request The 
     * maximum allowed waypoints is 8, plus the origin, and destination. 
     * ( Google Maps API for Business customers may contain requests with 
     * up to 23 waypoints.)
     */
    MAX_WAYPOINTS_EXCEEDED,
    /**
     * indicates that the provided request was invalid. Common causes 
     * of this status include an invalid parameter or parameter value.
     */
    INVALID_REQUEST,
    /**
     * indicates the service has received too many requests from your 
     * application within the allowed time period.
     */
    OVER_QUERY_LIMIT,
    /**
     * indicates that the service denied use of the directions service 
     * by your application.
     */
    REQUEST_DENIED,
    /**
     * indicates a directions request could not be processed due to a server error. 
     * The request may succeed if you try again.
     */
    UNKNOWN_ERROR;
}
