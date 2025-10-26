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

public final class GameOverScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton playAgainButton;
    private TextButton backButton;
    public GameOverScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();


    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);

        touchPos = new Vector3();
        backButton = new TextButton("Main Menu", 100, 300, 200, 50);
        playAgainButton = new TextButton("Play Again", 100, 240, 200, 50);
    }

    @Override
    public void render(float delta) {
        // Render game over screen
        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin(camera);
        // Draw game over text here
        renderer.drawText("Day la Game Over Screen", 250, 300);
        camera.update();
        viewport.apply();
        //renderer.begin(camera);
        playAgainButton.draw(renderer);
        backButton.draw(renderer);

        renderer.end();

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }

            if (playAgainButton.isClicked(touchPos.x, touchPos.y)) {
                game.replaceCurrentScreen(new GameScreen(game));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.launchScreen(new MainMenuScreen(game));
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
