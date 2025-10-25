package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.utils.Renderer;

public final class PauseScreen implements Screen {

    private final Exterminator3618 game;
    private final GameScreen gameScreen;
    private final Renderer renderer;

    public PauseScreen(Exterminator3618 game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.renderer = game.getRenderer();
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render(float v) {
        // Render paused screen overlay

        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin();
        // Draw paused text here
        renderer.drawTextMiddle("Day la Pause Screen", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
    }

    @Override
    public void resize(int width, int height) {

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
