package io.exterminator3618.client;

/**
 * StrongBrick represents a durable brick that requires multiple hits to destroy.
 * This brick type is harder to break and provides more challenge in the game.
 */
public class StrongBrick extends Brick {

    /**
     * Default hit points for strong bricks
     */
    private static final int DEFAULT_STRONG_BRICK_HITPOINTS = 3;

    /**
     * Creates a new strong brick with default hit points.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     */
    public StrongBrick(int x, int y, int width, int height, String regionName) {
        super(x, y, width, height, regionName, DEFAULT_STRONG_BRICK_HITPOINTS, "strong");
    }

    /**
     * Creates a new strong brick with custom hit points.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     * @param hitPoints  custom hit points for this brick
     */
    public StrongBrick(int x, int y, int width, int height, String regionName, int hitPoints) {
        super(x, y, width, height, regionName, hitPoints, "strong");
    }

    /**
     * Creates a new strong brick with default dimensions.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public StrongBrick(int x, int y) {
        //this(x, y, 64, 32, "strong_brick", DEFAULT_STRONG_BRICK_HITPOINTS);
        this(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.NORMAL_ORANGE_BRICK, DEFAULT_STRONG_BRICK_HITPOINTS);
    }

    /**
     * Updates the strong brick state.
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
        // Strong bricks don't have special behavior
        super.update(deltaTime);
    }

    /**
     * Gets the default hit points for strong bricks.
     * @return default hit points
     */
    public static int getDefaultHitPoints() {
        return DEFAULT_STRONG_BRICK_HITPOINTS;
    }
}
