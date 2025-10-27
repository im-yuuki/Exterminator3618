package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Renderer;

public final class SettingsScreen implements Screen {

    private final Exterminator3618 game;
    private Renderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton backButton;
    public SettingsScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = new Renderer();
        this.camera = new OrthographicCamera();

        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        touchPos = new Vector3();
        backButton = new TextButton("Back", 100, 300, 200, 50);
    }

    @Override
    public void show() {
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void render(float delta) {
        camera.update();
        viewport.apply();
        renderer.begin(camera);
        // Draw text here
        backButton.draw(renderer);
        renderer.end();
        if (Gdx.input.justTouched()) {
            // Lấy tọa độ nhấp chuột trên màn hình
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Chuyển đổi tọa độ màn hình -> tọa độ thế giới game
            viewport.unproject(touchPos);

            // Bây giờ mới kiểm tra click
            if(backButton.isClicked(touchPos.x, touchPos.y)) {
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

    }

    @Override
    public void dispose() {

    }

}
