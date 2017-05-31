package net.afterlifelochie.fontbox.layout;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.formatting.PageProperties;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;
import net.afterlifelochie.fontbox.api.layout.*;
import net.afterlifelochie.fontbox.layout.components.LineWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.io.IntegerExclusionStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class PageWriter implements IPageWriter {
    private final Object lock = new Object();
    private final FontboxManager manager;
    private ArrayList<Page> pages = new ArrayList<>();
    private ArrayList<PageCursor> cursors = new ArrayList<>();
    private PageProperties attributes;
    private PageIndex index;
    private boolean closed = false;
    private int ptr = 0;

    public PageWriter(PageProperties attributes, FontboxManager manager) {
        this.attributes = attributes;
        this.manager = manager;
        this.index = new PageIndex();
    }

    @Override
    public void close() {
        synchronized (lock) {
            closed = true;
        }
    }

    private void checkOpen() throws IOException {
        synchronized (lock) {
            if (closed)
                throw new IOException("Writer closed!");
        }
    }

    @Override
    public Page previous() throws IOException {
        synchronized (lock) {
            checkOpen();
            seek(-1);
            return pages.get(ptr);
        }
    }

    @Override
    public Page next() throws IOException {
        synchronized (lock) {
            checkOpen();
            seek(1);
            return pages.get(ptr);
        }
    }

    @Override
    public Page current() throws IOException {
        synchronized (lock) {
            checkOpen();
            seek(0);
            return pages.get(ptr);
        }
    }

    @Override
    public boolean write(IElement element) throws IOException {
        synchronized (lock) {
            checkOpen();
            Page currentPage = current();
            if (element.bounds() == null)
                throw new IOException("Cannot write unbounded object to page.");
            manager.doAssert(currentPage.insidePage(element.bounds()), "Element outside page boundary.");
            IElement intersect = currentPage.intersectsElement(element.bounds());
            manager.doAssert(intersect == null, "Element intersects existing element " + intersect + ": box "
                + ((intersect != null && intersect.bounds() != null) ? intersect.bounds() : "<null>") + " and "
                + element.bounds() + "!");

            if (element.identifier() != null)
                index.push(element.identifier(), ptr);

            currentPage.push(element);

            PageCursor current = cursor();
            for (IElement e : currentPage.allElements()) {
                if (e.bounds().floating())
                    continue;
                ObjectBounds bb = e.bounds();
                if (bb.y + bb.height + 1 > current.y())
                    current.top(bb.y + bb.height + 1);
            }

            IntegerExclusionStream window = new IntegerExclusionStream(0, currentPage.width);
            for (IElement e : currentPage.allElements()) {
                ObjectBounds bb = e.bounds();
                if (current.y() >= bb.y && bb.y + bb.height >= current.y())
                    window.excludeRange(0, bb.x + bb.width);
            }
            current.left(window.largest());

            manager.tracer().trace("PageWriter.write", "pushCursor", current);

            return true;
        }
    }

    @Override
    public PageCursor cursor() throws IOException {
        synchronized (lock) {
            checkOpen();
            return cursors.get(ptr);
        }
    }

    private void seek(int which) throws IOException {
        synchronized (lock) {
            ptr += which;
            if (0 > ptr)
                ptr = 0;
            if (ptr > pages.size())
                ptr = pages.size();
            if (ptr == pages.size()) {
                pages.add(new Page(attributes.copy()));
                cursors.add(new PageCursor());
            }
        }
    }

    @Override
    public List<? extends IPage> pages() {
        synchronized (lock) {
            if (!closed)
                return (List<Page>) pages.clone();
            return pages;
        }
    }

    @Override
    public PageIndex index() throws IOException {
        synchronized (lock) {
            if (!closed)
                throw new IOException("Writing not finished!");
            return index;
        }
    }

    @Override
    public ILineWriter getLineWriter(TextFormatter formatter, AlignmentMode alignment, IElement underlyingElement) {
        return new LineWriter(this, formatter, alignment, underlyingElement);
    }
}
