package net.afterlifelochie.fontbox.layout.components;

import net.afterlifelochie.fontbox.api.layout.IContainer;

/**
 * Layout container object. Containers have properties such as a width and a
 * height, and can contain child Container elements.
 *
 * @author AfterLifeLochie
 */
public abstract class Container implements IContainer {
    /**
     * The width of the container
     */
    public int width;
    /**
     * The height of the container
     */
    public int height;

    /**
     * Initialize a new container
     *
     * @param width  The desired width
     * @param height The desired height
     */
    public Container(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
