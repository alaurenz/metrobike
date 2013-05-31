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
 * 
 * This class is the class that is actually responsible for making requests to Google and
 * deals with real data.
 *
 */
public class GoogleAPIWrapper implements APIQuery {

    /* (non-Javadoc)
     * @see com.HuskySoft.metrobike.backend.APIQuery#doQuery(java.lang.String)
     */
    @Override
    public String doQuery(final String theURL) throws IOException {

        /*
         * Some example web connection code help from
         * http://stackoverflow.com/questions/6951611/extract-message
         * -body-out-of-httpresponse and other StackOverflow examples for
         * URLConnection.
         */

        StringBuilder response = new StringBuilder();
        System.err.println("GoogleAPIWrapper: About to make query to this url: [" + theURL + "]");
        URL url = new URL(theURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
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
        
        return response.toString();
    }
}
