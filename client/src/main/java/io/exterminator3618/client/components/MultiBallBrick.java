package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * Represents a special brick that spawns multiple new balls when destroyed.
 */
public class MultiBallBrick extends Brick {

    /**
     * Creates a new MultiBallBrick.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     */
    public MultiBallBrick(int x, int y, int width, int height, String regionName) {
        // This brick is destroyed in one hit (hitPoints=1) and has a special type.
        super(x, y, width, height, regionName, 1, "multiball");
    }

    /**
     * Creates a new MultiBallBrick with default dimensions and texture.
     * For simplicity, we can reuse an existing texture or create a new one.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public MultiBallBrick(int x, int y) {
        this(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.MULTIBALL_BRICK);
    }
}