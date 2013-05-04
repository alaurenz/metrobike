package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;

import android.os.Bundle;
import android.app.Activity;

/**
 * This is a about activity which shows the HuskySoft logo, copyright
 * information, developer's name and more.
 * 
 * @author Sam Wilson
 */
public class AboutActivity extends Activity {

    // More details will added in this about activity in beta
    // version

    /**
     * {@inheritDoc}
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // linked to the activity about xml
        setContentView(R.layout.activity_about);
    }

}
