package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kotcrab.vis.ui.VisUI;

import java.io.IOException;

import static io.exterminator3618.client.Constants.*;

public class LoginScreen extends OverlayScreen {

    private static final Logger log = LoggerFactory.getLogger(LoginScreen.class);

    private final Stage stage = new Stage(new ScreenViewport());

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
    private final Vector3 touchPos = new Vector3();
    private final Box box = new Box(500, 400, "Login");
    private final TextButton loginButton = new TextButton("OK", WINDOW_WIDTH / 2f - 185, WINDOW_HEIGHT / 2f - 140, 180, 60, true);
    private final TextButton cancelButton = new TextButton("Cancel", WINDOW_WIDTH / 2f + 5, WINDOW_HEIGHT / 2f - 140, 180, 60, true);

    private final TextField usernameField = new TextField("", VisUI.skin);
    private final TextField passwordField = new TextField("", VisUI.skin);

    protected LoginScreen(Exterminator3618 game, Screen backScreen) {
        super(game, backScreen);

        usernameField.setPosition(WINDOW_WIDTH / 2f - 180, WINDOW_HEIGHT / 2f + 40);
        usernameField.setSize(360, 60);
        usernameField.setMessageText("   Username:");
        usernameField.setBlinkTime(0.3f);
        stage.addActor(usernameField);

        passwordField.setMessageText("   Password:");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setPosition(WINDOW_WIDTH / 2f - 180, WINDOW_HEIGHT / 2f - 40);
        passwordField.setSize(360, 60);
        passwordField.setBlinkTime(0.3f);
        stage.addActor(passwordField);
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera.position.set(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        backScreen.render(0);

        game.getRenderer().drawOverlay(camera, 0.7f);
        viewport.apply();
        camera.update();
        game.getRenderer().begin(camera);
        box.draw(game.getRenderer());
        loginButton.draw(game.getRenderer());
        cancelButton.draw(game.getRenderer());
        game.getRenderer().end();

        stage.act(delta);
        stage.draw();

        if (delta <= 0) return;

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (loginButton.isClicked(touchPos.x, touchPos.y)) {
                onClickLogin();
            } else if (cancelButton.isClicked(touchPos.x, touchPos.y)) {
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
        Gdx.input.setInputProcessor(null);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
    }

    private void onClickLogin() {
        if (usernameField.getText().isEmpty()) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Login Failed", "Empty username", "OK"));
            return;
        } else if (passwordField.getText().isEmpty()) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Login Failed", "Empty password", "OK"));
            return;
        }
        AlertScreen screen = new AlertScreen(game, backScreen, "Login", "Please wait...", "Cancel");
        Thread thread = new Thread(() -> {
            log.info("Attempting login for user: {}", usernameField.getText());
            try {
                ApiClient apiClient = ApiClient.login(game, usernameField.getText(), passwordField.getText());
                game.setApiClient(apiClient);
                game.backToPreviousScreen();
            } catch (IOException | InterruptedException e) {
                log.error("Login failed", e);
                game.replaceCurrentScreen(new AlertScreen(game, this, "Login Failed", "Check the log", "OK"));
            }
        });
        screen.setOnConfirm(() -> {
            log.warn("Login cancelled by user");
            thread.interrupt();
            game.backToPreviousScreen();
        });
        game.replaceCurrentScreen(screen);
        thread.start();
    }

}
