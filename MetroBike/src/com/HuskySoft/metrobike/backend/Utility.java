/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * @author coreyh3
 * 
 * This class holds any utility functions that will be used across the 
 * MetroBike project. 
 *
 */
public class Utility {
    private static final String TAG = "MetroBikeUtility";
    
    
    /**
     * Converts a JSONArray into a list of strings
     * 
     * @param jsonArray 
     *          input JSONAray containing the list of strings
     * @return 
     *          a list of Strings
     * @throws JSONException
     */
    public static List<String> jsonArrayToStringList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }
    
    
    /**
     * This method takes in a URL as a String and returns a JSON String as a response
     * 
     * @param theURL the URL to send to the server.  
     * @return Returns what the server sends back (should be a JSON String)
     */
    public static String doQuery(String theURL){
        StringBuilder response = new StringBuilder();
        
        try {
            URL url = new URL(theURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                byte[] thisResponse = new byte[2048];
                int bytesThisTime = 0;
                do{
                    bytesThisTime = in.read(thisResponse);
                    if(bytesThisTime < 0) break;
                    String stringResponse = new String(thisResponse,0,bytesThisTime);
                    response.append(stringResponse);
                } while(bytesThisTime > 0);
            } finally {
                urlConnection.disconnect();
            }
            
        } catch (MalformedURLException e) {
            Log.e(TAG,"Bad url..." + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG,"IO Exception..." + e.getMessage());
        }
        return response.toString();
    }
}
