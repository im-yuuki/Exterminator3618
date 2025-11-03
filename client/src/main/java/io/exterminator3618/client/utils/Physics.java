package io.exterminator3618.client.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.components.Ball;
import io.exterminator3618.client.components.Brick;
import io.exterminator3618.client.components.GameObject;
import io.exterminator3618.client.components.Paddle;
import io.exterminator3618.client.components.PowerUp;
import io.exterminator3618.client.components.SolidBrick;
import io.exterminator3618.client.screens.GameScreen;

/**
 * Physics engine for handling all collision detection and response in the game.
 * Centralizes collision logic for better maintainability and performance.
 */
public class Physics {
    private static final Logger log = LoggerFactory.getLogger(Physics.class);

    /**
     * Checks if two game objects are colliding using AABB (Axis-Aligned Bounding Box) collision detection.
     * 
     * @param obj1 first game object
     * @param obj2 second game object
     * @return true if objects are colliding, false otherwise
     */
    public static boolean checkAABBCollision(GameObject obj1, GameObject obj2) {
        // Early exit if either object is null
        if (obj1 == null || obj2 == null) {
            return false;
        }
        
        // Get bounds for object 1
        int obj1Left = obj1.getX();
        int obj1Right = obj1Left + obj1.getWidth();
        int obj1Top = obj1.getY() + obj1.getHeight();
        int obj1Bottom = obj1.getY();
        
        // Get bounds for object 2
        int obj2Left = obj2.getX();
        int obj2Right = obj2Left + obj2.getWidth();
        int obj2Top = obj2.getY() + obj2.getHeight();
        int obj2Bottom = obj2.getY();
        
        // Check for separation on each axis - if separated on any axis, no collision
        return !(obj1Right <= obj2Left || obj1Left >= obj2Right || 
                 obj1Top <= obj2Bottom || obj1Bottom >= obj2Top);
    }
    
    /**
     * Checks if ball collides with a brick using optimized AABB collision detection.
     * 
     * @param ball the ball to check
     * @param brick the brick to check collision with
     * @return true if colliding, false otherwise
     */
    public static boolean checkBallBrickCollision(Ball ball, Brick brick) {
        // Early exit if brick is destroyed
        if (brick.isDestroyed()) {
            return false;
        }
        
        return checkAABBCollision(ball, brick);
    }
    
    /**
     * Precise collision detection: only center bottom of ball with top of paddle.
     * 
     * @param ball the ball to check
     * @param paddle the paddle to check collision with
     * @param previousBallY previous Y position of ball for swept collision detection
     * @return true if ball center bottom hits paddle top, false otherwise
     */
    public static boolean checkBallPaddleCollision(Ball ball, Paddle paddle, int previousBallY) {
        // Only when ball is moving downward
        if (ball.getVelocityY() >= 0) return false;

        // Get ball center bottom point
        int ballCenterX = ball.getX() + ball.getWidth() / 2;
        int ballBottomY = ball.getY();
        
        // Get paddle top surface
        int paddleTopY = paddle.getY() + paddle.getHeight();
        int paddleLeft = paddle.getX() - ball.getWidth();
        int paddleRight = paddle.getX() + paddle.getWidth() + ball.getWidth();
        
        // Check if ball center bottom is horizontally within paddle bounds
        if (ballCenterX < paddleLeft || ballCenterX > paddleRight) {
            return false;
        }

        // Swept collision: did the ball center bottom cross the paddle top this frame?
        if (previousBallY >= paddleTopY && ballBottomY <= paddleTopY) {
            return true;
        }
        
        // Also check if ball center bottom is currently touching paddle top
        if (ballBottomY == paddleTopY) {
            return true;
        }
        
        return false;
    }

