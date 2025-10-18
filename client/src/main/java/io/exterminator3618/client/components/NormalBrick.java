package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * NormalBrick represents a standard brick that is destroyed after one hit.
 * This is the most common type of brick in the arkanoid game.
 */
public class NormalBrick extends Brick {

    /**
     * Creates a new normal brick.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     */
    public NormalBrick(int x, int y, int width, int height, String regionName) {
        super(x, y, width, height, regionName, 1, "normal");
    }

    /**
     * Creates a new normal brick with default dimensions.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     */
    public NormalBrick(int x, int y) {
        //this(x, y, 64, 32, "normal_brick"); // Default brick size and texture
        this(x,y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.NORMAL_GREEN_BRICK);
    }

    /**
     * Updates the normal brick state.
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
        // Normal bricks don't have special behavior
        super.update(deltaTime);
    }
}
