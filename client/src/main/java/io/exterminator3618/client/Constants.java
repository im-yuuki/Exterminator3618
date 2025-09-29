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
    public static final int BALL_WIDTH = 32;
    /**
     * Default ball height in pixels.
     */
    public static final int BALL_HEIGHT = 32;
    /**
     * Path to the ball texture.
     */
    public static final String BALL_REGION_NAME = "green_ball";
    /**
     * Default ball speed.
     */
    public static final double BALL_SPEED = 2000.0;

    /**
     * Audio file paths.
     */
    public static final String TEST_LONG_MUSIC_2 = "sound/test_bgr2.wav";
    public static final String BACKGROUND_MUSIC = "sound/test_bgr.mp3";
    public static final String BUFF_SOUND = "sound/buff_sound.mp3";

    /**
     * Path to the texture atlas file.
     */
    public static final String ATLAS_PATH = "assets/textures.atlas";

    /**
     * Brick texture region names.
     */
    public static final String NORMAL_BLUE_BRICK = "normal_blue_brick";
    public static final String NORMAL_GREEN_BRICK = "normal_green_brick";
    public static final String NORMAL_ORANGE_BRICK = "normal_orange_brick";
    public static final String NORMAL_PURPLE_BRICK = "normal_purple_brick";
    public static final String NORMAL_RED_BRICK = "normal_red_brick";
    public static final String NORMAL_YELLOW_BRICK = "normal_yellow_brick";

    public static final String SQUARE_BLUE_BRICK = "square_blue_brick";
    public static final String SQUARE_GREEN_BRICK = "square_green_brick";
    public static final String SQUARE_ORANGE_BRICK = "square_orange_brick";
    public static final String SQUARE_PURPLE_BRICK = "square_purple_brick";
    public static final String SQUARE_RED_BRICK = "square_red_brick";
    public static final String SQUARE_YELLOW_BRICK = "square_yellow_brick";

    public static final String THICK_BLUE_BRICK = "thick_blue_brick";
    public static final String THICK_GREEN_BRICK = "thick_green_brick";
    public static final String THICK_ORANGE_BRICK = "thick_orange_brick";
    public static final String THICK_PURPLE_BRICK = "thick_purple_brick";
    public static final String THICK_RED_BRICK = "thick_red_brick";
    public static final String THICK_YELLOW_BRICK = "thick_yellow_brick";

    /**
     * Brick dimensions and layout.
     */
    public static final int BRICK_WIDTH = 64;
    public static final int BRICK_HEIGHT = 32;
    public static final int BRICK_SPACING = 5;
    public static final int BRICK_START_X = 100;
    public static final int BRICK_START_Y = 100;
    public static final int BRICK_ROW_HEIGHT = BRICK_HEIGHT;
}
