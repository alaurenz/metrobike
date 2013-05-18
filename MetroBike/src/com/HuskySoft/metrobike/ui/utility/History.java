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
public class History {

	// this class may need to serialize

	/** the object of this class */
	private static History HISTORY;
	/** list that stores all the history */
	private List<String> historyList;

	/** private constructor for this singleton class */
	private History() {
		historyList = new ArrayList<String>();
		// right now we just hard code the address in order to save time to type
		hardCodeAddress();
	}

	/**
	 * static method to get this object
	 * 
	 * @return the history object
	 */
	public static History getInstance() {
		if (HISTORY == null) {
			HISTORY = new History();
		}
		return HISTORY;
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
	 */
	public void addAddress(String address) {
	    // only added if no duplicate history
	    if (!historyList.contains(address))
		historyList.add(address);
	}

	public String getAddress(int index) {
		return historyList.get(index);
	}

	/**
	 * This method should be called from setting activity.<br>
	 * delete all the address histories.
	 */
	public void deleteAll() {
		historyList = new ArrayList<String>();
		// I leave this method call is just for testing.
		hardCodeAddress();
	}

	/**
	 * remove only one address.<br>
	 * This method should be called from setting activity.
	 * 
	 * @param address
	 *            the address that need to remove
	 */
	public void deleteAddress(String address) {
		// here although the address should be in this list, we just need to
		// make sure (defensive programming)
		if (historyList.contains(address)) {
			historyList.remove(address);
		}
	}

	/**
	 * Remove the address by given the index
	 * 
	 * @param index
	 *            the index of the address
	 */
	public void deleteAddress(int index) {
		historyList.remove(index);
	}

	/*
	 * Will delete this method in version 1 phrase
	 */
	private void hardCodeAddress() {
		historyList
				.add("Paul G. Allen Center for Computer Science & Engineering (CSE)");
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
