package com.HuskySoft.metrobike.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.HuskySoft.metrobike.algorithm.AlgorithmWorker;
import com.HuskySoft.metrobike.algorithm.BicycleOnlyAlgorithm;
import com.HuskySoft.metrobike.algorithm.SimpleComboAlgorithm;
import com.HuskySoft.metrobike.algorithm.SmartAlgorithm;

/**
 * @author coreyh3
 * @author dutchscout Represents a request for directions to the MetroBike
 *         backend.
 */
public final class DirectionsRequest implements Serializable {
    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods, so we don't have old-version DirectionsRequest
     * objects (ex: from the log) being made into new-version DirectionsRequest
     * objects.
     */
    private static final long serialVersionUID = 2L;

    /**
     * The tag for Android logging.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: DirectionsRequest.java: ";

    /**
     * The longest route a request may return, in kilometers.
     */
    public static final long MAX_ROUTE_LENGTH_IN_METERS = 500000;

    /**
     * The request parameters object for this request, which specifies all of
     * the important request details.
     */
    private RequestParameters myParams;

    /**
     * When the request is complete, this holds a list of all of the chosen
     * routes.
     */
    private List<Route> solutions;

    /**
     * Holds any error message(s) generated during execution.
     */
    private String errorMessages;

    /**
     * Constructs a new DirectionsRequest object.
     */
    public DirectionsRequest() {
        myParams = new RequestParameters();
        solutions = null;
        errorMessages = null;
    }

    /**
     * Initiates the request calculation. This is a blocking call. NOTE: This
     * method is currently under heavy testing and does not currently meet style
     * guidelines.
     * 
     * @return the final status of the doRequest process
     */
    public DirectionsStatus doDummyRequest() {

        // First, validate our parameters!
        if (!myParams.validateParameters()) {
            return DirectionsStatus.INVALID_REQUEST_PARAMS;
        }

        System.err.println(TAG + "RUNNING ON DUMMY JSON RESPONSE");

        solutions = new ArrayList<Route>();

        JSONObject myJSON;
        try {
            myJSON = new JSONObject(DUMMY_BICYCLE_JSON);
            // Commenting this out until we have this part completely working.
            // myJSON = new JSONObject(jsonResponse);
            if (!myJSON.getString(WebRequestJSONKeys.STATUS.getLowerCase()).equalsIgnoreCase(
                    GoogleMapsResponseStatusCodes.OK.toString())) {
                System.err.println(TAG + "JSON Response returned: "
                        + myJSON.getString(WebRequestJSONKeys.STATUS.getLowerCase()));
            }
        } catch (JSONException e) {
            appendErrorMessage("Error parsing JSON.");
            return DirectionsStatus.PARSING_ERROR;
        }

        JSONArray routesArray;

        try {
            routesArray = myJSON.getJSONArray(WebRequestJSONKeys.ROUTES.getLowerCase());
            for (int i = 0; i < routesArray.length(); i++) {
                Route currentLeg = Route.buildRouteFromJSON(routesArray.getJSONObject(i));
                solutions.add(currentLeg);
            }
            System.err.println("JSON_TEST" + "Processed " + routesArray.length() + " routes!");
        } catch (JSONException e1) {
            appendErrorMessage("Error parsing JSON routes.");
            return DirectionsStatus.PARSING_ERROR;
        }

        return DirectionsStatus.REQUEST_SUCCESSFUL;
    }

    /**
     * Initiates the request calculation. This is a blocking call.
     * 
     * @return the final status of the doRequest process
     */
    public DirectionsStatus doRequest() {

        // First, validate our parameters!
        if (!myParams.validateParameters()) {
            return DirectionsStatus.INVALID_REQUEST_PARAMS;
        }

        solutions = new ArrayList<Route>();

        AlgorithmWorker alg = null;

        // Query the simple algorithm first
        switch (myParams.getTravelMode()) {
        case BICYCLING:
            alg = new BicycleOnlyAlgorithm();
            alg.setResource(myParams.getResource());
            alg.setQueryLanguage(myParams.getQueryLanguage());
            DirectionsStatus simpleStatus = doAlgorithm(alg);

            // If the user cancelled the request while this algorithm
            // was running, stop trying other algorithms
            if (simpleStatus == DirectionsStatus.USER_CANCELLED_REQUEST){
            	return simpleStatus;
            }
            
            // Sort results by total duration, then filter by user requirements
            filterSolutions();
            if (simpleStatus == DirectionsStatus.REQUEST_SUCCESSFUL && solutions.size() == 0) {
                return DirectionsStatus.NO_RESULTS_FOUND;
            }

            return simpleStatus;
        case MIXED:
            // Travel Mode Mix also needs the bicycle only routes
            BicycleOnlyAlgorithm bikeAlg = new BicycleOnlyAlgorithm();
            bikeAlg.setResource(myParams.getResource());
            bikeAlg.setQueryLanguage(myParams.getQueryLanguage());
            DirectionsStatus bikeStatus = doAlgorithm(bikeAlg);
            
            // If the user cancelled the request while this algorithm
            // was running, stop trying other algorithms
            if (bikeStatus == DirectionsStatus.USER_CANCELLED_REQUEST){
            	return bikeStatus;
            }
            
            // this is to prevent need for SmartAlgorithm to re-query
            // directions API
            SmartAlgorithm smartAlg = new SmartAlgorithm();
            smartAlg.setResource(myParams.getResource());
            smartAlg.setQueryLanguage(myParams.getQueryLanguage());
            smartAlg.setReferencedRoute(bikeAlg.getReferencedRoute());
            DirectionsStatus smartStatus = doAlgorithm(smartAlg);
            
            // If the user cancelled the request while this algorithm
            // was running, stop trying other algorithms
            if (smartStatus == DirectionsStatus.USER_CANCELLED_REQUEST){
            	return smartStatus;
            }
            
            SimpleComboAlgorithm scAlg = new SimpleComboAlgorithm();
            scAlg.setResource(myParams.getResource());
            scAlg.setQueryLanguage(myParams.getQueryLanguage());
            DirectionsStatus comboStatus = doAlgorithm(scAlg);

            // If the user cancelled the request while this algorithm
            // was running, stop!
            if (comboStatus == DirectionsStatus.USER_CANCELLED_REQUEST){
            	return comboStatus;
            }
            
            if (bikeStatus == DirectionsStatus.CONNECTION_ERROR 
                    || smartStatus == DirectionsStatus.CONNECTION_ERROR 
                    || comboStatus == DirectionsStatus.CONNECTION_ERROR) {
                return DirectionsStatus.CONNECTION_ERROR;
            }
            if (bikeStatus.isError() && smartStatus.isError() && comboStatus.isError()) {
                return bikeStatus; // Return the first error we received
            }

            // Sort results by total duration, then filter by user requirements
            Collections.sort(solutions);
            filterSolutions();
            if (solutions.size() == 0) {
                // If filter took out all results, none were found
                return DirectionsStatus.NO_RESULTS_FOUND;
            }

            return DirectionsStatus.REQUEST_SUCCESSFUL;
        default:
            System.err
                    .println("Incorrect Travel Mode here: " + myParams.getTravelMode().toString());
            return DirectionsStatus.UNSUPPORTED_TRAVEL_MODE_ERROR;
        }

    }

