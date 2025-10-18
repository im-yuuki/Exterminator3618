package io.exterminator3618.client.components;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Physics;

public abstract class PowerUp extends MovableObject {

    /**
     * Creates a power-up that falls down with constant speed.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param filepath texture resource path
     */
    public PowerUp(int x, int y, int width, int height, String filepath) {
        super(x, y, width, height, filepath);
        // Set power-up to fall down with constant speed
        setVelocity(0, -Constants.POWERUP_FALL_SPEED);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Check if power-up has fallen out of screen bounds.
     *
     * @return true if power-up is below screen
     */
    public boolean isOutOfBounds() {
        return getY() < -getHeight();
    }

    /**
     * to define what happens when power-up is collected.
     * each power-up class must implement this.
     */

    public boolean checkPaddleCollision(Paddle paddle) {
        boolean collision = Physics.checkPaddlePowerUpCollision(paddle, this);
        if (collision) {
            active(paddle);
        }
        return collision;
    }

    public abstract void active(Paddle paddle);

}