    public static void handleScreenCollision(Ball ball) {
        int screenWidth = Constants.WINDOW_WIDTH;
        int screenHeight = Constants.WINDOW_HEIGHT;
        boolean bounced = false;
        //double epsilon = Constants.BALL_EPSILON;
        
        // Check wall collisions
        if (ball.getX() <= Constants.PLAY_AREA_X_MIN) {
            // Left wall
            ball.setPosition(Constants.PLAY_AREA_X_MIN, ball.getY());
            ball.velocityX = Math.abs(ball.velocityX);
            bounced = true;
        } else if (ball.getX() + ball.getWidth() >= Constants.PLAY_AREA_X_MAX) {
            // Right wall
            ball.setPosition(Constants.PLAY_AREA_X_MAX - ball.getWidth(), ball.getY());ball.velocityX = -Math.abs(ball.velocityX);
            bounced = true;
        }
        
        // Check top and bottom walls
        if (ball.getY() <= Constants.PLAY_AREA_Y_MIN) {
            // Bottom wall
        } else if (ball.getY() + ball.getHeight() >= Constants.PLAY_AREA_Y_MAX) {
            // Top wall
            ball.setPosition(ball.getX(), Constants.PLAY_AREA_Y_MAX - ball.getHeight());
            ball.velocityY = -Math.abs(ball.velocityY);
            bounced = true;
        }
        
        // Add small random deviation to prevent infinite 90-degree bouncing
        if (bounced) {
            // không cần
            // double randomDeviationX = (Math.random() - 0.5) * epsilon * 2; // Range: [-epsilon, epsilon]
            // double randomDeviationY = (Math.random() - 0.5) * epsilon * 2; // Range: [-epsilon, epsilon]
            //
            // // Apply deviation to velocity components
            // ball.velocityX += randomDeviationX;
            // ball.velocityY += randomDeviationY;
            //
            // Maintain constant speed after collision
            normalizeBallVelocity(ball);

        }
    }
    
    /**
     * Handles collision when ball center bottom hits paddle top.
     * Calculates bounce angle based on hit position on paddle.
     * Handles sticky paddle behavior when enabled.
     * Adds small random deviation to prevent infinite 90-degree bouncing.
     * 
     * @param ball the ball that hit the paddle
     * @param paddle the paddle that was hit
     */
    public static void handleBallPaddleCollision(Ball ball, Paddle paddle) {
        // Only process if ball is moving downward
        if (ball.getVelocityY() < 0) {
            // Position ball so its center bottom is exactly on paddle top
            int paddleTopY = paddle.getY() + paddle.getHeight();
            ball.setPosition(ball.getX(), paddleTopY);

            // Check if paddle is sticky
            if (paddle.isSticky()) {
                // Calculate offset
                double ballCenterX = ball.getX() + ball.getWidth() / 2.0;
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
                int offsetX = (int) (ballCenterX - paddleCenterX);
                
                ball.setStuckToPaddle(true);
                ball.setStuckOffsetX(offsetX);
                ball.setVelocity(0, 0); // Stop the ball
                return;
            }

            // Calculate hit position relative to paddle center [-1, 1]
            double ballCenterX = ball.getX() + ball.getWidth() / 2.0;
            double paddleCenterX = paddle.getX() + paddle.getWidth() / 2.0;
            double halfWidth = paddle.getWidth() / 2.0;
            double offset = (ballCenterX - paddleCenterX) / halfWidth;
            
            // Clamp offset to valid range
            offset = Math.max(-1.0, Math.min(1.0, offset));

            // Add small random deviation to prevent infinite 90-degree bouncing
            //ĐANG NỔ
            // double epsilon = Constants.BALL_EPSILON;
            // double randomDeviation = (Math.random() - 0.5) * epsilon * 2; // Range: [-epsilon, epsilon]
            // offset += randomDeviation;
            //
            // // Re-clamp after adding deviation
            // offset = Math.max(-1.0, Math.min(1.0, offset));

            // Map offset to bounce angle
            // 0 => straight up, -1 => max left, 1 => max right
            double angleFromVertical = offset * Constants.MAX_BOUNCE_ANGLE;

            // Calculate new velocity
            double radiansFromVertical = Math.toRadians(angleFromVertical);
            double speed = Constants.BALL_SPEED;
            double vx = speed * Math.sin(radiansFromVertical);
            double vy = speed * Math.cos(radiansFromVertical);
            
            // Ensure ball always goes upward
            if (vy < 0) vy = -vy;

            ball.setVelocity(vx, vy);
            normalizeBallVelocity(ball);

            double newAngle = Math.toDegrees(Math.atan2(ball.getVelocityY(), ball.getVelocityX()));
            // log.debug("Ball center bottom hit paddle top! Offset: {}, Deviation: {}, Angle: {}°, Velocity: ({}, {})",
            //     String.format("%.2f", offset - randomDeviation), String.format("%.2f", randomDeviation),
            //     String.format("%.1f", newAngle), String.format("%.1f", ball.getVelocityX()),
            //     String.format("%.1f", ball.getVelocityY()));
        }
    }
    
