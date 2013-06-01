/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author coreyh3
 * @author dutchsct
 * 
 *         This class is the class that is actually responsible for making
 *         requests to Google and deals with real data.
 * 
 */
public class GoogleAPIWrapper implements APIQuery {

    /**
     * TAG for logging statements.
     */
    private static final String TAG = "com.HuskySoft.metrobike.backend: GoogleAPIWrapper.java: ";

    /**
     * Used to turn off Internet capabilites, so searches can be cancelled.
     */
    private static volatile boolean INTERNET_ENABLED = true;

    /**
     * Disables Internet capabilities, disabling Google API access.
     */
    public static void disableAPIConnection() {
        INTERNET_ENABLED = false;
    }

    /**
     * Enables Internet capabilities, disabling Google API access.
     */
    public static void enableAPIConnection() {
        INTERNET_ENABLED = true;
    }
    
    /**
     * (non-Javadoc) @see
     * com.HuskySoft.metrobike.backend.APIQuery#doQuery(java.lang.String).
     * 
     * @param theURL
     *            The theURL with the parameters to make the request to.
     * 
     * @throws IOException
     *             If the connection to the Google Maps APIs is not reached.
     * 
     * @return The JSON response.
     */
    public final String doQuery(final String theURL) throws IOException {

        /*
         * Some example web connection code help from
         * http://stackoverflow.com/questions/6951611/extract-message
         * -body-out-of-httpresponse and other StackOverflow examples for
         * URLConnection.
         */
        System.out.println(TAG + "doQuery()->theURL: " + theURL);
        StringBuilder response = new StringBuilder();
        System.err.println("GoogleAPIWrapper: About to make query to this url: [" + theURL + "]");

        if (!INTERNET_ENABLED) {
            System.err.println("Internet access is currently disabled by the "
                    + "API user.  Aborting query...");
            throw new IOException("Internet access disabled by API user.");
        }

        URL url = new URL(theURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String aLine = "";
            while (aLine != null) {
                response.append(aLine);
                aLine = in.readLine();
                // Log.w(" GoogleAPIWrapper: Got line: '" + aLine + "'");
            }
            in.close();
        } finally {
            urlConnection.disconnect();
        }

        // This line may slow down the system too much.
        // System.out.println(TAG + "doQuery()->response.toString(): " +
        // response.toString());

        return response.toString();
    }
}
