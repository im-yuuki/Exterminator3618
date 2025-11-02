package io.exterminator3618.client.screens;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.exterminator3618.client.Constants.POLL_INTERVAL_MS;

public class OnlineGameScreen extends GameScreen {

    private static final Logger log = LoggerFactory.getLogger(OnlineGameScreen.class);

    private final ApiClient client;
    private final Thread statusPollingThread = new Thread(this::pollStatus);

    public OnlineGameScreen(Exterminator3618 game) {
        super(game);
        this.client = game.getApiClient();
        statusPollingThread.start();
    }

    @Override
    public void show() {
        super.show();
    }

    private void pollStatus() {
        log.info("Starting status polling thread");
        try {
            while (true) {
                long startTime = System.currentTimeMillis();
                if (client.fetchRoomStatus()) {
                    //
                }
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < POLL_INTERVAL_MS) {
                    Thread.sleep(POLL_INTERVAL_MS - elapsedTime);
                }
            }
        } catch (InterruptedException e) {
            log.info("Status polling thread interrupted, stopping");
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        statusPollingThread.interrupt();
    }

}
