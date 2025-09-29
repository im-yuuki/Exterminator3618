package io.exterminator3618.client;

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
    }

    /**
     * Reduces hit points when the brick takes damage.
     * @return true if the brick was destroyed, false otherwise
     */
    public boolean takeHit() {
        hitPoints--;
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
        // nó đứng im, đéo cần update đâu, thêm cái này cho có
    }
}
