package io.exterminator3618.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple moving ball that derives its velocity from a constant speed and an angle.
 */
public class Ball extends MovableObject {
    private static final Logger log = LoggerFactory.getLogger(Ball.class);
    
    /**
     * Angle in degrees.
     */
    private double angle;
    private int previousY;

    /**
     * Creates a new ball with constant speed from Constants.BALL_SPEED.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param regionName texture region name in the atlas
     * @param angle    angle in degrees
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
        super.update(deltaTime);
        handleScreenCollision();
    }

    /**
     * Handles collision with screen boundaries and makes the ball bounce.
     * Maintains constant speed after collision.
     */
    private void handleScreenCollision() {
        int screenWidth = Constants.WINDOW_WIDTH;
        int screenHeight = Constants.WINDOW_HEIGHT;
        boolean bounced = false;
        
        // check đụng tường
        if (getX() <= 0) {
            // tường trái
            setPosition(0, getY());
            velocityX = Math.abs(velocityX);
            bounced = true;
        } else if (getX() + getWidth() >= screenWidth) {
            // tưởng phải
            setPosition(screenWidth - getWidth(), getY());
            velocityX = -Math.abs(velocityX); //thêm abs cho chắc
            bounced = true;
        }
        
        // tường trên dưới
        if (getY() <= 0) {
            // tường dưới
            setPosition(getX(), 0);
            velocityY = Math.abs(velocityY);
            bounced = true;
        } else if (getY() + getHeight() >= screenHeight) {
            // tường trên
            setPosition(getX(), screenHeight - getHeight());
            velocityY = -Math.abs(velocityY);
            bounced = true;
        }
        
        // maintain const spid
        if (bounced) {
            normalizeVelocity();
        }

    }
    
    /**
     * Normalizes velocity to maintain constant speed.
     * Uses the original speed from Constants.BALL_SPEED for consistency.
     * Preserves the natural bounce angle while maintaining speed.
     */
    private void normalizeVelocity() {
        double currentSpeed = getCurrentSpeed();
        if (currentSpeed > 0) {
            // Use the original ball speed from constants
            double targetSpeed = Constants.BALL_SPEED;
            double normalizedVx = velocityX / currentSpeed * targetSpeed;
            double normalizedVy = velocityY / currentSpeed * targetSpeed;
            setVelocity(normalizedVx, normalizedVy);
            
            // Log bounce event with angle and speed
            double newAngle = Math.toDegrees(Math.atan2(normalizedVy, normalizedVx));
            double newSpeed = getCurrentSpeed();
            log.debug("Ball bounced! Angle: {}°, Speed: {}, Velocity: ({}, {})",
                String.format("%.1f", newAngle), String.format("%.1f", newSpeed), 
                String.format("%.1f", normalizedVx), String.format("%.1f", normalizedVy));
        }
    }

    /**
     * Resets the ball to the center of the screen with the original speed and angle.
     */
    public void resetToCenter() {
        int centerX = Constants.WINDOW_WIDTH / 2 - getWidth() / 2;
        int centerY = Constants.WINDOW_HEIGHT / 2 - getHeight() / 2;
        setPosition(centerX, centerY);
        updateVelocity(); // Reset to original speed and angle
        //log.info("Ball reset to center at ({}, {})", centerX, centerY);
    }

    /**
     * Gets the current speed magnitude.
     * @return current speed
     */
    public double getCurrentSpeed() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    /**
     * Checks if this ball collides with a brick.
     * Uses optimized AABB (Axis-Aligned Bounding Box) collision detection.
     * 
     * @param brick the brick to check collision with
     * @return true if colliding, false otherwise
     */
    public boolean collidesWith(Brick brick) {
        // Early exit if brick is destroyed
        if (brick.isDestroyed()) {
            return false;
        }
        
        // Optimized AABB collision detection with early exits
        int ballLeft = getX();
        int ballRight = ballLeft + getWidth();
        int ballTop = getY() + getHeight();
        int ballBottom = getY();
        
        int brickLeft = brick.getX();
        int brickRight = brickLeft + brick.getWidth();
        int brickTop = brick.getY() + brick.getHeight();
        int brickBottom = brick.getY();
        
        // Check for separation on each axis - if separated on any axis, no collision
        return !(ballRight <= brickLeft || ballLeft >= brickRight || 
                 ballTop <= brickBottom || ballBottom >= brickTop);
    }


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
     * outgoing angle based on hit position.
     */
    public void handlePaddleCollision(Paddle paddle) {
        // chỉ xử lý nếu đi xuống
        if (velocityY < 0) {
            // resolve overlap
            int newY = paddle.getY() + paddle.getHeight();
            setPosition(getX(), newY);

            // set hit position to be in range [-1, 1]
            double ballCenterX = getX() + getWidth() / 2.0;
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
            double halfWidth = paddle.getWidth() / 2.0;
            double offset = (ballCenterX - paddleCenterX) / halfWidth;
            if (offset < -1) offset = -1;
            if (offset > 1) offset = 1;

            // Map offset to an angle relative to vertical (straight up)
            // 0 => straight up, -1 => max left, 1 => max right
            double maxBounceFromVerticalDeg = 75.0; // cap to avoid too-horizontal
            double angleFromVertical = offset * maxBounceFromVerticalDeg;

            // ổn định speed
            double radiansFromVertical = Math.toRadians(angleFromVertical);
            double speed = Constants.BALL_SPEED;
            double vx = speed * Math.sin(radiansFromVertical);
            double vy = speed * Math.cos(radiansFromVertical);
            // Ensure going upwards
            if (vy < 0) vy = -vy;

            setVelocity(vx, vy);
            normalizeVelocity();

            double newAngle = Math.toDegrees(Math.atan2(velocityY, velocityX));
            log.debug("Ball hit paddle! Offset: {}, Angle: {}°, Velocity: ({}, {})",
                String.format("%.2f", offset), String.format("%.1f", newAngle),
                String.format("%.1f", velocityX), String.format("%.1f", velocityY));
        }
    }

