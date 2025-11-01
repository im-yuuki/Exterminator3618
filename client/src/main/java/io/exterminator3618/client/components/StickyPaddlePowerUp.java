package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

/**
 * Power-up that makes the ball stick to the paddle when it hits, press space to launch with random angle 45-130°.
 * This is a concrete implementation of the abstract PowerUp class.
 */
public class StickyPaddlePowerUp extends PowerUp {

    public StickyPaddlePowerUp(int x, int y) {
        super("Sticky Paddle", 10.0f, x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "sticky_paddle_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            // Enable sticky paddle mode
            gameScreen.getPaddle().setSticky(true);
        }
    }

    @Override
    public void removeEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            // Disable sticky paddle mode
            gameScreen.getPaddle().setSticky(false);
        }
    }
}
