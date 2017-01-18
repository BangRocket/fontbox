package net.afterlifelochie.fontbox.api.font;

import java.util.Map;

public interface IGLFontMetrics {
    /**
     * The individual dimensions and u-v locations of each character in the set
     */
    public Map<Integer, IGLGlyphMetric> getGlyphs();

    /**
     * The universal width of the font image.
     */
    public float getFontImageWidth();

    /**
     * The universal height of the font image.
     */
    float getFontImageHeight();
}
