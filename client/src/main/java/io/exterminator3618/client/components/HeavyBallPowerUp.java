package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

/**
 * Power-up that makes the ball destroy all bricks it passes through without bouncing for 3 seconds.
 * This is a concrete implementation of the abstract PowerUp class.
 */
public class HeavyBallPowerUp extends PowerUp {

    public HeavyBallPowerUp(int x, int y) {
        super("heavy_ball_power_up", 5.0f, x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "heavy_ball_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            // Set heavy ball mode for main ball
            gameScreen.getBall().setHeavyBall(true);
            
            // Set heavy ball mode for all extra balls
            for (Ball extraBall : gameScreen.getExtraBalls()) {
                extraBall.setHeavyBall(true);
            }
        }
    }

    @Override
    public void removeEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            // Remove heavy ball mode from main ball
            gameScreen.getBall().setHeavyBall(false);
            
            // Remove heavy ball mode from all extra balls
            for (Ball extraBall : gameScreen.getExtraBalls()) {
                extraBall.setHeavyBall(false);
            }
        }
    }
}
