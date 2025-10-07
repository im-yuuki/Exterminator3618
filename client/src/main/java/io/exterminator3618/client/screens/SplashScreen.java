package io.exterminator3618.client.screens;

import com.badlogic.gdx.Screen;
import io.exterminator3618.client.Exterminator3618;

public class SplashScreen implements Screen {

    private final Exterminator3618 game;

    public SplashScreen(Exterminator3618 game) {
        this.game = game;
    }

    @Override
    public void show() {
        // TODO: add splash screen implementation
        // For now, i let it immediately switch to the main menu
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void render(float delta) {

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