    /**
     * Filters the result solutions based on the request parameters object and
     * the max trip distance.
     */
    private void filterSolutions() {
        List<Route> newSolutions = new ArrayList<Route>();

        for (Route res : solutions) {
            // If we discard a route, this will say why.
            String routeDeleteReason = null;

            // First, check for route length
            if (res.getDistanceInMeters() > MAX_ROUTE_LENGTH_IN_METERS) {
                routeDeleteReason = "Discarding route because length " + res.getDistanceInMeters()
                        + "(m) > " + MAX_ROUTE_LENGTH_IN_METERS + "(m)";
            }

            // Next, check for biking distance
            long bikeDistance = 0;
            long numTransitSteps = 0;

            // Calculate total distance of bicycle riding and number of transit
            // steps
            for (Leg l : res.getLegList()) {
                for (Step s : l.getStepList()) {
                    if (s.getTravelMode() == TravelMode.BICYCLING) {
                        bikeDistance += s.getDistanceInMeters();
                    } else if (s.getTravelMode() == TravelMode.TRANSIT) {
                        numTransitSteps++;
                    }
                }
            }

            long bikeMin = myParams.getMinDistanceToBikeInMeters();
            long bikeMax = myParams.getMaxDistanceToBikeInMeters();

            // Make sure bikeMin <= bikeDistance <= bikeMax
            // (but only if bikeMin and/or bikeMax is set)
            if ((bikeMin != RequestParameters.DONT_CARE) && (bikeDistance < bikeMin)) {
                routeDeleteReason = "Discarding route because bicycling distance " + bikeDistance
                        + "(m) < " + bikeMin + "(m) set by user.";
            }

            if ((bikeMax != RequestParameters.DONT_CARE) && (bikeDistance > bikeMax)) {
                routeDeleteReason = "Discarding route because bicycling distance " + bikeDistance
                        + "(m) > " + bikeMin + "(m) set by user.";
            }

            // Next, check for transit transfers
            long numTransfers;

            if (numTransitSteps == 0) {
                numTransfers = 0;
            } else {
                numTransfers = numTransitSteps - 1;
            }

            long transferMin = myParams.getMinNumberBusTransfers();
            long transferMax = myParams.getMaxNumberBusTransfers();

            // Make sure transferMin <= numTransfers <= transferMax
            // (but only if transferMin and/or transferMax is set)
            if ((transferMin != RequestParameters.DONT_CARE) && (numTransfers < transferMin)) {
                routeDeleteReason = "Discarding route because number of tranfers " + numTransfers
                        + " < " + transferMin + " set by user.";
            }

            if ((transferMax != RequestParameters.DONT_CARE) && (numTransfers > transferMax)) {
                routeDeleteReason = "Discarding route because number of tranfers " + numTransfers
                        + " > " + transferMin + " set by user.";
            }

            // If there were no errors, keep the route!
            // Otherwise, save an error message and discard it.
            if (routeDeleteReason == null) {
                newSolutions.add(res);
            } else {
                System.err.println(routeDeleteReason);
                appendErrorMessage(routeDeleteReason);
            }
        }

        solutions = newSolutions;
    }

