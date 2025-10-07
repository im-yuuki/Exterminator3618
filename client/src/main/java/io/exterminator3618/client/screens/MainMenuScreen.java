package io.exterminator3618.client.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.exterminator3618.client.Exterminator3618;

public class MainMenuScreen implements Screen {

    private Stage stage;

    private final Exterminator3618 game;

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
    }

    @Override
    public void show() {
         stage = new Stage(new ScreenViewport());
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
