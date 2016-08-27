package net.afterlifelochie.fontbox.font;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.FontException;
import net.afterlifelochie.fontbox.api.font.IGLFont;
import net.afterlifelochie.fontbox.api.font.IGLFontBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GLFontBuilder implements IGLFontBuilder
{
    @Override
    public IGLFont fromTTF(FontboxManager manager, float px, ResourceLocation ttf) throws FontException
    {
        if (manager == null || manager.tracer() == null)
            throw new IllegalArgumentException("trace may not be null");
        if (ttf == null)
            throw new IllegalArgumentException("ttf may not be null");
        try
        {
            IResource metricResource = Minecraft.getMinecraft().getResourceManager().getResource(ttf);
            InputStream stream = metricResource.getInputStream();
            if (stream == null)
                throw new IOException("Could not open TTF file.");
            Font sysfont = Font.createFont(Font.TRUETYPE_FONT, stream);
            manager.tracer().trace("GLFont.fromTTF", sysfont.getName());
            return fromFont(manager, sysfont.deriveFont(px));
        } catch (IOException ioex)
        {
            manager.tracer().trace("GLFont.fromTTF", ioex);
            throw new FontException("Can't perform I/O operation!", ioex);
        } catch (FontFormatException ffe)
        {
            manager.tracer().trace("GLFont.fromTTF", ffe);
            throw new FontException("Invalid TTF file!", ffe);
        }
    }

    @Override
    public IGLFont fromSpriteFont(FontboxManager manager, String name, ResourceLocation image, ResourceLocation xml) throws FontException
    {
        if (manager == null || manager.tracer() == null)
            throw new IllegalArgumentException("trace may not be null");
        if (name == null)
            throw new IllegalArgumentException("name may not be null");
        if (image == null)
            throw new IllegalArgumentException("image may not be null");
        if (xml == null)
            throw new IllegalArgumentException("xml may not be null");
        try
        {
            IResource imageResource = Minecraft.getMinecraft().getResourceManager().getResource(image);
            InputStream stream = imageResource.getInputStream();
            if (stream == null)
                throw new IOException("Could not open image file.");
            BufferedImage buffer = ImageIO.read(stream);

            GLFontMetrics metric = GLFontMetrics.fromResource(manager.tracer(), xml, buffer.getWidth(), buffer.getHeight());
            manager.tracer().trace("GLFont.fromSpriteFont", "fromMetric", metric);
            IGLFont f0 = GLFont.fromBuffer(manager, name, buffer, buffer.getWidth(), buffer.getHeight(), metric);
            manager.tracer().trace("GLFont.fromSpriteFont", f0);
            return f0;

        } catch (IOException ioex)
        {
            manager.tracer().trace("GLFont.fromSpriteFont", ioex);
            throw new FontException("Can't perform I/O operation!", ioex);
        }
    }

    @Override
    public IGLFont fromFont(FontboxManager manager, Font font) throws FontException
    {
        if (manager == null || manager.tracer() == null)
            throw new IllegalArgumentException("trace may not be null");
        if (font == null)
            throw new IllegalArgumentException("font may not be null");
        BufferedImage buffer = new BufferedImage(RASTER_DIM, RASTER_DIM, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) buffer.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int off = 0;
        int charsPerRow = 12;
        for (char k = IGLFont.MIN_CH; k <= IGLFont.MAX_CH; k++, off++)
        {
            TextLayout layout = new TextLayout(String.valueOf(k), font, graphics.getFontRenderContext());
            Rectangle2D rect = layout.getBounds();
            int x = (off % charsPerRow) * (RASTER_DIM / charsPerRow);
            int y = (off / charsPerRow) * (RASTER_DIM / charsPerRow);
            float cy = (float) rect.getHeight();
            graphics.setColor(Color.WHITE);
            manager.tracer().trace("GLFont.fromFont", "placeGlyph", k, x, y - cy);
            layout.draw(graphics, x, y - cy);
        }

        GLFontMetrics metric = GLFontMetrics.fromFontMetrics(manager.tracer(), font, graphics.getFontRenderContext(), RASTER_DIM,
                RASTER_DIM, charsPerRow, IGLFont.MIN_CH, IGLFont.MAX_CH);
        manager.tracer().trace("GLFont.fromFont", "fromMetric", metric);
        IGLFont f0 = GLFont.fromBuffer(manager, font.getFontName(), buffer, RASTER_DIM, RASTER_DIM, metric);
        manager.tracer().trace("GLFont.fromFont", f0);
        return f0;
    }
}
