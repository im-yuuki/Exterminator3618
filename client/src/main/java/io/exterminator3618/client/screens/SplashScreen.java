package io.exterminator3618.client.screens;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.utils.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SplashScreen implements Screen {

    private static final Logger log = LoggerFactory.getLogger(SplashScreen.class);

    private final Exterminator3618 game;
    private final Renderer renderer;
    private final Preferences prefs;

    private final Box box;
    private boolean loadFinished = false;
    private final Thread initApiClientThread;

    public SplashScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.prefs = game.getPreferences();
        this.box = new Box(800, 100, "Connecting to server...");
        initApiClientThread = new Thread(this::initApiClient);
    }

    @Override
    public void show() {
        if (prefs.contains(ApiClient.AUTHTOKEN_KEY)) {
            initApiClientThread.start();
        } else {
            log.warn("No auth token found in preferences");
            loadFinished = true;
        }
    }

    @Override
    public void render(float delta) {
        if (loadFinished) game.replaceCurrentScreen(new MainMenuScreen(game));
        renderer.begin();
        box.draw(renderer);
        renderer.end();

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

    private void initApiClient() {
        String authToken = prefs.getString(ApiClient.AUTHTOKEN_KEY);
        if (authToken != null && !authToken.isEmpty()) {
            try {
                game.setApiClient(ApiClient.create(game));
            } catch (Exception e) {
                log.error("Failed to initialize ApiClient with saved auth token", e);
                game.setApiClient(null);
                prefs.remove(ApiClient.AUTHTOKEN_KEY);
            }
        }
        loadFinished = true;
    }

}