    /**
     * Run the given algorithm and add all the routes.
     * 
     * @param algorithmWorker
     *            : the abstracted algorithm class
     * @return direction status after the algorithm completes
     */
    private DirectionsStatus doAlgorithm(final AlgorithmWorker algorithmWorker) {
        DirectionsStatus status = algorithmWorker.findRoutes(myParams);

        if (status.isError()) {
            appendErrorMessage(algorithmWorker.getErrors());
            return status;
        }

        List<Route> routes = algorithmWorker.getResults();

        if (routes == null) {
            System.err.println(TAG + "Got null from " + algorithmWorker.getClass().getName()
                    + " without an error");
            appendErrorMessage(DirectionsStatus.NO_RESULTS_FOUND.getMessage());
            return DirectionsStatus.NO_RESULTS_FOUND;
        }
        solutions.addAll(routes);

        return DirectionsStatus.REQUEST_SUCCESSFUL;
    }

    /**
     * Holds the parameters of a request for directions. Made separate from
     * DirectionsRequest so it can be passed to various Algorithm
     * implementations.
     * 
     * @author dutchscout
     */
    public final class RequestParameters implements Serializable {
        /*
         * NOTE: The structure of this inner class is weird. The getters are
         * inside the class, but the setters are outside the class (inside the
         * DirectionsRequest). This is attempting to make the class read-only to
         * the world, but writable by its enclosing DirectionsRequest object.
         * That way, the DirectionsRequest object can control if/how other
         * classes can modify the RequestParameters, but we can still pass
         * around the RequestParameters object as input to the AlgorithmWorker
         * classes.
         */

        /**
         * Part of serializability, this id tracks if a serialized object can be
         * deserialized using this version of the class.
         * 
         * NOTE: Please add 1 to this number every time you change the
         * readObject() or writeObject() methods, so we don't have old-version
         * DirectionsRequest objects (ex: from the log) being made into
         * new-version DirectionsRequest objects.
         */
        private static final long serialVersionUID = 2L;

        /**
         * Represents a number-based option that hasn't been specified by the
         * user.
         */
        public static final long DONT_CARE = Long.MIN_VALUE;

        /**
         * String representation of DONT_CARE.
         */
        public static final String DONT_CARE_STRING = "<don't care>";

        /**
         * The Android log tag for this inner class.
         */
        private static final String RP_TAG = "DirectionsRequest";

        /**
         * The starting location that the user wants directions from.
         */
        private String startAddress = null;

        /**
         * The ending location that the user wants directions to.
         */
        private String endAddress = null;

        /**
         * Time the user would like to arrive at their destination. If this is
         * set (ie. it equals a nonzero value), then departure time should not
         * be set.
         */
        private long arrivalTime = DONT_CARE;

        /**
         * Time the user would like to depart from their starting location. If
         * this is set (ie. it equals a nonzero value), then arrival time should
         * not be set.
         */
        private long departureTime = DONT_CARE;

        /**
         * The travel mode the user is requesting directions for. Default is
         * "MIXED."
         */
        private TravelMode travelMode = TravelMode.MIXED;

        /**
         * An optional field. It sets a minimum distance the user would like to
         * bike.
         */
        private long minDistanceToBikeInMeters = DONT_CARE;

        /**
         * An optional field. It sets the maximum distance the user would like
         * to bike. (In case they are exercise averse)
         */
        private long maxDistanceToBikeInMeters = DONT_CARE;

        /**
         * An optional field. The minimum number of bus transfers to have. In
         * case they like multiple transfers.
         */
        private long minNumberBusTransfers = DONT_CARE;

        /**
         * An optional field. Setting the maximum number of bus transfers in
         * case the user doesn't want routes with lots of transfers.
         */
        private long maxNumberBusTransfers = DONT_CARE;

        /**
         * This determines whether or not to use the StubGoogleAPIWrapper.
         */
        private APIQuery resource = null;
        
        /**
         * This determines the language that directions responses
         * are returned in from API calls (English by default).
         * NOTE: Must be a valid Google Directions API language code
         */
        private String queryLanguage = "en";

        /**
         * Getter for the resource field.
         * 
         * @return Returns the current APIQuery Object.
         */
        public APIQuery getResource() {
            return resource;
        }

        /**
         * @return the startAddress
         */
        public String getStartAddress() {
            ////System.out.println(TAG + "getStartAddress()->startAddress: " + startAddress);
            return startAddress;
        }

        /**
         * @return the endAddress
         */
        public String getEndAddress() {
            ////System.out.println(TAG + "getEndAddress()->endAddress: " + endAddress);
            return endAddress;
        }

        /**
         * @return the arrivalTime
         */
        public long getArrivalTime() {
            //System.out.println(TAG + "getArrivalTime()->arrivalTime: " + arrivalTime);
            return arrivalTime;
        }

        /**
         * @return the departureTime
         */
        public long getDepartureTime() {
            //System.out.println(TAG + "getDepartureTime()->departureTime: " + departureTime);
            return departureTime;
        }

        /**
         * @return the travelMode
         */
        public TravelMode getTravelMode() {
            //System.out.println(TAG + "getTravelMode()->travelMode.name(): " + travelMode.name());
            return travelMode;
        }

        /**
         * @return the minDistanceToBikeInMeters
         */
        public long getMinDistanceToBikeInMeters() {
            //System.out.println(TAG + "getMinDistanceToBikeInMeters()->minDistanceToBikeInMeters: "
            //        + minDistanceToBikeInMeters);
            return minDistanceToBikeInMeters;
        }

