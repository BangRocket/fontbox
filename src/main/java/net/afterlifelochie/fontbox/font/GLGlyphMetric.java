package net.afterlifelochie.fontbox.font;

import net.afterlifelochie.fontbox.api.font.IGLGlyphMetric;

/**
 * Metrics about a character
 *
 * @author AfterLifeLochie
 */
public class GLGlyphMetric implements IGLGlyphMetric
{

    public final int width, height, ascent, ux, vy;

    /**
     * Creates a new GlpyhMetric.
     *
     * @param w The character's glyph width. Used to compute the size of the
     *          glyph on the screen and the size of the texture region.
     * @param h The character's height. Used to compute the size of the glyph
     *          on the screen and the size of the texture region.
     * @param a The character's ascent (ie, the distance from the baseline of
     *          the character to the top of the character). Used to calculate
     *          the vertical offset of the rendered character.
     * @param u The u origin-coordinate of the texture
     * @param v The v origin-coordinate of the texture
     */
    public GLGlyphMetric(int w, int h, int a, int u, int v)
    {
        width = w;
        height = h;
        ascent = a;
        ux = u;
        vy = v;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getAscent()
    {
        return ascent;
    }

    @Override
    public int getUx()
    {
        return ux;
    }

    @Override
    public int getVy()
    {
        return vy;
    }

    @Override
    public String toString()
    {
        return "GLGlyphMetric { width: " + width + ", height: " + height + ", ascent: " + ascent + ", ux: " + ux
                + ", vy: " + vy + " }";
    }
}