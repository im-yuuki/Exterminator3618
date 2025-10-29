package io.exterminator3618.client.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import io.exterminator3618.client.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import io.exterminator3618.client.Constants;
import static io.exterminator3618.client.Constants.*;
import io.exterminator3618.client.Exterminator3618;
import static io.exterminator3618.client.Physics.checkPowerUpCollision;
import io.exterminator3618.client.components.Ball;
import io.exterminator3618.client.components.Brick;
import io.exterminator3618.client.components.Paddle;
import io.exterminator3618.client.components.PowerUp;
import io.exterminator3618.client.components.PowerUpBrick;
import io.exterminator3618.client.components.StrongBrick;
import io.exterminator3618.client.managers.SoundManager;

import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.LevelLoader;
import io.exterminator3618.client.utils.Renderer;

/**
 * Main LibGDX application for the Exterminator3618 client. It owns the renderer
 * and the root game objects and drives the frame loop.
 */
public final class GameScreen implements Screen {

    private static final String[] BRICK_COLORS = {"red", "green", "blue", "yellow", "purple"};

    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);
    private static final Random random = new Random();

    private final Exterminator3618 game;
    private final Renderer renderer;
    private final SoundManager soundManager;

    private Ball ball;
    private List<Brick> bricks;
    private List<Ball> extraBalls;
    private Paddle paddle;
    private int score = 0;
    private int lives = 5;
    private int currentLevel;
    private List<PowerUp> powerUps;
    private List<PowerUp> activePowerUps;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    private TextButton pauseButton;


    public GameScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.currentLevel = 1;
        loadLevel(currentLevel, null);
        soundManager = game.getSoundManager();
        soundManager.setVolume(0.05f);
        soundManager.play("sound/gameplay_bgm.mp3", true);
    }

    public void loadLevel(int levelNumber, Ball oldball) {
        log.info("Loading level {}", levelNumber);
        // Initialize ball
        if (ball != null){
            this.ball = oldball;
            log.debug("Create newball as oldball - Combo:" + ball.getComboCount() + " / " + oldball.getComboCount());
        } else {
            this.ball = new Ball(
                    WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                    WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                    BALL_WIDTH,
                    BALL_HEIGHT,
                    BALL_REGION_NAME, // object name
                    BALL_SPEED,
                    67
            );
        }

        // Initialize extra balls list
        extraBalls = new ArrayList<>();

        // Initialize powerups list
        powerUps = new ArrayList<>();
        // Active power-ups (timed)
        activePowerUps = new ArrayList<>();

        // Initialize paddle
        paddle = new Paddle(
                PADDLE_START_X,
                PADDLE_START_Y,
                PADDLE_WIDTH,
                PADDLE_HEIGHT,
                PADDLE_REGION_NAME
        );

        bricks = LevelLoader.load(getClass().getResourceAsStream(
                String.format("/levels/level%d.dat", levelNumber)
        ));

    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
        touchPos = new Vector3();
        pauseButton = new TextButton("Pause", 1545, 900, 300, 75, true);
    }

    /**
     * Frame callback: updates and renders the scene.
     */
    @Override
    public void render(float deltaTime) {
        // Update game logic
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ball.update(deltaTime);
        paddle.update(deltaTime);

        // Make stuck balls follow the paddle position at their stuck offset
        if (ball.isStuckToPaddle()) {
            int followX = paddle.getX() + paddle.getWidth() / 2 + ball.getStuckOffsetX() - ball.getWidth() / 2;
            int followY = paddle.getY() + paddle.getHeight();
            ball.setPosition(followX, followY);
        }
        for (Ball extraBall : extraBalls) {
            if (extraBall.isStuckToPaddle()) {
                int followX = paddle.getX() + paddle.getWidth() / 2 + extraBall.getStuckOffsetX() - extraBall.getWidth() / 2;
                int followY = paddle.getY() + paddle.getHeight();
                extraBall.setPosition(followX, followY);
            }
        }

        // Mạng
        if (ball.getY() <= 0 && extraBalls.isEmpty()) {
            lives--; // Trừ 1 mạng
            soundManager.play("sound/lose_heart.wav", false);
            ball.resetCombo();

            if (lives <= 0) {
                soundManager.play("sound/gameover_sfx.wav", false);
                gotoGameOverScreen();
            } else {
                // Reset lại vị trí bóng và paddle để chơi tiếp màn hiện tại
                ball.resetToCenter(paddle);
            }
        }

        // CẬP NHẬT TẤT CẢ BÓNG PHỤ
        for (Ball extraBall : extraBalls) {
            extraBall.update(deltaTime);
        }
        updateExtraBalls(deltaTime);

        for (Brick brick : bricks) {
            brick.update(deltaTime);
        }

        updatePowerUps(deltaTime);
        updateActivePowerUps(deltaTime);
        checkBallBrickCollisions();
        ball.checkPaddleCollision(paddle);

        // KIỂM TRA VA CHẠM PADDLE CHO TẤT CẢ BÓNG
        //ball.checkPaddleCollision(paddle); // vcl vibe code
        for (Ball extraBall : extraBalls) {
            extraBall.checkPaddleCollision(paddle);
        }

        // Render game objects
        viewport.apply();
        camera.update();
        renderer.begin(camera);
        renderer.drawBackground(Assets.gameBackground);
        renderer.draw(ball);
        renderer.draw(paddle);
        for (PowerUp powerUp : powerUps) {
            renderer.draw(powerUp);
        }

        // Vẽ bóng phụ
        for (Ball extraBall : extraBalls) {
            renderer.draw(extraBall);
        }

        for (Brick brick : bricks) {
            renderer.draw(brick);
        }

        renderer.setFontSize(50);
        renderer.drawTextMiddle("SCORE: " + score, 1680, WINDOW_HEIGHT - 600);
        renderer.drawTextMiddle("COMBO: " + ball.getComboCount(), 1680, WINDOW_HEIGHT - 460);
        renderer.drawTextMiddle("POWER UP", 1680, WINDOW_HEIGHT - 750);
        renderer.setFontSize(40);
        renderer.drawTextMiddle("LIVES", 1680, WINDOW_HEIGHT - 290);
        for (int i = 0; i < lives; i++) {
            renderer.drawLives(1600 + i * 30, WINDOW_HEIGHT - 350);
        }


        pauseButton.draw(renderer);
        // (Optional) Could display active power-up timers here if desired

        renderer.setFontSize(36);
        renderer.end();
        // Handle sticky paddle space key input
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (ball.isStuckToPaddle()) {
                ball.launchFromPaddle();
                log.debug("Ball launched from sticky paddle!");
            }
            // Also check extra balls
            for (Ball extraBall : extraBalls) {
                if (extraBall.isStuckToPaddle()) {
                    extraBall.launchFromPaddle();
                    log.debug("Extra ball launched from sticky paddle!");
                }
            }
        }

        // Pause game on input
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.launchScreen(new PauseScreen(game, this));
        }

        if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (pauseButton.isClicked(touchPos.x, touchPos.y)) {
                game.launchScreen(new PauseScreen(game, this));
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

    /**
     * Releases resources and other disposable assets.
     */
    @Override
    public void dispose() {
        bricks.clear();
        extraBalls.clear();
        //Assets.dispose();
        log.info("Game disposed");
    }

    private void gotoVictoryScreen() {
        // TODO: implement victory screen transition
    }

    private void gotoGameOverScreen() {
        game.launchScreen(new GameOverScreen(game));
        soundManager.dispose();
        //game.replaceCurrentScreen(new GameOverScreen(game));
    }

    /**
     * THÊM PHƯƠNG THỨC MỚI
     * Spawns three extra balls at the location of a destroyed brick.
     *
     * @param x The x-coordinate of the spawn location.
     * @param y The y-coordinate of the spawn location.
     */
    public void spawnExtraBalls(int x, int y) {
        log.info("Spawning 3 extra balls!");

        // Vị trí spawn có thể đặt lại ở tâm viên gạch
        int spawnY = y + (BRICK_HEIGHT / 2);

        // Tạo 3 bóng với 3 góc khác nhau
        Ball ball1 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, EXTRA_BALL_REGION_NAME, BALL_SPEED, 45);
        extraBalls.add(ball1);

        Ball ball2 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, EXTRA_BALL_REGION_NAME, BALL_SPEED, 90);
        extraBalls.add(ball2);

        Ball ball3 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, EXTRA_BALL_REGION_NAME, BALL_SPEED, 135);
        extraBalls.add(ball3);
    }

    /**
     * THÊM PHƯƠNG THỨC MỚI
     * Updates extra balls and removes them if they fall off the bottom of the screen.
     *
     * @param deltaTime The time since the last frame.
     */
    private void updateExtraBalls(float deltaTime) {
        Iterator<Ball> iterator = extraBalls.iterator();
        while (iterator.hasNext()) {
            Ball extraBall = iterator.next();
            // Nếu bóng chạm đáy màn hình
            if (extraBall.getY() <= 0) {
                iterator.remove(); // Xóa bóng
                log.debug("Extra ball removed. Remaining: {}", extraBalls.size());
            }
        }
    }

    private void updatePowerUps(float deltaTime) {
        // Update falling power-ups and handle collection
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update(deltaTime);

            // Remove if it falls off-screen
            if (powerUp.getY() + powerUp.getHeight() < 0) {
                iterator.remove();
                continue;
            }

            // Collect on paddle collision
            if (checkPowerUpCollision(powerUp, paddle)) {
                // If this is an instant power-up, apply immediately and do not track
                soundManager.play("sound/collected_and_level.wav");
                if (powerUp.isInstant()) {
                    powerUp.applyEffect(this);
                } else {
                    PowerUp existing = null;
                    for (PowerUp apu : activePowerUps) {
                        if (apu.getType().equals(powerUp.getType())) {
                            existing = apu;
                            break;
                        }
                    }
                    if (existing != null) {
                        existing.resetRemainingDuration();
                    } else {
                        powerUp.applyEffect(this);
                        powerUp.resetRemainingDuration();
                        activePowerUps.add(powerUp);
                    }
                }
                // Remove the falling item regardless
                iterator.remove();
            }
        }
    }

    private void updateActivePowerUps(float deltaTime) {
        if (activePowerUps == null || activePowerUps.isEmpty()) return;
        Iterator<PowerUp> it = activePowerUps.iterator();
        while (it.hasNext()) {
            PowerUp apu = it.next();
            apu.decreaseDuration(deltaTime);
            if (apu.isExpired()) {
                apu.removeEffect(this);
                it.remove();
            }
        }
    }

    /**
     * Checks for collisions between any ball and all bricks.
     * Handles brick destruction and ball bouncing.
     * This corrected version iterates through each brick and checks against all balls.
     */
    private void checkBallBrickCollisions() {
        // Thoát sớm nếu không còn gạch
        if (bricks.isEmpty()) {
            return;
        }

        // Tạo danh sách tất cả các bóng để kiểm tra
        List<Ball> allBalls = new ArrayList<>(extraBalls);
        allBalls.add(ball);

        // Dùng iterator cho bricks để có thể xóa an toàn khi đang duyệt
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            // Bỏ qua gạch đã bị phá hủy (để phòng vệ)
            if (brick.isDestroyed()) {
                brickIterator.remove();
                continue;
            }

            // Với mỗi viên gạch, kiểm tra va chạm với TẤT CẢ các quả bóng
            for (Ball currentBall : allBalls) {
                if (currentBall.collidesWith(brick)) {

                    // 1. Xử lý bóng nảy lại
                    currentBall.handleBrickCollision(brick);

                    // 2. Gạch nhận sát thương
                    boolean wasDestroyed = brick.takeHit();

                    if (wasDestroyed) {
                        ball.incrementCombo();
                        score += 10 * ball.getComboCount();
                        // Nếu gạch bị phá hủy, kiểm tra xem có phải loại đặc biệt không
                        if ("multiball".equals(brick.getType())) {
                            spawnExtraBalls(brick.getX() + brick.getWidth() / 2, brick.getY());
                        } else if (brick instanceof PowerUpBrick) {
                            PowerUp powerUp = PowerUp.createRandomPowerUp(brick.getX() + brick.getWidth() / 2 - Constants.POWERUP_WIDTH / 2,
                                    brick.getY() + brick.getHeight() / 2 - Constants.POWERUP_HEIGHT / 2);
                            powerUps.add(powerUp);
                            log.debug("PowerUp created at position ({}, {})", powerUp.getX(), powerUp.getY());
                        } else if (brick instanceof StrongBrick) {
                            score += 10 * ball.getComboCount();
                        }

                        // Xóa gạch khỏi danh sách
                        brickIterator.remove();

                        // KIỂM TRA ĐIỀU KIỆN THẮNG MÀN
                        if (levelClear()) {
                            currentLevel++;
                            soundManager.play("sound/collected_and_level.wav");
                            ball.resetToCenter(paddle);
                            // Giả sử bạn có 2 level, đánh số 1 và 2
                            if (currentLevel > 2) {
                                gotoVictoryScreen();
                            } else {
                                // Tải màn chơi tiếp theo
                                loadLevel(currentLevel, ball);
                            }
                        }

                        // log
                        log.debug("Brick destroyed! Remaining bricks: {}", bricks.size());
                    } else {
                        // Gạch vẫn còn máu
                        log.debug("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(),
                                brick.getType().equals("strong") ? 3 : 1);
                    }

                    // 3. QUAN TRỌNG: Thoát khỏi vòng lặp kiểm tra bóng
                    // Vì viên gạch này đã được xử lý va chạm rồi.
                    // Điều này ngăn một viên gạch bị nhiều bóng phá hủy trong cùng một frame.
                    break;
                }
            }
        }
    }

    public Paddle getPaddle(){
        return paddle;
    }

    public Ball getBall(){
        return ball;
    }

    public int getLives(){
        return lives;
    }

    public void setLives(int lives){
        if (lives > 5) {
            this.lives = 5;
        } else {
            this.lives = lives;
        }
    }

    public List<Ball> getExtraBalls() {
        return extraBalls;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public boolean isPowerUpTypeExist(String type) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean levelClear() {
        if (bricks.isEmpty()) {
            return true;
        }
        for (Brick brick : bricks) {
            if (!brick.getType().equals("solid_brick")) {
                return false;
            }
        }
        return true;
    }

    public List<Ball> getExtraBall(){
        return extraBalls;
    }

}
