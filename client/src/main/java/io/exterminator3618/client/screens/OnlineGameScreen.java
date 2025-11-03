package io.exterminator3618.client.screens;

import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import io.exterminator3618.client.api.RoomStatus;
import io.exterminator3618.client.components.Ball;
import io.exterminator3618.client.components.TextButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static io.exterminator3618.client.Constants.*;
import static io.exterminator3618.client.Constants.BALL_HEIGHT;
import static io.exterminator3618.client.Constants.BALL_REGION_NAME;
import static io.exterminator3618.client.Constants.BALL_SPEED;
import static io.exterminator3618.client.Constants.BALL_WIDTH;
import static io.exterminator3618.client.Constants.WINDOW_HEIGHT;

public class OnlineGameScreen extends GameScreen {

    private static final Logger log = LoggerFactory.getLogger(OnlineGameScreen.class);

    private final ApiClient client;
    private final Thread statusPollingThread = new Thread(this::pollStatus);

    private final Queue<String> eventQueue = new LinkedList<>();
    private final ArrayList<String> playerStates = new ArrayList<>();
    private final String mapCode;

    private final long startTime;
    private boolean finished = false;

    public OnlineGameScreen(Exterminator3618 game) {
        super(game);
        this.client = game.getApiClient();
        pauseButton = new TextButton("Leave Room", 1545, 900, 300, 75, true);
        if (client.fetchRoomStatus()) {
            RoomStatus status = client.getRoomStatus();
            this.mapCode = status.getMapCode();
            log.info(
                    "Fetched initial room status: mode={}, mapCode={}, totalPlayers={}",
                    status.getMode(), status.getMapCode(), status.getTotalPlayers()
            );
        } else {
            log.error("Failed to fetch initial room status");
            throw new IllegalStateException("Failed to launch online game screen");
        }
        loadLevel(Integer.parseInt(mapCode.substring(5)), new Ball(
                WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                BALL_WIDTH,
                BALL_HEIGHT,
                BALL_REGION_NAME,
                BALL_SPEED,
                67
        ));
        startTime = System.currentTimeMillis();
        statusPollingThread.start();
    }

    private void pollStatus() {
        log.info("Starting status polling thread");
        try {
            while (true) {
                long startTime = System.currentTimeMillis();
                if (client.fetchRoomStatus()) {
                    RoomStatus status = client.getRoomStatus();
                    synchronized (playerStates) {
                        playerStates.clear();
                        status.getMembersList().forEach((memberStatus -> {
                            StringBuilder sb = new StringBuilder();
                            if (memberStatus.isFinished()) sb.append('[');
                            sb.append(memberStatus.getPlayerName()).append('(').append(memberStatus.getPlayerUsername()).append(')');
                            sb.append(" - Score: ").append(memberStatus.getScore());
                            sb.append(" Combo: ").append(memberStatus.getCombo()).append('/').append(memberStatus.getMaxCombo());
                            sb.append(" HP: ").append(memberStatus.getHp());
                            if (memberStatus.isFinished()) sb.append(']');
                            playerStates.add(sb.toString());
                        }));
                    }
                }
                dumpFrameEvent();
                ArrayList<String> newEvents = new ArrayList<>();
                synchronized (eventQueue) {
                    while (!eventQueue.isEmpty()) {
                        newEvents.add(eventQueue.poll());
                    }
                }
                client.pushGameEvents(newEvents);
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
    public void render(float delta) {
        super.render(delta);
        renderer.begin();
        renderer.setFontSize(24);
        synchronized (playerStates) {
            for (int i = 0; i < playerStates.size(); i++) {
                renderer.drawText(playerStates.get(i), 50, 250 - i * 50);
            }
        }
        renderer.setFontSize(36);
        renderer.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        client.setInMatch(false);
        statusPollingThread.interrupt();
        new Thread(() -> {
            if (!client.leaveRoom()) {
                game.launchScreen(new AlertScreen(game, this, "Leave Match Failed", "Check the log", "OK"));
            }
        }).start();
    }

    @Override
    protected void onPauseButtonClicked() {
        client.setInMatch(false);
        game.backToPreviousScreen();
    }

    @Override
    protected void gotoGameOverScreen() {
        client.setInMatch(false);
        finished = true;
        StringBuilder sb = new StringBuilder("You lose the game!");
        synchronized (playerStates) {
            for (String state : playerStates) {
                sb.append(" | ").append(state);
            }
        }
        AlertScreen screen = new AlertScreen(game, this, "Lose!", sb.toString(), "Back");
        game.replaceCurrentScreen(screen);
    }

    @Override
    public void gotoVictoryScreen() {
        client.setInMatch(false);
        finished = true;
        StringBuilder sb = new StringBuilder("You have finished the game!");
        synchronized (playerStates) {
            for (String state : playerStates) {
                sb.append(" | ").append(state);
            }
        }
        AlertScreen screen = new AlertScreen(game, this, "Victory!", sb.toString(), "Back");
        game.replaceCurrentScreen(screen);
    }

    @Override
    public void gotoWinLevelScreen(int level) {
        gotoVictoryScreen();
    }

    private void dumpFrameEvent() {
        // Data schema is FRAME:<timestamp>:<isFinished>:<hp>:<currentScore>:<currentCombo>
        int timestamp = (int) (System.currentTimeMillis() - startTime);
        boolean isFinished = finished;
        int hp = getLives();
        int currentScore = getScore();
        int currentCombo = getBall().getComboCount();
        String eventData = "FRAME:" + timestamp + ":" + isFinished + ":" + hp + ":" + currentScore + ":" + currentCombo;
        synchronized (eventQueue) {
            eventQueue.add(eventData);
        }
    }

}
