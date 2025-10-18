package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.utils.Renderer;

public final class GameOverScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;

    public GameOverScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Render game over screen
        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin();
        // Draw game over text here
        renderer.drawText("Day la Game Over Screen", 250, 300);
        renderer.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.backToPreviousScreen();
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
