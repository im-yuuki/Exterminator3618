package io.exterminator3618.client;

import com.badlogic.gdx.*;
import io.exterminator3618.client.screens.SplashScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Exterminator3618 extends Game {

    public static final String GAME_NAME = "Exterminator3618";
    public static final String GAME_VERSION = "0.1.0-dev";

    private static final Logger log = LoggerFactory.getLogger(Exterminator3618.class);

    private Preferences preferences = null;

    @Override
    public void create() {
        log.info("Starting {} v{}", GAME_NAME, GAME_VERSION);
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        // save game preferences before exit
        if (preferences != null) {
            log.info("Saving preferences");
            preferences.flush();
        }
        log.debug("Disposing object");
        super.dispose();
    }

    /**
     * Get game preferences storage, lazy loaded on first call.
     *
     * @return preferences store object
     */
    protected Preferences getPreferences() {
        if (preferences == null) {
            log.info("Loading preferences");
            preferences = Gdx.app.getPreferences(getClass().getPackageName());
        }
        return preferences;
    }

}
