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

public class Heading extends Element {
    /**
     * The heading ID
     */
    public String id;
    /**
     * The heading text string
     */
    public FormattedString text;

    /**
     * Creates a new Heading element
     *
     * @param id   The heading's unique identifier
     * @param text The heading's text value
     */
    public Heading(String id, FormattedString text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException {
        IPage page = writer.current();
        boxText(trace, writer, page.getProperties().headingFormat, text, id, AlignmentMode.LEFT);
        writer.cursor().pushDown(10);
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

    @Override
    public String identifier() {
        return id;
    }

}