    /**
     * Handles collision with a brick by reversing appropriate velocity component.
     * Determines which side of the brick was hit and bounces accordingly.
     * For heavy ball mode, the ball passes through bricks without bouncing.
     * Adds small random deviation to prevent infinite 90-degree bouncing.
     * 
     * @param ball the ball that hit the brick
     * @param brick the brick that was hit
     */
    public static void handleBallBrickCollision(Ball ball, Brick brick) {
        // If ball is in heavy ball mode, only pass through breakable bricks.
        // For unbreakable bricks (e.g., SolidBrick), still apply normal bounce.
        if (ball.isHeavyBall() && !(brick instanceof SolidBrick)) {
            return;
        }

        // Current bounds
        int ballLeft = ball.getX();
        int ballRight = ballLeft + ball.getWidth();
        int ballBottom = ball.getY();
        int ballTop = ballBottom + ball.getHeight();

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
            double ballCenterX = ballLeft + ball.getWidth() / 2.0;
            double brickCenterX = brickLeft + brick.getWidth() / 2.0;

            if (ballCenterX < brickCenterX) {
                // Collided with brick's left face
                ball.setPosition(brickLeft - ball.getWidth(), ball.getY());
                ball.velocityX = -Math.abs(ball.velocityX);
            } else {
                // Collided with brick's right face
                ball.setPosition(brickRight, ball.getY());
                ball.velocityX = Math.abs(ball.velocityX);
            }
        } else {
            // Resolve along Y axis
            double ballCenterY = ballBottom + ball.getHeight() / 2.0;
            double brickCenterY = brickBottom + brick.getHeight() / 2.0;

            if (ballCenterY < brickCenterY) {
                // Collided with brick's bottom face
                ball.setPosition(ball.getX(), brickBottom - ball.getHeight());
                ball.velocityY = -Math.abs(ball.velocityY);
            } else {
                // Collided with brick's top face
                ball.setPosition(ball.getX(), brickTop);
                ball.velocityY = Math.abs(ball.velocityY);
            }
        }

        // Add small random deviation to prevent infinite 90-degree bouncing
        // double epsilon = Constants.BALL_EPSILON;
        // double randomDeviationX = (Math.random() - 0.5) * epsilon * 2; // Range: [-epsilon, epsilon]
        // double randomDeviationY = (Math.random() - 0.5) * epsilon * 2; // Range: [-epsilon, epsilon]

        // Apply deviation to velocity components
        ball.velocityX += (Math.random() + 0.5) * Constants.BALL_EPSILON * 2;
        //ball.velocityY += epsilon;

        // Maintain constant speed after collision
        normalizeBallVelocity(ball);

