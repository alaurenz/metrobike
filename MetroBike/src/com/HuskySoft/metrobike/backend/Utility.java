/**
 * 
 */
package com.HuskySoft.metrobike.backend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author coreyh3
 * 
 * This class holds any utility functions that will be used across the 
 * MetroBike project. 
 *
 */
public class Utility {
    
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
}
