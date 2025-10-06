package io.exterminator3618.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper around LibGDX SpriteBatch with a texture cache.
 * It batches draw calls and ensures textures are loaded only once per path.
 */
public class Renderer {
    private static final Logger log = LoggerFactory.getLogger(Renderer.class);
    /**
     * Underlying batch used for 2D rendering.
     */
    private final SpriteBatch batch;

    private final BitmapFont font;

    /**
     * Creates a renderer and its SpriteBatch.
     */
    public Renderer() {
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Medium.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 24;
        param.color = Color.WHITE;
        param.borderWidth = 2;
        param.borderColor = Color.BLACK;
        font = generator.generateFont(param);
        generator.dispose();
    }

    /**
     * Begins a batch. Must be called before draw calls.
     */
    public void begin() {
        batch.begin();
    }

    /**
     * Ends a batch. Must be called after draw calls.
     */
    public void end() {
        batch.end();
    }

    /**
     * Draws a GameObject using its region in TextureAtlas and current bounds.
     *
     * @param obj object to draw
     */
    public void draw(GameObject obj) {
        String name = obj.getRegionName();
        TextureRegion region = Assets.getRegion(name);
        if (region == null) {
            return;
        }
        batch.draw(region, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
        // log.trace("Drew object '{}' at ({}, {}) with size {}x{}", obj.getFilepath(), obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
    }

    /**
     * Draws text at the specified position using the default font.
     *
     * @param text text to draw
     * @param x    X position in pixels
     * @param y    Y position in pixels
     */
     public void drawText(String text, float x, float y) {
        font.draw(batch, text, x, y);
    }

    public void setFontColor(Color color) {
        font.setColor(color);
    }

    public void setFontSize(int size) {
        font.getData().setScale(size / 24f);
    }

    /**
     * Disposes all cached textures and the underlying SpriteBatch.
     * Should be called when the application shuts down.
     */
    public void dispose() {
        batch.dispose();
        log.info("Disposed of renderer resources");
    }
}
