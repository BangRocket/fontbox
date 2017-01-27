package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.render.RenderException;
import net.minecraft.client.gui.GuiScreen;

/**
 * A renderable element
 */
public interface IElement {

    /**
     * <p>
     * Get the {@link ObjectBounds} for this element
     * </p>
     *
     * @return bounds for this element
     */
    ObjectBounds bounds();

    /**
     * <p>
     * Called to render the element on the page.
     * </p>
     *
     * @param gui   The GUI rendering on
     * @param mx    The mouse x-coordinate
     * @param my    The mouse y-coordinate
     * @param frame The current partial frame
     * @throws RenderException Any rendering exception which prevents the element from being
     *                         rendered on the page
     */
    void render(GuiScreen gui, int mx, int my, float frame) throws RenderException;

    /**
     * <p>
     * Called by the container controller when a click occurs on the element.
     * </p>
     *
     * @param gui The GUI being clicked
     * @param mx  The mouse x-coordinate
     * @param my  The mouse y-coordinate
     */
    void clicked(GuiScreen gui, int mx, int my);

    /**
     * <p>
     * Called by the container when a key press occurs.
     * </p>
     *
     *  @param gui  The GUI being typed into
     * @param val  The character value
     * @param code The key code
     */
    void typed(GuiScreen gui, char val, int code);

    /**
     * <p>
     * Called by the generator to ask for unique ID for this element. If the
     * object does not need to be indexed, this method should return null and
     * not be overridden; else, you should return a unique identifier.
     * </p>
     *
     * @return The unique identifier for this element
     */
     default String identifier() {
        return null;
    }
}
