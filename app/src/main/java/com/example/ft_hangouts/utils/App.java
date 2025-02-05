package com.example.ft_hangouts.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.os.Bundle;

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private int startedCount = 0;
    private static long lastBackgroundTime = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable checkBackgroundRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        startedCount++;
        // Cancel background check if app returns to foreground
        if (checkBackgroundRunnable != null) {
            handler.removeCallbacks(checkBackgroundRunnable);
            checkBackgroundRunnable = null;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        startedCount--;
        if (startedCount == 0) {
            // Check after delay to avoid false background detection during activity transitions
            checkBackgroundRunnable = () -> {
                if (startedCount == 0) {
                    lastBackgroundTime = System.currentTimeMillis();
                }
            };
            handler.postDelayed(checkBackgroundRunnable, 500);
        }
    }

    public static long getLastBackgroundTime() {
        return lastBackgroundTime;
    }

    public static void resetLastBackgroundTime() {
        lastBackgroundTime = 0;
    }

    // Other unused lifecycle methods
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityResumed(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override public void onActivityDestroyed(Activity activity) {}
}