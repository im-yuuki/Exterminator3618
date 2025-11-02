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

public class RegisterScreen extends OverlayScreen {
    
    private static final Logger log = LoggerFactory.getLogger(RegisterScreen.class);

    private final Stage stage = new Stage(new ScreenViewport());
    private final TextField nameField = new TextField("", VisUI.skin);
    private final TextField usernameField = new TextField("", VisUI.skin);
    private final TextField passwordField = new TextField("", VisUI.skin);
    private final TextField confirmPasswordField = new TextField("", VisUI.skin);

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
    private final Vector3 touchPos = new Vector3();
    private final Box box = new Box(500, 540, "Register");
    private final TextButton RegisterButton = new TextButton("OK", WINDOW_WIDTH / 2f - 185, WINDOW_HEIGHT / 2f - 200, 180, 60, true);
    private final TextButton cancelButton = new TextButton("Cancel", WINDOW_WIDTH / 2f + 5, WINDOW_HEIGHT / 2f - 200, 180, 60, true);

    protected RegisterScreen(Exterminator3618 game, Screen backScreen) {
        super(game, backScreen);

        nameField.setPosition(WINDOW_WIDTH / 2f - 180, WINDOW_HEIGHT / 2f + 120);
        nameField.setSize(360, 60);
        nameField.setMessageText("   Name:");
        nameField.setBlinkTime(0.3f);
        stage.addActor(nameField);

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

        confirmPasswordField.setMessageText("   Confirm password:");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.setPosition(WINDOW_WIDTH / 2f - 180, WINDOW_HEIGHT / 2f - 120);
        confirmPasswordField.setSize(360, 60);
        confirmPasswordField.setBlinkTime(0.3f);
        stage.addActor(confirmPasswordField);
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
        RegisterButton.draw(game.getRenderer());
        cancelButton.draw(game.getRenderer());
        game.getRenderer().end();

        stage.act(delta);
        stage.draw();

        if (delta <= 0) return;

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (RegisterButton.isClicked(touchPos.x, touchPos.y)) {
                onClickRegister();
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
    
    public void onClickRegister() {
        if (nameField.getText().isEmpty()) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Register Failed", "Empty name", "OK"));
            return;
        }
        if (usernameField.getText().isEmpty()) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Register Failed", "Empty username", "OK"));
            return;
        } else if (passwordField.getText().isEmpty()) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Register Failed", "Empty password", "OK"));
            return;
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            game.replaceCurrentScreen(new AlertScreen(game, this, "Register Failed", "Passwords do not match", "OK"));
            return;
        }
        AlertScreen screen = new AlertScreen(game, backScreen, "Register", "Please wait...", "Cancel");
        Thread thread = new Thread(() -> {
            log.info("Attempting to register user: {}", usernameField.getText());
            try {
                ApiClient apiClient = ApiClient.register(game, nameField.getText(), usernameField.getText(), passwordField.getText());
                game.setApiClient(apiClient);
                game.backToPreviousScreen();
            } catch (IOException | InterruptedException e) {
                log.error("Register failed", e);
                game.replaceCurrentScreen(new AlertScreen(game, this, "Register Failed", "Check the log", "OK"));
            }
        });
        screen.setOnConfirm(() -> {
            log.warn("Register cancelled by user");
            thread.interrupt();
            game.backToPreviousScreen();
        });
        game.replaceCurrentScreen(screen);
        thread.start();
    }

}
