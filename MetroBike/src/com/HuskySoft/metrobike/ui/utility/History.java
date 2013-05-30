package com.HuskySoft.metrobike.ui.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * History class contains valid history address that user used to use.
 * 
 * @author Sam Wilson
 * 
 */
public final class History {

    /**
     * The tag name of this class.
     */
    private static final String TAG = "History";

    /**
     * The file name that we will write the history in.
     */
    public static final String FILENAME = "History";

    /**
     * The object of this class.
     */
    private static History hist;

    /**
     * List that stores all the history.
     */
    private List<String> historyList;

    /**
     * Private constructor for this singleton class.
     */
    private History() {
        historyList = new ArrayList<String>();
        // right now we just hard code the address in order to save time to type
        hardCodeAddress();
        System.out.println(TAG + " Done creating a singleton class for History");
    }

    /**
     * Static method to get this object.
     * 
     * @return the history object
     */
    public static History getInstance() {
        if (hist == null) {
            hist = new History();
        } else {
            System.out.println(TAG + " Get an existing history object");
        }
        return hist;
    }

    /**
     * @return unmodifiable list to prevent representation exposition
     */
    public List<String> getHistory() {
        return Collections.unmodifiableList(historyList);
    }

    /**
     * Add address into this list.<br>
     * This method should be called from search activity
     * 
     * @precondition the address should be valid address
     * @param address
     *            the address to be added
     */
    public void addAddress(final String address) {
        if (address == null) {
            // defensive programming.
            System.err.println(TAG + " Add address should not be null!");
            return;
        }
        // only added if no duplicate history
        if (!historyList.contains(address)) {
            historyList.add(address);
        }
    }

    /**
     * Add all address into this history list.
     * 
     * @param addresses
     *            list of addresses
     */
    public void addAddress(final String[] addresses) {
        if (addresses == null) {
            // defensive programming.
            System.err.println(TAG + " Add address array should not be null!");
            return;
        }
        for (int i = 0; i < addresses.length; i++) {
            addAddress(addresses[i]);
        }
    }

    /**
     * Get address from the list.
     * 
     * @param index
     *            the address from list
     * @return a string for address
     */
    public String getAddress(final int index) {
        if (index < 0 || index >= getSize()) {
            // defensive programming.
            System.err.println(TAG + " Don't give an invalid index of the history list to me!");
            return null;
        }
        return historyList.get(index);
    }

    /**
     * This method should be called from setting activity.<br>
     * delete all the address histories.
     */
    public void deleteAll() {
        historyList = new ArrayList<String>();
    }

    /**
     * remove only one address.<br>
     * This method should be called from setting activity.
     * 
     * @param address
     *            the address that need to remove
     */
    public void deleteAddress(final String address) {
        // make sure (defensive programming)
        if (address == null) {
            System.err.println(TAG + " Delete address should not be null!");
            return;
        }
        // here although the address should be in this list, we just need to
        if (historyList.contains(address)) {
            historyList.remove(address);
        } else {
            System.err.println(TAG + " This address " + address + " cannot be found in this history list!");
        }
    }

    /**
     * Remove the address by given the index.
     * 
     * @param index
     *            the index of the address.
     */
    public void deleteAddress(final int index) {
        if (index < 0 || index >= getSize()) {
            // defensive programming.
            System.err.println(TAG + " Don't give an invalid index of the history list to me!");
            return;
        }
        historyList.remove(index);
    }

    /**
     * Show how many history address left.
     * 
     * @return the size of this list
     */
    public int getSize() {
        return historyList.size();
    }

    /**
     * Hardcoded address for testing. Will delete this method in version 1
     * phase. TODO: Remove this in V1 phase.
     */
    private void hardCodeAddress() {
        historyList.add("Paul G. Allen Center for Computer Science & Engineering (CSE)");
        historyList.add("Guggenheim Hall (GUG)");
        historyList.add("4311 11th Ave NE, Seattle, WA");
        historyList.add("Schmitz Hall (SMZ)");
        historyList.add("85 Pike St, Seattle, Washington");
        historyList.add("7201 East Green Lake Dr N, Seattle, WA");
        historyList.add("601 N 59th St, Seattle, WA");
        historyList.add("400 Broad St, Seattle, WA");
        historyList.add("401 Broad St, Seattle, WA");
        historyList.add("402 Broad St, Seattle, WA");
        historyList.add("403 Broad St, Seattle, WA");
        historyList.add("404 Broad St, Seattle, WA");
        historyList.add("405 Broad St, Seattle, WA");
        historyList.add("2623 NE University Village St #7, Seattle, WA");
    }
}
