package io.exterminator3618.client.components;


import io.exterminator3618.client.Constants;
import io.exterminator3618.client.utils.Physics;

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
    private int stuckOffsetX = 0; // Offset từ giữa paddle đến vị trí dính
    private int comboCount = 0;
    private float speedMultiplier = 1.0f;
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
    public void updateVelocity() {
        double radians = Math.toRadians(angle);
        double vx = Constants.BALL_SPEED * Math.cos(radians) * speedMultiplier;
        double vy = Constants.BALL_SPEED * Math.sin(radians) * speedMultiplier;
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
        stuckOffsetX = 0; // Reset offset to center
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
     * Sets the offset from paddle center where the ball is stuck.
     *
     * @param offsetX offset in pixels from paddle center
     */
    public void setStuckOffsetX(int offsetX) {
        this.stuckOffsetX = offsetX;
    }

    /**
     * Gets the offset from paddle center where the ball is stuck.
     *
     * @return offset in pixels from paddle center
     */
    public int getStuckOffsetX() {
        return stuckOffsetX;
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
     * Launches the ball from the paddle with angle based on stuck position.
     */
    public void launchFromPaddle() {
        if (isStuckToPaddle) {
            // Calculate angle based on stuck position (similar to bounce logic)
            double offset = (double) stuckOffsetX / (Constants.PADDLE_WIDTH / 2.0);
            
            // Clamp offset to valid range [-1, 1]
            offset = Math.max(-1.0, Math.min(1.0, offset));
            
            // Map offset to bounce angle (same as Physics.handleBallPaddleCollision)
            double angleFromVertical = offset * Constants.MAX_BOUNCE_ANGLE;
            
            // Convert to standard angle (0 = right, 90 = up, 180 = left, 270 = down)
            // We want upward direction, so we need to adjust the angle
            double radiansFromVertical = Math.toRadians(angleFromVertical);
            double vx = Constants.BALL_SPEED * Math.sin(radiansFromVertical);
            double vy = Constants.BALL_SPEED * Math.cos(radiansFromVertical);
            
            // Ensure ball always goes upward
            if (vy < 0) vy = -vy;
            
            // Calculate angle from standard coordinate system
            this.angle = Math.toDegrees(Math.atan2(vy, vx));
            if (this.angle < 0) this.angle += 360;
            
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

    public void resetCombo() {
        comboCount = 0;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        updateVelocity();
    }
    public void setBallSpeed(float speedMultiplier) {
        Constants.BALL_SPEED *= speedMultiplier;
        //updateVelocity();
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

}