        /**
         * @return the maxDistanceToBikeInMeters
         */
        public long getMaxDistanceToBikeInMeters() {
            //System.out.println(TAG + "getMaxDistanceToBikeInMeters()->maxDistanceToBikeInMeters: "
            //        + maxDistanceToBikeInMeters);
            return maxDistanceToBikeInMeters;
        }

        /**
         * @return the minNumberBusTransfers
         */
        public long getMinNumberBusTransfers() {
            //System.out.println(TAG + "getMinNumberBusTransfers()->minNumberBusTransfers: "
            //        + minNumberBusTransfers);
            return minNumberBusTransfers;
        }

        /**
         * @return the maxNumberBusTransfers
         */
        public long getMaxNumberBusTransfers() {
            //System.out.println(TAG + "getMaxNumberBusTransfers()->maxNumberBusTransfers: "
            //        + maxNumberBusTransfers);
            return maxNumberBusTransfers;
        }
        
        /**
         * @return the queryLanguage
         */
        public String getQueryLanguage() {
            //System.out.println(TAG + "getQueryLanguage()->queryLanguage: "
            //        + queryLanguage);
            return queryLanguage;
        }

        /**
         * Returns whether the request parameters are valid. If they are not
         * valid, also set errors indicating the problem.
         * 
         * @return true if parameters are valid, false otherwise
         */
        public boolean validateParameters() {

            if (startAddress == null || startAddress.trim().isEmpty()) {
                appendErrorMessage("Origin address may not be empty.");
                System.err.println(TAG + "validateParameters()->startAddress was null or empty.");
                return false;
            }

            if (endAddress == null || endAddress.trim().isEmpty()) {
                appendErrorMessage("Destination address may not be empty.");
                System.err.println(TAG + "validateParameters()->endAddress was null or empty.");
                return false;
            }

            switch (travelMode) {
            case BICYCLING:
                // This mode requires no extra checks.
                break;
            case TRANSIT:
            case MIXED:
                // Check to be sure we got a time!
                if (departureTime == DONT_CARE && arrivalTime == DONT_CARE) {
                    appendErrorMessage("Directions for transit must include"
                            + " a departure or arrival time.");
                    System.err.println(TAG + "validateParameters()->departureTime or "
                            + "arrivalTime must be set for transit.");
                    return false;
                }
                break;
            default:
                // Including walking since the Algorithm switch statements
                // don't check for walking.
                appendErrorMessage("Unsupported desired travel mode " + travelMode);
                return false;
            }

            // Not checking if the departure time is valid (if set)
            // as google lets you set times in the past.

            // Validate optional parameters
            if ((minDistanceToBikeInMeters != DONT_CARE && minDistanceToBikeInMeters < 0)
                    || (maxDistanceToBikeInMeters != DONT_CARE && maxDistanceToBikeInMeters < 0)
                    || (minNumberBusTransfers != DONT_CARE && minNumberBusTransfers < 0)
                    || (maxNumberBusTransfers != DONT_CARE && maxNumberBusTransfers < 0)) {
                appendErrorMessage("All optional parameters (biking distance and bus transfers)"
                        + " must be greater than or equal to zero");
                System.err.println(TAG + "validateParameters()-> :All optional parameters must "
                        + "be greater than or equal to zero.");
                return false;
            }

            if (minDistanceToBikeInMeters != DONT_CARE && maxDistanceToBikeInMeters != DONT_CARE
                    && minDistanceToBikeInMeters > maxDistanceToBikeInMeters) {
                appendErrorMessage("Min > Max for distance to bike.");
                System.err.println(TAG + "validateParameters()->Min > Max for distance to bike.");
                return false;
            }

            if (minNumberBusTransfers != DONT_CARE && maxNumberBusTransfers != DONT_CARE
                    && minNumberBusTransfers > maxNumberBusTransfers) {
                appendErrorMessage("Min > Max for number of transfers");
                System.err.println(TAG + "validateParameters()->Min > Max for number of "
                        + "transfers");
                return false;
            }
            
            // validate queryLanguage (currently only accepting English and Chinese (Simplified)
            if(!queryLanguage.equals("en") && !queryLanguage.equals("zh-CN")) {
                appendErrorMessage("Invalid queryLanguage (only 'en' and 'zh-CN' accepted)");
                System.err.println(TAG + "validateParameters()->Invalid queryLanguage (only"
                		+ " 'en' and 'zh-CN' accepted)");
                return false;
            }

            // Printing out the parameters for debug purposes
            //System.out.println(RP_TAG + "StartAddress: " + startAddress);
            //System.out.println(RP_TAG + "EndAddress: " + endAddress);
            //System.out.println(RP_TAG + "ArrivalTime: " + arrivalTime);
            //System.out.println(RP_TAG + "DepartureTime: " + departureTime);
            //System.out.println(RP_TAG + "TravelMode: " + travelMode);
            //System.out.println(RP_TAG + "MinDistanceToBikeInMeters: " + minDistanceToBikeInMeters);
            //System.out.println(RP_TAG + "MaxDistanceToBikeInMeters: " + maxDistanceToBikeInMeters);
            //System.out.println(RP_TAG + "MinNumberBusTransfers: " + minNumberBusTransfers);
            //System.out.println(RP_TAG + "MaxNumberBusTransfers: " + maxNumberBusTransfers);
            //System.out.println(RP_TAG + "QueryLanguage: " + queryLanguage);
            return true;
        }

