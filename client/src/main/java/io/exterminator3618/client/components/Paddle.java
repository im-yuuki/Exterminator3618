package io.exterminator3618.client.components;

import com.badlogic.gdx.Gdx;
import io.exterminator3618.client.Constants;


public class Paddle extends MovableObject{

    // PowerUp timer system
    private float widenPaddleTimer = 0f;
    private boolean isWidened = false;
    private int originalWidth;

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
        this.originalWidth = width;
    }

    /**
     * Update vị trí trên trục hoành của paddle, chỉ trục hoành, CHỈ TRỤC HOÀNH
     * hiêện đang dùng chuột, dùng phím đang bị phế
     */
    @Override
    public void update(float deltaTime) {
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

        // Update powerup timer
        updatePowerUpTimer(deltaTime);
    }

    /**
     * Updates the powerup timer and handles powerup expiration
     */
    private void updatePowerUpTimer(float deltaTime) {
        if (isWidened && widenPaddleTimer > 0) {
            widenPaddleTimer -= deltaTime;
            if (widenPaddleTimer <= 0) {
                // Powerup expired, return to original size
                resize(originalWidth, getHeight());
                isWidened = false;
                widenPaddleTimer = 0f;
            }
        }
    }

    /**
     * Activates the widen paddle powerup for a specified duration
     * @param duration Duration in seconds
     */
    public void activateWidenPowerUp(float duration) {
        if (!isWidened) {
            // Only widen if not already widened
            resize(originalWidth + 100, getHeight());
            isWidened = true;
        }
        // Reset timer to new duration (allows extending existing powerup)
        widenPaddleTimer = duration;
    }

    /**
     * Gets the remaining time for the current powerup
     * @return Remaining time in seconds, or 0 if no active powerup
     */
    public float getPowerUpRemainingTime() {
        return widenPaddleTimer;
    }

    /**
     * Checks if paddle is currently widened by powerup
     * @return true if paddle is widened
     */
    public boolean isWidened() {
        return isWidened;
    }
}
