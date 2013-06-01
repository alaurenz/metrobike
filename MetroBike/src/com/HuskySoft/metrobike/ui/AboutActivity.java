package com.HuskySoft.metrobike.ui;

import com.HuskySoft.metrobike.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

/**
 * This is a about activity which shows the HuskySoft logo, copyright
 * information, developer's name and more.
 * 
 * @author Sam Wilson, Shuo Wang
 */
public class AboutActivity extends Activity {

    /**
     * The tag of this activity.
     */
    private static final String TAG = "AboutActivity";

    /**
     * The current layout ID.
     */
    private int layoutID;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // linked to the activity about xml
        layoutID = R.layout.activity_about;
        setContentView(layoutID);
        Button term = (Button) findViewById(R.id.termAndConditions);
        term.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.v(TAG, "Terms and Conditions button");
                layoutID = R.layout.term_and_conditions;
                setContentView(layoutID);
            }
        });
        Log.v(TAG, "Done created About activity");
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public final void onBackPressed() {
        if (layoutID == R.layout.term_and_conditions) {
            Intent current = getIntent();
            finish();
            startActivity(current);
            Log.v(TAG, "Go back to about activity");
        } else {
            super.onBackPressed();
            Log.v(TAG, "exit about activity");
        }
    }

}
