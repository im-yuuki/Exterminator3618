package io.exterminator3618.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.exterminator3618.client.Constants.*;

public class Launcher {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(WINDOW_TITLE);
        cfg.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        cfg.useVsync(true);
        cfg.setIdleFPS(10);
        cfg.setWindowIcon("icons/logo.png", "icons/logo32.png", "icons/logo64.png", "icons/logo128.png");
        //
        log.info("Starting game client");
        new Lwjgl3Application(new Game(), cfg);
    }

}