        /**
         * Implements a custom serialization of a DirectionsRequest object.
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
            out.writeObject(startAddress);
            out.writeObject(endAddress);
            out.writeLong(arrivalTime);
            out.writeLong(departureTime);
            out.writeObject(travelMode);
            out.writeLong(minDistanceToBikeInMeters);
            out.writeLong(maxDistanceToBikeInMeters);
            out.writeLong(minNumberBusTransfers);
            out.writeLong(maxNumberBusTransfers);
            out.writeObject(queryLanguage);
        }

        /**
         * Implements a custom deserialization of a DirectionsRequest object.
         * 
         * @param in
         *            the ObjectInputStream to read from
         * @throws IOException
         *             if the stream fails
         * @throws ClassNotFoundException
         *             if a class is not found
         */
        private void readObject(final ObjectInputStream in) throws IOException,
                ClassNotFoundException {
            // Read each field from the stream in a specific order.
            // Specifying this order helps shield the class from problems
            // in future versions.
            // The order must be the same as the writing order in writeObject()
            startAddress = (String) in.readObject();
            endAddress = (String) in.readObject();
            arrivalTime = in.readLong();
            departureTime = in.readLong();
            travelMode = (TravelMode) in.readObject();
            minDistanceToBikeInMeters = in.readLong();
            maxDistanceToBikeInMeters = in.readLong();
            minNumberBusTransfers = in.readLong();
            maxNumberBusTransfers = in.readLong();
            queryLanguage = (String) in.readObject();
        }

        @Override
        public String toString() {
            return "RequestParameters:" + "\nstartAddress: " + startAddress + "\nendAddress: "
                    + endAddress + "\narrivalTime: " + optionToString(arrivalTime)
                    + "\ndepartureTime: " + optionToString(departureTime) + "\ntravelMode: "
                    + travelMode.toString() + "\nminDistanceToBikeInMeters: "
                    + optionToString(minDistanceToBikeInMeters) + "\nmaxDistanceToBikeInMeters: "
                    + optionToString(maxDistanceToBikeInMeters) + "\nminNumberBusTransfers: "
                    + optionToString(minNumberBusTransfers) + "\nmaxNumberBusTransfers: "
                    + optionToString(maxNumberBusTransfers) + "\nqueryLanguage: "
                    + queryLanguage + "\n";
        }

        /**
         * Returns the number as a string, or "Don't care" if it matches the
         * DONT_CARE constant value.
         * 
         * @param toFormat
         *            the number to format
         * @return the number as a string, or "Don't care" if it matches the
         *         DONT_CARE constant value
         */
        private String optionToString(final long toFormat) {
            String toReturn = "";
            if (toFormat == DONT_CARE) {
                toReturn = DONT_CARE_STRING;
            } else {
                toReturn = "" + toFormat;
            }

            return toReturn;
        }

    }

    /**
     * Set the starting address for the trip.
     * 
     * @param newStartAddress
     *            the address to start from.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setStartAddress(final String newStartAddress) {
        //System.out.println(TAG + "setStartAddress()->newStartAddress: " + newStartAddress);
        myParams.startAddress = newStartAddress;
        return this;
    }

    /**
     * Set the ending address for the trip.
     * 
     * @param newEndAddress
     *            the address to end at.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setEndAddress(final String newEndAddress) {
        //System.out.println(TAG + "setEndAddress()->newEndAddress: " + newEndAddress);
        myParams.endAddress = newEndAddress;
        return this;
    }

    /**
     * Set the arrival time. If this is set then departure time should not be
     * set.
     * 
     * @param newArrivalTime
     *            the time the user would like to arrive at their destination.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setArrivalTime(final long newArrivalTime) {
        if (myParams.departureTime != RequestParameters.DONT_CARE) {
            throw new IllegalArgumentException("departureTime was already " + "set.");
        }
        //System.out.println(TAG + "setArrivalTime()->newArrivalTime: " + newArrivalTime);

        myParams.arrivalTime = newArrivalTime;
        return this;
    }

    /**
     * Set the departure time. If this is set then the arrival time should not
     * be set.
     * 
     * @param newDepartureTime
     *            the time the user would like to depart from their starting
     *            location.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setDepartureTime(final long newDepartureTime) {
        if (myParams.arrivalTime != RequestParameters.DONT_CARE) {
            throw new IllegalArgumentException("arrivalTime was " + "already set.");
        }
        //System.out.println(TAG + "setDepartureTime()->newDepartureTime: " + newDepartureTime);
        myParams.departureTime = newDepartureTime;
        return this;
    }

    /**
     * Set the travel mode that the user would like to arrive at their
     * destination. The default travel mode is MIXED.
     * 
     * @param newTravelMode
     *            the TravelMode that the user would like to arrive at their
     *            destination.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setTravelMode(final TravelMode newTravelMode) {
        //System.out.println(TAG + "setTravelMode()->newTravelMode: " + newTravelMode);
        myParams.travelMode = newTravelMode;
        return this;
    }

    /**
     * Set the minimum distance the user would like to bike in meters. This
     * parameter is optional.
     * 
     * @param newMinDistanceToBikeInMeters
     *            the minimum distance to bike in mneters
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMinDistanceToBikeInMeters(final long newMinDistanceToBikeInMeters) {
        //System.out.println(TAG + "setMinDistanceToBikeInMeters()->newMinDistanceToBikeInMeters: "
        //        + newMinDistanceToBikeInMeters);
        myParams.minDistanceToBikeInMeters = newMinDistanceToBikeInMeters;
        return this;
    }

    /**
     * Set the maximum distance to bike in meters. This parameter is optional.
     * 
     * @param newMaxDistanceToBikeInMeters
     *            the max distance to bike in meters.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMaxDistanceToBikeInMeters(final long newMaxDistanceToBikeInMeters) {
        //System.out.println(TAG + "setMaxDistanceToBikeInMeters()->newMaxDistanceToBikeInMeters: "
        //        + newMaxDistanceToBikeInMeters);
        myParams.maxDistanceToBikeInMeters = newMaxDistanceToBikeInMeters;
        return this;
    }

    /**
     * Set the minimum number of bus transfers. This parameter is optional.
     * 
     * @param newMinNumberBusTransfers
     *            the minimum number of bus transfers.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMinNumberBusTransfers(final long newMinNumberBusTransfers) {
        //System.out.println(TAG + "setMinNumberBusTransfers()->newMinNumberBusTransfers: "
        //        + newMinNumberBusTransfers);
        myParams.minNumberBusTransfers = newMinNumberBusTransfers;
        return this;
    }

    /**
     * Set the maximum number of bus transfers. This parameter is optional.
     * 
     * @param newMaxNumberBusTransfers
     *            the maximum number of bus transfers.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setMaxNumberBusTransfers(final long newMaxNumberBusTransfers) {
        //System.out.println(TAG + "setMaxNumberBusTransfers()->newMaxNumberBusTransfers: "
        //        + newMaxNumberBusTransfers);
        myParams.maxNumberBusTransfers = newMaxNumberBusTransfers;
        return this;
    }

    /**
     * Setter for the resource field.
     * 
     * @param queryObj
     *            The query object to set the resource to (the stub or the
     *            actual APIQuery).
     */
    public void setResource(final APIQuery queryObj) {
        myParams.resource = queryObj;
    }

