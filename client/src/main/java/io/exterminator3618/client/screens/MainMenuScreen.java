package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.managers.SoundManager;
import io.exterminator3618.client.utils.Renderer;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;
    private final SoundManager soundManager;

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.soundManager = game.getSoundManager();
        soundManager.setVolume(0.1f);
        soundManager.play("sound/main_menu.mp3", true);
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
        renderer.drawLogo(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 50);
        renderer.drawTextMiddle("Day la Main Screen", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        renderer.end();

        // Transition to game screen on input
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            soundManager.stop(); // Stop main menu music
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
