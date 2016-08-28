package net.afterlifelochie.fontbox.api.formatting.layout;

/**
 * Page layout container
 *
 * @author AfterLifeLochie
 */
public class Layout
{
    public int x, y;

    /**
     * Create a new Layout container for rendering the page on screen.
     *
     * @param x The x-coordinate to render at
     * @param y The y-coordinate to render at
     */
    public Layout(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
