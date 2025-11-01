package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.managers.SoundManager;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.SaveManager;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;
    private final SoundManager soundManager;
    private final OrthographicCamera camera;
    private final Vector3 touchPos;
    private Viewport viewport;

    private TextButton startButton;
    private TextButton continueButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    final int PADDING = 20;

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        renderer = game.getRenderer();
        soundManager = game.getSoundManager();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();

        final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
        final int START_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT + PADDING - 200;
        final int CONTINUE_Y = START_Y - Constants.BUTTON_HEIGHT - PADDING;
        final int SETTINGS_Y = CONTINUE_Y - Constants.BUTTON_HEIGHT - PADDING;
        final int EXIT_Y = SETTINGS_Y - Constants.BUTTON_HEIGHT - PADDING;
        startButton = new TextButton("Start Game",CENTER_X, CONTINUE_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        continueButton = new TextButton("Continue", CENTER_X, START_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        settingsButton = new TextButton("Options", CENTER_X, SETTINGS_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        exitButton = new TextButton("Exit", CENTER_X, EXIT_Y, Constants.BUTTON_WIDTH,  Constants.BUTTON_HEIGHT, true);
    }

    @Override
    public void show() {
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
        soundManager.play("sound/main_menu.mp3", true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        renderer.drawBackground(Assets.menuBackground);
        renderer.drawLogo(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        startButton.draw(renderer);
        if (SaveManager.hasSave(game)) {
            continueButton.draw(renderer);
        }
        settingsButton.draw(renderer);
        exitButton.draw(renderer);
        renderer.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            soundManager.stop(); // Stop main menu music
            game.launchScreen(new GameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (startButton.isClicked(touchPos.x, touchPos.y)) {
                SaveManager.clearSave(game);
                game.launchScreen(new GameScreen(game));
            }

            if (continueButton.isClicked(touchPos.x, touchPos.y)) {
                soundManager.stop();
                GameScreen gs = new GameScreen(game);
                SaveManager.loadInto(game, gs);
                game.launchScreen(gs);
            }

            if (settingsButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new SettingsScreen(game, this));
            }

            if (exitButton.isClicked(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
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