    /**
     * Set the ending address for the trip.
     * 
     * @param newEndAddress
     *            the address to end at.
     * @return the modified DirectionsRequest object. Used as part of the
     *         builder pattern.
     */
    public DirectionsRequest setQueryLanguage(final String newQueryLanguage) {
        //System.out.println(TAG + "setQueryLanguage()->newQueryLanguage: " + newQueryLanguage);
        myParams.queryLanguage = newQueryLanguage;
        return this;
    }
    
    /**
     * Returns the verbose error messages. This may contain repeated error
     * messages, and so may not be suitable for displaying to users. To display
     * user-friendly error messages, call .getMessage() on the return value of
     * doRequest().
     * 
     * @return the verbose errorMessages. Returns null if no messages have been
     *         set.
     */
    public String getVerboseErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessage
     *            the errorMessage to add
     */
    private void appendErrorMessage(final String errorMessage) {
        if (errorMessages == null) {
            errorMessages = errorMessage;
        } else {
            errorMessages = errorMessages + "\n" + errorMessage;
        }
    }

    /**
     * Return the solutions to the request for directions. This field is null
     * before doRequest() is called.
     * 
     * @return the solutions
     */
    public List<Route> getSolutions() {
        return solutions;
    }

    @Override
    public String toString() {
        return "DirectionsRequest: " + myParams.toString() + "solutions: "
                + Utility.listPrettyPrint(solutions, 0);
    }

