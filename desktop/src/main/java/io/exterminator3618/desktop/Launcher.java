package io.exterminator3618.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.exterminator3618.client.Game;

import static io.exterminator3618.client.Constants.*;

public class Launcher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(WINDOW_TITLE);
        cfg.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        cfg.useVsync(true);
        cfg.setIdleFPS(10);
        cfg.setWindowIcon("icons/logo.png", "icons/logo32.png", "icons/logo64.png", "icons/logo128.png");
        //
        new Lwjgl3Application(new Game(), cfg);
    }

}
