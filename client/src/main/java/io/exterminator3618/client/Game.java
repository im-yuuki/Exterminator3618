package io.exterminator3618.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.exterminator3618.client.Constants.*;

/**
 * Main LibGDX application for the Exterminator3618 client. It owns the renderer
 * and the root game objects and drives the frame loop.
 */
public class Game extends ApplicationAdapter {
    private static final Logger log = LoggerFactory.getLogger(Game.class);
    private Assets assets;
    private Renderer renderer;
    private Ball ball;
    private SoundManager soundManager;
    private List<Brick> bricks;

    /**
     * Initializes rendering and creates initial game objects.
     */
    @Override
    public void create() {
        log.info("Game created");
        assets = new Assets();
        Assets.load();
        renderer = new Renderer();
        // soundManager = new SoundManager();
        //
        // // Play background music
        // soundManager.play(BACKGROUND_MUSIC, true);
        // log.info("Đang phát nhạc nền: {}", BACKGROUND_MUSIC);
        // soundManager.play(TEST_LONG_MUSIC_2, true);
        // log.info("Đang phát nhạc nền: {}", BACKGROUND_MUSIC);

        //soundManager.play(BUFF_SOUND);
        // Phát BUFF_SOUND sau 5 giây
        // new Thread(() -> {
        //     try {
        //         Thread.sleep(5000);
        //         soundManager.play(BUFF_SOUND);
        //         Thread.sleep(2000);
        //         soundManager.play(BUFF_SOUND);
        //         log.info("Đã phát BUFF_SOUND sau 5 giây");
        //     } catch (InterruptedException e) {
        //         Thread.currentThread().interrupt();
        //     }
        // }).start();

        // Testing: create a ball in the center of the window
        ball = new Ball(
                WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                BALL_WIDTH,
                BALL_HEIGHT,
                BALL_REGION_NAME, //object name
                BALL_SPEED,
                67
        );

        // Create some bricks for testing
        createTestBricks();
    }

    /**
     * Frame callback: updates and renders the scene.
     */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update game objects
        ball.update(deltaTime);
        //log.info("ball velocity", ball.getVelocityX(), ball.getVelocityY()); đéo cần nữa

        // Check for ball-brick collisions
        checkBallBrickCollisions();

        // Update bricks
        for (Brick brick : bricks) {
            brick.update(deltaTime);
        }

        // Render everything
        renderer.begin();
        renderer.draw(ball);
        
        // Render only existing (non-destroyed) bricks
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                renderer.draw(brick);
            }
        }
        
        renderer.end();
    }



    /**
     * Checks for collisions between the ball and all bricks.
     * Handles brick destruction and ball bouncing.
     * Optimized for performance with early exits and efficient removal.
     */
    private void checkBallBrickCollisions() {
        // Early exit if no bricks to check
        if (bricks.isEmpty()) {
            return;
        }
        
        // Use iterator for safe removal during iteration
        var iterator = bricks.iterator();
        boolean collisionHandled = false;
        
        while (iterator.hasNext() && !collisionHandled) {
            Brick brick = iterator.next();
            
            // Skip destroyed bricks (defensive programming)
            if (brick.isDestroyed()) {
                iterator.remove();
                continue;
            }
            
            if (ball.collidesWith(brick)) {
                // Handle ball collision (bounce)
                ball.handleBrickCollision(brick);
                
                // Damage the brick
                boolean wasDestroyed = brick.takeHit();
                
                if (wasDestroyed) {
                    // Remove destroyed brick immediately
                    iterator.remove();
                    log.info("Brick destroyed! Remaining bricks: {}", bricks.size());
                } else {
                    // Brick still has hit points remaining
                    log.info("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(), 
                        brick.getType().equals("strong") ? 3 : 1);
                }
                
                // Only handle one collision per frame to avoid multiple hits
                collisionHandled = true;
            }
        }
    }


    /**
     * Creates test bricks for demonstration.
     */
    private void createTestBricks() {
        bricks = new ArrayList<>();
        
        // Create a row of normal bricks
        for (int i = 0; i < 10; i++) {
            Brick brick = BrickFactory.createRandomNormalBrick(
                i * (BRICK_WIDTH + BRICK_SPACING) + BRICK_START_X, 
                WINDOW_HEIGHT - BRICK_START_Y
            );
            bricks.add(brick);
        }
        
        // Create a row of strong bricks
        for (int i = 0; i < 10; i++) {
            Brick brick = BrickFactory.createRandomStrongBrick(
                i * (BRICK_WIDTH + BRICK_SPACING) + BRICK_START_X,
                WINDOW_HEIGHT - BRICK_START_Y - BRICK_ROW_HEIGHT
            );
            bricks.add(brick);
        }
        
        
        log.info("Created {} test bricks", bricks.size());
    }

    /**
     * Releases resources and other disposable assets.
     */
    @Override
    public void dispose() {
        Assets.dispose();
        renderer.dispose();
        //soundManager.dispose();
        log.info("Game disposed");
    }
}
