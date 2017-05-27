package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.render.RenderException;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

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
     * @param gui  The GUI being typed into
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

    /**
     * Called to determine if this element can be compile-rendered. If an
     * element is compiled-rendered, it will be drawn once to a video-buffer;
     * else, the element will be redrawn each frame.
     *
     * @return If the element can be compile-rendered.
     */
     boolean canCompileRender();

    /**
     * <p>
     * Called by the document generator to request this element fill in it's
     * rendering-based properties. The element should place itself on the
     * current page and update the writing cursor if required.
     * </p>
     *
     * @param trace  The debugging tracer object
     * @param writer The current page writer
     * @throws IOException     Any I/O exception which occurs when writing to the stream
     * @throws LayoutException Any exception which prevents the element from being written
     *                         to the writing stream
     */
     void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException;
}
