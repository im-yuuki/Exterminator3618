package io.exterminator3618.client.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.Assets;
public class TextButton {

    public String text;
    public Rectangle bounds;

    public String leftRegion;
    public String middleRegion;
    public String rightRegion;

    public TextButton(String text, float x, float y, float width, float height, boolean hasFrame) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);

        if (hasFrame) {
            this.leftRegion = Constants.BUTTON_LEFT_REGION;
            this.middleRegion = Constants.BUTTON_MIDDLE_REGION;
            this.rightRegion = Constants.BUTTON_RIGHT_REGION;
        } else {
            this.leftRegion = null;
            this.middleRegion = null;
            this.rightRegion = null;
        }
    }
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

        if (leftRegion != null && middleRegion != null && rightRegion != null) {

            TextureRegion leftTex = Assets.getUiRegion(leftRegion);
            TextureRegion rightTex = Assets.getUiRegion(rightRegion);

            if (leftTex != null && rightTex != null) {
                int leftWidth = leftTex.getRegionWidth();
                int rightWidth = rightTex.getRegionWidth();

                int x = (int)bounds.x;
                int y = (int)bounds.y;
                int width = (int)bounds.width;
                int height = (int)bounds.height;

                renderer.drawUi(leftRegion, x, y, leftWidth, height);

                renderer.drawUi(rightRegion, (x + width) - rightWidth, y, rightWidth, height);

                int middleX = x + leftWidth;
                int middleWidth = width - leftWidth - rightWidth;

                if (middleWidth > 0) {
                    renderer.drawUiTiledX(middleRegion, middleX, y, middleWidth, height);
                }
            }
        }

        int centerX = (int)(bounds.x + bounds.width / 2);
        int centerY = (int)(bounds.y + bounds.height / 2);
        renderer.drawTextMiddle(text, centerX, centerY);
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