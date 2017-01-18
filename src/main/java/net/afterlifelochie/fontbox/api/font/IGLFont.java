package net.afterlifelochie.fontbox.api.font;

import net.afterlifelochie.fontbox.api.FontboxManager;

public interface IGLFont {
    char MIN_CH = '\u0000';
    char MAX_CH = '\u00ff';

    /**
     * Get the name of the font.
     *
     * @return The name of the font
     */
    String getName();

    /**
     * Get the OpenGL texture ID for this font.
     *
     * @return The 2D texture ID for the font
     */
    int getTextureId();

    /**
     * Get the OpenGL font scale for this font.
     *
     * @return The 2D font scaling ratio
     */
    float getScale();

    /**
     * Get the font metric map associated with this font.
     *
     * @return The metric map
     */
    IGLFontMetrics getMetric();

    /**
     * Delete the font. This releases all the resources associated with the font
     * immediately.
     */
    void delete(FontboxManager manager);
}
