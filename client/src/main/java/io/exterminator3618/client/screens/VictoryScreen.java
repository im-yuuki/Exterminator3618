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
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;

public final class VictoryScreen implements Screen {
    private final Exterminator3618 game;
    private final Renderer renderer;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton playAgainButton;
    private TextButton backButton;

    private Box box;

    public VictoryScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);

        final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
        final int BACK_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT - 20;
        final int PLAY_Y = BACK_Y - Constants.BUTTON_HEIGHT - 20;

        backButton = new TextButton("Main Menu", CENTER_X, BACK_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        playAgainButton = new TextButton("Play Again", CENTER_X, PLAY_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        box = new Box(800,500,"VICTORY");
    }

    @Override
    public void show() {
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void render(float delta) {
        // Render game over screen
        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        renderer.drawBackground(Assets.menuBackground);
        box.draw(renderer);

        playAgainButton.draw(renderer);
        backButton.draw(renderer);
        renderer.end();

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new MainMenuScreen(game));
            }

            if (playAgainButton.isClicked(touchPos.x, touchPos.y)) {
                game.replaceCurrentScreen(new GameScreen(game));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.getSoundManager().stop(); // Stop any playing music
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
