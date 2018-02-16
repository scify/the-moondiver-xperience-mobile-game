package org.scify.engine;

public class UserAction {
    UserActionCode actionCode;
    Object payload;
    long timestamp;

    public UserAction(UserActionCode actionCode) {
        this.actionCode = actionCode;
    }

    public UserAction(UserActionCode actionCode, Object payload) {
        this.actionCode = actionCode;
        this.payload = payload;
    }

    public UserAction(UserActionCode actionCode, Object payload, long timestamp) {
        this.actionCode = actionCode;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public UserActionCode getActionCode() {
        return actionCode;
    }

    public Object getActionPayload() {
        return payload;
    }

    long getTimestamp() {
        return timestamp;
    }
}
