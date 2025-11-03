package io.exterminator3618.client.components;

import static java.lang.Math.random;

import io.exterminator3618.client.screens.GameScreen;
import static io.exterminator3618.client.Constants.POWERUP_FALL_SPEED;

public abstract class PowerUp extends MovableObject {
    private String type;
    private float duration;
    private float remainingDuration;
    private final float fallSpeed = POWERUP_FALL_SPEED;

    public PowerUp(String type, float duration, int x, int y, int width, int height, String regionName) {
        super(x, y, width, height, regionName);
        this.type = type;
        this.duration = duration;
        this.remainingDuration = duration;
   }

   public void decreaseDuration(float deltaTime) {
        if (remainingDuration > 0.0f) {
            remainingDuration -= deltaTime;
        }
    }

    public boolean isExpired() {
        return remainingDuration <= 0.0f;
    }

    public boolean isInstant() {
        return duration <= 0.0f;
    }

    public void resetRemainingDuration() {
        this.remainingDuration = this.duration;
    }

    public float getRemainingDuration() {
        return remainingDuration;
    }

    public float getDuration() {
        return duration;
    }

    public void setRemainingDuration(float duration) {
        this.remainingDuration = duration;
    }

    public abstract void applyEffect(GameScreen screen);
    public abstract void removeEffect(GameScreen screen);

    @Override
    public void update(float deltaTime) {
        int newY = getY() - (int) (fallSpeed * deltaTime);
        setPosition(getX(), newY);
    }

    public static PowerUp createRandomPowerUp(int x, int y){
        switch ((int) (random() * 6)) {
            case 0:
                return new WidenPaddlePowerUp(x, y);
            case 1:
                return new HeavyBallPowerUp(x, y);
            case 2:
                return new StickyPaddlePowerUp(x, y);
            case 3:
                return new ExtraLifePowerUp(x, y);
            case 4:
                return new SplitBallPowerUp(x, y);
            case 5:
                return new SlowBallPowerUp(x, y);
            default:
                return null;
        }
    }

    public String getType() {
        return type;
    }

}



