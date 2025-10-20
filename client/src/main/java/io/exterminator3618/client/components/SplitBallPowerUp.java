package io.exterminator3618.client.components;

import java.util.ArrayList;
import java.util.List;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

public class SplitBallPowerUp extends PowerUp {

    public SplitBallPowerUp(int x, int y) {
        super("split_ball_power_up", 0.0f , x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "split_ball_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        if (gameScreen != null) {
            Ball mainBall = gameScreen.getBall();
            gameScreen.spawnExtraBalls(mainBall.getX(), mainBall.getY());
        }

        // Also spawn from every extra ball currently on screen
        List<Ball> extraBall = new ArrayList<>(gameScreen.getExtraBalls());
        for (Ball b : extraBall) {
            gameScreen.spawnExtraBalls(b.getX(), b.getY());
        }
    }

    @Override
    public void removeEffect(GameScreen gameScreen) {
        // No-op: this is an instant effect power-up
    }

}
