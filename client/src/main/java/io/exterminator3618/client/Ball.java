package io.exterminator3618.client;

/**
 * A simple moving ball that derives its velocity from a speed and an angle.
 */
public class Ball extends MovableObject {
    /**
     * Initial speed.
     */
    private double speed;
    /**
     * Angle in degrees.
     */
    private double angle;

    /**
     * Creates a new ball.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param regionName texture region name in the atlas
     * @param speed    initial speed
     * @param angle    angle in degrees
     */
    public Ball(int x, int y, int width, int height, String regionName, double speed, double angle) {
        super(x, y, width, height, regionName);
        this.speed = speed;
        this.angle = angle;
        updateVelocity();
    }

    /**
     * Recomputes velocity components based on current speed and angle.
     */
    private void updateVelocity() {
        double radians = Math.toRadians(angle);
        double vx = speed * Math.cos(radians);
        double vy = speed * Math.sin(radians);
        setVelocity(vx, vy);
    }

    /**
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed and updates velocity components.
     *
     * @param speed new speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
        updateVelocity();
    }

    /**
     * @return angle in degrees
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the angle and updates velocity.
     *
     * @param angle in degrees
     */
    public void setAngle(double angle) {
        this.angle = angle;
        updateVelocity();
    }

    /**
     * Updates the ball by MovableObject update().
     *
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}