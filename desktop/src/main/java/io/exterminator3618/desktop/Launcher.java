package io.exterminator3618.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.exterminator3618.client.Exterminator3618;

public class Launcher {

    public static final String GAME_NAME = "Exterminator3618";

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(GAME_NAME);
        cfg.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        // cfg.setWindowedMode(1600, 900);
        cfg.useVsync(true);
        cfg.setIdleFPS(5);
        cfg.setWindowIcon("icons/logo.png");
        // start Discord RPC
        DiscordConnection discordConnection = new DiscordConnection();
        // start the game
        new Lwjgl3Application(new Exterminator3618(), cfg);
        // shutdown Discord RPC on exit
        discordConnection.shutdown();
    }

}
