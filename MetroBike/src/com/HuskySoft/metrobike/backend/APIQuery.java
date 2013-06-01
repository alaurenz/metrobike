/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.io.IOException;

/**
 * @author coreyh3
 * 
 *         This class is an interface that is used by both the actual class and
 *         the stub class, as defined in the lecture slides:
 *         https://www.cs.washington
 *         .edu/education/courses/403/13sp/lectures/14-integrationtesting.ppt
 * 
 */
public interface APIQuery {
    /**
     * This method takes in a URL as a String and returns a JSON String as a
     * response.
     * 
     * @param theURL
     *            the URL to send to the server.
     * @return Returns what the server sends back (should be a JSON String).
     * @throws IOException
     *             if the connection fails for any reason
     */
    String doQuery(final String theURL) throws IOException;
}
