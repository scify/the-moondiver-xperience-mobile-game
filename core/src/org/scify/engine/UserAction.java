package org.scify.engine;

public class UserAction {
    String actionCode;
    Object payload;
    long timestamp;

    public UserAction(String actionCode) {
        this.actionCode = actionCode;
    }

    public UserAction(String actionCode, Object payload) {
        this.actionCode = actionCode;
        this.payload = payload;
    }

    public UserAction(String actionCode, Object payload, long timestamp) {
        this.actionCode = actionCode;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public String getActionCode() {
        return actionCode;
    }

    public Object getActionPayload() {
        return payload;
    }

    long getTimestamp() {
        return timestamp;
    }
}
