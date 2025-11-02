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
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.SoundManager;

public final class SettingsScreen extends OverlayScreen {

    private Renderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();
    private SoundManager soundManager;

    private TextButton backButton;
    private TextButton enableMusicButton;
    private Box box;
    final int CENTER_X = (Constants.WINDOW_WIDTH / 2) - (Constants.BUTTON_WIDTH / 2);
    final int BACK_Y = (Constants.WINDOW_HEIGHT / 2) + Constants.BUTTON_HEIGHT - 20;

    public SettingsScreen(Exterminator3618 game) {
        super(game, null);
        this.renderer = game.getRenderer();
        this.camera = new OrthographicCamera();
        this.soundManager = game.getSoundManager();

        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        backButton = new TextButton("Back", 100, 300, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        enableMusicButton = new TextButton(String.format("Music: %s", (isMusicEnabled() ? "On" : "Off")), CENTER_X, BACK_Y - Constants.BUTTON_HEIGHT - 20, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        box = new Box(800,500,"SETTINGS");
    }

    public SettingsScreen(Exterminator3618 game, Screen previousScreen) {
        super(game, previousScreen);
        this.renderer = game.getRenderer();
        this.camera = new OrthographicCamera();
        this.soundManager = game.getSoundManager();

        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        backButton = new TextButton("Back", CENTER_X, BACK_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
        enableMusicButton = new TextButton(String.format("Music: %s", (isMusicEnabled() ? "On" : "Off")), CENTER_X, BACK_Y - Constants.BUTTON_HEIGHT - 20, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT, true);
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
        // Only render game screen if previous screen is PauseScreen
        if (backScreen != null) {
            if (backScreen instanceof PauseScreen) {
                ((PauseScreen) backScreen).getGameScreen().render(0);
            } else {
                backScreen.render(0);
            }
        }
        renderer.drawOverlay(camera, 0.7f);
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        box.draw(renderer);
        // Update button text to reflect current setting
        enableMusicButton.setText(String.format("Music: %s", (isMusicEnabled() ? "On" : "Off")));
        enableMusicButton.draw(renderer);
        backButton.draw(renderer);
        renderer.end();

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (backButton.isClicked(touchPos.x, touchPos.y)) {
                game.backToPreviousScreen();
            }

            if (enableMusicButton.isClicked(touchPos.x, touchPos.y)) {
                setMusicEnabled(!isMusicEnabled());
                if (isMusicEnabled()) {
                    soundManager.setVolume(1f);
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

    private boolean isMusicEnabled() {
        return game.getPreferences().getBoolean("music_enabled", true);
    }

    private void setMusicEnabled(boolean enabled) {
        game.getPreferences().putBoolean("music_enabled", enabled);
        game.getPreferences().flush();
    }

}
