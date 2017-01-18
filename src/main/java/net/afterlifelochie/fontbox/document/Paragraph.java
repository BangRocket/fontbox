package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.data.FormattedString;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;

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
    public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
        Page page = writer.current();
        boxText(trace, writer, page.properties.bodyFormat, text, null, align);
        writer.cursor().pushDown(page.properties.line_height_size);
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
    public void render(BookGUI gui, int mx, int my, float frame) {
        /* No action required */
    }

    @Override
    public void clicked(BookGUI gui, int mx, int my) {
		/* No action required */
    }

    @Override
    public void typed(BookGUI gui, char val, int code) {
		/* No action required */
    }

}
