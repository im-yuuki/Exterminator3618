package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

public class SlowBallPowerUp extends PowerUp {

    public SlowBallPowerUp(int x, int y) {
        super("slow_ball_power_up", 20.0f, x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "slow_ball_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            //gameScreen.getBall().setSpeedMultiplier(0.5f);
            gameScreen.getBall().setBallSpeed(0.5f);
            gameScreen.getBall().updateVelocity();
        }
    }
    @Override
    public void removeEffect (GameScreen gameScreen){
        if (gameScreen != null) {
            //gameScreen.getBall().setSpeedMultiplier(1.0f);
            gameScreen.getBall().setBallSpeed(2f);
            gameScreen.getBall().updateVelocity();
        }
    }
}