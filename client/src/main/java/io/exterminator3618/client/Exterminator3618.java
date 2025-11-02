package io.exterminator3618.client;

import com.badlogic.gdx.*;
import io.exterminator3618.client.utils.SoundManager;
import io.exterminator3618.client.screens.SplashScreen;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class Exterminator3618 extends Game {

    private static final Logger log = LoggerFactory.getLogger(Exterminator3618.class);

    private final Stack<Screen> screenStack = new Stack<>();
    private Preferences preferences = null;
    private Renderer renderer = null;
    private SoundManager soundManager = null;

    @Override
    public void create() {
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
        if (renderer != null) {
            renderer.dispose();
        }
        if (soundManager != null) {
            soundManager.dispose();
        }
        Assets.dispose();
        super.dispose();
    }

    public Preferences getPreferences() {
        if (preferences == null) {
            log.info("Loading preferences");
            preferences = Gdx.app.getPreferences(getClass().getPackageName());
        }
        return preferences;
    }

    public Renderer getRenderer() {
        if (renderer == null) {
            log.info("Creating renderer");
            renderer = new Renderer();
        }
        return renderer;
    }

    public SoundManager getSoundManager() {
        if (soundManager == null) {
            log.info("Creating sound manager");
            soundManager = SoundManager.getInstance();
            soundManager.setVolume(getPreferences().getBoolean("music_enabled", true) ? 1f : 0f);
        }
        return soundManager;
    }

    public void launchScreen(Screen screen) {
        log.info("Launching screen: {}", screen.getClass().getSimpleName());
        screenStack.push(screen);
        super.setScreen(screen);
    }

/*
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
*/

    public void backToPreviousScreen () {
        super.getScreen().dispose();
        if (screenStack.isEmpty()) {
            log.warn("Screen stack is empty, cannot go back.");
            Gdx.app.exit();
            return;
        }
        screenStack.pop();
        screenStack.peek().show();
        if (screenStack.isEmpty()) {
            log.warn("Popped the last screen, quiting game.");
            Gdx.app.exit();
        } else {
            log.info("Returning to screen: {}", screenStack.peek().getClass().getSimpleName());
            super.setScreen(screenStack.peek());
        }
    }

    public void replaceCurrentScreen(Screen screen) {
        super.getScreen().dispose();
        screenStack.pop();
        screenStack.push(screen);
        super.setScreen(screen);
        // launchScreen(screen);
    }

    @Override
    @Deprecated
    public Screen getScreen() {
        throw new UnsupportedOperationException("Calling this method is prohibited.");
    }

    @Override
    @Deprecated
    public void setScreen(Screen screen) {
        throw new UnsupportedOperationException("Calling this method is prohibited.");
    }

}
