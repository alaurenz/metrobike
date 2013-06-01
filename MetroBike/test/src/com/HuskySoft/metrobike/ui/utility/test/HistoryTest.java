package com.HuskySoft.metrobike.ui.utility.test;

import com.HuskySoft.metrobike.ui.utility.History;

import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * JUnit test class to test History class. Noted that wrong junit test import
 * before.
 * 
 * @author Sam Wilson
 * 
 */
public class HistoryTest extends TestCase {

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
    public final void testGetInstance() {
        History firstHistoryObject = History.getInstance();
        History secondHistoryObject = History.getInstance();
        Assert.assertTrue(firstHistoryObject.equals(secondHistoryObject));
    }

    /**
     * White box test: test addAddress(String address).
     */
    public final void testAddSingleAddress01() {
        setup();
        // add a single address
        history.addAddress(TEST_STRING);
        Assert.assertTrue("Add single address", history.getHistory().contains(TEST_STRING));
        history.deleteAddress(TEST_STRING);
    }

    /**
     * White box test: test addAddress(String address).
     */
    public final void testAddSingleEmptyAddress02() {
        setup();
        // add a single address
        history.addAddress("");
        Assert.assertTrue("Add single empty address", history.getHistory().contains(""));
        history.deleteAddress("");
    }

    /**
     * White box test: test addAddress(String address).
     */
    public final void testAddSingleDuplicateAddress03() {
        setup();
        // add 2 address
        history.addAddress(TEST_STRING);
        history.addAddress(TEST_STRING);
        Assert.assertTrue("Add single duplicate address", history.getHistory()
                .contains(TEST_STRING));
        history.deleteAddress(TEST_STRING);
    }

    /**
     * White box test: test addAddress(String address).
     */
    public final void testAddSingleNullAddress04() {
        setup();
        int expected = history.getSize();
        String str = null;
        // add a single address
        history.addAddress(str);
        int actual = history.getSize();
        Assert.assertTrue("Add null address", expected == actual);
    }

    /**
     * White box test: test addAddress(String address).
     */
    public final void testAddSingleDuplicateNullAddress04() {
        setup();
        int expected = history.getSize();
        String str = null;
        // add a single address
        history.addAddress(str);
        history.addAddress(str);
        // should not be crash
        int actual = history.getSize();
        Assert.assertTrue("Add 2 null address", expected == actual);
    }

    /**
     * White box test: test addAddress(String[] address).
     */
    public final void testAddArrayOfAddress01() {
        setup();
        String[] strArray = { "a", "b", "c", "d", "e" };
        // add array address
        history.addAddress(strArray);
        // should not be crash
        Assert.assertTrue(history.getHistory().contains(strArray[0]));
        deleteArrayAfterTesting(strArray);
    }

    /**
     * White box test: test addAddress(String[] address).
     */
    public final void testAddArrayOfAddress02() {
        setup();
        String[] strArray = { "a", "b", "c", "d", "e" };
        int expected = history.getSize() + strArray.length;
        // add array address
        history.addAddress(strArray);
        int actual = history.getSize();
        Assert.assertEquals(expected, actual);
        deleteArrayAfterTesting(strArray);
    }

    /**
     * White box test: test addAddress(String[] address).
     */
    public final void testAddArrayOfDuplicateAddress03() {
        setup();
        String[] strArray = { "a", "a", "a", "a", "a" };
        // add array address
        history.addAddress(strArray);
        // should not be crash
        Assert.assertTrue(history.getHistory().contains(strArray[0]));
        deleteArrayAfterTesting(strArray);
    }

    /**
     * White box test: test addAddress(String[] address).
     */
    public final void testAddArrayOfDuplicateAddress04() {
        setup();
        String[] strArray = { "a", "a", "a", "a", "a" };
        int expected = history.getSize() + 1;
        // add array address
        history.addAddress(strArray);
        int actual = history.getSize();
        Assert.assertEquals(expected, actual);
        deleteArrayAfterTesting(strArray);
    }

    /**
     * White box test: test addAddress(String[] address).
     */
    public final void testAddArrayOfNullAddress05() {
        setup();
        String[] strArray = null;
        int expected = history.getSize();
        // add array address
        history.addAddress(strArray);
        int actual = history.getSize();
        Assert.assertEquals("The size should not changed ", expected, actual);
    }

    /**
     * White box test: test getAddress(int index).
     */
    public final void testGetAddedAddress01() {
        setup();
        int index = history.getSize();
        // add array address
        history.addAddress(TEST_STRING);
        String actual = history.getAddress(index);
        Assert.assertEquals(TEST_STRING, actual);
        history.deleteAddress(TEST_STRING);
    }

    /**
     * White box test: test getAddress(int index).
     */
    public final void testGetAddedAddress02() {
        setup();
        // add array address
        history.addAddress(TEST_STRING);
        String actual = history.getAddress(-1);
        Assert.assertNull(actual);
        history.deleteAddress(TEST_STRING);
    }
    
