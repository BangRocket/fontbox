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

public class Link extends Element {
    /**
     * The heading text string
     */
    public FormattedString text;
    /**
     * The link ID
     */
    public String id;

    /**
     * Creates a new Link element
     *
     * @param text The link's text value
     * @param id   The unique identifier that it links to
     */
    public Link(FormattedString text, String id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public void layout(ITracer trace, IPageWriter writer) throws IOException, LayoutException {
        IPage page = writer.current();
        boxText(trace, writer, page.getProperties().headingFormat, text, AlignmentMode.LEFT, this);
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
        gui.go(id);
    }

    @Override
    public void typed(GuiScreen gui, char val, int code) {
		/* No action required */
    }
}
