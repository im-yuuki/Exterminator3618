package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import io.exterminator3618.client.api.MatchInvite;
import io.exterminator3618.client.api.UserInfo;
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static io.exterminator3618.client.Constants.*;

public class OnlineMenuScreen implements Screen {

    private static final Logger log = LoggerFactory.getLogger(OnlineMenuScreen.class);


    private final Exterminator3618 game;
    private final ApiClient client;
    private final Renderer renderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Vector3 touchPos;

    private final TextButton backButton = new TextButton("Back", 50, 50, 200, 80, true);
    private final TextButton addFriendButton = new TextButton("Add Friend", 270, 50, 300, 80, true);
    private final TextButton invisibleButton = new TextButton("Invisible: ", 590, 50, 460, 80, true);
    private final TextButton queueButton = new TextButton("Find Match", WINDOW_WIDTH - 450, 50, 400, 200, true);
    private final Box friendListBox = new Box(1000, WINDOW_HEIGHT - 200, "Friends");

    private final ArrayList<FriendEntryButton> friendButtons = new ArrayList<>();
    private final ArrayList<AcceptInviteButton> acceptInviteButtons = new ArrayList<>();

    private final Thread statusPollingThread = new Thread(this::pollStatus);
    private boolean pausePolling = false;
    private boolean isInMatchQueue = false;