    /**
     * Implements a custom serialization of a DirectionsRequest object.
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
        out.writeObject(myParams);
        out.writeObject(solutions);
    }

    /**
     * Implements a custom deserialization of a DirectionsRequest object.
     * 
     * @param in
     *            the ObjectInputStream to read from
     * @throws IOException
     *             if the stream fails
     * @throws ClassNotFoundException
     *             if a class is not found
     */
    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read each field from the stream in a specific order.
        // Specifying this order helps shield the class from problems
        // in future versions.
        // The order must be the same as the writing order in writeObject()
        myParams = (RequestParameters) in.readObject();
        solutions = (List<Route>) in.readObject();
    }

    /**
     * Dummy JSON bicycle directions.
     */
    private static final String DUMMY_BICYCLE_JSON = "{" + "\n   \"routes\" : [\n    "
            + "  {\n         \"bounds\" : {\n            \"northeast\" : {\n  "
            + "             \"lat\" : 47.676040,\n               \"lng\" : -12"
            + "2.313250\n            },\n            \"southwest\" : {\n      "
            + "         \"lat\" : 47.65358000000001,\n               \"lng\" :"
            + " -122.325820\n            }\n         },\n         \"copyrights"
            + "\" : \"Map data \u00a92013 Google\",\n         \"legs\" : [\n  "
            + "          {\n               \"distance\" : {\n                 "
            + " \"text\" : \"2.1 mi\",\n                  \"value\" : 3402\n  "
            + "             },\n               \"duration\" : {\n             "
            + "     \"text\" : \"13 mins\",\n                  \"value\" : 761"
            + "\n               },\n               \"end_address\" : \"3801 Br"
            + "ooklyn Avenue Northeast, University of Washington, Seattle, WA "
            + "98105, USA\",\n               \"end_location\" : {\n           "
            + "       \"lat\" : 47.65390000000001,\n                  \"lng\" "
            + ": -122.314480\n               },\n               \"start_addres"
            + "s\" : \"6504 Latona Avenue Northeast, Seattle, WA 98115, USA\","
            + "\n               \"start_location\" : {\n                  \"la"
            + "t\" : 47.676040,\n                  \"lng\" : -122.325820\n    "
            + "           },\n               \"steps\" : [\n                  "
            + "{\n                     \"distance\" : {\n                     "
            + "   \"text\" : \"59 ft\",\n                        \"value\" : 1"
            + "8\n                     },\n                     \"duration\" :"
            + " {\n                        \"text\" : \"1 min\",\n            "
            + "            \"value\" : 6\n                     },\n           "
            + "          \"end_location\" : {\n                        \"lat\""
            + " : 47.675910,\n                        \"lng\" : -122.325690\n "
            + "                    },\n                     \"html_instruction"
            + "s\" : \"Head \\u003cb\\u003esoutheast\\u003c/b\\u003e on \\u003"
            + "cb\\u003eLatona Ave NE\\u003c/b\\u003e toward \\u003cb\\u003eNE"
            + " 65th St\\u003c/b\\u003e\",\n                     \"polyline\" "
            + ": {\n                        \"points\" : \"gv~aHjwriVXY\"\n   "
            + "                  },\n                     \"start_location\" :"
            + " {\n                        \"lat\" : 47.676040,\n             "
            + "           \"lng\" : -122.325820\n                     },\n    "
            + "                 \"travel_mode\" : \"BICYCLING\"\n             "
            + "     },\n                  {\n                     \"distance\""
            + " : {\n                        \"text\" : \"0.2 mi\",\n         "
            + "               \"value\" : 252\n                     },\n      "
            + "               \"duration\" : {\n                        \"text"
            + "\" : \"1 min\",\n                        \"value\" : 63\n      "
            + "               },\n                     \"end_location\" : {\n "
            + "                       \"lat\" : 47.67588000000001,\n          "
            + "              \"lng\" : -122.322330\n                     },\n "
            + "                    \"html_instructions\" : \"Turn \\u003cb\\u0"
            + "03eleft\\u003c/b\\u003e onto \\u003cb\\u003eNE 65th St\\u003c/b"
            + "\\u003e\",\n                     \"polyline\" : {\n            "
            + "            \"points\" : \"mu~aHpvriV@s@@qE@gE?w@?u@?cB\"\n    "
            + "                 },\n                     \"start_location\" : "
            + "{\n                        \"lat\" : 47.675910,\n              "
            + "          \"lng\" : -122.325690\n                     },\n     "
            + "                \"travel_mode\" : \"BICYCLING\"\n              "
            + "    },\n                  {\n                     \"distance\" "
            + ": {\n                        \"text\" : \"0.4 mi\",\n          "
            + "              \"value\" : 593\n                     },\n       "
            + "              \"duration\" : {\n                        \"text"
            + "\" : \"2 mins\",\n                        \"value\" : 142\n    "
            + "                 },\n                     \"end_location\" : {"
            + "\n                        \"lat\" : 47.67201000000001,\n       "
            + "                 \"lng\" : -122.317360\n                     },"
            + "\n                     \"html_instructions\" : \"Turn \\u003cb"
            + "\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eNE Ravenna Blv"
            + "d\\u003c/b\\u003e\",\n                     \"polyline\" : {\n  "
            + "                      \"points\" : \"gu~aHpariVZYn@m@bA{@x@s@PO"
            + "FI`@]XWBCBEZYNOh@e@dB{ALMPOjC}BZYN]Jq@|@}G\"\n                 "
            + "    },\n                     \"start_location\" : {\n          "
            + "              \"lat\" : 47.67588000000001,\n                   "
            + "     \"lng\" : -122.322330\n                     },\n          "
            + "           \"travel_mode\" : \"BICYCLING\"\n                  }"
            + ",\n                  {\n                     \"distance\" : {\n"
            + "                        \"text\" : \"1.1 mi\",\n               "
            + "         \"value\" : 1775\n                     },\n           "
            + "          \"duration\" : {\n                        \"text\" : "
            + "\"6 mins\",\n                        \"value\" : 378\n         "
            + "            },\n                     \"end_location\" : {\n    "
            + "                    \"lat\" : 47.65609000000001,\n             "
            + "           \"lng\" : -122.317880\n                     },\n    "
            + "                 \"html_instructions\" : \"Turn \\u003cb\\u003e"
            + "right\\u003c/b\\u003e onto \\u003cb\\u003eRoosevelt Way NE\\u00"
            + "3c/b\\u003e\",\n                     \"polyline\" : {\n        "
            + "                \"points\" : \"a}}aHnbqiVnA?X?nC?jC?V?f@?|@?VAp"
            + "C@fA@~E@~B?hA@P?r@?xB@b@?^?`A@R?jCAjB@~GDj@F^PRLHDHBFBJ@L?tC?~@"
            + "BpA@r@@`DBlD@|BB\\\\?^@r@?L@@?PDHB\"\n                     },\n"
            + "                     \"start_location\" : {\n                  "
            + "      \"lat\" : 47.67201000000001,\n                        \"l"
            + "ng\" : -122.317360\n                     },\n                  "
            + "   \"travel_mode\" : \"BICYCLING\"\n                  },\n     "
            + "             {\n                     \"distance\" : {\n        "
            + "                \"text\" : \"0.2 mi\",\n                       "
            + " \"value\" : 348\n                     },\n                    "
            + " \"duration\" : {\n                        \"text\" : \"2 mins"
            + "\",\n                        \"value\" : 114\n                 "
            + "    },\n                     \"end_location\" : {\n            "
            + "            \"lat\" : 47.65598000000001,\n                     "
            + "   \"lng\" : -122.313250\n                     },\n            "
            + "         \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u0"
            + "03c/b\\u003e onto \\u003cb\\u003eNE Campus Pkwy\\u003c/b\\u003e"
            + "\",\n                     \"polyline\" : {\n                   "
            + "     \"points\" : \"qyzaHveqiVDS@I@IAkAByB?OBkE@sB?C?qB@wB@qB\""
            + "\n                     },\n                     \"start_locatio"
            + "n\" : {\n                        \"lat\" : 47.65609000000001,\n"
            + "                        \"lng\" : -122.317880\n                "
            + "     },\n                     \"travel_mode\" : \"BICYCLING\"\n"
            + "                  },\n                  {\n                    "
            + " \"distance\" : {\n                        \"text\" : \"0.2 mi"
            + "\",\n                        \"value\" : 268\n                 "
            + "    },\n                     \"duration\" : {\n                "
            + "        \"text\" : \"1 min\",\n                        \"value"
            + "\" : 31\n                     },\n                     \"end_lo"
            + "cation\" : {\n                        \"lat\" : 47.653580000000"
            + "01,\n                        \"lng\" : -122.313340\n           "
            + "          },\n                     \"html_instructions\" : \"Tu"
            + "rn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eUni"
            + "versity Way NE\\u003c/b\\u003e\",\n                     \"polyl"
            + "ine\" : {\n                        \"points\" : \"{xzaHxhpiV^B|"
            + "A@hCDlB@L@zA@\"\n                     },\n                     "
            + "\"start_location\" : {\n                        \"lat\" : 47.65"
            + "598000000001,\n                        \"lng\" : -122.313250\n "
            + "                    },\n                     \"travel_mode\" : "
            + "\"BICYCLING\"\n                  },\n                  {\n     "
            + "                \"distance\" : {\n                        \"tex"
            + "t\" : \"371 ft\",\n                        \"value\" : 113\n   "
            + "                  },\n                     \"duration\" : {\n  "
            + "                      \"text\" : \"1 min\",\n                  "
            + "      \"value\" : 21\n                     },\n                "
            + "     \"end_location\" : {\n                        \"lat\" : 47"
            + ".654220,\n                        \"lng\" : -122.314470\n      "
            + "               },\n                     \"html_instructions\" :"
            + " \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u00"
            + "3eBurke-Gilman Trail\\u003c/b\\u003e\",\n                     "
            + "\"polyline\" : {\n                        \"points\" : \"{izaHj"
            + "ipiVM`@Sh@EHCDEBIDGDEDINEJOb@Un@\"\n                     },\n  "
            + "                   \"start_location\" : {\n                    "
            + "    \"lat\" : 47.65358000000001,\n                        \"lng"
            + "\" : -122.313340\n                     },\n                    "
            + " \"travel_mode\" : \"BICYCLING\"\n                  },\n       "
            + "           {\n                     \"distance\" : {\n          "
            + "              \"text\" : \"115 ft\",\n                        "
            + "\"value\" : 35\n                     },\n                     "
            + "\"duration\" : {\n                        \"text\" : \"1 min\","
            + "\n                        \"value\" : 6\n                     }"
            + ",\n                     \"end_location\" : {\n                 "
            + "       \"lat\" : 47.65390000000001,\n                        \""
            + "lng\" : -122.314480\n                     },\n                 "
            + "    \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b"
            + "\\u003e onto \\u003cb\\u003eBrooklyn Ave NE\\u003c/b\\u003e\\u0"
            + "03cdiv style=\\\"font-size:0.9em\\\"\\u003eDestination will be "
            + "on the right\\u003c/div\\u003e\",\n                     \"polyl"
            + "ine\" : {\n                        \"points\" : \"{mzaHlppiVp@@"
            + "L?\"\n                     },\n                     \"start_loc"
            + "ation\" : {\n                        \"lat\" : 47.654220,\n    "
            + "                    \"lng\" : -122.314470\n                    "
            + " },\n                     \"travel_mode\" : \"BICYCLING\"\n    "
            + "              }\n               ],\n               \"via_waypoi"
            + "nt\" : []\n            }\n         ],\n         \"overview_poly"
            + "line\" : {\n            \"points\" : \"gv~aHjwriVXY@s@ByK?mB?cB"
            + "ZYrBiBjAcAfAcAxAuApGwFZYN]hAoIdK?tCAxPDbLDvF?~GDj@Fr@^RHRDbD?fJ"
            + "JhJF`A@RDHBDSBS@eEB{EBaJ@qB^BfFFzBBzA@M`@Yr@IHQJOTUn@Un@p@@L?\""
            + "\n         },\n         \"summary\" : \"Roosevelt Way NE\",\n  "
            + "       \"warnings\" : [\n            \"Bicycling directions are"
            + " in beta. Use caution \u2013 This route may contain streets tha"
            + "t aren\'t suited for bicycling.\"\n         ],\n         \"wayp"
            + "oint_order\" : []\n      }\n   ],\n   \"status\" : \"OK\"\n}";

}
