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

import static io.exterminator3618.client.Constants.*;

public class AlertScreen extends OverlayScreen {

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
    private final Vector3 touchPos = new Vector3();
    private final Box box;
    private final TextButton button;

    private Runnable onConfirm = game::backToPreviousScreen;

    protected AlertScreen(Exterminator3618 game, Screen backScreen, String title, String message, String buttonText) {
        super(game, backScreen);
        box = new Box(800, 400, title, message);
        button = new TextButton(buttonText, WINDOW_WIDTH / 2f - 100, WINDOW_HEIGHT / 2f - 140, 200, 60, true);

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
        button.draw(game.getRenderer());
        game.getRenderer().end();

        if (delta <= 0) return;

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (button.isClicked(touchPos.x, touchPos.y)) {
                onConfirm.run();
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

    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

}
