package net.afterlifelochie.fontbox.layout.components;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.font.IGLFont;
import net.afterlifelochie.fontbox.api.font.IGLFontMetrics;
import net.afterlifelochie.fontbox.api.font.IGLGlyphMetric;
import net.afterlifelochie.fontbox.api.formatting.style.DecorationStyle;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;
import net.afterlifelochie.fontbox.api.layout.IIndexed;
import net.afterlifelochie.fontbox.api.layout.IPageWriter;
import net.afterlifelochie.fontbox.api.layout.ObjectBounds;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.document.Element;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.afterlifelochie.fontbox.render.RenderException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * One formatted line with a spacing and line-height
 *
 * @author AfterLifeLochie
 */
public class Line extends Element {
    /**
     * The characters
     */
    public final char[] line;
    /**
     * The character formatter
     */
    public final TextFormatter formatter;

    /**
     * The line's ID
     */
    public String id;
    /**
     * The size of the spacing between words
     */
    public final int space_size;

    /**
     * Create a new line
     *
     * @param line       The line's text
     * @param formatter  The text formatter
     * @param bounds     The location of the line
     * @param space_size The size of the spacing between words
     */
    public Line(char[] line, TextFormatter formatter, ObjectBounds bounds, int space_size) {
        setBounds(bounds);
        this.id = null;
        this.line = line;
        this.formatter = formatter;
        this.space_size = space_size;
    }

    /**
     * Create a new line with an ID
     *
     * @param line       The line's text
     * @param formatter  The text formatter
     * @param uid        The line's ID
     * @param bounds     The location of the line
     * @param space_size The size of the spacing between words
     */
    public Line(char[] line, TextFormatter formatter, String uid, ObjectBounds bounds, int space_size) {
        this(line, formatter, bounds, space_size);
        this.id = uid;
    }

    @Override
    public void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException {
        throw new LayoutException("Cannot layout Line type; Line already laid!");
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

    private void safeSwitchToFont(IGLFont font) throws RenderException {
        if (font.getTextureId() == -1)
            throw new RenderException("Font object not loaded!");
        IGLFontMetrics metric = font.getMetric();
        if (metric == null)
            throw new RenderException("Font object not loaded!");
        GlStateManager.bindTexture(font.getTextureId());
        GlStateManager.scale(font.getScale(), font.getScale(), 1.0f);
    }

    @Override
    public void render(GuiScreen gui, int mx, int my, float frame) throws RenderException {
        float x = 0, y = 0;
        if (line.length == 0)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        TextFormat decorator = formatter.getFormat(0);
        GlStateManager.pushMatrix();
        safeSwitchToFont(decorator.font);
        GlStateManager.translate(bounds().x, bounds().y, 0);

        for (int i = 0; i < line.length; i++) {
            char c = line[i];
            if (c != ' ') {
                TextFormat newDecorator = formatter.getFormat(i);
                if (newDecorator != null && !newDecorator.equals(decorator)) {
                    if (newDecorator.font != decorator.font) {
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        safeSwitchToFont(newDecorator.font);
                        GlStateManager.translate(bounds().x, bounds().y, 0);
                    }
                    decorator = newDecorator;
                }

                IGLFontMetrics metric = decorator.font.getMetric();
                IGLGlyphMetric glyph = metric.getGlyphs().get((int) c);
                if (glyph == null) // blank glyph?
                    continue;

                if (decorator.color == null)
                    GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0f);
                else
                    GlStateManager.color(decorator.color.redF(), decorator.color.greenF(), decorator.color.blueF(),
                        decorator.color.alphaF());

                float tiltTop = 0.0f, tiltBottom = 0.0f;
                if (decorator.decorations.contains(DecorationStyle.ITALIC)) {
                    tiltTop = -5.55f;
                    tiltBottom = 5.55f;
                }

                boolean underline = decorator.decorations.contains(DecorationStyle.UNDERLINE);
                renderGlyphInPlace(metric, glyph, x, y, tiltTop, tiltBottom, underline);
                if (decorator.decorations.contains(DecorationStyle.BOLD))
                    renderGlyphInPlace(metric, glyph, x + 0.5f, y + 0.5f, tiltTop, tiltBottom, underline);

                x += glyph.getWidth();
            } else
                x += space_size;
        }

        GlStateManager.popMatrix();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void renderGlyphInPlace(IGLFontMetrics metric, IGLGlyphMetric glyph, float x, float y, float tiltTop, float tiltBottom, boolean underline) {
        final double z = 1.0;
        double u = glyph.getUx() / metric.getFontImageWidth();
        double v = (glyph.getVy() - glyph.getAscent()) / metric.getFontImageHeight();
        double us = glyph.getWidth() / metric.getFontImageWidth();
        double vs = glyph.getHeight() / metric.getFontImageHeight();

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(x + tiltTop, y + glyph.getHeight(), z).tex(u, v + vs).endVertex();
        buffer.pos(x + tiltTop + glyph.getWidth(), y + glyph.getHeight(), z).tex(u + us, v + vs).endVertex();
        buffer.pos(x + tiltBottom + glyph.getWidth(), y, z).tex(u + us, v).endVertex();
        buffer.pos(x + tiltBottom, y, z).tex(u, v).endVertex();

        tessellator.draw();

        if (underline)
            GLUtils.drawLine(x, y + glyph.getHeight() * 0.75, x + glyph.getWidth() + tiltBottom, y + glyph.getHeight() * 0.75, z);
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
    public String identifier() {
        return id;
    }
}