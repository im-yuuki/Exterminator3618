package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.utils.Renderer;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.begin();
        // Draw text here
        renderer.drawText("Day la Main Screen", 300, 300);
        renderer.end();

        // Transition to game screen on input
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.launchScreen(new GameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
