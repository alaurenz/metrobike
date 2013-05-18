package com.HuskySoft.metrobike.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.HuskySoft.metrobike.R;
import com.HuskySoft.metrobike.ui.utility.History;

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
     * Keeps an array of history entries.
     */
    private History historyItem;
    
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
        
        historyItem = History.getInstance();
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
                startActivity(new Intent(preference.getContext(),
                        AboutActivity.class));
            } else if (key.equals(CLR_HISTORY)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(preference.getContext());
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Are you sure want to delete all histories?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                        final int which) {
                            // may be we need a toast message?
                            historyItem.deleteAll();
                        }
                    });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                            final int which) {
                                // cancel this dialog    
                                dialog.cancel();
                            }
                    });
                // show this dialog on the screen
                alertDialog.create().show();
            } else if (key.equals(VIEW_HISTORY)) {
                // TODO something for viewing the history
                // Currently just return isClick. To be Changed.
                return isClick;
            } else {
                isClick = false;
            }
            return isClick;
        }
    };

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener bindToValueListener = 
            new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(final Preference preference,
                final Object value) {
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
     * @see #bindToValueListener
     */
    private void bindPreferenceSummaryToValue(final Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(bindToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        bindToValueListener.onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }
}