    /**
     * White box test: test getAddress(int index).
     */
    public final void testGetAddedAddress03() {
        setup();
        int index = history.getSize();
        // add array address
        history.addAddress(TEST_STRING);
        String actual = history.getAddress(index + 1);
        Assert.assertNull(actual);
        history.deleteAddress(TEST_STRING);
    }
    
    /**
     * White box test: test deleteAll().
     */
    public final void testDeleteAllAddress01() {
        setup();
        // add array address
        history.deleteAll();
        int actual = history.getSize();
        Assert.assertEquals(0, actual);
    }
    
    /**
     * White box test: test deleteAll().
     */
    public final void testDeleteNullAddress01() {
        setup();
        int expected = history.getSize();
        // add array address
        history.deleteAddress(null);
        int actual = history.getSize();
        Assert.assertEquals(expected, actual);
    }

    /**
     * White box test: test delete address (int index).
     */
    public final void testDeleteAddress01() {
        setup();
        int numHistoryEntriesBefore = history.getSize();
        history.addAddress(TEST_STRING);
        history.deleteAddress(0);
        int numHistoryEntriesAfter = history.getSize();
        Assert.assertEquals(numHistoryEntriesBefore, numHistoryEntriesAfter);
    }

    /**
     * White box test: test delete address (int index).
     */
    public final void testDeleteAddress02() {
        setup();
        int expected = history.getSize();
        history.addAddress(TEST_STRING);
        history.deleteAddress(-1);
        // should not be deleted this
        int actual = history.getSize() - 1;
        Assert.assertEquals(expected, actual);
        // done testing
        history.deleteAddress(TEST_STRING);
    }
    
    /**
     * White box test: test delete address (int index).
     */
    public final void testDeleteAddress03() {
        setup();
        int expected = history.getSize();
        history.addAddress(TEST_STRING);
        history.deleteAddress(expected + 1);
        // should not be deleted this
        int actual = history.getSize() - 1;
        Assert.assertEquals(expected, actual);
        history.deleteAddress(TEST_STRING);
    }
    
    /**
     * White box test: test writeOneAddressToFile.
     */
    public final void testWriteToFile01() {
        History.writeOneAddressToFile(null, TEST_STRING);
        // nothing should happen.
    }

    /**
     * White box test: test readOneAddressToFile.
     */
    public final void testReadToFile01() {
        History.readFromFile(null);
        // nothing should happen.
    }

    /**
     * White box test: test addToHistory.
     */
    public final void testAddToHistory01() {
        // add null for address
        setup();
        Assert.assertFalse(history.addToHistory(TEST_STRING, null));
    }

    /**
     * White box test: test writeOneAddressToFile.
     *//* TODO: Get this test working!
    public final void testWriteToFile02() {
        try {
            History.writeOneAddressToFile(new FileOutputStream("./testFile"), TEST_STRING);
        } catch (FileNotFoundException e) {
            Assert.fail("Cannot create a test file");
        }
    }*/

    /**
     * White box test: test writeOneAddressToFile.
     *//* TODO: Get this test working!
    public final void testWriteToFile03() {
        String file = "./testFile";
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(file);
            History.writeOneAddressToFile(fos, TEST_STRING);
            fis = new FileInputStream(file);
            byte[] expected = TEST_STRING.getBytes();
            byte[] actual = new byte[expected.length];
            fis.read(actual);
            Assert.assertTrue(Arrays.equals(expected, actual));
        } catch (FileNotFoundException e) {
            Assert.fail("File cannot open");
        } catch (IOException e) {
            Assert.fail("Cannot read or write file");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("file didn't close");
            }
        }
    }*/
    
    /**
     * White box test: test addToHistory.
     */
    public final void testAddToHistory02() {
        // add null for curr lat and lng.
        setup();
        Assert.assertFalse(history.addToHistory(null, TEST_STRING));
    }

    /**
     * White box test: test addToHistory.
     */
    public final void testAddToHistory03() {
        // add diff latlng and address
        setup();
        Assert.assertTrue(history.addToHistory(TEST_STRING, TEST_STRING + TEST_STRING));
    }

    /**
     * White box test: test addToHistory.
     */
    public final void testAddToHistory04() {
        // add real address
        setup();
        Assert.assertFalse(history.addToHistory(TEST_STRING, TEST_STRING));
    }

    /**
     * White box test: test readOneAddressToFile.
     *//* TODO: Get this test working!
    public final void testReadToFile02() {
        setup();
        String file = "./testFile";
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            int expected = history.getSize();
            fos = new FileOutputStream(file);
            byte[] strBytes = TEST_STRING.getBytes();
            fos.write(strBytes);
            fos.write("\n".getBytes());
            fis = new FileInputStream(file);
            History.readFromFile(fis);
            int actual = history.getSize() - 1;
            Assert.assertEquals(expected, actual);
        } catch (FileNotFoundException e) {
            Assert.fail("File cannot open");
        } catch (IOException e) {
            Assert.fail("Cannot read or write file");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("file didn't close");
            }
        }
    }*/
    
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
