package org.scify.moonwalker.app.desktop;

import com.badlogic.gdx.Gdx;
import org.scify.moonwalker.app.helpers.AnalyticsLogger;

import java.util.Map;

public class DesktopAnalyticsLogger implements AnalyticsLogger {

    private static String TAG = DesktopAnalyticsLogger.class.getName();

    @Override
    public void logEvent(String eventName , Map<String, String> payload) {
        Gdx.app.log(TAG, eventName);
    }

    @Override
    public void logEvent(String eventName, String payload) {
        Gdx.app.log(TAG, eventName + payload);
    }
}
