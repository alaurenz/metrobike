package com.HuskySoft.metrobike.ui.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * History class contains valid history address that user used to use.
 * 
 * @author Sam Wilson
 * 
 */
public final class History implements Serializable {

    /**
     * Part of serializability, this id tracks if a serialized object can be
     * deserialized using this version of the class.
     * 
     * NOTE: Please add 1 to this number every time you change the readObject()
     * or writeObject() methods.
     */
    private static final long serialVersionUID = 1L;
    /** The object of this class. */
    private static History hist;
    /** List that stores all the history. */
    private List<String> historyList;

    /** Private constructor for this singleton class. */
    private History() {
        historyList = new ArrayList<String>();
        // right now we just hard code the address in order to save time to type
        hardCodeAddress();
    }

    /**
     * Static method to get this object.
     * 
     * @return the history object
     */
    public static History getInstance() {
        if (hist == null) {
            hist = new History();
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
        return historyList.get(index);
    }

    /**
     * This method should be called from setting activity.<br>
     * delete all the address histories.
     */
    public void deleteAll() {
        historyList = new ArrayList<String>();
        // I leave this method call is just for testing.
        // hardCodeAddress();
    }

    /**
     * remove only one address.<br>
     * This method should be called from setting activity.
     * 
     * @param address
     *            the address that need to remove
     */
    public void deleteAddress(final String address) {
        // here although the address should be in this list, we just need to
        // make sure (defensive programming)
        if (historyList.contains(address)) {
            historyList.remove(address);
        }
    }

    /**
     * Remove the address by given the index.
     * 
     * @param index
     *            the index of the address.
     */
    public void deleteAddress(final int index) {
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
