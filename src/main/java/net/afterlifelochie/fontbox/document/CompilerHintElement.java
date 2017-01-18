package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.CompilerHint;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.layout.PageCursor;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.fontbox.render.BookGUI;

import java.io.IOException;
import java.util.EnumSet;

public class CompilerHintElement extends Element {
    public EnumSet<CompilerHint> types;

    /**
     * Constructs a new compiler hint rule with one hint type.
     *
     * @param first The type of hint. May not be null.
     */
    public CompilerHintElement(CompilerHint first, CompilerHint... more) {
        if (first == null)
            throw new IllegalArgumentException("Hint type cannot be null");
        types = EnumSet.of(first, more);
    }

    /**
     * Constructs a new compiler hint rule with a set of hint types.
     *
     * @param types The list of hints. May not be null, may not be empty.
     */
    public CompilerHintElement(EnumSet<CompilerHint> types) {
        if (types == null)
            throw new IllegalArgumentException("Hint types cannot be null");
        if (types.size() == 0)
            throw new IllegalArgumentException("Hint list must include 1 or more hints");
        this.types = types;
    }

    @Override
    public void layout(ITracer trace, PageWriter writer) throws IOException, LayoutException {
        for (CompilerHint whatHint : types) {
            switch (whatHint) {
                case FLOAT_BREAK:
                    PageCursor cursor = writer.cursor();
                    Page current = writer.current();
                    Element lowest = null;
                    int dfx = 0;
                    for (Element elem : current.allElements()) {
                        int dux = elem.bounds().y + elem.bounds().height;
                        if (dux > dfx) {
                            dfx = dux;
                            lowest = elem;
                        }
                    }
                    if (lowest == null || !lowest.bounds().floating())
                        return;
                    if (lowest.bounds().x == 0) {
                        cursor.top(lowest.bounds().y + lowest.bounds().height);
                        cursor.left(0);
                    }
                    cursor.top(dfx);

                    break;
                case PAGE_BREAK:
                    writer.next();
                    break;
                default:
                    throw new LayoutException("Unknown compiler hint: " + ((whatHint == null) ? "<null>" : whatHint.getClass().getName()));
            }
        }
    }

    @Override
    public boolean canCompileRender() {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }

    @Override
    public boolean canUpdate() {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }

    @Override
    public void update() {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }

    @Override
    public void render(BookGUI gui, int mx, int my, float frame) {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }

    @Override
    public void clicked(BookGUI gui, int mx, int my) {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }

    @Override
    public void typed(BookGUI gui, char val, int code) {
        throw new RuntimeException("Undefined behaviour: CompilerHintElement in doctree!");
    }
}
