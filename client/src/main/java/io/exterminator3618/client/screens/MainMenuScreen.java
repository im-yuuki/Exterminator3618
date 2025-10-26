package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Renderer;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton startButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    final int BUTTON_WIDTH = 220;
    final int BUTTON_HEIGHT = 50;
    final int PADDING = 20;

    final int TITLE_FONT_SIZE = 100;
    final int BUTTON_FONT_SIZE = 24;

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);

        touchPos = new Vector3();

        final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (BUTTON_WIDTH / 2);
        final int TITLE_Y = Constants.WINDOW_HEIGHT - 100;
        final int START_Y = (Constants.WINDOW_HEIGHT / 2) + BUTTON_HEIGHT + PADDING;
        final int SETTINGS_Y = START_Y - BUTTON_HEIGHT - PADDING;
        final int EXIT_Y = SETTINGS_Y - BUTTON_HEIGHT - PADDING;
        startButton = new TextButton("Start Game", CENTER_X, START_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton = new TextButton("Options", CENTER_X, SETTINGS_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        //exitButton = new TextButton("Exit", CENTER_X, EXIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        viewport.apply();
        renderer.begin(camera);
        // Draw text here
        renderer.setFontSize(TITLE_FONT_SIZE);
        final int TITLE_X = (Constants.WINDOW_WIDTH / 2) - 140;
        renderer.drawText("ARKANOID", TITLE_X, Constants.WINDOW_HEIGHT - 100);

        renderer.setFontSize(BUTTON_FONT_SIZE);
        startButton.draw(renderer);
        settingsButton.draw(renderer);

        renderer.end();
        // 3. Xử lý Input (Logic)
        if (Gdx.input.justTouched()) { // Chỉ kiểm tra khi người dùng vừa nhấp
            // Lấy tọa độ nhấp chuột trên màn hình
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.launchScreen(new GameScreen(game));
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            }

            if (startButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new GameScreen(game));
            }

            // Transition to setting screen on input
            if (settingsButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new SettingsScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

}
