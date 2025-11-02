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
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.SaveManager;

public final class PauseScreen implements Screen {

    private final Exterminator3618 game;
    private final GameScreen gameScreen;
    private final Renderer renderer;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton backButton;
    private TextButton playButton;
    private TextButton settingButton;

    private Box box;

    public PauseScreen(Exterminator3618 game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.renderer = game.getRenderer();
        touchPos = new Vector3();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);

        final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
        final int BACK_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT - 20;
        final int PLAY_Y = BACK_Y - Constants.BUTTON_HEIGHT - 20;
        final int SETTING_Y = PLAY_Y - Constants.BUTTON_HEIGHT - 20;

        backButton = new TextButton("Main Menu", CENTER_X, BACK_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        playButton = new TextButton("Continue", CENTER_X, PLAY_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        settingButton = new TextButton("Settings", CENTER_X, SETTING_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        box = new Box(800,500,"GAME PAUSED");
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void render(float v) {
        gameScreen.render(0);
        renderer.drawOverlay(camera, 0.7f);
        viewport.apply();
        camera.update();
        renderer.begin(camera);

        box.draw(renderer);
        playButton.draw(renderer);
        backButton.draw(renderer);
        settingButton.draw(renderer);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Resume game on input (e.g., P or ESC key) - use justPressed to avoid immediate close
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.backToPreviousScreen();
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            viewport.unproject(touchPos);

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
                SaveManager.saveGame(game, gameScreen);
                game.replaceCurrentScreen(new MainMenuScreen(game));
            }
            if (playButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }
            if (settingButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new SettingsScreen(game, this));
            }
        }

    }

    public GameScreen getGameScreen(){
        return this.gameScreen;
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
