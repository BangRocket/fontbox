package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;

/**
 * Defines the bounds of an object
 */
public class ObjectBounds {
    public final int x, y;
    public final int width, height;
    public final FloatMode floating;

    public ObjectBounds(int x, int y, int width, int height, FloatMode floating) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.floating = floating;
    }

    /**
     * An object is floating when {@link #floating} is not equal to {@link FloatMode#NONE}
     *
     * @return whether the object is floating or not
     */
    public boolean floating() {
        return floating != FloatMode.NONE;
    }

    /**
     * Check if this object intersects with that object
     *
     * @param that the object to check intersection with
     * @return true when the objects intersect
     */
    public boolean intersects(ObjectBounds that) {
        return (x < that.x + that.width && x + width > that.x)
            && (y < that.y + that.height && y + height > that.y);
    }

    /**
     * Check if this object encloses a point
     *
     * @param x x coord of the point
     * @param y y coord of the point
     * @return true when the objects encloses the point
     */
    public boolean encloses(int x, int y) {
        return (this.x <= x && this.x + width >= x)
            && (this.y <= y && this.y + height >= y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "] => [" + width + " x " + height + "]";
    }

    /**
     * Check if this object is inside the given bounds
     *
     * @param bounds bounds to be inside of
     * @return true when this object is inside the given bounds
     */
    public boolean inside(ObjectBounds bounds) {
        return bounds.encloses(x, y) && bounds.encloses(x + width, y + height);
    }
}
