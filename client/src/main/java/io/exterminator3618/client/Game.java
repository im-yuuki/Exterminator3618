package io.exterminator3618.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer; //SAU NÀY XÓA ĐI, GIỜ ADD ĐỂ TEST I/O THÔI
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
    private GameState state;
    private Assets assets;
    private Renderer renderer;
    private Ball ball;
    private SoundManager soundManager;
    private List<Brick> bricks;
    private Paddle paddle;

    /**
     * Initializes rendering and creates initial game objects.
     */
    @Override
    public void create() {
        log.info("Game created");
        assets = new Assets();
        Assets.load();
        renderer = new Renderer();
        state = GameState.MENU;
    }
    private static final String[] BRICK_COLORS = {"red", "green", "blue", "yellow", "purple"};

    public void loadLevel () {
        // Initialize ball
        ball = new Ball(
                WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                BALL_WIDTH,
                BALL_HEIGHT,
                BALL_REGION_NAME, //object name
                BALL_SPEED,
                67
        );

        // Initialize paddle
        paddle = new Paddle(
                PADDLE_START_X,
                PADDLE_START_Y,
                PADDLE_WIDTH,
                PADDLE_HEIGHT,
                PADDLE_REGION_NAME
        );

        // Initialize random bricks for testing
        bricks = new ArrayList<>();
        // int rows = 5;
        // int cols = 17;
        // int startX = 50;
        // int startY = WINDOW_HEIGHT - 100;
        // for (int row = 0; row < rows; row++) {
        //     for (int col = 0; col < cols; col++) {
        //         int x = startX + col * (BRICK_WIDTH + BRICK_SPACING);
        //         int y = startY - row * (BRICK_HEIGHT + BRICK_SPACING);
        //         String color = BRICK_COLORS[row % BRICK_COLORS.length];
        //         Brick brick = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT,
        //                 "normal_" + color + "_brick", 3, "normal");
        //         bricks.add(brick);
        //     }
        // }
    }

    /**
     * Frame callback: updates and renders the scene.
     */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Clear screen
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case MENU -> {
                // Render menu screen
                Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw text here
                renderer.drawText("Day la Main Screen", 300, 300);
                renderer.end();

                // Transition to PLAYING state on input
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    loadLevel();
                    state = GameState.PLAYING;
                }
            }
            case PLAYING -> {
                // Update game logic
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                ball.update(deltaTime);
                paddle.update(deltaTime);
                for (Brick brick : bricks) {
                    brick.update(deltaTime);
                }
                checkBallBrickCollisions();
                ball.checkPaddleCollision(paddle);
                // Render game objects
                renderer.begin();
                renderer.draw(ball);
                renderer.draw(paddle);
                for (Brick brick : bricks) {
                    renderer.draw(brick);
                }
                renderer.end();
                // Pause game on input
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.PAUSED;
                }
            }
            case PAUSED -> {
                // Render paused screen overlay
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Gdx.gl.glClearColor(0, 0, 0, 0.5f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw paused text here
                renderer.drawText("Day la Pause Screen", 250, 300);
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                // Resume game on input (e.g., P key)
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.PLAYING;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    state = GameState.GAME_OVER;
                }
            }
            case GAME_OVER -> {
                // Render game over screen
                Gdx.gl.glClearColor(0.2f, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw game over text here
                renderer.drawText("Day la Game Over Screen", 250, 300);
                renderer.end();

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    state = GameState.MENU;
                    disposeLevel();
                }
            }
        }
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
                    // Remove the destroyed brick immediately
                    iterator.remove();
                    log.debug("Brick destroyed! Remaining bricks: {}", bricks.size());
                } else {
                    // Brick still has hit points remaining
                    log.debug("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(),
                            brick.getType().equals("strong") ? 3 : 1);
                }

                // Only handle one collision per frame to avoid multiple hits
                collisionHandled = true;
            }
        }
    }

    public void disposeLevel () {
        bricks.clear();
        ball = null;
        paddle = null;
        log.info("Level disposed");
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
