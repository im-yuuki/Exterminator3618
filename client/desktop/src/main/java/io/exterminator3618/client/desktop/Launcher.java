package io.exterminator3618.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.exterminator3618.client.core.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Exterminator3618");
        cfg.setWindowedMode(1280, 720);
        cfg.useVsync(true);
        cfg.setIdleFPS(10);
        cfg.setWindowIcon("icon.png", "icon32.png", "icon64.png", "icon128.png");
        //
        log.info("Starting game client");
        new Lwjgl3Application(new GameClient(), cfg);
    }

}
