package io.exterminator3618.server.utils;

public class ForbiddenAction extends RuntimeException {

    public ForbiddenAction(String message) {
        super(message);
    }

}
