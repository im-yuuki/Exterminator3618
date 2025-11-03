package io.exterminator3618.client.utils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.exterminator3618.client.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import io.exterminator3618.client.components.GameObject;

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

    private ShapeRenderer shapeRenderer;

    /**
     * Creates a renderer and its SpriteBatch.
     */
    public Renderer() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RetroGaming.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 36;
        param.color = Color.BLACK;
        param.borderWidth = 2;
        param.borderColor = Color.WHITE;
        font = generator.generateFont(param);
        generator.dispose();
    }

    /**
     * Begins a batch. Must be called before draw calls.
     */
    public void begin() {
        batch.begin();
    }

    public void begin(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
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

    public void draw(String regionName, int x, int y, int width, int height){
        TextureRegion region = Assets.getRegion(regionName);
        if (region == null) {
            return;
        }
        batch.draw(region, x, y, width, height);
    }

    public void drawUi(String regionName, int x, int y, int width, int height){
        TextureRegion region = Assets.getUiRegion(regionName);
        if (region == null) {
            log.warn("UI TextureRegion not found: {}", regionName);
            return;
        }
        batch.draw(region, x, y, width, height);
    }

    public void drawBackground(Texture bgTexture) {
        if (bgTexture == null) return;
        float screenWidth = Constants.WINDOW_WIDTH;
        float screenHeight = Constants.WINDOW_HEIGHT;
        batch.draw(bgTexture, 0, 0, screenWidth, screenHeight);
    }

    public void drawLives(int x, int y) {
        String name = "heart_4";
        TextureRegion region = Assets.getRegion(name);
        if (region == null) {
            return;
        }
        batch.draw(region, x, y, 30, 30);
    }

    public void drawLogo(int x, int y) {
        TextureRegion region = Assets.getRegion("logo");
        if (region == null) {
            return;
        }
        batch.draw(region, x - region.getRegionWidth() , y + 200 - region.getRegionHeight(), 2*region.getRegionWidth(), 2*region.getRegionHeight());
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

    public void drawTextMiddle(String text, int x, int y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float textWidth = layout.width;
        float textHeight = layout.height;
        
        // Center horizontally: subtract half the text width from the center X
        float drawX = x - textWidth / 2;
        
        // Center vertically: add half the text height to the center Y (since LibGDX draws from baseline)
        float drawY = y + textHeight / 2;
        
        font.draw(batch, text, drawX, drawY);
    }
    
    public void setFontColor(Color color) {
        font.setColor(color);
    }

    public void setFontSize(int size) {
        font.getData().setScale(size / 36f);
    }

    /**
     * Disposes all cached textures and the underlying SpriteBatch.
     * Should be called when the application shuts down.
     */
    public void dispose() {
        batch.dispose();
        //backgroundTexture.dispose();
        shapeRenderer.dispose();
        log.info("Disposed of renderer resources");
    }

    public BitmapFont getFont(){
        return font;
    }

    /**
     * Draw button background
     */
    public void drawUiTiledX(String regionName, int x, int y, int totalWidth, int height) {
        TextureRegion region = Assets.getUiRegion(regionName);

        if (region == null) {
            log.warn("UI Tiled TextureRegion not found: {}", regionName);
            return;
        }

        float originalWidth = region.getRegionWidth();
        if (originalWidth == 0) {
            return;
        }

        float currentX = x;
        float remainingWidth = totalWidth;
        float oldU2 = region.getU2();

        while (remainingWidth > 0) {
            float drawWidth = Math.min(originalWidth, remainingWidth);

            if (drawWidth < originalWidth) {
                float newU2 = region.getU() + (region.getU2() - region.getU()) * (drawWidth / originalWidth);
                region.setU2(newU2);

                batch.draw(region, currentX, y, drawWidth, height);

                region.setU2(oldU2);
            } else {
                batch.draw(region, currentX, y, originalWidth, height);
            }

            remainingWidth -= drawWidth;
            currentX += drawWidth;
        }
    }

    /**
     * Draw overlay
     */
    public void drawOverlay(OrthographicCamera camera, float alpha) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, alpha);

        shapeRenderer.rect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
