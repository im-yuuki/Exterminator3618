package io.exterminator3618.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.exterminator3618.client.Constants.*;

/**
 * Desktop entry point for the Exterminator3618 client using LWJGL3 backend.
 * Configures the window and starts the LibGDX application.
 */
public class Launcher {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);

    /**
     * Application main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Configure application
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(WINDOW_TITLE);
        cfg.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        cfg.useVsync(VSYNC_ENABLED);
        cfg.setIdleFPS(IDLE_FPS);
        cfg.setWindowIcon(ICON_16_PATH, ICON_32_PATH, ICON_64_PATH, ICON_128_PATH);

        // Start application
        log.info("Starting game client");
        new Lwjgl3Application(new Game(), cfg);
    }
}
