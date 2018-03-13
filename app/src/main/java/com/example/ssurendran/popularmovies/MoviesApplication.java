package com.example.ssurendran.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ssurendran on 3/14/18.
 */

public class MoviesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
