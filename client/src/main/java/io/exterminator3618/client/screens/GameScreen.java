package io.exterminator3618.client.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.exterminator3618.client.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.exterminator3618.client.Constants;
import static io.exterminator3618.client.Constants.*;
import io.exterminator3618.client.Exterminator3618;
import io.exterminator3618.client.components.Ball;
import io.exterminator3618.client.components.Brick;
import io.exterminator3618.client.components.ExtraLifePowerUp;
import io.exterminator3618.client.components.HeavyBallPowerUp;
import io.exterminator3618.client.components.MultiBallBrick;
import io.exterminator3618.client.components.NormalBrick;
import io.exterminator3618.client.components.Paddle;
import io.exterminator3618.client.components.PowerUp;
import io.exterminator3618.client.components.PowerUpBrick;
import io.exterminator3618.client.components.SlowBallPowerUp;
import io.exterminator3618.client.components.SolidBrick;
import io.exterminator3618.client.components.SplitBallPowerUp;
import io.exterminator3618.client.components.StickyPaddlePowerUp;
import io.exterminator3618.client.components.StrongBrick;
import io.exterminator3618.client.components.TextButton;
import io.exterminator3618.client.components.WidenPaddlePowerUp;
import io.exterminator3618.client.utils.SoundManager;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.GameSaveData;
import io.exterminator3618.client.utils.LevelLoader;
import static io.exterminator3618.client.utils.Physics.checkPowerUpCollision;
import io.exterminator3618.client.utils.Renderer;

/**
 * Main LibGDX application for the Exterminator3618 client. It owns the renderer
 * and the root game objects and drives the frame loop.
 */
public class GameScreen implements Screen {

