package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.formatting.PageMode;
import net.afterlifelochie.fontbox.api.formatting.PageProperties;
import net.minecraft.client.gui.GuiScreen;

public interface IBook
{
    PageProperties getPageProperties();
    PageMode getPageMode();

    /**
     * Called when the current page is changed
     *
     * @param gui     The current GUI
     * @param whatPtr The new page pointer value
     */
    void onPageChanged(GuiScreen gui, int whatPtr);

    /**
     * <p>
     * Draw the background layer of the interface. You must leave the opengl
     * state such that the layout (0, 0) will be drawn in the current place.
     * </p>
     *
     * @param width     The width of the gui
     * @param height    The height of the gui
     * @param mx        The mouse x-coordinate
     * @param my        The mouse y-coordinate
     * @param frame     The partial frames rendered
     * @param zLevel    The current zLevel
     */
    void drawBackground(int width, int height, int mx, int my, float frame, float zLevel);

    /**
     * <p>
     * Draw the foreground layer of the interface. The opengl state is such that
     * the layout coordinates (0, 0) are in the top-left corner of the written
     * text.
     * </p>
     *
     * @param width     The width of the gui
     * @param height    The height of the gui
     * @param mx        The mouse x-coordinate
     * @param my        The mouse y-coordinate
     * @param frame     The partial frames rendered
     * @param zLevel    The current zLevel
     */
    void drawForeground(int width, int height, int mx, int my, float frame, float zLevel);
}
