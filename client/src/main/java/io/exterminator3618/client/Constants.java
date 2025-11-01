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
     * Audio file paths.
     */
    public static final String TEST_LONG_MUSIC_2 = "sound/test_bgr2.wav";
    public static final String BACKGROUND_MUSIC = "sound/test_bgr.mp3";
    public static final String BUFF_SOUND = "sound/buff_sound.mp3";

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

    public static final String MULTIBALL_BRICK = "normal_purple_brick";
    /**
     * Brick dimensions and layout.
     */
    public static final int BRICK_WIDTH = 64;
    public static final int BRICK_HEIGHT = 32;
    public static final int BRICK_SPACING = 5;
    public static final int BRICK_START_X = 80;
    public static final int BRICK_START_Y = 100;
    public static final int BRICK_ROW_HEIGHT = BRICK_HEIGHT;

    /**
     * Paddle dimensions and layout.
     */
    public static final int PADDLE_WIDTH = 150;
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_START_X = WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2;
    public static final int PADDLE_START_Y = 100;
    public static final String PADDLE_REGION_NAME = "red_paddle";
    public static final int PADDLE_SPEED = 900;

    public enum GameState {
        MENU,
        PLAYING,
        PAUSED,
        GAME_OVER,
        VICTORY,
        LOSE
    }

    /**
     * FOR POWERUP
     */
    public static final int POWERUP_WIDTH = 32;
    public static final int POWERUP_HEIGHT = 32;
    public static final double POWERUP_FALL_SPEED = 1000.0;
    // PowerUp texture regions
    public static final String POWERUP_BALLS_FROM_PADDLE = "big_purple_ball"; //KHÔNG CÓ ASSET POWERUP, DÙNG TẠM
    
    // PowerUp duration constants
    public static final float WIDEN_PADDLE_DURATION = 5.0f; // 5 seconds

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

}
