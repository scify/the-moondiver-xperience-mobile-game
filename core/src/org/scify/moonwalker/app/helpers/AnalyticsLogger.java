package org.scify.moonwalker.app.helpers;

import java.util.Map;

public interface AnalyticsLogger {

    void logEvent(String eventName, Map<String, String> payloadAttributesMap);

    void logEvent(String eventName,String payload);
}