    public OnlineMenuScreen(Exterminator3618 game) {
        this.game = game;
        this.client = game.getApiClient();
        this.renderer = game.getRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
        this.touchPos = new Vector3();
        friendListBox.setPos(50, 150);
        camera.position.set(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);
        statusPollingThread.start();
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        pausePolling = false;
        invisibleButton.setText("Invisible: " + (client.getUserInfo().isInvisibleMode() ? "ON" : "OFF"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        renderer.drawBackground(Assets.menuBackground);
        backButton.draw(renderer);
        addFriendButton.draw(renderer);
        friendListBox.draw(renderer);
        queueButton.draw(renderer);
        invisibleButton.draw(renderer);

        synchronized (friendButtons) {
            for (FriendEntryButton button : friendButtons) {
                button.draw(renderer);
            }
        }

        synchronized (acceptInviteButtons) {
            for (AcceptInviteButton button : acceptInviteButtons) {
                button.draw(renderer);
            }
        }

        renderer.end();

        if (client.isInMatch()) {
            log.info("Detected in-match status, opening game screen");
            synchronized (friendButtons) {
                friendButtons.clear();
            }
            synchronized (acceptInviteButtons) {
                acceptInviteButtons.clear();
            }
            pausePolling = true;
            isInMatchQueue = false;
            queueButton.setText("Find Match");
            game.launchScreen(new OnlineGameScreen(game));
        }

        if (delta <= 0) return;

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }

            if (addFriendButton.isClicked(touchPos.x, touchPos.y)) {
                InputScreen screen = new InputScreen(game, this, "Add friend", "Enter friend's username:");
                screen.setOnInputSubmitted((username) -> {
                    Thread thread = new Thread(() -> {
                        if (client.addFriend(username)) {
                            log.info("Added friend: {}", username);
                            game.backToPreviousScreen();
                        } else {
                            log.warn("Failed to add friend: {}", username);
                            game.replaceCurrentScreen(new AlertScreen(game, this, "Failed", "Check the log", "OK"));
                        }
                    });
                    AlertScreen waitScreen = new AlertScreen(game, this, "Add friend", "Please wait...", "Cancel");
                    waitScreen.setOnConfirm(thread::interrupt);
                    game.replaceCurrentScreen(waitScreen);
                    thread.start();
                    return null;
                });
                game.launchScreen(screen);
            }

            if (invisibleButton.isClicked(touchPos.x, touchPos.y)) {
                boolean newStatus = !client.getUserInfo().isInvisibleMode();
                client.getUserInfo().setInvisibleMode(newStatus);
                invisibleButton.setText("Invisible: " + (newStatus ? "ON" : "OFF"));
                new Thread(() -> {
                    if (!client.updateUserInfo()) {
                        game.launchScreen(new AlertScreen(game, this, "Invisible failed", "Check the log", "OK"));
                    }
                }).start();
            }

            if (queueButton.isClicked(touchPos.x, touchPos.y)) {
                if (isInMatchQueue) {
                    new Thread(() -> {
                        if (client.leaveQueue()) {
                            isInMatchQueue = false;
                            queueButton.setText("Find Match");
                        } else {
                            game.launchScreen(new AlertScreen(game, this, "Leave queue failed", "Check the log", "OK"));
                        }
                    }).start();
                } else {
                    new Thread(() -> {
                        if (client.joinQueue()) {
                            isInMatchQueue = true;
                            queueButton.setText("Cancel...");
                        } else {
                            game.launchScreen(new AlertScreen(game, this, "Join queue failed", "Check the log", "OK"));
                            client.leaveQueue(); // Ensure we are not in queue
                        }
                    }).start();
                }
            }

            synchronized (friendButtons) {
                for (FriendEntryButton button : friendButtons) {
                    if (button.isClicked(touchPos.x, touchPos.y)) {
                        button.onClick(game, this);
                    }
                }
            }
            synchronized (acceptInviteButtons) {
                for (AcceptInviteButton button : acceptInviteButtons) {
                    if (button.isClicked(touchPos.x, touchPos.y)) {
                        button.onClick(game);
                    }
                }
            }

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        statusPollingThread.interrupt();
    }

    private void pollStatus() {
        log.info("Starting status polling thread");
        try {
            while (true) {
                if (pausePolling) {
                    Thread.sleep(POLL_INTERVAL_MS);
                    continue;
                }
                long startTime = System.currentTimeMillis();
                if (client.fetchFriendsList()) {
                    ArrayList<UserInfo> friends = client.getFriendsList();
                    friends.sort((a, b) -> {
                        if (a.isOnline() && !b.isOnline()) return -1;
                        if (!a.isOnline() && b.isOnline()) return 1;
                        return a.getUsername().compareToIgnoreCase(b.getUsername());
                    });
                    synchronized (friendButtons) {
                        friendButtons.clear();
                        for (int i = 0; i < friends.size(); i++) {
                            UserInfo friend = friends.get(i);
                            FriendEntryButton button = new FriendEntryButton(friend.getName(), friend.getUsername(), i, friend.isOnline(), friend.isInMatch());
                            button.averageScore = friend.getAverageScore();
                            button.bestScore = friend.getBestScore();
                            button.gamesPlayed = friend.getTotalGamesPlayed();
                            friendButtons.add(button);
                        }
                    }
                }
                if (client.fetchStatus()) {
                    ArrayList<MatchInvite> invites = client.getMatchInvites();
                    synchronized (acceptInviteButtons) {
                        acceptInviteButtons.clear();
                        for (int i = 0; i < invites.size(); i++) {
                            MatchInvite invite = invites.get(i);
                            AcceptInviteButton button = new AcceptInviteButton(invite.fromPlayerUsername(), i);
                            acceptInviteButtons.add(button);
                        }
                    }
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

    private static class FriendEntryButton extends TextButton {

        public String name;
        public String username;

        public boolean invitable;

        public int averageScore = 0;
        public int bestScore = 0;
        public int gamesPlayed = 0;

        public FriendEntryButton(String name, String username, int row, boolean isOnline, boolean isInMatch) {
            super(
                    name + " (" + username + "): " + (isOnline ? (isInMatch ? "In Match" : "Online") : "Offline"),
                    100, WINDOW_HEIGHT - 200 - (row * 60), 900, 50, false
            );
            this.name = name;
            this.username = username;
            this.invitable = isOnline && !isInMatch;
        }

        public void onClick(Exterminator3618 game, OnlineMenuScreen screen) {
            game.launchScreen(new FriendMenuScreen(game, screen, name, username, averageScore, bestScore, gamesPlayed, invitable));
        }

    }

    private static class AcceptInviteButton extends TextButton {

        public String fromUsername;

        public AcceptInviteButton(String fromUsername, int row) {
            super("Accept match with " + fromUsername, WINDOW_WIDTH - 550, WINDOW_HEIGHT - 50 - (row * 60), 500, 50, true);
            this.fromUsername = fromUsername;
        }

        public void onClick(Exterminator3618 game) {
            new Thread(() -> {
                log.info("Accept match invite from {}", fromUsername);
                game.getApiClient().acceptInvite(fromUsername);
            }).start();

        }

    }

}
