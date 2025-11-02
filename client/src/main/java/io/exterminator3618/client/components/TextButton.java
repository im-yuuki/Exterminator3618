package io.exterminator3618.client.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.utils.Renderer;
import io.exterminator3618.client.utils.Assets;

/**
 * A simple text button with an optional 9-patch-style background frame.
 */
public class TextButton {

    public String text;
    public Rectangle bounds;

    public String leftRegion;
    public String middleRegion;
    public String rightRegion;

    /**
     * Creates a new TextButton.
     *
     * @param text     The text to display.
     * @param x        The x-coordinate (bottom-left).
     * @param y        The y-coordinate (bottom-left).
     * @param width    The button's width.
     * @param height   The button's height.
     * @param hasFrame True to draw the 9-patch style frame, false for text only.
     */
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
     * Creates a new TextButton without a frame.
     *
     * @param text   The text to display.
     * @param x      The x-coordinate (bottom-left).
     * @param y      The y-coordinate (bottom-left).
     * @param width  The button's width.
     * @param height The button's height.
     */
    public TextButton(String text, float x, float y, float width, float height) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Draws the button (frame and text) using the provided Renderer.
     * @param renderer The renderer to use for drawing.
     */
    public void draw(Renderer renderer) {

        // Draw the 9-patch style frame if regions are defined
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

                // Draw left cap
                renderer.drawUi(leftRegion, x, y, leftWidth, height);

                // Draw right cap
                renderer.drawUi(rightRegion, (x + width) - rightWidth, y, rightWidth, height);

                // Draw tiled middle section
                int middleX = x + leftWidth;
                int middleWidth = width - leftWidth - rightWidth;

                if (middleWidth > 0) {
                    renderer.drawUiTiledX(middleRegion, middleX, y, middleWidth, height);
                }
            }
        }

        // Draw the text centered
        int centerX = (int)(bounds.x + bounds.width / 2);
        int centerY = (int)(bounds.y + bounds.height / 2);
        renderer.drawTextMiddle(text, centerX, centerY);
    }

    /**
     * Checks if the given (unprojected) coordinates are within the button's bounds.
     *
     * @param touchX The world x-coordinate (already unprojected).
     * @param touchY The world y-coordinate (already unprojected).
     * @return true if clicked, false otherwise.
     */
    public boolean isClicked(float touchX, float touchY) {
        return bounds.contains(touchX, touchY);
    }
}