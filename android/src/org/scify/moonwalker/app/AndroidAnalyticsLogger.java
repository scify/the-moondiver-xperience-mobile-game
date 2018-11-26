package org.scify.moonwalker.app;

import android.content.Context;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import org.scify.moonwalker.app.helpers.AnalyticsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import org.scify.moonwalker.app.helpers.AppInfo;

import java.util.Map;

public class AndroidAnalyticsLogger implements AnalyticsLogger {

    private static String TAG = AndroidAnalyticsLogger.class.getName();
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppInfo appInfo;

    public AndroidAnalyticsLogger(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        appInfo = AppInfo.getInstance();
    }

    @Override
    public void logEvent(String eventName, Map<String, String> payloadAttributesMap) {

        Gdx.app.log(TAG, eventName);
        Bundle bundle = new Bundle();
        for(Map.Entry<String, String> entry : payloadAttributesMap.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        bundle.putString("SCREEN_HEIGHT", String.valueOf(appInfo.getScreenHeight()));
        bundle.putString("SCREEN_WIDTH", String.valueOf(appInfo.getScreenWidth()));
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    @Override
    public void logEvent(String eventName, String payload) {
        Gdx.app.log(TAG, eventName);
        Bundle bundle = new Bundle();
        bundle.putString("SCREEN_HEIGHT", String.valueOf(appInfo.getScreenHeight()));
        bundle.putString("SCREEN_WIDTH", String.valueOf(appInfo.getScreenWidth()));
        bundle.putString("PAYLOAD", payload);
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }
}
