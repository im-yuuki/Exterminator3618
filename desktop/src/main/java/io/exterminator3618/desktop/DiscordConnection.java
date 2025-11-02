package io.exterminator3618.desktop;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordConnection {

    private static final String DISCORD_APPLICATION_ID = "1425211272403619982";

    private final Thread runCallbackThread = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordRPC.discordRunCallbacks();
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }, "Discord-RPC");

    public DiscordConnection() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.ready = (user) -> {
            // Set initial presence
            DiscordRichPresence presence = new DiscordRichPresence
                    .Builder("Breaking Bricks!")
                    .setBigImage("logo", Launcher.GAME_NAME)
                    .build();
            DiscordRPC.discordUpdatePresence(presence);
        };
        DiscordRPC.discordInitialize(DISCORD_APPLICATION_ID, eventHandlers, true);
        runCallbackThread.setDaemon(true);
        runCallbackThread.setPriority(Thread.MIN_PRIORITY);
        runCallbackThread.start();
    }

    public void shutdown() {
        runCallbackThread.interrupt();
        DiscordRPC.discordShutdown();
    }

}
