package io.exterminator3618.client.components;

/**
 * A GameObject that can move with a constant velocity.
 * Positions are updated every frame based on velocity without friction.
 */
public abstract class MovableObject extends GameObject {

    /**
     * Current horizontal velocity.
     */
    public double velocityX;
    /**
     * Current vertical velocity.
     */
    public double velocityY;

    /**
     * Creates a stationary movable object.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param filepath texture resource path
     */
    public MovableObject(int x, int y, int width, int height, String filepath) {
        super(x, y, width, height, filepath);
        this.velocityX = 0;
        this.velocityY = 0;
    }


    /**
     * Sets the velocity. Add thresholds to avoid drift.
     *
     * @param vx horizontal velocity
     * @param vy vertical velocity
     */
    public void setVelocity(double vx, double vy) {
        if (Math.abs(vx) < 0.01) {
            this.velocityX = 0;
        } else {
            this.velocityX = vx;
        }

        if (Math.abs(vy) < 0.01) {
            this.velocityY = 0;
        } else {
            this.velocityY = vy;
        }
    }

    /**
     * @return current horizontal velocity
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * @return current vertical velocity
     */
    public double getVelocityY() {
        return velocityY;
    }


    /**
     * Updates the position according to the current velocity.
     * đéo cần friction.
     *
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
        int newX = getX() + (int) (velocityX * deltaTime);
        int newY = getY() + (int) (velocityY * deltaTime);
        setPosition(newX, newY);
    }

}
