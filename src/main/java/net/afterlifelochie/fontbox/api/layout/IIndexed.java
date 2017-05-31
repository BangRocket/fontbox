package net.afterlifelochie.fontbox.api.layout;

import net.minecraft.client.gui.GuiScreen;

public interface IIndexed {
    /**
     * Advance to the next page
     */
    void next();

    /**
     * Go to a page in the index; if the item doesn't exist, no navigation
     * occurs
     *
     * @param id The ID of the anchor to go to
     */
    void go(String id);

    /**
     * Go to a page
     *
     * @param where The page pointer
     */
    void go(int where);

    /**
     * Reverse to the previous page
     */
    void previous();

    GuiScreen getGuiScreen();
}
