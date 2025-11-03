package io.exterminator3618.client.components;

/**
 * Base type for anything that can be rendered by the client.
 * A GameObject has a position, a size and a reference to the texture file
 * used for rendering. Subclasses define how the object updates over time.
 */
public abstract class GameObject {

    /**
     * Name of the texture region in the atlas.
     */
    private String regionName;
    /**
     * X and Y coordinates in screen pixels, measured from the bottom-left.
     */
    private int x, y;
    /**
     * Width and height of the object in screen pixels.
     */
    private int width, height;

    /**
     * Creates a new game object.
     *
     * @param x          initial X position in pixels
     * @param y          initial Y position in pixels
     * @param width      width in pixels
     * @param height     height in pixels
     * @param regionName name of the texture region in the atlas
     */
    public GameObject(int x, int y, int width, int height, String regionName) {
        this.regionName = regionName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return current X position in pixels
     */
    public int getX() {
        return x;
    }

    /**
     * @return current Y position in pixels
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the X position.
     * @param x new X position in pixels
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Y position.
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return height in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the position of the object.
     *
     * @param x X coordinate in pixels
     * @param y Y coordinate in pixels
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Resizes the object.
     *
     * @param width  new width in pixels
     * @param height new height in pixels
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Updates the internal state of the object.
     *
     * @param deltaTime time since last frame in seconds
     */
    public abstract void update(float deltaTime);

    /**
     * @return name of the texture region in the atlas
     */
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        // do nothing, override in subclass if needed
        this.regionName = regionName;
    }

}