    private static final String[] BRICK_COLORS = {"red", "green", "blue", "yellow", "purple"};

    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);
    private static final Random random = new Random();

    protected final Exterminator3618 game;
    protected final Renderer renderer;
    private final SoundManager soundManager;

    private Ball ball;
    private List<Brick> bricks;
    private List<Ball> extraBalls;
    private Paddle paddle;
    protected int score = 0;
    protected int lives = 5;
    private int currentLevel;
    private List<PowerUp> powerUps;
    private List<PowerUp> activePowerUps;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touchPos = new Vector3();

    protected TextButton pauseButton;


    public GameScreen(Exterminator3618 game) {
        this.game = game;
        this.renderer = game.getRenderer();
        this.currentLevel = 0;
        loadLevel(currentLevel, null);
        soundManager = game.getSoundManager();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, camera);
        camera.position.set(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2, 0);
        touchPos = new Vector3();
        pauseButton = new TextButton("Pause", 1545, 900, 300, 75, true);
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

        ball.setStuckToPaddle(true);
    }


    public GameSaveData.SaveData exportState() {
        GameSaveData.SaveData d = new GameSaveData.SaveData();
        d.level = currentLevel;
        d.score = score;
        d.lives = lives;

        GameSaveData.Obj b = new GameSaveData.Obj();
        b.x = ball.getX();
        b.y = ball.getY();
        b.width = ball.getWidth();
        b.height = ball.getHeight();
        b.vx = ball.getVelocityX();
        b.vy = ball.getVelocityY();
        b.region = ball.getRegionName();
        b.bool1 = ball.isHeavyBall();
        b.bool2 = ball.isStuckToPaddle();
        b.i1 = ball.getStuckOffsetX();
        b.cb = ball.getComboCount();
        d.ball = b;

        GameSaveData.Obj p = new GameSaveData.Obj();
        p.x = paddle.getX();
        p.y = paddle.getY();
        p.width = paddle.getWidth();
        p.height = paddle.getHeight();
        p.region = paddle.getRegionName();
        p.bool1 = paddle.isSticky();
        d.paddle = p;

        d.extraBalls = new ArrayList<>();
        for (Ball eb : extraBalls) {
            GameSaveData.Obj o = new GameSaveData.Obj();
            o.x = eb.getX();
            o.y = eb.getY();
            o.width = eb.getWidth();
            o.height = eb.getHeight();
            o.vx = eb.getVelocityX();
            o.vy = eb.getVelocityY();
            o.region = eb.getRegionName();
            o.bool1 = eb.isHeavyBall();
            o.bool2 = eb.isStuckToPaddle();
            o.i1 = eb.getStuckOffsetX();
            d.extraBalls.add(o);
        }

        d.bricks = new ArrayList<>();
        for (Brick bk : bricks) {
            if (bk.isDestroyed()) continue;
            GameSaveData.BrickState bs = new GameSaveData.BrickState();
            bs.x = bk.getX();
            bs.y = bk.getY();
            bs.width = bk.getWidth();
            bs.height = bk.getHeight();
            bs.region = bk.getRegionName();
            bs.hp = bk.getHitPoints();
            bs.type = bk.getType();
            d.bricks.add(bs);
        }

        d.activePowerUps = new ArrayList<>();
        if (activePowerUps != null) {
            for (PowerUp apu : activePowerUps) {
                GameSaveData.PowerUpState ps = new GameSaveData.PowerUpState();
                ps.type = apu.getType();
                ps.remaining = apu.getRemainingDuration();
                d.activePowerUps.add(ps);
            }
        }
        return d;
    }

    public void importState(GameSaveData.SaveData d) {
        this.currentLevel = Math.max(1, d.level);
        this.score = Math.max(0, d.score);
        setLives(d.lives);

        // fresh lists
        powerUps = new ArrayList<>();
        activePowerUps = new ArrayList<>();
        extraBalls = new ArrayList<>();

        // paddle
        paddle = new Paddle(d.paddle.x, d.paddle.y, d.paddle.width, d.paddle.height, d.paddle.region);
        paddle.setSticky(d.paddle.bool1);

        // ball
        ball = new Ball(d.ball.x, d.ball.y, d.ball.width, d.ball.height, d.ball.region, BALL_SPEED, 90);
        ball.setVelocity(d.ball.vx, d.ball.vy);
        ball.setHeavyBall(d.ball.bool1);
        ball.setStuckToPaddle(d.ball.bool2);
        ball.setStuckOffsetX(d.ball.i1);
        ball.setComboCount(d.ball.cb);

        // extra balls
        for (GameSaveData.Obj o : d.extraBalls) {
            Ball eb = new Ball(o.x, o.y, o.width, o.height, o.region, BALL_SPEED, 90);
            eb.setVelocity(o.vx, o.vy);
            eb.setHeavyBall(o.bool1);
            eb.setStuckToPaddle(o.bool2);
            eb.setStuckOffsetX(o.i1);
            extraBalls.add(eb);
        }

        // bricks
        bricks = new ArrayList<>();
        for (GameSaveData.BrickState bs : d.bricks) {
            switch (bs.type) {
                case "solid_brick":
                    Brick sb = new SolidBrick(bs.x, bs.y);
                    bricks.add(sb);
                    break;
                case "normal":
                    Brick nb = new NormalBrick(bs.x, bs.y, bs.width, bs.height, bs.region);
                    bricks.add(nb);
                    break;
                case "multiball":
                    Brick mb = new MultiBallBrick(bs.x, bs.y);
                    bricks.add(mb);
                    break;
                case "strong":
                    Brick stb = new StrongBrick(bs.x, bs.y, bs.width, bs.height, bs.region, bs.hp);
                    bricks.add(stb);
                    break;
                default:
                    // Unknown brick type; skip
                    break;
            }
        }

        // active powerups: re-apply with saved remaining duration
        if (d.activePowerUps != null) {
            for (GameSaveData.PowerUpState ps : d.activePowerUps) {
                PowerUp pu = createPowerUpByType(ps.type);
                if (pu != null) {
                    pu.applyEffect(this);
                    pu.setRemainingDuration(ps.remaining);
                    activePowerUps.add(pu);
                }
            }
        }
    }

    private PowerUp createPowerUpByType(String type) {
        switch (type) {
            case "Widen Paddle":
                return new WidenPaddlePowerUp(0, 0);
            case "Heavy Ball":
                return new HeavyBallPowerUp(0, 0);
            case "Sticky Paddle":
                return new StickyPaddlePowerUp(0, 0);
            case "Extra Life":
                return new ExtraLifePowerUp(0, 0);
            case "Split Ball":
                return new SplitBallPowerUp(0, 0);
            case "Slow Ball":
                return new SlowBallPowerUp(0, 0);
            default:
                return null;
        }
    }

    @Override
    public void show() {
        soundManager.play("sound/gameplay_bgm.mp3", true);

    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // deltaTime > 0 (not paused)
        if (deltaTime > 0) {
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

            // Lives
            if (ball.getY() <= 0){
                ball.setY(-100);
            }

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

            // Update extraballs
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


            for (Ball extraBall : extraBalls) {
                extraBall.checkPaddleCollision(paddle);
            }
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

        // Draw extraballs
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
        renderer.setFontSize(24);
        for (int i = 0; i < activePowerUps.size(); i++) {
            PowerUp p = activePowerUps.get(i);
            renderer.drawTextMiddle(String.format("%s: %.2f", p.getType(), p.getRemainingDuration()), 1680, WINDOW_HEIGHT - 750 - 40 * (i + 1));
        }
        renderer.setFontSize(40);
        renderer.drawTextMiddle("LIVES", 1680, WINDOW_HEIGHT - 290);
        for (int i = 0; i < lives; i++) {
            renderer.drawLives(1600 + i * 30, WINDOW_HEIGHT - 350);
        }


        pauseButton.draw(renderer);
        // (Optional) Could display active power-up timers here if desired

        renderer.setFontSize(36);
        renderer.end();

        if (deltaTime > 0) {
            // Handle sticky paddle space key input
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (ball.isStuckToPaddle()) {
                    ball.launchFromPaddle();
                    log.debug("Ball launched from sticky paddle!");
                }
                // Check extra balls
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
                if (ball.isStuckToPaddle()) {
                    ball.launchFromPaddle();
                }
                for (Ball extraBall : extraBalls) {
                    if (extraBall.isStuckToPaddle()) {
                        extraBall.launchFromPaddle();
                    }
                }
                
                if (pauseButton.isClicked(touchPos.x, touchPos.y)) {
                    onPauseButtonClicked();
                }
            }
        }

    }

    protected void onPauseButtonClicked() {
        game.launchScreen(new PauseScreen(game, this));
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

    protected void gotoVictoryScreen() {
        game.launchScreen(new VictoryScreen(game));
    }

    protected void gotoWinLevelScreen(int level) {
        game.launchScreen(new WinLevelScreen(game, level, this));
    }

    protected void gotoGameOverScreen() {
        game.launchScreen(new GameOverScreen(game));
        //soundManager.dispose();
        soundManager.stop();
    }

    /**
     * Spawns three extra balls at the location of a destroyed brick.
     *
     * @param x The x-coordinate of the spawn location.
     * @param y The y-coordinate of the spawn location.
     */
    public void spawnExtraBalls(int x, int y) {
        log.info("Spawning 3 extra balls!");
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
        if (bricks.isEmpty()) {
            return;
        }

        List<Ball> allBalls = new ArrayList<>(extraBalls);
        allBalls.add(ball);

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            if (brick.isDestroyed()) {
                brickIterator.remove();
                continue;
            }

            for (Ball currentBall : allBalls) {
                if (currentBall.collidesWith(brick)) {
                    currentBall.handleBrickCollision(brick);
                    boolean wasDestroyed = brick.takeHit();

                    if (wasDestroyed) {
                        ball.incrementCombo();
                        score += 10 * ball.getComboCount();
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

                        brickIterator.remove();

                        if (levelClear()) {

                            gotoWinLevelScreen(currentLevel);
                            soundManager.play("sound/collected_and_level.wav");
                            ball.resetToCenter(paddle);
                            int nextLevel = currentLevel + 1;
                            if (currentLevel > Constants.Level) {
                                gotoVictoryScreen();

                            } else {
                                currentLevel = nextLevel;
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

    public int getScore() {
        return score;
    }

    public int getCurrentLevel() {
        return currentLevel;
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
