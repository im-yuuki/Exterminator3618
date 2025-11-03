package io.exterminator3618.client;

/**
 * Client configuration constants and default values
 * used throughout the Exterminator3618 client application.
 */
public class Constants {

    /**
     * Window configuration.
     */
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;


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
    public static final String EXTRA_BALL_REGION_NAME = "blue_ball";
    /**
     * Default ball speed.
     */
    public static double BALL_SPEED = 1000.0;
    /**
     * Ball collision tolerance.
     */
    public static final double MAX_BOUNCE_ANGLE = 65;
    public static final double BALL_EPSILON = 50;

    /**
     * Path to backgounds
     */
    public static final String MENU_BACKGROUND_PATH = "assets/Background_Tiles.png";
    public static final String GAME_BACKGROUND_PATH = "assets/gameScreen.png";
    /**
     * Path to the texture atlas file.
     */
    public static final String ATLAS_PATH = "assets/textures.atlas";
    public static final String UI_PATH = "assets/uitextures.atlas";
    /**
     * Brick texture region names.
     */
    public static final String NORMAL_GREEN_BRICK = "normal_green_brick";
    public static final String NORMAL_ORANGE_BRICK = "normal_orange_brick";
    public static final String NORMAL_YELLOW_BRICK = "normal_yellow_brick";
    public static final String THICK_BLUE_BRICK = "thick_blue_brick";
    public static final String MULTIBALL_BRICK = "normal_purple_brick";
    /**
     * Brick dimensions and layout.
     */
    public static final int BRICK_WIDTH = 64;
    public static final int BRICK_HEIGHT = 32;
    public static final int BRICK_SPACING = 5;
    public static final int BRICK_START_X = 80;
    public static final int BRICK_START_Y = 120;

    /**
     * Paddle dimensions and layout.
     */
    public static final int PADDLE_WIDTH = 150;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_START_X = WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2;
    public static final int PADDLE_START_Y = 100;
    public static final String PADDLE_REGION_NAME = "red_paddle";

    /**
     * FOR POWERUP
     */
    public static final int POWERUP_WIDTH = 32;
    public static final int POWERUP_HEIGHT = 32;
    public static final float POWERUP_FALL_SPEED = 200.0f;

    public static final String BUTTON_LEFT_REGION = "tile_82";
    public static final String BUTTON_MIDDLE_REGION = "tile_83";
    public static final String BUTTON_RIGHT_REGION = "tile_84";

    public static final String HEADER_LEFT_REGION = "tile_43";
    public static final String HEADER_MIDDLE_REGION = "tile_44";
    public static final String HEADER_RIGHT_REGION = "tile_45";

    public static int BUTTON_WIDTH = 300;
    public static int BUTTON_HEIGHT = 75;

    /**
     * game borders
     */
    public static final int PLAY_AREA_X_MAX = 1410;
    public static final int PLAY_AREA_X_MIN = 55;
    public static final int PLAY_AREA_Y_MAX = 985;
    public static final int PLAY_AREA_Y_MIN = 85;

    /**
     * Number of game levels
     */
    public static final int Level = 3;

    public static final int POLL_INTERVAL_MS = 3000;
}
