package io.exterminator3618.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.utils.Renderer;

public final class MainMenuScreen implements Screen {

    private final Exterminator3618 game;
    private final Renderer renderer;
    private OrthographicCamera camera;
    private Vector3 touchPos = new Vector3();

    private TextButton startButton;
    private TextButton settingsButton;
    public MainMenuScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        touchPos = new Vector3();
        startButton = new TextButton("Start Game", 100, 300, 200, 50);
        settingsButton = new TextButton("Options", 100, 240, 200, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.begin(camera);
        // Draw text here
        renderer.drawText("Day la Main Screen", 300, 300);
        startButton.draw(renderer);
        settingsButton.draw(renderer);

        renderer.end();
        // 3. Xử lý Input (Logic)
        if (Gdx.input.justTouched()) { // Chỉ kiểm tra khi người dùng vừa nhấp
            // Lấy tọa độ nhấp chuột trên màn hình
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            // Chuyển đổi tọa độ màn hình -> tọa độ thế giới game (quan trọng!)
            camera.unproject(touchPos);

            // Transition to game screen on input
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                game.launchScreen(new GameScreen(game));
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
            } else if (startButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new GameScreen(game));
            }

            // Transition to setting screen on input
            if (settingsButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new SettingsScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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
