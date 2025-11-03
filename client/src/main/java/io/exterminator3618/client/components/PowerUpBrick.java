package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * Represents a special brick that grants a power-up when destroyed.
 * For now, it makes the paddle longer.
 */
public class PowerUpBrick extends Brick {

    /**
     * Creates a new PowerUpBrick.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public PowerUpBrick(int x, int y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.NORMAL_YELLOW_BRICK, 1, "powerup_brick");
    }
}