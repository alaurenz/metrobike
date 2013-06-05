package com.HuskySoft.metrobike.ui.utility;

import android.app.Application;
import android.content.Context;

/**
 * singleton class for getting resource outside of activity classes.
 * @author mengwan
 *
 */
public class MyApplicationClass extends Application {
    /**
     * singleton context class field.
     */
    private static Context appContext;

    /**
     * overwritten onCreate() method to set the instance of the field.
     */
    @Override
    public final void onCreate() { // Always called before anything else in the app
                            // so in the rest of your code safe to call
                            // MyApplicationClass.getContext();
        super.onCreate();
        appContext = this;
    }

    /**
     * Get the instance of the class.
     * @return Context instance
     */
    public static Context getContext() {
        return appContext;
    }
}
