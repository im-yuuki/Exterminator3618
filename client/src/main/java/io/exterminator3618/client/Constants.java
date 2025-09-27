package io.exterminator3618.client;

/**
 * Client configuration constants and default values
 * used throughout the Exterminator3618 client application.
 */
public class Constants {

    /**
     * Window configuration.
     */
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String WINDOW_TITLE = "Exterminator3618";


    /**
     * Default ball width in pixels.
     */
    public static final int BALL_WIDTH = 64;
    /**
     * Default ball height in pixels.
     */
    public static final int BALL_HEIGHT = 64;
    /**
     * Path to the ball texture.
     */
    public static final String BALL_IMAGE_PATH = "ball.png";
    /**
     * Default ball speed.
     */
    public static final double BALL_SPEED = 500.0;
    /**
     * Friction factor applied each frame to velocity (0..1).
     */
    public static final double FRICTION = 0.98;
}
