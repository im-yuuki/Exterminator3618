package io.exterminator3618.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        // Render everything
        renderer.begin();
        renderer.draw(ball);
        renderer.end();
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
