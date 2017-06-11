package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.data.IBookProperties;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;
import net.afterlifelochie.fontbox.api.layout.*;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class Image extends Element {
    /**
     * The resource source
     */
    public ResourceLocation source;
    public int width, height;

    /**
     * The alignment of the image
     */
    public AlignmentMode align;
    /**
     * The floating of the image
     */
    public FloatMode floating;

    /**
     * Creates a new inline image with the properties specified.
     *
     * @param source The image source location, may not be null.
     * @param width  The width of the image.
     * @param height The height of the image.
     */
    public Image(ResourceLocation source, int width, int height) {
        this(source, width, height, AlignmentMode.LEFT, FloatMode.NONE);
    }

    /**
     * Creates a new inline image with the properties specified.
     *
     * @param source The image source location, may not be null.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @param align  The alignment of the image.
     */
    public Image(ResourceLocation source, int width, int height, AlignmentMode align) {
        this(source, width, height, align, FloatMode.NONE);
    }

    /**
     * Creates a new floating image with the properties specified.
     *
     * @param source   The image source location, may not be null.
     * @param width    The width of the image.
     * @param height   The height of the image.
     * @param floating The floating mode.
     */
    public Image(ResourceLocation source, int width, int height, FloatMode floating) {
        this(source, width, height, AlignmentMode.LEFT, floating);
    }

    /**
     * Creates a new image with the properties specified.
     *
     * @param source   The image source location, may not be null.
     * @param width    The width of the image.
     * @param height   The height of the image.
     * @param align    The alignment of the image.
     * @param floating The floating mode.
     */
    public Image(ResourceLocation source, int width, int height, AlignmentMode align, FloatMode floating) {
        this.source = source;
        this.width = width;
        this.height = height;
        this.align = align;
        this.floating = floating;
    }

    @Override
    public void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException {
        IPage current = writer.current();
        PageCursor cursor = writer.cursor();
        int yh = cursor.y() + height;
        if (yh > current.getProperties().height) {
            current = writer.next();
            cursor = writer.cursor();
        }

        int x;

        switch (align) {
            case CENTER:
                float qt = current.getProperties().width - width;
                x = (int) Math.floor(qt / 2.0f);
                break;
            case JUSTIFY:
                float srh = (float) height / (float) width;
                width = current.getProperties().width - cursor.x();
                height = (int) Math.ceil(width * srh);
                x = cursor.x();
                break;
            case LEFT:
                x = 0;
                break;
            default:
                x = cursor.x();
                break;
            case RIGHT:
                x = current.getProperties().width - width;
                break;
        }

        if (floating == FloatMode.RIGHT)
            x = current.getProperties().width - width;

        trace.trace("Image.layout", "finalize", x, cursor.y(), width, height, floating != FloatMode.NONE);
        setBounds(new ObjectBounds(x, cursor.y(), width, height, floating));
        writer.write(this);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void update() {
        /* No action required */
    }

    @Override
    public boolean canCompileRender() {
        return true;
    }

    @Override
    public void render(GuiScreen gui, int mx, int my, float frame) {
        GlStateManager.pushMatrix();
        GLUtils.useSystemTexture(source);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GLUtils.drawTexturedRectUV(
            bounds().x * IBookProperties.SCALE, bounds().y * IBookProperties.SCALE,
            bounds().width * IBookProperties.SCALE, bounds().height * IBookProperties.SCALE,
            0, 0, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void clicked(IIndexed gui, int mx, int my) {
        /* No action required */
    }

    @Override
    public void typed(GuiScreen gui, char val, int code) {
        /* No action required */
    }

    @Override
    public void hover(GuiScreen gui, int mx, int my) {
        /* No action required */
    }
}
