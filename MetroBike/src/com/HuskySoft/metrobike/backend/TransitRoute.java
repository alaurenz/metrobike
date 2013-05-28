package com.HuskySoft.metrobike.backend;

/**
 * @author Adrian Laurenzi
 * @author mengwan
 *
 *         Holds an entire route from the user's indicated start to end
 *         addresses specifically for a route that includes at least 
 *         one transit step.
 */
public final class TransitRoute implements Comparable<TransitRoute> {
	/**
	 * The source route.
	 */
	private Route route;

	/**
     * Total duration of transit steps of the route in seconds.
     */
    private long transitDuration;
    /**
     * Constructs an empty Route.
     * 
     * @param sourceRoute the source route for this
     * 					   transit route
     */
    public TransitRoute(final Route sourceRoute) {
        route = sourceRoute;
        // Calculate total duration of transit steps
        for (Leg l : sourceRoute.getLegList()) {
	        for (Step s : l.getStepList()) {
	            if (s.getTravelMode() == TravelMode.TRANSIT) {
	                transitDuration += s.getDurationInSeconds();
	            }
	        }
        }
    }
    
    /**
     * @return the source route
     */
    public Route getSourceRoute() {
        return route;
    }

    /**
     * @return the total duration of the route
     * 			in seconds
     */
    public long getDurationInSeconds() {
        return route.getDurationInSeconds();
    }

    /**
     * @return the total duration of transit steps
     * 	 		of the route in seconds
     */
    public long getTransitDurationInSeconds() {
        return transitDuration;
    }

    /**
     * Overwrite compareTo method.
     * @param other
     * @return comparison result
     */
    public int compareTo(TransitRoute other) {
        if (this.getTransitDurationInSeconds()
        		< other.getTransitDurationInSeconds()) {
            return -1;
        } else if (this.getTransitDurationInSeconds()
        		== other.getTransitDurationInSeconds()) {
            if (this.getDurationInSeconds() < other.getDurationInSeconds()) {
                return -1;
            } else if (this.getDurationInSeconds()
            		== other.getDurationInSeconds()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}
