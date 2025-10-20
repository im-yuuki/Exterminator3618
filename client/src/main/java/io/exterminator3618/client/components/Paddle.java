package io.exterminator3618.client.components;

import com.badlogic.gdx.Gdx;

import io.exterminator3618.client.Constants;


public class Paddle extends MovableObject{

    private boolean isSticky = false;

    /**
     * Creates a stationary movable object.
     *
     * @param x        initial X position
     * @param y        initial Y position
     * @param width    width in pixels
     * @param height   height in pixels
     * @param regionName texture region name in the atlas
     */
    public Paddle(int x, int y, int width, int height, String regionName) {
        super(x, y, width, height, regionName);
    }

    /**
     * Update vị trí trên trục hoành của paddle, chỉ trục hoành, CHỈ TRỤC HOÀNH
     * hiêện đang dùng chuột, dùng phím đang bị phế
     */
    @Override
    public void update(float deltaTime) {
        int desiredX = (int) (Gdx.input.getX() - getWidth() / 2);

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

    public void extend(){
        resize(getWidth() + 100, getHeight());
    }

    public void shrink(){
        resize(getWidth() - 100, getHeight());
    }

    public void setSticky(boolean sticky){
        this.isSticky = sticky;
    }
    
    public boolean isSticky(){
        return isSticky;
    }
    
}
