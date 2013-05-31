package com.HuskySoft.metrobike.algorithm;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.HuskySoft.metrobike.backend.DirectionsRequest.RequestParameters;
import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.Route;
import com.HuskySoft.metrobike.backend.TravelMode;

/**
 * The simplest algorithm, this class simply asks Google for multiple Routes and
 * returns all of them.
 * 
 * @author dutchscout
 */
public final class BicycleOnlyAlgorithm extends AlgorithmWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DirectionsStatus findRoutes(final RequestParameters toProcess) {
		clearErrors();

		// Get only bicycle routes, no matter what.
		if (toProcess.getTravelMode() == TravelMode.BICYCLING
				|| toProcess.getTravelMode() == TravelMode.TRANSIT
				|| toProcess.getTravelMode() == TravelMode.MIXED) {

			List<Route> bicycleRoutes = null;
			try {
				bicycleRoutes = getBicycleResults(
						toProcess.getStartAddress(), toProcess.getEndAddress());
			} catch (UnsupportedEncodingException e) {
				addError(DirectionsStatus.UNSUPPORTED_CHARSET);
			}

			if(bicycleRoutes != null && bicycleRoutes.size() > 0) {
				addResults(bicycleRoutes);
				setReferencedRoute(bicycleRoutes.get(0));
			}
		} else {
			return addError(DirectionsStatus.UNSUPPORTED_TRAVEL_MODE_ERROR, ": "
					+ toProcess.getTravelMode().toString());
		}

		// If we got no results, return the appropriate status code
		if (getResults() == null || getResults().size() == 0) {
			if (!hasErrors()) {
				// If we didn't notice not getting results somehow, add this
				// error manually.
				addError(DirectionsStatus.NO_RESULTS_FOUND);
			}
			return getMostRecentStatus();
		}

		return markSuccessful();
	}
}
