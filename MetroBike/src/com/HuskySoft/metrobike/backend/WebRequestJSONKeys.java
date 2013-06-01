/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import android.annotation.SuppressLint;

/**
 * An enum to represent the possible JSON keywords in replies from Google Maps
 * Web API requests.
 * 
 * @author coreyh3
 */
public enum WebRequestJSONKeys {

    /**
     * The list of result routes.
     */
    ROUTES,

    /**
     * The geographical bounds used when displaying the route.
     */
    BOUNDS,

    /**
     * The northeast bound of the map to display the route.
     */
    NORTHEAST,

    /**
     * The southwest bound of the map to display the route.
     */
    SOUTHWEST,

    /**
     * A latitude number.
     */
    LAT,

    /**
     * A longitude number.
     */
    LNG,

    /**
     * A location (used for transit departure and arrival stops.
     */
    LOCATION,

    /**
     * Copyrights of map and transit data for display.
     */
    COPYRIGHTS,

    /**
     * The list of legs in the route.
     */
    LEGS,

    /**
     * The time to arrive.
     */
    ARRIVAL_TIME,

    /**
     * The time to depart.
     */
    DEPARTURE_TIME,

    /**
     * The segment distance.
     */
    DISTANCE,

    /**
     * The segment duration (time).
     */
    DURATION,

    /**
     * The end address.
     */
    END_ADDRESS,

    /**
     * The start address.
     */
    START_ADDRESS,

    /**
     * The start location (Lat/Lng).
     */
    START_LOCATION,

    /**
     * The end location (Lat/Lng).
     */
    END_LOCATION,

    /**
     * The time zone.
     */
    TIME_ZONE,

    /**
     * Text related to this segment.
     */
    TEXT,

    /**
     * A value.
     */
    VALUE,

    /**
     * The list of steps for this leg.
     */
    STEPS,

    /**
     * HTML instructions to be displayed to the user during navigation/trip
     * instructions.
     */
    HTML_INSTRUCTIONS,

    /**
     * A set of encoded points representing the smooth route path.
     */
    POLYLINE,

    /**
     * The points string in a polyline set.
     */
    POINTS,

    /**
     * The travel mode for this segment.
     */
    TRAVEL_MODE,

    /**
     * Details about the transit (bus number, etc).
     */
    TRANSIT_DETAILS,

    /**
     * The arrival transit stop location.
     */
    ARRIVAL_STOP,

    /**
     * The departure transit stop location.
     */
    DEPARTURE_STOP,

    /**
     * The transit service name.
     */
    NAME,

    /**
     * An abbreviated transit route description, eg. one that might be displayed
     * on the sign of a city bus.
     */
    HEADSIGN,

    /**
     * A transit line identifier.
     */
    LINE,

    /**
     * Transit agencies.
     */
    AGENCIES,

    /**
     * Transit agency phone number.
     */
    PHONE,

    /**
     * Transit agency website or trip url.
     */
    URL,

    /**
     * Short version of transit route name.
     */
    SHORT_NAME,

    /**
     * Transit vehicle type (?).
     */
    VEHICLE,

    /**
     * An icon to display for this item.
     */
    ICON,

    /**
     * Type of transit.
     */
    TYPE,

    /**
     * Number of stops to stay aboard transit.
     */
    NUM_STOPS,

    /**
     * An overview of the route path give as rougher encoded location points.
     */
    OVERVIEW_POLYLINE,

    /**
     * Warnings about the route that must be displayed.
     */
    WARNINGS,

    /**
     * The status of the request for directions.
     */
    STATUS,

    /**
     * Indicates that the travel mode is not known.
     */
    UNKNOWN;

    /**
     * Returns the lower-case string matching the enum name. For use in parsing
     * JSON responses
     * 
     * @return the lower-case string matching the enum name. For use in parsing
     *         JSON responses
     */
    @SuppressLint("DefaultLocale")
    public String getLowerCase() {
        return this.toString().toLowerCase();
    }
}
