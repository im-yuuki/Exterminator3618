package io.exterminator3618.client.utils;

public class ApiError extends RuntimeException {

    protected final int statusCode;

    public ApiError(String message) {
        super(message);
        statusCode = 200;
    }

    public ApiError(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiError(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
