package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.data.FormattedString;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.layout.IIndexed;
import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.layout.IPageWriter;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class Paragraph extends Element {
    public FormattedString text;
    public AlignmentMode align;

    /**
     * Create a new paragraph with a specified text and the default alignment
     * (justified).
     *
     * @param text The text
     */
    public Paragraph(FormattedString text) {
        this(text, AlignmentMode.JUSTIFY);
    }

    /**
     * Create a new paragraph with the specified properties.
     *
     * @param text  The text
     * @param align The alignment mode
     */
    public Paragraph(FormattedString text, AlignmentMode align) {
        this.text = text;
        this.align = align;
    }

    @Override
    public void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException {
        IPage page = writer.current();
        boxText(trace, writer, page.getProperties().bodyFormat, text, align, this);
        writer.cursor().pushDown(page.getProperties().line_height_size);
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
        /* No action required */
    }

    @Override
    public void clicked(IIndexed gui, int mx, int my) {
        /* No action required */
    }

    @Override
    public void typed(GuiScreen gui, char val, int code) {
		/* No action required */
    }

}
