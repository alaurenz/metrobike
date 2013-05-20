package com.HuskySoft.metrobike.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.Toast;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.History;
import com.HuskySoft.metrobike.ui.utility.MapSetting;
import com.google.android.gms.maps.GoogleMap;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 * 
 * @author Sam Wilson
 */
public class SettingsActivity extends PreferenceActivity {
    /*
     * noted that if you going to change the strings in the instance field, you
     * also need to change them in the string_activity_setting.xml
     */
    /**
     * the xml key of version tab.
     */
    private static final String VERSION = "version_key";

    /**
     * the xml key of locate tab.
     */
    private static final String LOCALE = "locale_key";

    /**
     * the xml key of clear history tab.
     */
    private static final String CLR_HISTORY = "clear_history_key";

    /**
     * the xml key of view history tab.
     */
    private static final String VIEW_HISTORY = "view_history_key";

    /**
     * the xml key of about tab.
     */
    private static final String ABOUT = "about_key";

    /**
     * the xml key of map type tab.
     */
    private static final String MAP_TYPE = "map_type";

    /**
     * the xml key of traffic tab.
     */
    private static final String TRAFFIC_TYPE = "traffic_type";

    /**
     * the xml key of current_type.
     */
    private static final String CURRENT_TYPE = "current_type";
    
    /**
     * the mapType list preference.
     */
    private ListPreference mapType;
    
    /**
     * the traffic list preference.
     */
    private ListPreference trafficType;
    
    /**
     * the current list preference. 
     */
    private ListPreference currentType;
    
    /**
     * Keeps an array of history entries.
     */
    private History historyItem;

    /**
     * The map setting object.
     */
    private MapSetting map;

    /**
     * The 4 types of map.
     */
    private static final int[] MAP_ARRAYS = { GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_SATELLITE, GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_TERRAIN };

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    protected final void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen() {
        historyItem = History.getInstance();
        // noted that it should not crash if passing null.
        // Since setting activity is not the first class to be called.
        map = MapSetting.getInstance(null);
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Bind the summaries of EditText preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(VERSION));
        bindPreferenceSummaryToValue(findPreference(LOCALE));
        // this is binding the button
        bindPreferenceToClick(findPreference(ABOUT));
        bindPreferenceToClick(findPreference(CLR_HISTORY));
        bindPreferenceToClick(findPreference(VIEW_HISTORY));
        
        mapType = (ListPreference) findPreference(MAP_TYPE);
        trafficType = (ListPreference) findPreference(TRAFFIC_TYPE);
        currentType = (ListPreference) findPreference(CURRENT_TYPE);
        
        bindPreferenceToClick(mapType);
        bindPreferenceToClick(trafficType);
        bindPreferenceToClick(currentType);
    }

    /**
     * bind the preference into listener. So that it reacts when user click some
     * preferences
     * 
     * @param preference
     *            the preference from the setting view list
     */
    private void bindPreferenceToClick(final Preference preference) {
        preference.setOnPreferenceClickListener(preferenceOnClick);
    }

    /**
     * A preference value click listener that react the preference's clicked by
     * user.
     */
    private Preference.OnPreferenceClickListener preferenceOnClick = 
            new OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(final Preference preference) {
            String key = preference.getKey();

            boolean isClick = true;
            // I don't use switch statement because it didn't support java 6 or
            // below
            if (key.equals(ABOUT)) {
                // start a new about activity
                startActivity(new Intent(preference.getContext(), AboutActivity.class));
            } else if (key.equals(CLR_HISTORY)) {
                deleteAllHistory(preference);
            } else if (key.equals(VIEW_HISTORY)) {
                viewHistory(preference);
            } else if (key.equals(MAP_TYPE)) {
                changeTheMapType(preference);
            } else if (key.equals(TRAFFIC_TYPE)) {
                switchMode(preference, key);
            } else if (key.equals(CURRENT_TYPE)) {
                switchMode(preference, key);
            } else {
                isClick = false;
            }
            return isClick;
        }

        /**
         * Invoke when user click change traffic.
         * 
         * @param preference
         * @param key
         */
        private void switchMode(final Preference preference, final String key) {
            ((ListPreference) preference)
                    .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(final Preference preference,
                                final Object newValue) {
                            boolean isClick = true;
                            boolean show = ((String) newValue).equals("true");
                            if (key.equals(TRAFFIC_TYPE)) {
                                map.setTraffic(show);
                            } else if (key.equals(CURRENT_TYPE)) {
                                map.setCurrentLocationButton(show);
                            } else {
                                isClick = false;
                            }
                            return isClick;
                        }
                    });
        }

        /**
         * Invoke when user click view history.
         * 
         * @param preference
         */
        private void viewHistory(final Preference preference) {
            if (historyItem.getSize() > 0) {
                // show the history activity if there are some history
                Intent i = new Intent(preference.getContext(), HistoryActivity.class);
                startActivity(i);
            } else {
                // no history, show the toast
                Context context = preference.getContext();
                int emptyHistory = R.string.empty_history;
                Toast.makeText(context, emptyHistory, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Invoke when user click clear history.
         * 
         * @param preference
         */
        private void deleteAllHistory(final Preference preference) {
            // show the alertDialog to make sure user want to delete all
            // history
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(preference.getContext());
            // set title warning and red color
            alertDialog.setTitle(Html.fromHtml("<font color='red'>Warning</font>"));
            alertDialog.setMessage("Are you sure want to delete all histories?");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    historyItem.deleteAll();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    // cancel this dialog
                    dialog.cancel();
                }
            });
            // show this dialog on the screen
            alertDialog.create().show();
        }

        /**
         * Invoke when user want to change the map type.
         * 
         * @param preference
         */
        private void changeTheMapType(final Preference preference) {
            // ****** bug *******
            // the default value does not match!
            ((ListPreference) preference)
                    .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(final Preference preference,
                                final Object newValue) {
                            try {
                                int value = Integer.parseInt((String) newValue);
                                map.setMapDisplay(MAP_ARRAYS[value]);
                                return true;
                            } catch (Exception e) {
                                // Defensive programming even though it
                                // should not be crashed
                                return false;
                            }
                        }
                    });
        }
    };

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener bindToVal = 
            new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(final Preference preference, final Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                if (index >= 0) {
                    preference.setSummary(listPreference.getEntries()[index]);
                } else {
                    preference.setSummary(null);
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }

    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     * 
     * @param preference
     *            the setting preference in the view list
     * @see #bindToVal
     */
    private void bindPreferenceSummaryToValue(final Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(bindToVal);

        // Trigger the listener immediately with the preference's
        // current value.
        bindToVal.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(
                        preference.getKey(), ""));
    }
    
    /**
     * update the value of the list preference.
     */
    @Override
    protected final void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        int temp = map.getMapDisplay();
        int value = 0;
        for (; value < MAP_ARRAYS.length; value++) {
            if (temp == MAP_ARRAYS[value]) {
                break;
            }
        }
        mapType.setValueIndex(value);
        trafficType.setValue(map.getTrafficDisplay() + "");
        currentType.setValue(map.getMyCurrentLocation() + "");
    }
}
