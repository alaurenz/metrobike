package com.HuskySoft.metrobike.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.backend.DirectionsRequest;
import com.HuskySoft.metrobike.backend.DirectionsStatus;
import com.HuskySoft.metrobike.backend.TravelMode;
import com.HuskySoft.metrobike.ui.utility.History;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * An activity that receives and handles users' requests for routes.
 * 
 * @author Shuo Wang, Sam Wilson
 */
public class SearchActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    /**
     * The tag of this class.
     */
    private static final String TAG = "SearchActivity";

    /**
     * Minimum two digit number.
     */
    private static final int MIN_TWO_DIGIT_NUMBER = 10;

    /**
     * A Fragment that contains a date picker to let user select a date of
     * departure/arrival.
     */
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public final Dialog onCreateDialog(final Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            // Set the name of the dialog box
            dpd.setTitle(R.string.dialogbox_date);
            Log.v(TAG, "Done creating the calendar dialog");
            return dpd;
        }

        /**
         * Update the date EditText widget after an user picks a date.
         * 
         * @param view
         *            The DatePicker view whose date is set by user
         * @param year
         *            The year of the date picked
         * @param month
         *            The month of the date picked (1-12 as Jan. to Dec.)
         * @param day
         *            The day of month of the date picked
         */
        public final void onDateSet(final DatePicker view, final int year, final int month,
                final int day) {
            EditText dateEditText = (EditText) getActivity().findViewById(R.id.editTextDate);
            // Update the date EditText widget
            dateEditText.setText(com.HuskySoft.metrobike.ui.utility.Utility
                                .convertAndroidSystemDateToFormatedDateString(year, month, day));
            Log.v(TAG, "Done on data set");
        }
    }

    /**
     * A Fragment that contains a time picker to let user select a time (hh:mm)
     * of departure/arrival.
     */
    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        /**
         * Minimum two digit number.
         */
        private static final int MIN_TWO_DIGIT_NUMBER = 10;

        @Override
        public final Dialog onCreateDialog(final Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, true);
            // Set the name of the dialog box
            tpd.setTitle(R.string.dialogbox_time);
            Log.v(TAG, "Done on creating the calender Time dialog");
            return tpd;
        }

        /**
         * Update the date EditText widget after an user picks a date.
         * 
         * @param view
         *            The TimePicker view whose date is set by user
         * @param hourOfDay
         *            The hour of a day of the time picked
         * @param minute
         *            The minute of the time picked
         */
        public final void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
            EditText timeEditText = (EditText) getActivity().findViewById(R.id.editTextTime);

            // Formatting string be displayed
            String hourString = "";
            if (hourOfDay < MIN_TWO_DIGIT_NUMBER) {
                hourString += "0";
            }
            hourString += hourOfDay;

            String minuteString = "";
            if (minute < MIN_TWO_DIGIT_NUMBER) {
                minuteString += "0";
            }
            minuteString += minute;

            // Update the time EditText widget
            timeEditText.setText(hourString + ":" + minuteString);
        }
    }

    /**
     * An inner class that generates a request for routes and allows the backend
     * request to run on its own thread.
     * 
     * @author dutchscout, Shuo Wang (modification)
     */
    private class DirThread implements Runnable {

        /**
         * Fourth digit in a date/time.
         */
        private static final int DIGIT_FOURTH = 3;

        /**
         * Sixth digit in a date/time.
         */
        private static final int DIGIT_SIXTH = 5;

        /**
         * Seventh digit in a date/time.
         */
        private static final int DIGIT_SEVENTH = 6;

        /**
         * Eleventh digit in a date/time.
         */
        private static final int DIGIT_ELEVENTH = 10;

        /**
         * 1 second = 1000 milliseconds.
         */
        private static final int SEC_TO_MILLISEC = 1000;

        /**
         * 1 mile = 1609.34 meters.
         */
        private static final double MILE_TO_METER = 1609.34;

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            // Generate a direction request
            DirectionsRequest dReq = new DirectionsRequest();
            // Set up addresses for direction request
            String currLocationLatLagString = "";
            if (!fromAutoCompleteTextView.isEnabled() || !toAutoCompleteTextView.isEnabled()) {
                // defensive programming, avoid null pointer exception and leak window
                if (locationClient != null && locationClient.isConnected()
                        && locationClient.getLastLocation() != null) {
                    currLocationLatLagString += locationClient.getLastLocation().getLatitude()
                            + ", " + locationClient.getLastLocation().getLongitude();
                } else {
                    // Must call runOnUiThread if want to display a Toast or a
                    // // Dialog within a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorDialog("GPS location is not available");
                        }
                    });
                    if (pd != null) {
                        pd.dismiss();
                    }
                    return;
                }
            }
            String from = "";
            String to = "";
            if (fromAutoCompleteTextView.isEnabled()) {
                from = fromAutoCompleteTextView.getText().toString();
            } else {
                from = currLocationLatLagString;
            }
            if (toAutoCompleteTextView.isEnabled()) {
                to = toAutoCompleteTextView.getText().toString();
            } else {
                to = currLocationLatLagString;
            }
            Log.d(TAG, "src address: " + from + " dest address: " + to);
            dReq.setStartAddress(from).setEndAddress(to);

            // Set up travel mode for direction request
            if (bicycleOnlyCheckBox.isChecked()) {
                tm = TravelMode.BICYCLING;
            } else {
                tm = TravelMode.MIXED;
            }
            dReq.setTravelMode(tm);
            Log.d(TAG, "Done setting the travel mode: " + tm);
            long timeToSend = timeDirectionRequest();

            // Determine time mode
            if (arriveAtButton.isChecked()) {
                dReq.setArrivalTime(timeToSend);
            } else {
                dReq.setDepartureTime(timeToSend);
            }

            // Set up biking distance 
            // Note: Use round instead of floor or ceil converting miles into meters
            if (minBikingDistanceEditText.getText().length() != 0) {
                dReq.setMinDistanceToBikeInMeters(
                        Math.round(Integer.parseInt(minBikingDistanceEditText.getText()
                                .toString()) * MILE_TO_METER));
            }
            
            if (maxBikingDistanceEditText.getText().length() != 0) {
                dReq.setMaxDistanceToBikeInMeters(
                        Math.round(Integer.parseInt(maxBikingDistanceEditText.getText()
                                .toString()) * MILE_TO_METER));
            }
            
            // Set up number of buses
            if (!bicycleOnlyCheckBox.isChecked()) {
                if (minNumBusesEditText.getText().length() != 0) {
                    dReq.setMinNumberBusTransfers(Integer.parseInt(minNumBusesEditText.getText()
                            .toString()));
                }

                if (maxNumBusesEditText.getText().length() != 0) {
                    dReq.setMaxNumberBusTransfers(Integer.parseInt(maxNumBusesEditText.getText()
                            .toString()));
                }
            }
            
            // Do Request
            DirectionsStatus retVal = dReq.doRequest();
            Log.d(TAG, "Finish the do request");
            // If an error happens to the direction request
            // display an AlertDialog to let user to (re)start
            // a new request
            if (retVal.isError()) {
                Log.d(TAG, "Error on do request");
                final String errorMessage = retVal.getMessage();
                // Must call runOnUiThread if want to display a Toast or a
                // Dialog within a thread
                runOnUiThread(new Runnable() {
                    public void run() {
                        showErrorDialog(errorMessage);
                    }
                });

                // Closes the searching dialog and stay in the search activity
                pd.dismiss();
                return;
            }
            Log.d(TAG, "Do request success!");
            if (historyItem.addToHistory(currLocationLatLagString, from)) {
                saveHistoryFile(from);
            }
            if (historyItem.addToHistory(currLocationLatLagString, to)) {
                saveHistoryFile(to);
            }
            // send the result to ResultsActivity
            Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
            intent.putExtra("List of Routes", (Serializable) dReq.getSolutions());
            intent.putExtra("Current Route Index", 0);

            startActivity(intent);
            // Closes the searching dialog
            pd.dismiss();
            Log.v(TAG, "Done searching, go to result activity");
        }

        /**
         * Show the error dialog to user.
         * 
         * @param message
         *            the error message that will show in the dialog.
         */
        private void showErrorDialog(final String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setMessage(message);
            builder.setTitle(Html.fromHtml("<font color='red'>Error</font>"));
            // we can set the onClickListener parameter as null
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int which) {
    // cancel this dialog
                    dialog.cancel();
                }
            });
            // Create the AlertDialog object and return it
            builder.create().show();
        }

        /**
         * Setup the time direction request.
         * 
         * @return the time in ms.
         */
        private long timeDirectionRequest() {
            // Set up time for direction request
            int month, dayOfMonth, year, hourOfDay, minute, second = 0;
            Time time = new Time();
            if (leaveNowButton.isChecked()) {
                hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
            } else {
                String dateString = dateEditText.getText().toString();
                String timeString = timeEditText.getText().toString();

                // System uses month from 0 to 11 to represent January to
                // December
                month = Integer.parseInt(dateString.substring(0, 2)) - 1;

                dayOfMonth = Integer.parseInt(dateString.substring(DIGIT_FOURTH, DIGIT_SIXTH));
                year = Integer.parseInt(dateString.substring(DIGIT_SEVENTH, DIGIT_ELEVENTH));
                hourOfDay = Integer.parseInt(timeString.substring(0, 2));
                minute = Integer.parseInt(timeString.substring(DIGIT_FOURTH, DIGIT_SIXTH));
            }

            time.set(second, minute, hourOfDay, dayOfMonth, month, year);
            long timeToSend = time.toMillis(false) / SEC_TO_MILLISEC;
            return timeToSend;
        }
    }

    /**
     * Integer Representation of Color: Light Blue.
     */
    private static final int COLOR_LIGHT_BLUE = Color.rgb(13, 139, 217);

    /**
     * The calendar visible within this SearchActivity as a source of time Note:
     * Calendar subclass instance is set to the current date and time in the
     * default Timezone, could be changed as users live in different timezones.
     */
    private final Calendar calendar = Calendar.getInstance();

    /**
     * "Leave Now" radio button.
     */
    private RadioButton leaveNowButton;

    /**
     * "Depart At" radio button.
     */
    private RadioButton departAtButton;

    /**
     * "Arrive At" radio button.
     */
    private RadioButton arriveAtButton;

    /**
     * "Start from" AutoCompleteTextView for starting address.
     */
    private AutoCompleteTextView fromAutoCompleteTextView;

    /**
     * "To" AutoCompleteTextView for destination address.
     */
    private AutoCompleteTextView toAutoCompleteTextView;

    /**
     * Delete button for user to clear text in fromAutoCompleteTextView.
     */
    private ImageButton fromClearButton;

    /**
     * Delete button for user to clear text in toAutoCompleteTextView.
     */
    private ImageButton toClearButton;

    /**
     * Reverse button for user to switch starting and destination address.
     */
    private ImageButton reverseButton;

    /**
     * EditText for user to pick a date.
     */
    private EditText dateEditText;

    /**
     * EditText for user to pick a time.
     */
    private EditText timeEditText;

    /**
     * "Find" to start searching.
     */
    private Button findButton;

    /**
     * Keeps an array of history entries.
     */
    private History historyItem;

    /**
     * A CheckBox for user to select BICYCLE ONLY MODE.
     */
    private CheckBox bicycleOnlyCheckBox;

    /**
     * Keeps selected travelMode.
     */
    private TravelMode tm;

    /**
     * A progress dialog indicating the searching status of this activity.
     */
    private ProgressDialog pd;

    /**
     * Location Client to get user's current location.
     */
    private LocationClient locationClient;

    /**
     * Current Location (From) button for current location.
     */
    private ImageButton fromCurrLocationButton;

    /**
     * Current Location (To) button for current location.
     */
    private ImageButton toCurrLocationButton;

    /**
     * TextView for informing user to choose number of buses.
     */
    private TextView numBusesTextView;
    
    /**
     * EditText for user to set minimum number of buses.
     */
    private EditText minNumBusesEditText;
    
    /**
     * EditText for user to set maximum number of buses.
     */
    private EditText maxNumBusesEditText;

    /**
     * EditText for user to set minimum biking distance.
     */
    private EditText minBikingDistanceEditText;
    
    /**
     * EditText for user to set maximum biking distance.
     */
    private EditText maxBikingDistanceEditText;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        establishViewsAndOtherNecessaryComponents();
        setInitialText();
        setListeners();
        setHistorySection();
        Log.v(TAG, "Done on create");
    }

    /**
     * Connect location client (for power saving). {@inheritDoc}
     * 
     * @see android.app.Activity#onStart(android.os.Bundle)
     */
    @Override
    protected final void onStart() {
        super.onStart();
        locationClient.connect();
        Log.v(TAG, "Done on start");
    }

    /**
     * Refresh the history list.
     */
    @Override
    protected final void onResume() {
        setHistorySection();
        super.onResume();
        Log.v(TAG, "Done on Resume");
    }

    /**
     * Disconnect location client (for power saving). {@inheritDoc}
     * 
     * @see android.app.Activity#onStop(android.os.Bundle)
     */
    @Override
    protected final void onStop() {
        locationClient.disconnect();
        super.onStop();
        Log.v(TAG, "Done on Stop");
    }

    /**
     * Show the menu bar when the setting button is clicked.
     * 
     * @param menu
     *            The options menu in which you place your items.
     * @return true if the menu to be displayed.
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_search, menu);
        return true;
    }

    /**
     * This method will be called when user click buttons in the setting menu.
     * 
     * @param item
     *            the menu item that user will click
     * @return true if user select an item
     */
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return true;
        case R.id.action_settings:
            // user click the setting button, start the settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("parent", "Search");
            startActivity(settingsIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets up default text to be displayed on the UI of this activity.
     */
    private void setInitialText() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourString = "";
        if (hour < MIN_TWO_DIGIT_NUMBER) {
            hourString += "0";
        }
        hourString += hour;

        int minute = calendar.get(Calendar.MINUTE);
        String minuteString = "";
        if (minute < MIN_TWO_DIGIT_NUMBER) {
            minuteString += "0";
        }
        minuteString += minute;

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);
        // System uses month from 0 to 11 to represent January to December
        month++;

        String monthString = "";
        if (month < MIN_TWO_DIGIT_NUMBER) {
            monthString += "0";
        }
        monthString += month;

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayString = "";
        if (day < MIN_TWO_DIGIT_NUMBER) {
            dayString += "0";
        }
        dayString += day;

        dateEditText.setText(monthString + "/" + dayString + "/" + year);
        timeEditText.setText(hourString + ":" + minuteString);
    }

    /**
     * Find and establish all UI components from xml to this activity.
     */
    private void establishViewsAndOtherNecessaryComponents() {
        leaveNowButton = (RadioButton) findViewById(R.id.radioButtonLeaveNow);
        departAtButton = (RadioButton) findViewById(R.id.radioButtonDepartAt);
        arriveAtButton = (RadioButton) findViewById(R.id.radioButtonArriveAt);

        dateEditText = (EditText) findViewById(R.id.editTextDate);
        timeEditText = (EditText) findViewById(R.id.editTextTime);

        findButton = (Button) findViewById(R.id.buttonFind);

        fromAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.editTextStartFrom);
        toAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.editTextTo);
        reverseButton = (ImageButton) findViewById(R.id.imageButtonReverse);
        fromClearButton = (ImageButton) findViewById(R.id.imageButtonClearFrom);
        toClearButton = (ImageButton) findViewById(R.id.imageButtonClearTo);
        numBusesTextView = (TextView) findViewById(R.id.textViewNumBuses);
        minNumBusesEditText = (EditText) findViewById(R.id.editTextMinNumBuses);
        maxNumBusesEditText = (EditText) findViewById(R.id.editTextMaxNumBuses);
        minBikingDistanceEditText = (EditText) findViewById(R.id.editTextMinBikingDistance);
        maxBikingDistanceEditText = (EditText) findViewById(R.id.editTextMaxBikingDistance);
        
        tm = TravelMode.MIXED;
        bicycleOnlyCheckBox = (CheckBox) findViewById(R.id.checkboxBicycleOnly);

        // Location setup
        locationClient = new LocationClient(this, this, this);
        fromCurrLocationButton = (ImageButton) findViewById(R.id.imageButtonCurrentLocationFrom);
        toCurrLocationButton = (ImageButton) findViewById(R.id.imageButtonCurrentLocationTo);
    }

    /**
     * Attach all listeners to corresponding UI widgets.
     */
    private void setListeners() {
        setAddressRelatedListeners();
        leaveNowButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                dateEditText.setEnabled(false);
                timeEditText.setEnabled(false);
            }
        });

        departAtButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                dateEditText.setEnabled(true);
                timeEditText.setEnabled(true);
            }
        });

        arriveAtButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                dateEditText.setEnabled(true);
                timeEditText.setEnabled(true);
            }
        });

        dateEditText.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                DialogFragment dpf = new DatePickerFragment();
                dpf.show(getFragmentManager(), "datePicker");
            }
        });
        // Prohibit user from directly typing a date (instead, users should pick
        // a date)
        dateEditText.setKeyListener(null);

        timeEditText.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                DialogFragment tpf = new TimePickerFragment();
                tpf.show(getFragmentManager(), "timePicker");
            }
        });
        // Prohibit user from directly typing a time (instead, users should pick
        // a time)
        timeEditText.setKeyListener(null);
        findButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                pd = ProgressDialog.show(v.getContext(), "Searching", "Searching for routes...");

                Thread dirThread = new Thread(new DirThread());
                dirThread.start();
            }
        });
        
        bicycleOnlyCheckBox.setOnClickListener(new OnClickListener() {
            private boolean isCheckedBefore = false;
            public void onClick(final View v) {
                if (isCheckedBefore) {
                    numBusesTextView.setVisibility(View.VISIBLE);
                    minNumBusesEditText.setVisibility(View.VISIBLE);
                    maxNumBusesEditText.setVisibility(View.VISIBLE);          
                    isCheckedBefore = false;
                } else {
                    numBusesTextView.setVisibility(View.INVISIBLE);
                    minNumBusesEditText.setVisibility(View.INVISIBLE);
                    maxNumBusesEditText.setVisibility(View.INVISIBLE);
                    isCheckedBefore = true;
                }
            }
        });
        
        minNumBusesEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus && minNumBusesEditText.getText().length() != 0) {
                    // Format numbers
                    int formattedNumber = Integer
                            .parseInt(minNumBusesEditText.getText().toString());
                    minNumBusesEditText.setText("" + formattedNumber);
                }
            }
        });
        
        maxNumBusesEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus && maxNumBusesEditText.getText().length() != 0) {
                    // Format numbers
                    int formattedNumber = Integer
                            .parseInt(maxNumBusesEditText.getText().toString());
                    maxNumBusesEditText.setText("" + formattedNumber);
                }
            }
            
        });

        minBikingDistanceEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus && minBikingDistanceEditText.getText().length() != 0) {
                    // Format numbers
                    int formattedNumber = Integer.parseInt(minBikingDistanceEditText.getText()
                            .toString());
                    minBikingDistanceEditText.setText("" + formattedNumber);
                }
            }
            
        });
        
        maxBikingDistanceEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus && maxBikingDistanceEditText.getText().length() != 0) {
                    // Format numbers
                    int formattedNumber = Integer.parseInt(maxBikingDistanceEditText.getText()
                            .toString());
                    maxBikingDistanceEditText.setText("" + formattedNumber);
                }
            }
        });
    }

    /**
     * Attach address-related listeners to corresponding UI widgets.
     */
    private void setAddressRelatedListeners() {
        // Determine whether to show clear button for fromAutoCompleteTextView
        // when it is focused
        setAddressBoxListeners();

        reverseButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                boolean fromACTVisEnabled = fromAutoCompleteTextView.isEnabled();
                boolean toACTVisEnabled = toAutoCompleteTextView.isEnabled();
                String fromACTVOriginalText = fromAutoCompleteTextView.getText().toString();
                String toACTVOriginalText = toAutoCompleteTextView.getText().toString();

                if (!fromACTVisEnabled) {
                    toAutoCompleteTextView.clearComposingText();
                    toAutoCompleteTextView.setEnabled(false);
                    toAutoCompleteTextView.setText(fromACTVOriginalText);
                    toAutoCompleteTextView.setTextColor(COLOR_LIGHT_BLUE);
                    toAutoCompleteTextView.setTypeface(null, Typeface.ITALIC);
                    toCurrLocationButton.setImageResource(R.drawable.current_location_cancel);
                    toClearButton.setVisibility(View.INVISIBLE);
                } else {
                    toAutoCompleteTextView.setTextColor(Color.BLACK);
                    toAutoCompleteTextView.setTypeface(null, Typeface.NORMAL);
                    toCurrLocationButton.setImageResource(R.drawable.current_location_select);
                    toAutoCompleteTextView.setText(fromACTVOriginalText);
                    toAutoCompleteTextView.setEnabled(true);
                }

                if (!toACTVisEnabled) {
                    fromAutoCompleteTextView.clearComposingText();
                    fromAutoCompleteTextView.setEnabled(false);
                    fromAutoCompleteTextView.setText(toACTVOriginalText);
                    fromAutoCompleteTextView.setTextColor(COLOR_LIGHT_BLUE);
                    fromAutoCompleteTextView.setTypeface(null, Typeface.ITALIC);
                    fromCurrLocationButton.setImageResource(R.drawable.current_location_cancel);
                    fromClearButton.setVisibility(View.INVISIBLE);
                } else {
                    fromAutoCompleteTextView.setTextColor(Color.BLACK);
                    fromAutoCompleteTextView.setTypeface(null, Typeface.NORMAL);
                    fromCurrLocationButton.setImageResource(R.drawable.current_location_select);
                    fromAutoCompleteTextView.setText(toACTVOriginalText);
                    fromAutoCompleteTextView.setEnabled(true);
                }
            }
        });

        fromClearButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                fromAutoCompleteTextView.clearComposingText();
                fromAutoCompleteTextView.setText("");
            }
        });

        toClearButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                toAutoCompleteTextView.clearComposingText();
                toAutoCompleteTextView.setText("");
            }
        });

        fromCurrLocationButton.setOnClickListener(new OnClickListener() {
            private boolean currentLocationSelected = false;

            public void onClick(final View v) {
                if (currentLocationSelected) {
                    fromAutoCompleteTextView.setText("");
                    fromAutoCompleteTextView.setTextColor(Color.BLACK);
                    fromAutoCompleteTextView.setTypeface(null, Typeface.NORMAL);
                    fromCurrLocationButton.setImageResource(R.drawable.current_location_select);
                    fromAutoCompleteTextView.setEnabled(true);
                    fromAutoCompleteTextView.requestFocus();
                    currentLocationSelected = false;
                } else {
                    fromAutoCompleteTextView.clearComposingText();
                    fromAutoCompleteTextView.setEnabled(false);
                    fromAutoCompleteTextView.setText("Current Location");
                    fromAutoCompleteTextView.setTextColor(COLOR_LIGHT_BLUE);
                    fromAutoCompleteTextView.setTypeface(null, Typeface.ITALIC);
                    fromCurrLocationButton.setImageResource(R.drawable.current_location_cancel);
                    fromClearButton.setVisibility(View.INVISIBLE);
                    currentLocationSelected = true;
                }

            }
        });

        toCurrLocationButton.setOnClickListener(new OnClickListener() {
            private boolean currentLocationSelected = false;

            public void onClick(final View v) {
                if (currentLocationSelected) {
                    toAutoCompleteTextView.setText("");
                    toAutoCompleteTextView.setTextColor(Color.BLACK);
                    toAutoCompleteTextView.setTypeface(null, Typeface.NORMAL);
                    toCurrLocationButton.setImageResource(R.drawable.current_location_select);
                    toAutoCompleteTextView.setEnabled(true);
                    toAutoCompleteTextView.requestFocus();
                    currentLocationSelected = false;
                } else {
                    toAutoCompleteTextView.clearComposingText();
                    toAutoCompleteTextView.setEnabled(false);
                    toAutoCompleteTextView.setText("Current Location");
                    toAutoCompleteTextView.setTextColor(COLOR_LIGHT_BLUE);
                    toAutoCompleteTextView.setTypeface(null, Typeface.ITALIC);
                    toCurrLocationButton.setImageResource(R.drawable.current_location_cancel);
                    toClearButton.setVisibility(View.INVISIBLE);
                    currentLocationSelected = true;
                }
            }
        });
    }

    /**
     * Attach the two address boxes listeners to corresponding UI widgets.
     */
    private void setAddressBoxListeners() {
        fromAutoCompleteTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    if (fromAutoCompleteTextView.getText().toString().isEmpty()) {
                        fromClearButton.setVisibility(View.INVISIBLE);
                    } else {
                        fromClearButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    fromClearButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Determine whether to show clear button for fromAutoCompleteTextView
        // when it is being edited
        fromAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(final Editable s) {
                if (!fromAutoCompleteTextView.hasFocus()
                        || fromAutoCompleteTextView.getText().toString().isEmpty()) {
                    fromClearButton.setVisibility(View.INVISIBLE);
                } else {
                    fromClearButton.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(final CharSequence s, final int start, final int count,
                    final int after) {
                // Do nothing
            }

            public void onTextChanged(final CharSequence s, final int start, final int before,
                    final int count) {
                // Do nothing
            }
        });

        // Handle Event when user press "Next" on Keyboard:
        // Jump from fromAutoCompleteTextView to toAutoCompleteTextView
        fromAutoCompleteTextView.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, 
                    final int actionId, final KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    toAutoCompleteTextView.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        // Determine whether to show clear button for toAutoCompleteTextView
        // when it is focused
        toAutoCompleteTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    if (toAutoCompleteTextView.getText().toString().isEmpty()) {
                        toClearButton.setVisibility(View.INVISIBLE);
                    } else {
                        toClearButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    toClearButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Determine whether to show clear button for fromAutoCompleteTextView
        // when it is being edited
        toAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(final Editable s) {
                if (!toAutoCompleteTextView.hasFocus()
                        || toAutoCompleteTextView.getText().toString().isEmpty()) {
                    toClearButton.setVisibility(View.INVISIBLE);
                } else {
                    toClearButton.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(final CharSequence s, final int start, final int count,
                    final int after) {
                // Do nothing
            }

            public void onTextChanged(final CharSequence s, final int start, final int before,
                    final int count) {
                // Do nothing
            }
        });
    }

    /**
     * Fill in the history section. TODO: currently hard-coded, creating a live
     * version in next phases. Since the data is dummy, I keep all the numbers
     * even if they are marked as magic numbers by Check-Style.
     */
    private void setHistorySection() {
        historyItem = History.getInstance();
        String[] f = historyItem.getHistory().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, f);
        fromAutoCompleteTextView.setAdapter(adapter);
        toAutoCompleteTextView.setAdapter(adapter);
    }

    /**
     * After onStart calls locationClient to connect, this is triggered if there
     * is a problem connecting the Google Play Location Services.
     * 
     * @param cr
     *            the connection result
     */
    @Override
    public final void onConnectionFailed(final ConnectionResult cr) {
        Toast.makeText(this,
                "Connection Failed, please check your" + "Google Play Services status",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * After onStart calls locationClient to connect, this is triggered if
     * connection to Google Play Location Services is successfully established.
     * 
     * @param bd
     *            the bundle passed in by Google Play Location Services
     */
    @Override
    public final void onConnected(final Bundle bd) {
        // Nothing happens
    }

    /**
     * After onStart calls locationClient to disconnect, this is triggered if
     * connection to Google Play Location Services is successfully disconnected.
     */
    @Override
    public final void onDisconnected() {
        // Nothing happens
    }

    /**
     * Write all history addresses into file.
     * 
     * @param address
     *            the address that will add the file.
     */
    private void saveHistoryFile(final String address) {
        FileOutputStream fos = null;
        try {
            // append the address into exist file.
            fos = openFileOutput(History.FILENAME, Context.MODE_APPEND);
            History.writeOneAddressToFile(fos, address);
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Cannot create history file");
        }  finally {
            try {
                // close the file output stream.
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Connot close the file ouput stream");
            }
        }
    }
}