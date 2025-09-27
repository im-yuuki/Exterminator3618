package io.exterminator3618.client;

/**
 * Client configuration constants and default values
 * used throughout the Exterminator3618 client application.
 */
public class Constants {
    // Window configuration
    /**
     * Default window width in pixels.
     */
    public static final int WINDOW_WIDTH = 1280;
    /**
     * Default window height in pixels.
     */
    public static final int WINDOW_HEIGHT = 720;
    /**
     * Window title.
     */
    public static final String WINDOW_TITLE = "Exterminator3618";
    /**
     * Whether to enable VSync.
     */
    public static final boolean VSYNC_ENABLED = true;
    /**
     * FPS when the window is unfocused (idle).
     */
    public static final int IDLE_FPS = 10;
    /**
     * 16x16 window icon path.
     */
    public static final String ICON_16_PATH = "icons/logo.png";
    /**
     * 32x32 window icon path.
     */
    public static final String ICON_32_PATH = "icons/logo32.png";
    /**
     * 64x64 window icon path.
     */
    public static final String ICON_64_PATH = "icons/logo64.png";
    /**
     * 128x128 window icon path.
     */
    public static final String ICON_128_PATH = "icons/logo128.png";

    // Ball defaults
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
