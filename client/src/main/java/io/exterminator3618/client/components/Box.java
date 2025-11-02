// DialogBox.java
package io.exterminator3618.client.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.exterminator3618.client.Constants;
import io.exterminator3618.client.utils.Assets;
import io.exterminator3618.client.utils.Renderer;

/**
 * Menu box for pause screen and setting screen.
 */
public class Box {

    private int x = Constants.WINDOW_WIDTH / 2;
    private int y = Constants.WINDOW_HEIGHT / 2;
    private int width, height;
    private String title, content;
    private String leftRegion = Constants.HEADER_LEFT_REGION;
    private String rightRegion = Constants.HEADER_RIGHT_REGION;
    private String middleRegion = Constants.HEADER_MIDDLE_REGION;
    private Rectangle bounds = new Rectangle(x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

    public Box(int width, int height, String title, String content) {
        this.x = Constants.WINDOW_WIDTH / 2 - width / 2;
        this.y = Constants.WINDOW_HEIGHT / 2 - height / 2;
        this.width = width;
        this.height = height;
        this.title = title;
        this.content = content;

    }

    public Box(int width, int height, String title) {
        this.x = Constants.WINDOW_WIDTH / 2 - width / 2;
        this.y = Constants.WINDOW_HEIGHT / 2 - height / 2;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void draw(Renderer renderer) {
        renderer.drawUi("tile_1", x, y, width, height);
        renderer.drawTextMiddle(title, x + width / 2, y + height - 50);
        if (content != null) {
            renderer.drawTextMiddle(content, x + width / 2, y + height / 2 + 20);
        }
    }

}