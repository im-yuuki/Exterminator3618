package io.exterminator3618.client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.exterminator3618.client.Constants.ATLAS_PATH;

/**
 * Manages game texture assets using LibGDX AssetManager.
 * Loads assets from a texture atlas and provides access to texture regions.
 */
public class Assets {
    private static final Logger log = LoggerFactory.getLogger(Assets.class);
    // Single AssetManager instance for the application.
    private static final AssetManager assetManager = new AssetManager();
    // Loaded texture atlas.
    public static TextureAtlas atlas;

    /**
     * Loads the texture atlas and prepares texture regions.
     * Must be called once at application startup.
     */
    public static void load() {
        assetManager.load(ATLAS_PATH, TextureAtlas.class);
        assetManager.finishLoading();
        log.info("Loaded assets: {}", assetManager.getAssetNames());
        atlas = assetManager.get(ATLAS_PATH, TextureAtlas.class);

        // Set texture filters to Nearest for pixelated textures.
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    /**
     * Retrieves a texture region by name from the loaded atlas.
     *
     * @param name name of the texture region
     * @return TextureRegion instance, or null if not found
     */
    public static TextureRegion getRegion(String name) {
        return atlas.findRegion(name);
    }

    /**
     * Disposes of all loaded assets. Should be called on application exit.
     */
    public static void dispose() {
        assetManager.dispose();
        log.info("Disposed all assets");
    }
}
