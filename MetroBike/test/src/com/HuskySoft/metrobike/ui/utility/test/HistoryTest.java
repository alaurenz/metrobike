package com.HuskySoft.metrobike.ui.utility.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.HuskySoft.metrobike.ui.utility.History;

/**
 * JUnit test class to test History class.
 * 
 * @author Sam Wilson
 * 
 */
public class HistoryTest {
	/**
	 * The default timeout time 3s.
	 */
	private static final int TIMEOUT = 3000;

	/**
	 * Dump value to test.
	 */
	private static final String TEST_STRING = "Dump String";

	/**
	 * The history object that we are going to test.
	 */
	private static History history;

	/**
	 * before setup.
	 */
	public final void setup() {
		history = History.getInstance();
	}

	/**
	 * Black box test: test this class is singleton or not.
	 */
	@Test(timeout = TIMEOUT)
	public final void testGetInstance() {
		History firstHistoryObject = History.getInstance();
		History secondHistoryObject = History.getInstance();
		assertTrue("Get the History Instacne twice",
				firstHistoryObject.equals(secondHistoryObject));
	}

	/**
	 * White box test: test can it get the list of history.
	 */
	@Test(timeout = TIMEOUT, expected = UnsupportedOperationException.class)
	public final void testGetHistoryReturnUnmodifityList() {
		setup();
		// this should throw exception
		history.getHistory().add(TEST_STRING);
	}

	/**
	 * White box test: test addAddress(String address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddSingleAddress01() {
		setup();
		// add a single address
		history.addAddress(TEST_STRING);
		assertTrue("Add single address",
				history.getHistory().contains(TEST_STRING));
		history.deleteAddress(TEST_STRING);
	}

	/**
	 * White box test: test addAddress(String address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddSingleEmptyAddress02() {
		setup();
		// add a single address
		history.addAddress("");
		assertTrue("Add single empty address", history.getHistory()
				.contains(""));
		history.deleteAddress("");
	}

	/**
	 * White box test: test addAddress(String address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddSingleDuplicateAddress03() {
		setup();
		// add 2 address
		history.addAddress(TEST_STRING);
		history.addAddress(TEST_STRING);
		assertTrue("Add single duplicate address", history.getHistory()
				.contains(TEST_STRING));
		history.deleteAddress(TEST_STRING);
	}

	/**
	 * White box test: test addAddress(String address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddSingleNullAddress04() {
		setup();
		String str = null;
		// add a single address
		history.addAddress(str);
		assertTrue("Add null address", history.getHistory().contains(str));
		history.deleteAddress(str);
	}

	/**
	 * White box test: test addAddress(String address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddSingleDuplicateNullAddress04() {
		setup();
		String str = null;
		// add a single address
		history.addAddress(str);
		history.addAddress(str);
		// should not be crash
		assertTrue("Add 2 null address", history.getHistory().contains(str));
		history.deleteAddress(str);
	}

	/**
	 * White box test: test addAddress(String[] address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddArrayOfAddress01() {
		setup();
		String[] strArray = { "a", "b", "c", "d", "e" };
		// add array address
		history.addAddress(strArray);
		// should not be crash
		assertTrue(history.getHistory().contains(strArray[0]));
		deleteArrayAfterTesting(strArray);
	}

	/**
	 * White box test: test addAddress(String[] address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddArrayOfAddress02() {
		setup();
		String[] strArray = { "a", "b", "c", "d", "e" };
		int expected = history.getSize() + strArray.length;
		// add array address
		history.addAddress(strArray);
		int actual = history.getSize();
		assertEquals(expected, actual);
		deleteArrayAfterTesting(strArray);
	}

	/**
	 * White box test: test addAddress(String[] address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddArrayOfDuplicateAddress03() {
		setup();
		String[] strArray = { "a", "a", "a", "a", "a" };
		// add array address
		history.addAddress(strArray);
		// should not be crash
		assertTrue(history.getHistory().contains(strArray[0]));
		deleteArrayAfterTesting(strArray);
	}

	/**
	 * White box test: test addAddress(String[] address).
	 */
	@Test(timeout = TIMEOUT)
	public final void testAddArrayOfDuplicateAddress04() {
		setup();
		String[] strArray = { "a", "a", "a", "a", "a" };
		int expected = history.getSize() + 1;
		// add array address
		history.addAddress(strArray);
		int actual = history.getSize();
		assertEquals(expected, actual);
		deleteArrayAfterTesting(strArray);
	}

	/**
	 * White box test: test getAddress(int index).
	 */
	@Test(timeout = TIMEOUT)
	public final void testGetAddedAddress01() {
		setup();
		int index = history.getSize();
		// add array address
		history.addAddress(TEST_STRING);
		String actual = history.getAddress(index);
		assertEquals(TEST_STRING, actual);
		history.deleteAddress(TEST_STRING);
	}

	/**
	 * helper method for deleting the string array that has been added.
	 * 
	 * @param strArray
	 *            the array that need to be deleted
	 */
	private void deleteArrayAfterTesting(final String[] strArray) {
		for (int i = 0; i < strArray.length; i++) {
			history.deleteAddress(strArray[i]);
		}
	}

}
