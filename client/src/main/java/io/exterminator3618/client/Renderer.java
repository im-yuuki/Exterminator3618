package io.exterminator3618.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.exterminator3618.client.Constants.WINDOW_WIDTH;
import static io.exterminator3618.client.Constants.WINDOW_HEIGHT;
import static io.exterminator3618.client.Constants.GameState;

/**
 * Renderer for the game.
 */
public class Renderer {
    private static final Logger log = LoggerFactory.getLogger(Renderer.class);   
    // Core elements
    private final SpriteBatch batch;
    private final BitmapFont font;
    // State management
    private GameState currentState;
    private StateRenderer currentStateRenderer;
    
    /**
     * Creates a new Renderer
     */
    public Renderer() {
        batch = new SpriteBatch();
        
        // Initialize font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Quicksand-Medium.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 24;
        param.color = Color.WHITE;
        param.borderWidth = 2;
        param.borderColor = Color.BLACK;
        font = generator.generateFont(param);
        generator.dispose();
        
        // Initialize state
        currentState = GameState.MENU;
        currentStateRenderer = new MenuStateRenderer();
        
        log.info("GameStateRenderer initialized");
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
     */
    public void draw(GameObject obj) {
        String name = obj.getRegionName();
        TextureRegion region = Assets.getRegion(name);
        if (region == null) {
            return;
        }
        batch.draw(region, obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
    }
    
    /**
     * Draws text at the specified position using the default font.
     */
    public void drawText(String text, float x, float y) {
        font.draw(batch, text, x, y);
    }
    
    /**
     * Draws centered text at the specified position.
     */
    public void drawCenteredText(String text, float x, float y) {
        float textWidth = font.getData().getGlyph(text.charAt(0)).width * text.length(); 
        float textHeight = font.getData().getGlyph(text.charAt(0)).height;
        drawText(text, x - textWidth / 2, y + textHeight / 2);
    }

    /**
     * Main function to render the game.
     *
     * @param game the game instance 
     */
    public void render(Game game) {
        GameState state = game.getState();
        
        // Switch state renderer if state changed
        if (state != currentState) {
            currentState = state;
            currentStateRenderer = createStateRenderer(state);
            log.debug("Switched to render state: {}", state);
        }
        currentStateRenderer.render(game, this);
    }

    /**
     * Helper method to clear the screen with a specific color.
     */
    public void clearScreen(Color color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    /**
     * Disposes of all resources.
     */
    public void dispose() {
        batch.dispose();
        font.dispose();
        log.info("GameStateRenderer disposed");
    }

/**
     * Base class renderer for states
     */
    public abstract static class StateRenderer {
        /**
         * Renders the specific game state.
         * 
         * @param game the game instance
         * @param renderer the renderer to use for drawing
         */
        public abstract void render(Game game, Renderer renderer);
    }

    /**
     * Function to create a renderer for the given game state.
     */
    private StateRenderer createStateRenderer(GameState state) {
        return switch (state) {
            case MENU -> new MenuStateRenderer();
            case PLAYING -> new PlayingStateRenderer();
            case PAUSED -> new PausedStateRenderer();
            case GAME_OVER -> new GameOverStateRenderer();
            case VICTORY -> new VictoryStateRenderer();
            case LOSE -> new LoseStateRenderer();
        };
    }

    
    /**
     * Renders the menu state.
     */
    private static class MenuStateRenderer extends StateRenderer {
        private static final Color MENU_BACKGROUND_COLOR = new Color(0.1f, 0.1f, 0.1f, 1);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(MENU_BACKGROUND_COLOR);
            
            renderer.begin();
            renderer.setFontSize(36);
            renderer.drawCenteredText("Welcome to Exterminator3618", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            renderer.setFontSize(24);
            renderer.drawCenteredText("Press SPACE to Start", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 - 50);
            renderer.end();
        }
    }
    
    /**
     * Renders the playing state.
     */
    private static class PlayingStateRenderer extends StateRenderer {
        private static final Color PLAYING_BACKGROUND_COLOR = new Color(0, 0, 0, 1);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(PLAYING_BACKGROUND_COLOR);
            
            renderer.begin();
            
            // Draw ball
            if (game.getBall() != null) {
                renderer.draw(game.getBall());
            }
            
            // Draw paddle
            if (game.getPaddle() != null) {
                renderer.draw(game.getPaddle());
            }
            
            // Draw bricks
            if (game.getBricks() != null) {
                for (Brick brick : game.getBricks()) {
                    renderer.draw(brick);
                }
            }
            
            renderer.end();
        }
    }
    
    /**
     * Renders the paused state.
     */
    private static class PausedStateRenderer extends StateRenderer {
        private static final Color PAUSED_BACKGROUND_COLOR = new Color(0, 0, 0, 0.5f);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(PAUSED_BACKGROUND_COLOR);

            renderer.begin();
            renderer.setFontSize(36);
            renderer.drawCenteredText("Game Paused", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            renderer.setFontSize(24);
            renderer.drawCenteredText("Press P to Resume", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 - 50);
            renderer.drawCenteredText("Press ESC to Quit", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 - 100);
            renderer.end();
            
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
    
    /**
     * Renders the game over state.
     */
    private static class GameOverStateRenderer extends StateRenderer {
        private static final Color GAME_OVER_BACKGROUND_COLOR = new Color(0.2f, 0, 0, 1);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(GAME_OVER_BACKGROUND_COLOR);
            
            renderer.begin();
            renderer.setFontSize(36);
            renderer.drawCenteredText("Game Over", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            renderer.setFontSize(24);
            renderer.drawCenteredText("Press ENTER or ESC to Return to Menu", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 - 50);
            renderer.end();
        }
    }
    
    /**
     * Renders the victory state.
     */
    private static class VictoryStateRenderer extends StateRenderer {
        private static final Color VICTORY_BACKGROUND_COLOR = new Color(0, 0.3f, 0, 1);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(VICTORY_BACKGROUND_COLOR);
            
            renderer.begin();
            renderer.setFontSize(36);
            renderer.drawCenteredText("Victory! Congratulations!", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            renderer.setFontSize(24);
            renderer.drawCenteredText("Press ENTER to Continue", WINDOW_HEIGHT / 2, WINDOW_HEIGHT / 2 - 100);
            renderer.end();
        }
    }
    
    /**
     * Renders the lose state.
     */
    private static class LoseStateRenderer extends StateRenderer {
        private static final Color LOSE_BACKGROUND_COLOR = new Color(0.3f, 0, 0, 1);
        
        @Override
        public void render(Game game, Renderer renderer) {
            renderer.clearScreen(LOSE_BACKGROUND_COLOR);
            
            renderer.begin();
            renderer.setFontSize(36);
            renderer.drawCenteredText("You Lost! Better luck next time!", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            renderer.setFontSize(24);
            renderer.drawCenteredText("Press ENTER to Try Again", WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2 - 50);
            renderer.end();
        }
    }

    // Getters / Setters

    public void setFontColor(Color color) {
        font.setColor(color);
    }
    
    public void setFontSize(int size) {
        font.getData().setScale(size / 24f);
    }
    
}
