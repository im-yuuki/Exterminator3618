package io.exterminator3618.client;

import com.badlogic.gdx.*;
import io.exterminator3618.client.screens.SplashScreen;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class Exterminator3618 extends Game {

    public static final String GAME_NAME = "Exterminator3618";
    public static final String GAME_VERSION = "0.1.0-dev";

    private static final Logger log = LoggerFactory.getLogger(Exterminator3618.class);

    private final Stack<Screen> screenStack = new Stack<>();
    private Preferences preferences = null;
    private Renderer renderer = null;

    @Override
    public void create() {
        log.info("Starting {} v{}", GAME_NAME, GAME_VERSION);
        Assets.load();
        launchScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        // save game preferences before exit
        if (preferences != null) {
            log.info("Saving preferences");
            preferences.flush();
        }
        log.debug("Disposing object");
        renderer.dispose();
        Assets.dispose();
        super.dispose();
    }

    @NotNull
    public Preferences getPreferences() {
        if (preferences == null) {
            log.info("Loading preferences");
            preferences = Gdx.app.getPreferences(getClass().getPackageName());
        }
        return preferences;
    }

    @NotNull
    public Renderer getRenderer() {
        if (renderer == null) {
            log.info("Creating renderer");
            renderer = new Renderer();
        }
        return renderer;
    }

    public void launchScreen(@NotNull Screen screen) {
        log.info("Launching screen: {}", screen.getClass().getSimpleName());
        screenStack.push(screen);
        super.setScreen(screen);
    }

    public void backToPreviousScreen() {
        super.getScreen().dispose();
        if (screenStack.isEmpty()) {
            log.warn("Screen stack is empty, quiting game");
            Gdx.app.exit();
        } else {
            log.info("Returning to screen: {}", screenStack.peek().getClass().getSimpleName());
            super.setScreen(screenStack.pop());
        }
    }

    public void replaceCurrentScreen(@NotNull Screen screen) {
        super.getScreen().dispose();
        launchScreen(screen);
    }

    @Override
    @Deprecated
    public Screen getScreen() {
        throw new UnsupportedOperationException("Calling this method is prohibited.");
    }

    @Override
    @Deprecated
    public void setScreen(@NotNull Screen screen) {
        throw new UnsupportedOperationException("Calling this method is prohibited.");
    }

}
