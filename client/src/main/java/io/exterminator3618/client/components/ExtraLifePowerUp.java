package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.screens.GameScreen;

public class ExtraLifePowerUp extends PowerUp {

    public ExtraLifePowerUp(int x, int y) {
        super("Extra Life", 0.0f , x, y, Constants.POWERUP_WIDTH, Constants.POWERUP_HEIGHT, "extra_life_power_up");
    }

    @Override
    public void applyEffect(GameScreen gameScreen) {
        gameScreen.setLives(gameScreen.getLives() + 1);
    }

    @Override
    public void removeEffect(GameScreen gameScreen) {
    }
}
