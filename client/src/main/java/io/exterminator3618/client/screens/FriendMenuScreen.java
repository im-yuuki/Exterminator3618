package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;

import static io.exterminator3618.client.Constants.WINDOW_HEIGHT;
import static io.exterminator3618.client.Constants.WINDOW_WIDTH;

public class FriendMenuScreen extends OverlayScreen {

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
    private final Vector3 touchPos = new Vector3();
    private final Box box;
    private final TextButton inviteButton;

    private static final int posY = WINDOW_HEIGHT / 2 - 140;

    private final TextButton backButton = new TextButton("Back", WINDOW_WIDTH / 2f - 545, posY, 350, 60, true);
    private final TextButton deleteFriendButton = new TextButton("Delete Friend", WINDOW_WIDTH / 2f + 195, posY, 350, 60, true);

    private final String friendUsername;

    protected FriendMenuScreen(Exterminator3618 game, Screen backScreen, String friendName, String friendUsername, int playedMatches, int bestScore, int avgScore, boolean invitable) {
        super(game, backScreen);
        this.friendUsername = friendUsername;
        box = new Box(1400, 400, "About " + friendName,
                String.format("%s (%s) - PLAYED: %d | BEST: %d | AVG: %d", friendName, friendUsername, playedMatches, bestScore, avgScore)
        );
        if (invitable) {
            inviteButton = new TextButton("Invite Match", WINDOW_WIDTH / 2f - 175, posY, 350, 60, true);
        } else {
            inviteButton = null;
        }
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera.position.set(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void render(float delta) {
        backScreen.render(0);

        game.getRenderer().drawOverlay(camera, 0.7f);
        viewport.apply();
        camera.update();
        game.getRenderer().begin(camera);
        box.draw(game.getRenderer());
        if (inviteButton != null) inviteButton.draw(game.getRenderer());
        deleteFriendButton.draw(game.getRenderer());
        backButton.draw(game.getRenderer());
        game.getRenderer().end();

        if (delta <= 0) return;

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (deleteFriendButton.isClicked(touchPos.x, touchPos.y)) {
                new Thread(() -> {
                   if (game.getApiClient().removeFriend(friendUsername)) {
                       game.backToPreviousScreen();
                   } else {
                       game.replaceCurrentScreen(new AlertScreen(game, backScreen, "Error", "Failed to delete friend.", "OK"));
                   }
                }).start();
            }

            if (inviteButton != null && inviteButton.isClicked(touchPos.x, touchPos.y)) {
                new Thread(() -> {
                    if (game.getApiClient().inviteFriendToMatch(friendUsername)) {
                        game.backToPreviousScreen();
                    } else {
                        game.replaceCurrentScreen(new AlertScreen(game, backScreen, "Error", "Send match invite to " + friendUsername + " failed.", "OK"));
                    }
                }).start();
            }

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
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
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
    }

}
