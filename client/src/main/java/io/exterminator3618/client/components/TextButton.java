package io.exterminator3618.client.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.exterminator3618.client.utils.Renderer;
public class TextButton {

    public String text;
    public Rectangle bounds;

    /**
     * Tạo một TextButton mới.
     *
     * @param text   Văn bản sẽ hiển thị.
     * @param x      Tọa độ X (góc DƯỚI-BÊN TRÁI) của vùng nhấp.
     * @param y      Tọa độ Y (góc DƯỚI-BÊN TRÁI) của vùng nhấp.
     * @param width  Chiều rộng của vùng nhấp.
     * @param height Chiều cao của vùng nhấp.
     */
    public TextButton(String text, float x, float y, float width, float height) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Vẽ button này bằng Renderer.
     */
    public void draw(Renderer renderer) {
        float y_topLeft = bounds.y + bounds.height;
        renderer.drawText(text, bounds.x, y_topLeft);
    }

    /**
     * Kiểm tra xem tọa độ (đã được unproject) có nằm trong nút này không.
     *
     * @param touchX Tọa độ X của thế giới game (đã qua camera.unproject).
     * @param touchY Tọa độ Y của thế giới game (đã qua camera.unproject).
     * @return true nếu được nhấp, false nếu không.
     */
    public boolean isClicked(float touchX, float touchY) {
        return bounds.contains(touchX, touchY);
    }

}