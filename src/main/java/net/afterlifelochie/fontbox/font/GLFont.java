package net.afterlifelochie.fontbox.font;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.FontException;
import net.afterlifelochie.fontbox.api.font.IGLFont;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

/**
 * Represents a Font object for OpenGL.
 *
 * @author AfterLifeLochie
 */
public class GLFont implements IGLFont
{
    /**
     * Create a GLFont from an image buffer of a specified size with a specified
     * metric map.
     *
     * @param manager   The used manager
     * @param name      The name of the font
     * @param image     The buffered image
     * @param width     The width of the image, absolute pixels
     * @param height    The height of the image, absolute pixels
     * @param metric    The font metric map
     * @return The GLFont result
     * @throws FontException Any exception which occurs when transforming the buffer into
     *                       a GLFont container.
     */
    public static IGLFont fromBuffer(FontboxManager manager, String name, BufferedImage image, int width, int height, GLFontMetrics metric) throws FontException
    {
        if (manager == null || manager.tracer() == null)
            throw new IllegalArgumentException("trace may not be null");
        if (name == null)
            throw new IllegalArgumentException("name may not be null");
        if (image == null)
            throw new IllegalArgumentException("image may not be null");
        if (metric == null)
            throw new IllegalArgumentException("metric may not be null");
        ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{
                8, 8, 8, 8}, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
        BufferedImage texImage = new BufferedImage(glAlphaColorModel, raster, true, new Hashtable<Object, Object>());
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, width, height);
        g.drawImage(image, 0, 0, null);

        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.order(ByteOrder.nativeOrder());
        buffer.put(data, 0, data.length);
        buffer.flip();


        int texIdx = GlStateManager.generateTexture();
        GlStateManager.bindTexture(texIdx);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, buffer.asIntBuffer());
        manager.tracer().trace("GLFont.fromBuffer", "texId", texIdx);
        GLFont font = new GLFont(name, texIdx, 0.44f, metric);
        manager.tracer().trace("GLFont.fromBuffer", font);
        manager.allocateFont(font);
        return font;
    }

    private String name;
    private float scale;
    private int textureId;
    private GLFontMetrics metric;

    private GLFont(String name, int textureId, float scale, GLFontMetrics metric)
    {
        this.name = name;
        this.textureId = textureId;
        this.scale = scale;
        this.metric = metric;
    }

    /**
     * Get the name of the font.
     *
     * @return The name of the font
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the OpenGL texture ID for this font.
     *
     * @return The 2D texture ID for the font
     */
    public int getTextureId()
    {
        return textureId;
    }

    /**
     * Get the OpenGL font scale for this font.
     *
     * @return The 2D font scaling ratio
     */
    public float getScale()
    {
        return scale;
    }

    /**
     * Get the font metric map associated with this font.
     *
     * @return The metric map
     */
    public GLFontMetrics getMetric()
    {
        return metric;
    }

    /**
     * Delete the font. This releases all the resources associated with the font
     * immediately.
     */
    public void delete(FontboxManager manager)
    {
        manager.deleteFont(this);
        GlStateManager.deleteTexture(textureId);
        textureId = -1;
        name = null;
        metric = null;
    }

    @Override
    public String toString()
    {
        return "GLFont { hash: " + System.identityHashCode(this) + ", texture: " + textureId + ", metric: "
                + System.identityHashCode(metric) + " }";
    }
}
