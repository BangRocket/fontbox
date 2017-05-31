package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;

import java.io.IOException;
import java.util.List;

public interface IPageWriter {
    void close();

    IPage previous() throws IOException;

    IPage next() throws IOException;

    IPage current() throws IOException;

    boolean write(IElement element) throws IOException;

    PageCursor cursor() throws IOException;

    List<? extends IPage> pages();

    IPageIndex index() throws IOException;

    /**
     * Construct a new line writing utility. The underlying stream and the
     * writing font must be specified and cannot be null.
     *
     * @param formatter The text formatter.
     * @param alignment The alignment to paginate in.
     * @param underlyingElement       The lines underlying {@link IElementgr}.
     */
    ILineWriter getLineWriter(TextFormatter formatter, AlignmentMode alignment, IElement underlyingElement);
}
