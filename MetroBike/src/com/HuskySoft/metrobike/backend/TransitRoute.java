package com.HuskySoft.metrobike.backend;

/**
 * @author Adrian Laurenzi
 * @author mengwan
 * 
 *         Holds an entire route from the user's indicated start to end
 *         addresses specifically for a route that includes at least one transit
 *         step.
 */
public final class TransitRoute implements Comparable<TransitRoute> {

    /**
     * TAG for logging statements.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: TransitRoute.java: ";

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
     * @param sourceRoute
     *            the source route for this transit route
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
     * @return the total duration of the route in seconds
     */
    public long getDurationInSeconds() {
        System.out.println(TAG + "getDurationInSeconds()->route.getDurationInSeconds(): "
                + route.getDurationInSeconds());
        return route.getDurationInSeconds();
    }

    /**
     * @return the total duration of transit steps of the route in seconds
     */
    public long getTransitDurationInSeconds() {
        System.out.println(TAG + "getTransitDurationInSeconds()->transitDuration: "
                + transitDuration);
        return transitDuration;
    }

    /**
     * Overwrite compareTo method.
     * 
     * @param other
     *            The other route to compare to.
     * @return comparison result
     */
    public int compareTo(final TransitRoute other) {
        System.out.println(TAG + "compareTo()->Entering compareTo");
        if (this.getTransitDurationInSeconds() < other.getTransitDurationInSeconds()) {
            System.out.println(TAG + "compareTo()->Exiting compareTo");
            return -1;
        } else if (this.getTransitDurationInSeconds() == other.getTransitDurationInSeconds()) {
            if (this.getDurationInSeconds() < other.getDurationInSeconds()) {
                System.out.println(TAG + "compareTo()->Exiting compareTo");
                return -1;
            } else if (this.getDurationInSeconds() == other.getDurationInSeconds()) {
                System.out.println(TAG + "compareTo()->Exiting compareTo");
                return 0;
            } else {
                System.out.println(TAG + "compareTo()->Exiting compareTo");
                return 1;
            }
        } else {
            System.out.println(TAG + "compareTo()->Exiting compareTo");
            return 1;
        }
    }
}
