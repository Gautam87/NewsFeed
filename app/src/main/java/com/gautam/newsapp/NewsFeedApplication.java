package com.gautam.newsapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class NewsFeedApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
