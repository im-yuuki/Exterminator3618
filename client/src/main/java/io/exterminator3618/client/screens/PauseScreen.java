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

    public PauseScreen(Exterminator3618 game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.renderer = game.getRenderer();
        touchPos = new Vector3();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);

        backButton = new TextButton("Main Menu", 100, 300, 200, 50);
        playButton = new TextButton("Continue", 100, 400, 200, 50);
        settingButton = new TextButton("Settings", 100, 240, 200, 50);
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void render(float v) {
        // Render paused screen overlay

        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // renderer.begin();
        // Draw paused text here
        renderer.begin(camera);
        renderer.drawTextMiddle("Day la Pause Screen", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        camera.update();
        viewport.apply();
        playButton.draw(renderer);
        backButton.draw(renderer);
        settingButton.draw(renderer);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Resume game on input (e.g., P key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.backToPreviousScreen();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // Stop gameplay background music before transitioning to game over
            gameScreen.getSoundManager().stop();
            game.launchScreen(new GameOverScreen(game));
        }


        if (Gdx.input.justTouched()) {
            // Lấy tọa độ nhấp chuột trên màn hình
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Chuyển đổi tọa độ màn hình -> tọa độ thế giới game
            viewport.unproject(touchPos);

            // Bây giờ mới kiểm tra click
            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.replaceCurrentScreen(new MainMenuScreen(game));
            }
            if (playButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }
            if (settingButton.isClicked(touchPos.x, touchPos.y)) {
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
