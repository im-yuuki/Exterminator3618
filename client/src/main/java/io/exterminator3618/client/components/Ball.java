package io.exterminator3618.client.components;


import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Physics;

/**
 * A simple moving ball that derives its velocity from a constant speed and an angle.
 */
public class Ball extends MovableObject {

    /**
     * Angle in degrees.
     */
    private double angle;
    private int previousY;
    private boolean isHeavyBall = false;
    private boolean isStuckToPaddle = false;
    private int comboCount = 0;
    /**
     * Creates a new ball with constant speed from Constants.BALL_SPEED.
     *
     * @param x          initial X position
     * @param y          initial Y position
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName texture region name in the atlas
     * @param angle      angle in degrees
     */
    public Ball(int x, int y, int width, int height, String regionName, double speed, double angle) {
        super(x, y, width, height, regionName);
        this.angle = angle;
        updateVelocity();
    }

    /**
     * Recomputes velocity components based on current speed and angle.
     */
    private void updateVelocity() {
        double radians = Math.toRadians(angle);
        double vx = Constants.BALL_SPEED * Math.cos(radians);
        double vy = Constants.BALL_SPEED * Math.sin(radians);
        setVelocity(vx, vy);
    }

    /**
     * Updates the ball by MovableObject update().
     *
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
        // prevY để kiểm tra nó có bị bounce 2 lần ko, chống bug
        previousY = getY();

        // If ball is stuck to paddle, don't update position
        if (!isStuckToPaddle) {
            super.update(deltaTime);
            Physics.handleScreenCollision(this);
        }
    }

    /**
     * Resets the ball to the middle of the paddle.
     */
    public void resetToCenter(Paddle paddle) {
        setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() + getWidth() / 2);
        isStuckToPaddle = true;
        setVelocity(0,0);
    }

    /**
     * Gets the current speed magnitude.
     *
     * @return current speed
     */
    public double getCurrentSpeed() {
        return Physics.getBallCurrentSpeed(this);
    }

    /**
     * if this ball collides with a brick.
     *
     * @param brick the brick to check collision with
     * @return true if colliding, false otherwise
     */
    public boolean collidesWith(Brick brick) {
        return Physics.checkBallBrickCollision(this, brick);
    }

    //OLD COLLISION CHECKING METHOD
    // /**
    //  * check collision.
    //  * @param paddle paddle to test
    //  * @return true if overlapping
    //  */
    // public boolean collidesWith(Paddle paddle) {
    //     int ballLeft = getX();
    //     int ballRight = ballLeft + getWidth();
    //     int ballBottom = getY();
    //     int ballTop = ballBottom + getHeight();
    //
    //     int padLeft = paddle.getX();
    //     int padRight = padLeft + paddle.getWidth();
    //     int padBottom = paddle.getY();
    //     int padTop = padBottom + paddle.getHeight();
    //
    //     return !(ballRight <= padLeft || ballLeft >= padRight ||
    //              ballTop <= padBottom || ballBottom >= padTop);
    // }

    /**
     * Handle collision when ball center bottom hits paddle top.
     * Delegates to Physics class for collision handling.
     */
    public void handlePaddleCollision(Paddle paddle) {
        Physics.handleBallPaddleCollision(this, paddle);
    }

    /**
     * center bottom of ball with top of paddle.
     * Delegates to Physics class for collision detection.
     * Call this AFTER ball.update(deltaTime).
     *
     * @return true if a bounce was applied
     */
    public boolean checkPaddleCollision(Paddle paddle) {
        boolean collision = Physics.checkBallPaddleCollision(this, paddle, previousY);
        if (collision) {
            handlePaddleCollision(paddle);
        }
        return collision;
    }


    /**
     * Physics class for collision handling.
     *
     * @param brick the brick that was hit
     */
    public void handleBrickCollision(Brick brick) {
        Physics.handleBallBrickCollision(this, brick);
    }

    /**
     * Sets the heavy ball mode for this ball.
     * When heavy ball is active, the ball destroys bricks without bouncing.
     *
     * @param heavyBall true to enable heavy ball mode, false to disable
     */
    public void setHeavyBall(boolean heavyBall) {
        this.isHeavyBall = heavyBall;
    }

    /**
     * Gets whether this ball is in heavy ball mode.
     *
     * @return true if heavy ball mode is active, false otherwise
     */
    public boolean isHeavyBall() {
        return isHeavyBall;
    }

    /**
     * Sets whether this ball is stuck to the paddle.
     *
     * @param stuck true if stuck to paddle, false otherwise
     */
    public void setStuckToPaddle(boolean stuck) {
        this.isStuckToPaddle = stuck;
    }

    /**
     * Gets whether this ball is stuck to the paddle.
     *
     * @return true if stuck to paddle, false otherwise
     */
    public boolean isStuckToPaddle() {
        return isStuckToPaddle;
    }

    /**
     * Launches the ball from the paddle with a random angle between 45-130 degrees.
     */
    public void launchFromPaddle() {
        if (isStuckToPaddle) {
            // Generate random angle between 45-135 degrees
            double randomAngle = 45 + Math.random() * (135 - 45);
            this.angle = randomAngle;
            updateVelocity();
            setStuckToPaddle(false);
        }
    }

    public void incrementCombo() {
        comboCount++;
    }

    public int getComboCount() {
        return comboCount;
    }

}