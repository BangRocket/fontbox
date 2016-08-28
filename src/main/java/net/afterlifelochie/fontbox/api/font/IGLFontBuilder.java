package net.afterlifelochie.fontbox.api.font;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.FontException;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public interface IGLFontBuilder
{
    int RASTER_DIM = 512;

    /**
     * Create a GLFont from a TTF file
     *
     * @param manager   The manger used
     * @param px        The font pixel size
     * @param ttf       The TTF file
     * @return The GLFont result
     * @throws FontException Any exception which occurs when reading the TTF file, brewing
     *                       the buffer or creating the final font.
     */
    IGLFont fromTTF(FontboxManager manager, float px, ResourceLocation ttf) throws FontException;


    /**
     * Create a GLFont from a sprite font and XML descriptor
     *
     * @param manager   The manger used
     * @param name      The name of the font, case sensitive
     * @param image     The image file
     * @param xml       The XML descriptor file
     * @return The GLFont result
     * @throws FontException Any exception which occurs when reading the image file, when
     *                       reading the XML descriptor, when brewing the buffer or
     *                       creating the final font.
     */
    IGLFont fromSpriteFont(FontboxManager manager, String name, ResourceLocation image, ResourceLocation xml) throws FontException;

    /**
     * Create a GLFont from a Java Font object
     *
     * @param manager   The manger used
     * @param font      The font object
     * @return The GLFont result
     * @throws FontException Any exception which occurs when brewing the buffer or
     *                       creating the final result.
     */
    IGLFont fromFont(FontboxManager manager, Font font) throws FontException;
}
