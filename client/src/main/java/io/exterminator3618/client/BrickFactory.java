package io.exterminator3618.client;

import java.util.Random;

/**
 * Factory class for creating different types of bricks using available textures from the texture atlas.
 * Provides convenient methods to create normal, strong, square, and thick bricks in various colors.
 */
public class BrickFactory {

    private static final Random random = new Random();

    /**
     * Creates a normal brick with random color (blue or green).
     *
     * @param x position X
     * @param y position Y
     * @return NormalBrick with random color (blue or green)
     */
    public static NormalBrick createRandomNormalBrick(int x, int y) {
        String[] normalBricks = {
            Constants.NORMAL_BLUE_BRICK,
            Constants.NORMAL_GREEN_BRICK
        };

        String randomTexture = normalBricks[random.nextInt(normalBricks.length)];
        return new NormalBrick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, randomTexture);
    }

    /**
     * Creates a normal brick with specific color.
     *
     * @param x position X
     * @param y position Y
     * @param color brick color
     * @return NormalBrick with specified color
     */
    public static NormalBrick createNormalBrick(int x, int y, BrickColor color) {
        String textureName = getNormalBrickTexture(color);
        return new NormalBrick(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, textureName);
    }

    /**
     * Creates a strong brick with random color (blue or green).
     *
     * @param x position X
     * @param y position Y
     * @return StrongBrick with random color (blue or green)
     */

    /**
     * Creates a strong brick with specific color.
     *
     * @param x position X
     * @param y position Y
     * @param color brick color
     * @return StrongBrick with specified color
     */

    /**
     * Gets the texture name for a normal brick of blue or green.
     *
     * @param color brick color (only BLUE and GREEN are supported)
     * @return texture region name
     */
    private static String getNormalBrickTexture(BrickColor color) {
        switch (color) {
            case BLUE: return Constants.NORMAL_BLUE_BRICK;
            case GREEN: return Constants.NORMAL_GREEN_BRICK;
            default: return Constants.NORMAL_BLUE_BRICK;
        }
    }

    /**
     * Enum for brick colors available in the texture atlas (blue and green).
     */
    public enum BrickColor {
        BLUE, GREEN
    }
}
