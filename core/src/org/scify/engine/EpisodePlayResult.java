package org.scify.engine;

public class EpisodePlayResult<T> {
    protected boolean success;
    protected T value;
    protected String message;

    public EpisodePlayResult (boolean success, T value, String message) {
        this.success = success;
        this.value = value;
        this.message = message;
    }

    public EpisodePlayResult (boolean success, T value) {
        this.success = success;
        this.value = value;
        this.message = "";
    }

    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
