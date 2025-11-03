package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

/**
 * Power-up that makes the paddle wider when collected.
 * This is a concrete implementation of the abstract PowerUp class.
 */
public class WidenPaddlePowerUp extends PowerUp {

    public WidenPaddlePowerUp(int x, int y) {
        super("Widen Paddle", 10.0f, x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "expand_paddle_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            gameScreen.getPaddle().extend();
        }
    }

    @Override
    public void removeEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            gameScreen.getPaddle().shrink();
        }
    }
}
