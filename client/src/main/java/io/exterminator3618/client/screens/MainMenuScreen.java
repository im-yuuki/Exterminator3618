package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.api.ApiClient;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.SaveManager;
import io.exterminator3618.client.utils.SoundManager;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;
    private final SoundManager soundManager;
    private final OrthographicCamera camera;
    private final Vector3 touchPos;
    private final Viewport viewport;

    private final TextButton startButton;
    private final TextButton continueButton;
    private final TextButton settingsButton;
    private final TextButton exitButton;

    private final TextButton accountButton1; // Login / Multiplayer
    private final TextButton accountButton2; // Register / Logout

    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        renderer = game.getRenderer();
        soundManager = game.getSoundManager();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);


        /*
        final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
        final int START_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT + PADDING - 200;
        final int CONTINUE_Y = START_Y - Constants.BUTTON_HEIGHT - PADDING;
        final int SETTINGS_Y = CONTINUE_Y - Constants.BUTTON_HEIGHT - PADDING;
        final int EXIT_Y = SETTINGS_Y - Constants.BUTTON_HEIGHT - PADDING;
        */

        int PADDING = 20;
        int col1_x = (Constants.WINDOW_WIDTH / 2) - Constants.BUTTON_WIDTH - (PADDING / 2);
        int col2_x = (Constants.WINDOW_WIDTH / 2) + (PADDING / 2);
        int row1_y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT + PADDING - 200;
        int row2_y = row1_y - Constants.BUTTON_HEIGHT - PADDING;
        int row3_y = row2_y - Constants.BUTTON_HEIGHT - PADDING;

        startButton = new TextButton("Start Game",col1_x, row1_y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        continueButton = new TextButton("Continue", col1_x, row2_y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);

        accountButton1 = new TextButton("Login", col2_x, row1_y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        accountButton2 = new TextButton("Register", col2_x, row2_y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);

        settingsButton = new TextButton("Options", col1_x, row3_y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        exitButton = new TextButton("Exit", col2_x, row3_y, Constants.BUTTON_WIDTH,  Constants.BUTTON_HEIGHT, true);
    }

    @Override
    public void show() {
        soundManager.play("sound/main_menu.mp3", true);

        if (game.getApiClient() != null) {
            accountButton1.setText("Multiplayer");
            accountButton2.setText("Logout: " + game.getApiClient().getUserInfo().getUsername());
        } else {
            accountButton1.setText("Login");
            accountButton2.setText("Register");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        renderer.drawBackground(Assets.menuBackground);
        renderer.drawLogo(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        startButton.draw(renderer);
        if (SaveManager.hasSave(game)) {
            continueButton.draw(renderer);
        }
        accountButton1.draw(renderer);
        accountButton2.draw(renderer);
        settingsButton.draw(renderer);
        exitButton.draw(renderer);
        renderer.end();

        if (delta <= 0) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            soundManager.stop(); // Stop main menu music
            game.launchScreen(new GameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (startButton.isClicked(touchPos.x, touchPos.y)) {
                SaveManager.clearSave(game);
                game.launchScreen(new GameScreen(game));
            }

            if (continueButton.isClicked(touchPos.x, touchPos.y)) {
                soundManager.stop();
                GameScreen gs = new GameScreen(game);
                SaveManager.loadInto(game, gs);
                game.launchScreen(gs);
            }

            if (settingsButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new SettingsScreen(game, this));
            }

            if (exitButton.isClicked(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
            }

            if (accountButton1.isClicked(touchPos.x, touchPos.y)) {
                if (game.getApiClient() != null) {
                    // game.launchScreen(new MultiplayerScreen(game, this));
                } else {
                    game.launchScreen(new LoginScreen(game, this));
                }
            }

            if (accountButton2.isClicked(touchPos.x, touchPos.y)) {
                if (game.getApiClient() != null) {
                    logoutAccount();
                } else {
                    game.launchScreen(new RegisterScreen(game, this));
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
    }

    private void logoutAccount() {
        game.getApiClient().logout();
        game.setApiClient(null);
        game.getPreferences().remove(ApiClient.AUTHTOKEN_KEY);
        show();
    }

}
