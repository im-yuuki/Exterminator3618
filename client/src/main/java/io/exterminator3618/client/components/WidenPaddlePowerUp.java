package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;

/**
 * Power-up that makes the paddle wider when collected.
 * This is a concrete implementation of the abstract PowerUp class.
 */
public class WidenPaddlePowerUp extends PowerUp {
    public int sizeUp = 100;
    /**
     * WidenPaddlePowerUp.
     *
     * @param x initial X position in pixels
     * @param y initial Y position in pixels
     */
    public WidenPaddlePowerUp(int x, int y) {
        super(x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, 
              Constants.POWERUP_BALLS_FROM_PADDLE); // Using temporary texture
    }

    /**
     * Activates the widen paddle effect.
     * This method is called when the power-up is collected by the paddle.
     */
    @Override
    public void active(Paddle paddle) {
        // Use the new timer-based system instead of direct resize
        paddle.activateWidenPowerUp(Constants.WIDEN_PADDLE_DURATION);
    }
}
