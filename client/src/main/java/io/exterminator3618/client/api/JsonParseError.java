package io.exterminator3618.client.api;

public class JsonParseError extends RuntimeException {

    public final int statusCode;

    public JsonParseError(String message) {
        super(message);
        statusCode = 200;
    }

    public JsonParseError(String message, Throwable cause) {
        super(message, cause);
        statusCode = 200;
    }

    public JsonParseError(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public JsonParseError(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
