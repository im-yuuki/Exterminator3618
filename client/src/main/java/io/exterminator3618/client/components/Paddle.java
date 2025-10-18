package io.exterminator3618.client.components;

import com.badlogic.gdx.Gdx;
import io.exterminator3618.client.Constants;


public class Paddle extends MovableObject{
    //For PowerUpBrick
    private float powerUpTimer; // Biến đếm thời gian hiệu ứng
    private boolean isPoweredUp; // Trạng thái có hiệu ứng hay không

    /**
     * Creates a stationary movable object.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param filepath texture resource path
     */
    public Paddle(int x, int y, int width, int height, String filepath) {
        super(x, y, width, height, filepath);
    }


    /**
     * Kích hoạt hiệu ứng làm dài paddle.
     * @param duration thời gian hiệu ứng (giây)
     */
    public void activateWidenPowerUp(float duration) {
        if (!isPoweredUp) {
            resize(getWidth() + 50, getHeight()); // Tăng chiều rộng thêm 50px
            isPoweredUp = true;
        }
        this.powerUpTimer = duration; // Đặt lại hoặc cộng dồn thời gian
    }

    /**
     * Update vị trí trên trục hoành của paddle, chỉ trục hoành, CHỈ TRỤC HOÀNH
     * hiêện đang dùng chuột, dùng phím đang bị phế
     */
    @Override
    public void update(float deltaTime) {
        // Cập nhật bộ đếm thời gian hiệu ứng
        if (isPoweredUp) {
            powerUpTimer -= deltaTime;
            if (powerUpTimer <= 0) {
                // Hết thời gian, trả paddle về kích thước ban đầu
                resize(Constants.PADDLE_WIDTH, getHeight());
                isPoweredUp = false;
            }
        }

        int desiredX = (int) (Gdx.input.getX() - Constants.PADDLE_WIDTH / 2);

        //UPDATE TỪ BÀN PHÍM ĐANG BỊ TRÔN R`, ĐÉO DÙNG ĐƯỢC TẠI ĐANG CONFLICT VỚI CHUỘT, MÀ CỨ ĐỂ ĐÂY ĐI FIX SAU
        /*
        float keyboardDelta = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            keyboardDelta -= Constants.PADDLE_SPEED * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            keyboardDelta += Constants.PADDLE_SPEED * deltaTime;
        }
        desiredX += (int) keyboardDelta;
        */

        //nhét paddle vào đúng trong màn hình
        int clampedX = Math.max(0, Math.min(desiredX, Constants.WINDOW_WIDTH - getWidth()));
        setPosition(clampedX, getY());
    }
}
