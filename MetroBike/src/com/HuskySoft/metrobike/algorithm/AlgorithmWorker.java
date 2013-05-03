package com.HuskySoft.metrobike.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.HuskySoft.metrobike.backend.DirectionsRequest;
import com.HuskySoft.metrobike.backend.Route;

/**
 * An abstract class for the implementation of a metrobike route-finding
 * algorithm. This abstract class provides error message and result managing
 * facilities and defines the interface for the various algorithm
 * implementations.
 * 
 * @author dutchscout
 */
public abstract class AlgorithmWorker {

    /**
     * Holds any error message(s) generated during algorithm execution.
     */
    private String errorMessages;

    /**
     * Holds the routes found/built by the worker.
     */
    private List<Route> results;

    /**
     * Runs the algorithm on the RequestParameters.
     * 
     * @param toProcess
     *            the RequestParameters object describing the search to make
     * 
     */
    public abstract void findRoutes(
            DirectionsRequest.RequestParameters toProcess);

    /**
     * Returns true if there was an error, false otherwise.
     * 
     * @return true if there was an error, false otherwise
     */
    public final boolean hasErrors() {
        return errorMessages == null;
    }

    /**
     * Adds a message to the error message log.
     * 
     * @param theError
     *            the message to add
     */
    protected final void addError(final String theError) {
        if (errorMessages == null) {
            errorMessages = theError;
        } else {
            errorMessages = errorMessages + "\n" + theError;
        }
    }

    /**
     * Returns a String containing a human-readable error message.
     * 
     * @return A string if there is an error. Null otherwise.
     */
    public final String getErrors() {
        return errorMessages;
    }

    /**
     * Clears all error messages generated during algorithm execution.
     */
    public final void clearErrors() {
        errorMessages = null;
    }

    /**
     * Add results found by the algorithm to the results list.
     * 
     * @param theResults
     *            the results to add
     */
    protected final void addResults(final List<Route> theResults) {
        if (results == null) {
            results = new ArrayList<Route>(theResults);
        } else {
            results.addAll(theResults);
        }
    }

    /**
     * Clear the stored results.
     */
    protected final void clearResults() {
        results = null;
    }

    /**
     * This method returns the list of routes the resulted from the last call to
     * findRoutes().
     * 
     * @return A list of routes that this algorithm considers to be
     *         near-optimal. Returns null if no routes were found, if
     *         findRoutes() has not yet been run, or if there is an error.
     */
    public final List<Route> getResults() {
        return results;
    }

    /**
     * A helper method to build a list of routes from a String holding Google's
     * JSON response.
     * 
     * @param srcJSON
     *            the String holding the JSON to parse
     * @return a list of Routes parsed from the JSON
     */
    protected final List<Route> buildRouteListFromJSONString(
            final String srcJSON) {
        return null;
    }

}
