package io.exterminator3618.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    /**
     * Creates a renderer and its SpriteBatch.
     */
    public Renderer() {
        batch = new SpriteBatch();
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
     * Disposes all cached textures and the underlying SpriteBatch.
     * Should be called when the application shuts down.
     */
    public void dispose() {
        batch.dispose();
        log.info("Disposed of renderer resources");
    }
}
