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
     * Supports optional trailing index suffix (e.g., "normal_blue_brick_2").
     * If a suffix is present, uses TextureAtlas.findRegion(baseName, index).
     *
     * @param name name of the texture region (optionally ending with _<index>)
     * @return TextureRegion instance, or null if not found
     */
    public static TextureRegion getRegion(String name) {
        if (atlas == null || name == null) {
            return null;
        }

        // Parse trailing _<number> to support multi-index regions in the atlas
        int underscore = name.lastIndexOf('_');
        if (underscore > 0 && underscore < name.length() - 1) {
            String suffix = name.substring(underscore + 1);
            try {
                int index = Integer.parseInt(suffix);
                String base = name.substring(0, underscore);
                TextureRegion region = atlas.findRegion(base, index);
                if (region != null) {
                    return region;
                }
                // Fallback to full name if not found by index
            } catch (NumberFormatException ignored) {
                // Not an indexed name; fall through
            }
        }

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
