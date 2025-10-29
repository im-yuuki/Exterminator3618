package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.Box;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.Settings;
import io.exterminator3618.client.managers.SoundManager;

public final class SettingsScreen implements Screen {

    private final Exterminator3618 game;
    private Renderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();
    private Screen previousScreen;
    private SoundManager soundManager;

    private TextButton backButton;
    private TextButton enableMusicButton;
    private Box box;
    final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
    final int BACK_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT - 20;

    public SettingsScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.camera = new OrthographicCamera();
        this.soundManager = game.getSoundManager();

        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        backButton = new TextButton("Back", 100, 300, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        enableMusicButton = new TextButton(String.format("Music: %s", (Settings.enableMusic ? "On" : "Off")), CENTER_X, BACK_Y - Constants.BUTTON_HEIGHT - 20, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        box = new Box(800,500,"SETTINGS");
    }
    public SettingsScreen(Exterminator3618 game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.renderer = game.getRenderer();
        this.camera = new OrthographicCamera();
        this.soundManager = game.getSoundManager();

        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        backButton = new TextButton("Back", CENTER_X, BACK_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        enableMusicButton = new TextButton(String.format("Music: %s", (Settings.enableMusic ? "On" : "Off")), CENTER_X, BACK_Y - Constants.BUTTON_HEIGHT - 20, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        box = new Box(800,500,"SETTINGS");
    }

    @Override
    public void show() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
    }


    @Override
    public void render(float delta) {
        previousScreen.render(0);
        renderer.drawOverlay(camera, 0.7f);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        // Draw text here
        box.draw(renderer);
        // Update button text to reflect current setting
        enableMusicButton.text = String.format("Music: %s", (Settings.enableMusic ? "On" : "Off"));
        enableMusicButton.draw(renderer);
        backButton.draw(renderer);
        renderer.end();
        if (Gdx.input.justTouched()) {
            // Lấy tọa độ nhấp chuột trên màn hình
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Chuyển đổi tọa độ màn hình -> tọa độ thế giới game
            viewport.unproject(touchPos);

            // Bây giờ mới kiểm tra click
            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }

            if (enableMusicButton.isClicked(touchPos.x, touchPos.y)) {
                Settings.enableMusic = !Settings.enableMusic;
                if (Settings.enableMusic) {
                    soundManager.setVolume(0.1f);
                } else {
                    soundManager.setVolume(0f);
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

}