        log.debug("Ball hit brick! Deviation: ({}, {}), New velocity: ({}, {})",
            //String.format("%.2f", randomDeviationX), String.format("%.2f", randomDeviationY),
            String.format("%.1f", ball.getVelocityX()), String.format("%.1f", ball.getVelocityY()));
    }

    // public static void handleBallPowerUpCollision(Ball ball, PowerUp powerUp) {
    //
    // }

    /**
     * Normalizes ball velocity to maintain constant speed.
     * Uses the original speed from Constants.BALL_SPEED for consistency.
     * Preserves the natural bounce angle while maintaining speed.
     * 
     * @param ball the ball to normalize velocity for
     */
    public static void normalizeBallVelocity(Ball ball) {
        double currentSpeed = getBallCurrentSpeed(ball);
        if (currentSpeed > 0) {
            // Use the original ball speed from constants
            double targetSpeed = Constants.BALL_SPEED;
            double normalizedVx = ball.getVelocityX() / currentSpeed * targetSpeed;
            double normalizedVy = ball.getVelocityY() / currentSpeed * targetSpeed;
            ball.setVelocity(normalizedVx, normalizedVy);
            
            // Log bounce event with angle and speed
            double newAngle = Math.toDegrees(Math.atan2(normalizedVy, normalizedVx));
            double newSpeed = getBallCurrentSpeed(ball);
            log.debug("Ball bounced! Angle: {}°, Speed: {}, Velocity: ({}, {})",
                String.format("%.1f", newAngle), String.format("%.1f", newSpeed), 
                String.format("%.1f", normalizedVx), String.format("%.1f", normalizedVy));
        }
    }
    
    /**
     * Gets the current speed magnitude of a ball.
     * 
     * @param ball the ball to get speed for
     * @return current speed
     */
    public static double getBallCurrentSpeed(Ball ball) {
        return Math.sqrt(ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY());
    }

    public static boolean checkPowerUpCollision(PowerUp powerUp, Paddle paddle) {
        return checkAABBCollision(powerUp, paddle);
    }

    /**
     * Checks for collisions between any ball and all bricks.
     * Handles brick destruction and ball bouncing.
     * This corrected version iterates through each brick and checks against all balls.
     *
     * @param gameScreen the GameScreen instance to access game state
     */
    public static void checkBallBrickCollisions(GameScreen gameScreen) {
        List<Brick> bricks = gameScreen.getBricks();
        if (bricks == null || bricks.isEmpty()) {
            return;
        }

        List<Ball> allBalls = new ArrayList<>(gameScreen.getExtraBalls());
        allBalls.add(gameScreen.getBall());

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            if (brick.isDestroyed()) {
                brickIterator.remove();
                continue;
            }

            for (Ball currentBall : allBalls) {
                if (currentBall.collidesWith(brick)) {
                    currentBall.handleBrickCollision(brick);
                    boolean wasDestroyed = brick.takeHit();

                    if (wasDestroyed) {
                        Ball mainBall = gameScreen.getBall();
                        mainBall.incrementCombo();
                        gameScreen.addScore(10 * mainBall.getComboCount());

                        if ("multiball".equals(brick.getType())) {
                            gameScreen.spawnExtraBalls(brick.getX() + brick.getWidth() / 2, brick.getY());
                        } else if ("powerup_brick".equals(brick.getType())) {
                            PowerUp powerUp = PowerUp.createRandomPowerUp(
                                    brick.getX() + brick.getWidth() / 2 - Constants.POWERUP_WIDTH / 2,
                                    brick.getY() + brick.getHeight() / 2 - Constants.POWERUP_HEIGHT / 2);
                            if (powerUp != null) {
                                gameScreen.addPowerUp(powerUp);
                                log.debug("PowerUp {} created at position ({}, {})", powerUp.getType(), powerUp.getX(), powerUp.getY());
                            } else {
                                log.warn("Failed to create power-up at position ({}, {})",
                                        brick.getX() + brick.getWidth() / 2, brick.getY() + brick.getHeight() / 2);
                            }
                        } else if ("strong".equals(brick.getType())) {
                            gameScreen.addScore(10 * mainBall.getComboCount());
                        }

                        brickIterator.remove();

                        // Check if level is cleared AFTER removing brick but BEFORE loading next level
                        // This ensures power-ups spawned from the last brick are not lost
                        boolean levelCleared = gameScreen.levelClear();
                        if (levelCleared) {
                            gameScreen.gotoWinLevelScreen(gameScreen.getCurrentLevel());
                            gameScreen.getSoundManager().play("sound/collected_and_level.wav");
                            mainBall.resetToCenter(gameScreen.getPaddle());
                            int nextLevel = gameScreen.getCurrentLevel() + 1;
                            if (gameScreen.getCurrentLevel() > Constants.Level) {
                                gameScreen.gotoVictoryScreen();
                            } else {
                                gameScreen.setCurrentLevel(nextLevel);
                                gameScreen.loadLevel(nextLevel, mainBall);
                            }
                        }

                        // log
                        log.debug("Brick destroyed! Remaining bricks: {}", bricks.size());
                    } else {
                        // Gạch vẫn còn máu
                        log.debug("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(),
                                brick.getType().equals("strong") ? 3 : 1);
                    }

                    // 3. QUAN TRỌNG: Thoát khỏi vòng lặp kiểm tra bóng
                    // Vì viên gạch này đã được xử lý va chạm rồi.
                    // Điều này ngăn một viên gạch bị nhiều bóng phá hủy trong cùng một frame.
                    break;
                }
            }
        }
    }
}
