package io.exterminator3618.server.utils;

public class EventDeserializeError extends RuntimeException {

    public EventDeserializeError(String message) {
        super(message);
    }

    public EventDeserializeError(String message, Throwable cause) {
        super(message, cause);
    }

}
