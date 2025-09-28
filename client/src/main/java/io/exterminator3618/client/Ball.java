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
            log.info("Ball bounced! Angle: {}°, Speed: {}, Velocity: ({}, {})", 
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

}