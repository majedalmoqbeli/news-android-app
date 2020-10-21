package com.majedalmoqbeli.newsapp.base;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.security.ProviderInstaller;


public class MainApplication extends MultiDexApplication {

    public static Context sMainApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sMainApplicationContext = this;

        updateAndroidSecurityProvider();

    }

    // this is for kitkat android 4
   //
    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