    /**
     * Optimized single-step swept-Y collision test against paddle top and bounce.
     * Call this AFTER ball.update(deltaTime).
     * @return true if a bounce was applied
     */
    public boolean checkPaddleCollision(Paddle paddle) {
        // Only when moving downward
        if (velocityY >= 0) return false;

        // Broad-phase X overlap first
        int padLeft = paddle.getX();
        int padRight = padLeft + paddle.getWidth();
        int ballLeft = getX();
        int ballRight = ballLeft + getWidth();
        if (ballRight <= padLeft || ballLeft >= padRight) return false;

        // Swept Y: did the ball bottom cross the paddle top this frame?
        int paddleTop = paddle.getY() + paddle.getHeight();
        int prevBottom = previousY;
        int currBottom = getY();
        if (prevBottom >= paddleTop && currBottom <= paddleTop) {
            handlePaddleCollision(paddle);
            return true;
        }

        int ballTop = currBottom + getHeight();
        int padBottom = paddle.getY();
        boolean overlappingY = !(ballTop <= padBottom || currBottom >= paddleTop);
        if (overlappingY) {
            handlePaddleCollision(paddle);
            return true;
        }
        return false;
    }


    /**
     * Handles collision with a brick by reversing appropriate velocity component.
     * Determines which side of the brick was hit and bounces accordingly.
     * 
     * @param brick the brick that was hit
     */
    public void handleBrickCollision(Brick brick) {
        // Current bounds
        int ballLeft = getX();
        int ballRight = ballLeft + getWidth();
        int ballBottom = getY();
        int ballTop = ballBottom + getHeight();

        int brickLeft = brick.getX();
        int brickRight = brickLeft + brick.getWidth();
        int brickBottom = brick.getY();
        int brickTop = brickBottom + brick.getHeight();

        // Compute overlap depths on both axes (strictly positive because we already detected collision)
        int overlapX = Math.min(ballRight, brickRight) - Math.max(ballLeft, brickLeft);
        int overlapY = Math.min(ballTop, brickTop) - Math.max(ballBottom, brickBottom);

        // Decide resolution axis by minimal penetration to avoid corner flipping issues
        if (overlapX < overlapY) {
            // Resolve along X axis
            double ballCenterX = ballLeft + getWidth() / 2.0;
            double brickCenterX = brickLeft + brick.getWidth() / 2.0;

            if (ballCenterX < brickCenterX) {
                // Collided with brick's left face
                setPosition(brickLeft - getWidth(), getY());
                velocityX = -Math.abs(velocityX);
            } else {
                // Collided with brick's right face
                setPosition(brickRight, getY());
                velocityX = Math.abs(velocityX);
            }
        } else {
            // Resolve along Y axis
            double ballCenterY = ballBottom + getHeight() / 2.0;
            double brickCenterY = brickBottom + brick.getHeight() / 2.0;

            if (ballCenterY < brickCenterY) {
                // Collided with brick's bottom face
                setPosition(getX(), brickBottom - getHeight());
                velocityY = -Math.abs(velocityY);
            } else {
                // Collided with brick's top face
                setPosition(getX(), brickTop);
                velocityY = Math.abs(velocityY);
            }
        }

        // Maintain constant speed after collision
        normalizeVelocity();

        log.debug("Ball hit brick! New velocity: ({}, {})",
            String.format("%.1f", velocityX), String.format("%.1f", velocityY));
    }

}