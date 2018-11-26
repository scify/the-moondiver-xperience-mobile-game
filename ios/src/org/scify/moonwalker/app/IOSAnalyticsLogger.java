package org.scify.moonwalker.app;

import com.badlogic.gdx.Gdx;
import org.scify.moonwalker.app.helpers.AnalyticsLogger;

import java.util.Map;

public class IOSAnalyticsLogger implements AnalyticsLogger {

    private static String TAG = IOSAnalyticsLogger.class.getName();

    @Override
    public void logEvent(String eventName , Map<String, String> payload) {
        Gdx.app.log(TAG, eventName + payload);
    }

    @Override
    public void logEvent(String eventName, String payload) {
        Gdx.app.log(TAG, eventName + payload);
    }
}
