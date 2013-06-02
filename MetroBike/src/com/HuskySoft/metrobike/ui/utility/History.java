package com.HuskySoft.metrobike.ui.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private static final String TAG = "com.HuskySoft.metrobike.ui.utility: History.java: ";

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
        System.out.println(TAG + "History()->Done creating a singleton class for History");
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
            System.out.println(TAG + "getInstance()->Get an existing history object");
        }
        return hist;
    }

    /**
     * @return unmodifiable list to prevent representation exposition
     */
    public List<String> getHistory() {
        List<String> unmodifiedList = Collections.unmodifiableList(historyList);
        return unmodifiedList;
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
            System.err.println(TAG + "addAddress(String address)->Add address should not be null!");
            return;
        }
        // only added if no duplicate history
        boolean isContain = historyList.contains(address);
        if (!isContain) {
            historyList.add(address);
        } else {
            // do nothing
            System.out.println(TAG + "addAddress(String address)->Add duplicate address");
            return;
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
            System.err.println(TAG
                    + "addAddress(String[] addresses)->Add address array should not be null!");
            return;
        }
        int length = addresses.length;
        if (length == 0) {
            System.out.println(TAG + "addAddress(String[] addresses)->Nothing to added");
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
            System.err.println(TAG
                    + "getAddress()->Don't give an invalid index of the history list to me!");
            return null;
        }
        String address = historyList.get(index);
        return address;
    }

    /**
     * This method should be called from setting activity.<br>
     * delete all the address histories.
     */
    public void deleteAll() {
        System.out.println(TAG + "deleteAll()->Calling deleteAll()");
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
            System.err.println(TAG
                    + "deleteAddress(String address)->Delete address should not be null!");
            return;
        }
        // here although the address should be in this list, we just need to
        boolean isContain = historyList.contains(address);
        if (isContain) {
            historyList.remove(address);
        } else {
            System.err.println(TAG + " This address " + address
                    + " cannot be found in this history list!");
            return;
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
            System.err.println(TAG
             + "deleteAddress(int index)->Don't give an invalid index of the history list to me!");
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
     * Check if the address is not lat and lng, then add into the store this
     * into history.
     * 
     * @param currLocationLatLagString
     *            the current lat and log string.
     * @param address
     *            the address that may need to add into history.
     * @return true if this address is added into history.
     */
    public boolean addToHistory(final String currLocationLatLagString, final String address) {
        boolean isAdded = false;
        if (address == null || currLocationLatLagString == null) {
            return isAdded;
        }
        if (!address.equals(currLocationLatLagString)) {
            addAddress(address);
            isAdded = true;
        }
        return isAdded;
    }
    
    /**
     * Write one address into an existing file.
     * 
     * @param fos
     *            the file output stream object.
     * @param address
     *            the address that need to add.
     */
    public static void writeOneAddressToFile(final FileOutputStream fos, final String address) {
        if (fos == null || address == null) {
            // don't write to file as object is null
            System.err.println(TAG + "writeOneAddressToFile->fos or address is null");
            return;
        }
        try {
            fos.write(address.getBytes());
            // \n indicate the next address
            fos.write("\n".getBytes());
        } catch (IOException e) {
            System.out.println(TAG + " Connot write history from file");
        }
    }
    
    /**
     * Read addresses from saved file and add them into the history.
     * 
     * @param fis
     *            the file input stream.
     */
    public static void readFromFile(final FileInputStream fis) {
        History history = History.getInstance();
        StringBuilder sb = new StringBuilder();
        int readByte;
        char c;
        if (fis == null) {
            // input stream is null, don't process.
            System.err.println(TAG + "readFromFile->fis is null");
            return;
        }
        // read one byte at a time.
        try {
            while ((readByte = fis.read()) != -1) {
                c = (char) readByte;
                if (c == '\n') {
                    // if we hit the new line, that's the other address.
                    history.addAddress(sb.toString());
                    sb = new StringBuilder();
                    continue;
                }
                sb.append(c);
            }
        } catch (IOException e) {
            System.out.println(TAG + " Connot read history from file");
        }
    }
}
