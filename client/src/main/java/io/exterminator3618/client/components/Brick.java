package io.exterminator3618.client.components;

/**
 * Brick class represents destructible blocks in the arkanoid game.
 * Extends GameObject to inherit position, size and rendering capabilities.
 */
public class Brick extends GameObject {
    /**
     * Current hit points of the brick
     */
    protected int hitPoints;

    /**
     * Type of the brick (for different behaviors and appearances)
     */
    protected String type;

    /**
     * Base region name (without index) for texture switching
     */
    protected String baseRegionName;

    /**
     * Creates a new brick.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     * @param hitPoints  initial hit points
     * @param type       type of the brick
     */
    public Brick(int x, int y, int width, int height, String regionName, int hitPoints, String type) {
        super(x, y, width, height, regionName);
        this.hitPoints = hitPoints;
        this.type = type;
        // Store base region name (without index)
        this.baseRegionName = regionName;
    }

    /**
     * Reduces hit points when the brick takes damage.
     * @return true if the brick was destroyed, false otherwise
     */
    public boolean takeHit() {
        hitPoints--;
        if (hitPoints > 0) {
            int state = switch (hitPoints) {
                case 3 -> 1;
                case 2 -> 2;
                case 1 -> 3;
                default -> 1;
            };
            setRegionName(baseRegionName + "_" + state);
        }
        return isDestroyed();
    }

    /**
     * Checks if the brick is destroyed (no hit points remaining).
     * @return true if destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    /**
     * Gets the current hit points.
     * @return current hit points
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Gets the type of the brick.
     * @return brick type
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the brick state (no movement for static bricks).
     * @param deltaTime time since last frame in seconds
     */
    @Override
    public void update(float deltaTime) {
    }
}
