package io.exterminator3618.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer; //SAU NÀY XÓA ĐI, GIỜ ADD ĐỂ TEST I/O THÔI
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.exterminator3618.client.Constants.*;
import java.util.Random;
/**
 * Main LibGDX application for the Exterminator3618 client. It owns the renderer
 * and the root game objects and drives the frame loop.
 */
public class Game extends ApplicationAdapter {
    private static final Logger log = LoggerFactory.getLogger(Game.class);
    private static final Random random = new Random();
    private GameState state;
    private Assets assets;
    private Renderer renderer;
    private Ball ball;
    private SoundManager soundManager;
    private List<Brick> bricks;
    private List<Ball> extraBalls;
    private Paddle paddle;

    /**
     * Initializes rendering and creates initial game objects.
     */
    @Override
    public void create() {
        log.info("Game created");
        assets = new Assets();
        Assets.load();
        renderer = new Renderer();
        state = GameState.MENU;
    }
    private static final String[] BRICK_COLORS = {"red", "green", "blue", "yellow", "purple"};

    public void loadLevel () {
        // Initialize ball
        ball = new Ball(
                WINDOW_WIDTH / 2 - BALL_WIDTH / 2,
                WINDOW_HEIGHT / 2 - BALL_HEIGHT / 2,
                BALL_WIDTH,
                BALL_HEIGHT,
                BALL_REGION_NAME, //object name
                BALL_SPEED,
                67
        );

        //Initialize extra balls list
        extraBalls = new ArrayList<>();

        // Initialize paddle
        paddle = new Paddle(
                PADDLE_START_X,
                PADDLE_START_Y,
                PADDLE_WIDTH,
                PADDLE_HEIGHT,
                PADDLE_REGION_NAME
        );

        // Initialize random bricks for testing
        bricks = new ArrayList<>();
        int rows = 5;
        int cols = 17;
        int startX = 50;
        int startY = WINDOW_HEIGHT - 100;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (BRICK_WIDTH + BRICK_SPACING);
                int y = startY - row * (BRICK_HEIGHT + BRICK_SPACING);

                //Random number for creating bricks
                int randomNumber = random.nextInt(100);

                if (randomNumber < 20) { // 20% tạo MultiBallBrick
                    bricks.add(new MultiBallBrick(x, y));
                } else if (randomNumber < 40) { // 20% cơ hội tạo StrongBrick
                    bricks.add(new StrongBrick(x, y));
                } else { // 80% còn lại tạo NormalBrick
                    bricks.add(new NormalBrick(x, y));
                }

                //Test Multiball brick
                //if (row == 4 &&  col ==  7) {
                  //  bricks.add(new MultiBallBrick(x, y));
                //} else {
                  //  String color = BRICK_COLORS[row % BRICK_COLORS.length];
                    //Brick brick = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT,
                      //      "normal_" + color + "_brick", 3, "normal");
                   // bricks.add(brick);
                //}
                //String color = BRICK_COLORS[row % BRICK_COLORS.length];
                //Brick brick = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT,
                //        "normal_" + color + "_brick", 3, "normal");
                //bricks.add(brick);
            }
        }
    }

    /**
     * Frame callback: updates and renders the scene.
     */
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Clear screen
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (state) {
            case MENU -> {
                // Render menu screen
                Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw text here
                renderer.drawText("Day la Main Screen", 300, 300);
                renderer.end();

                // Transition to PLAYING state on input
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    loadLevel();
                    state = GameState.PLAYING;
                }
            }
            case PLAYING -> {
                // Update game logic
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                ball.update(deltaTime);
                paddle.update(deltaTime);

                // CẬP NHẬT TẤT CẢ BÓNG PHỤ
                for (Ball extraBall : extraBalls) {
                    extraBall.update(deltaTime);
                }
                updateExtraBalls(deltaTime);

                for (Brick brick : bricks) {
                    brick.update(deltaTime);
                }
                checkBallBrickCollisions();
                ball.checkPaddleCollision(paddle);

                // KIỂM TRA VA CHẠM PADDLE CHO TẤT CẢ BÓNG
                ball.checkPaddleCollision(paddle);
                for (Ball extraBall : extraBalls) {
                    extraBall.checkPaddleCollision(paddle);
                }

                // Render game objects
                renderer.begin();
                renderer.draw(ball);
                renderer.draw(paddle);

                //Vẽ bóng phụ
                for (Ball extraBall : extraBalls) {
                    renderer.draw(extraBall);
                }

                for (Brick brick : bricks) {
                    renderer.draw(brick);
                }
                renderer.end();
                // Pause game on input
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.PAUSED;
                }
            }
            case PAUSED -> {
                // Render paused screen overlay
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                Gdx.gl.glClearColor(0, 0, 0, 0.5f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw paused text here
                renderer.drawText("Day la Pause Screen", 250, 300);
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);

                // Resume game on input (e.g., P key)
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.PLAYING;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    state = GameState.GAME_OVER;
                }
            }
            case GAME_OVER -> {
                // Render game over screen
                Gdx.gl.glClearColor(0.2f, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.begin();
                // Draw game over text here
                renderer.drawText("Day la Game Over Screen", 250, 300);
                renderer.end();

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    state = GameState.MENU;
                    disposeLevel();
                }
            }
        }
    }

    /**
     * THÊM PHƯƠNG THỨC MỚI
     * Spawns three extra balls at the location of a destroyed brick.
     * @param x The x-coordinate of the spawn location.
     * @param y The y-coordinate of the spawn location.
     */
    private void spawnExtraBalls(int x, int y) {
        log.info("Spawning 3 extra balls!");

        // Thời gian bất tử cho bóng mới, ví dụ 0.1 giây
        final float INVULNERABLE_DURATION = 0.5f;

        // Vị trí spawn có thể đặt lại ở tâm viên gạch
        int spawnY = y + (BRICK_HEIGHT / 2);

        // Tạo 3 bóng với 3 góc khác nhau
        Ball ball1 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, BALL_REGION_NAME, BALL_SPEED, -45);
        ball1.setInvulnerable(INVULNERABLE_DURATION);
        extraBalls.add(ball1);

        Ball ball2 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, BALL_REGION_NAME, BALL_SPEED, -90);
        ball2.setInvulnerable(INVULNERABLE_DURATION);
        extraBalls.add(ball2);

        Ball ball3 = new Ball(x, spawnY, BALL_WIDTH, BALL_HEIGHT, BALL_REGION_NAME, BALL_SPEED, -135);
        ball3.setInvulnerable(INVULNERABLE_DURATION);
        extraBalls.add(ball3);
    }

    /**
     * THÊM PHƯƠNG THỨC MỚI
     * Updates extra balls and removes them if they fall off the bottom of the screen.
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
                        // Nếu gạch bị phá hủy, kiểm tra xem có phải loại đặc biệt không
                        if ("multiball".equals(brick.getType())) {
                            spawnExtraBalls(brick.getX() + brick.getWidth() / 2, brick.getY());
                        }
                        // Xóa gạch khỏi danh sách
                        brickIterator.remove();
                        log.debug("Brick destroyed! Remaining bricks: {}", bricks.size());
                    } else {
                        // Gạch vẫn còn máu
                        log.debug("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(),
                                brick.getType().equals("strong") ? 3 : 1);
                    }

                    if (currentBall.isInvulnerable()) {
                        continue;
                    }
                    // 3. QUAN TRỌNG: Thoát khỏi vòng lặp kiểm tra bóng
                    // Vì viên gạch này đã được xử lý va chạm rồi.
                    // Điều này ngăn một viên gạch bị nhiều bóng phá hủy trong cùng một frame.
                    break;
                }
            }
        }
    }
/**
    private void checkBallBrickCollisions() {
        // Thoát sớm nếu không còn gạch
        if (bricks.isEmpty()) {
            return;
        }

        // Tạo danh sách tất cả các bóng để kiểm tra
        List<Ball> allBalls = new ArrayList<>(extraBalls);
        allBalls.add(ball);

        // VỚI MỖI QUẢ BÓNG...
        for (Ball currentBall : allBalls) {
            // Bỏ qua nếu bóng đang bất tử (logic này vẫn giữ lại vì nó hữu ích)
            if (currentBall.isInvulnerable()) {
                continue;
            }

            // Dùng iterator cho bricks để có thể xóa an toàn
            Iterator<Brick> brickIterator = bricks.iterator();
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();

                // KIỂM TRA VA CHẠM
                if (currentBall.collidesWith(brick)) {
                    // 1. Xử lý bóng nảy lại
                    currentBall.handleBrickCollision(brick);

                    // 2. Gạch nhận sát thương
                    boolean wasDestroyed = brick.takeHit();

                    if (wasDestroyed) {
                        if ("multiball".equals(brick.getType())) {
                            spawnExtraBalls(brick.getX() + brick.getWidth() / 2, brick.getY());
                        }
                        brickIterator.remove(); // Xóa gạch
                        log.debug("Brick destroyed! Remaining bricks: {}", bricks.size());
                    } else {
                        log.debug("Brick hit! Remaining HP: {}/{}", brick.getHitPoints(),
                                brick.getType().equals("strong") ? 3 : 1);
                    }

                    // 3. QUAN TRỌNG NHẤT:
                    // Sau khi quả bóng này đã va chạm với MỘT viên gạch,
                    // chúng ta dừng việc xét va chạm cho quả bóng này và chuyển sang quả bóng tiếp theo.
                    // Điều này đảm bảo mỗi quả bóng chỉ phá được 1 gạch mỗi frame.
                    break; // Thoát khỏi vòng lặp `while` (vòng lặp gạch)
                }
            }
        }
    }
 */
    public void disposeLevel() {
        bricks.clear();
        extraBalls.clear();
        ball = null;
        paddle = null;
        log.info("Level disposed");
    }
    /**
     * Releases resources and other disposable assets.
     */
    @Override
    public void dispose() {
        Assets.dispose();
        renderer.dispose();
        //soundManager.dispose();
        log.info("Game disposed");
    }
}
