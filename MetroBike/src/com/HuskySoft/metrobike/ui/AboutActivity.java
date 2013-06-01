package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;

import android.os.Bundle;
import android.app.Activity;

/**
 * This is a about activity which shows the HuskySoft logo, copyright
 * information, developer's name and more.
 * 
 * @author Sam Wilson, Shuo Wang
 */
public class AboutActivity extends Activity {

    // we will need to add some copyright issue in this activity.
    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // linked to the activity about xml
        setContentView(R.layout.activity_about);
    }
}
