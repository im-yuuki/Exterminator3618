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
    private Renderer renderer;
    private Ball ball;
    private SoundManager soundManager;

    /**
     * Initializes rendering and creates initial game objects.
     */
    @Override
    public void create() {
        log.info("Game created");
        renderer = new Renderer();
        soundManager = new SoundManager();
        
        // Phát nhạc nền
        soundManager.play(BACKGROUND_MUSIC, true);
        log.info("Đang phát nhạc nền: {}", BACKGROUND_MUSIC);
        soundManager.play(TEST_LONG_MUSIC_2, true);
        log.info("Đang phát nhạc nền: {}", BACKGROUND_MUSIC);

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
        
        ball = new Ball(
                WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                BALL_WIDTH,
                BALL_HEIGHT,
                BALL_IMAGE_PATH,
                BALL_SPEED,
                45
        );
    }

    /**
     * Frame callback: updates and renders the scene.
     */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        ball.update(deltaTime);

        renderer.begin();
        renderer.draw(ball);
        renderer.end();
    }



    /**
     * Releases resources and other disposable assets.
     */
    @Override
    public void dispose() {
        renderer.dispose();
        soundManager.dispose();
        log.info("Game disposed");
    }
}
