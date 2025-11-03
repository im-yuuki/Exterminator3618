package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * Represents an indestructible brick that cannot be destroyed by the ball.
 * It acts as a permanent wall or obstacle.
 */
public class SolidBrick extends Brick {

    /**
     * Creates a new indestructible brick.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public SolidBrick(int x, int y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.THICK_BLUE_BRICK, 1, "solid_brick");
    }

    /**
     * This brick is indestructible.
     */
    @Override
    public boolean takeHit() {
        return false;
    }
}