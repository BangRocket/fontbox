package net.afterlifelochie.fontbox.layout;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.layout.IElement;
import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.document.Document;
import net.afterlifelochie.fontbox.document.Element;

import java.io.IOException;

public class DocumentProcessor {
    public static IElement getElementAt(IPage page, int x, int y) {
        for (IElement element : page.allElements())
            if (element.bounds().encloses(x, y))
                return element;
        return null;
    }

    /**
     * <p>
     * Generate a list of formatted Pages from a Document and a Page layout
     * configuration. The Document must contain at least one Element in the
     * list. After processing, the Elements inside the Document are written to
     * the pages on the writer stream.
     * </p>
     * <p>
     * The Elements in the Document list are modified so that they contain
     * rendering properties and other pre-computed parameters.
     * </p>
     *
     * @param trace  The debugger
     * @param doc    The Document to transform
     * @param writer The page writer
     * @throws IOException     Any I/O exception which occurs when reading from nested
     *                         streams or when writing to the Page output stream
     * @throws LayoutException Any layout exception which occurs when attempting to place an
     *                         element on a Page
     */
    public static void generatePages(ITracer trace, Document doc, PageWriter writer) throws IOException, LayoutException {
        for (Element element : doc.elements)
            element.layout(trace, writer);
    }
}
