package io.exterminator3618.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
     * Simple in-memory cache: texture path -> Texture. Disposed on {@link #dispose()}.
     */
    private final Map<String, Texture> textureCache = new HashMap<>();

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
     * Returns a cached texture for the provided path, loading it if necessary.
     *
     * @param path texture path
     * @return Texture instance
     */
    private Texture getTexture(String path) {
        Texture tex = textureCache.get(path);
        if (tex == null) {
            tex = new Texture(Gdx.files.internal(path));
            textureCache.put(path, tex);
            log.info("Loaded texture: {} ({}x{})", path, tex.getWidth(), tex.getHeight());
        }
        return tex;
    }

    /**
     * Draws a GameObject using its texture path and current bounds.
     *
     * @param obj object to draw
     */
    public void draw(GameObject obj) {
        Texture texture = getTexture(obj.getFilepath());
        batch.draw(texture, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
        // log.trace("Drew object '{}' at ({}, {}) with size {}x{}", obj.getFilepath(), obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
    }

    /**
     * Disposes all cached textures and the underlying SpriteBatch.
     * Should be called when the application shuts down.
     */
    public void dispose() {
        for (Texture t : textureCache.values()) {
            t.dispose();
        }
        textureCache.clear();
        batch.dispose();
        log.info("Disposed of renderer resources");
    }
}
