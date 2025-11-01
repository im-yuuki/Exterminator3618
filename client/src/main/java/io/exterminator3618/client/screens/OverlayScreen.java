package io.exterminator3618.client.screens;

import com.badlogic.gdx.Screen;
import io.exterminator3618.client.Exterminator3618;

public abstract class OverlayScreen implements Screen {

    protected final Exterminator3618 game;
    protected final Screen backScreen;

    protected OverlayScreen(Exterminator3618 game, Screen backScreen) {
        this.game = game;
        this.backScreen = backScreen;
    }


}
