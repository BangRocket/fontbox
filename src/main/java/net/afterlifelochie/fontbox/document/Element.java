package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.data.FormattedString;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.api.layout.IElement;
import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.layout.IPageWriter;
import net.afterlifelochie.fontbox.api.layout.ObjectBounds;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.layout.components.Line;
import net.afterlifelochie.fontbox.layout.components.LineWriter;
import net.afterlifelochie.fontbox.layout.components.Page;
import net.afterlifelochie.io.StackedPushBackStringReader;

import java.io.IOException;

/**
 * <p>
 * Document element class. Elements are used to in a Document to construct a
 * linked list of objects which are subsequently paginated and rendered.
 * </p>
 *
 * @author AfterLifeLochie
 */
public abstract class Element implements IElement {
    private ObjectBounds bounds;

    /**
     * Get the bounds of the object
     *
     * @return The bounds of the object
     */
    public ObjectBounds bounds() {
        return bounds;
    }

    /**
     * Set the bounds of the object
     *
     * @param bb The new bounds of the object
     */
    public void setBounds(ObjectBounds bb) {
        bounds = bb;
    }

     /**
     * Called to determine if this element requires explicit update ticks. This
     * value is cached; that is, if this method returns <code>false</code>, this
     * method will not be queried again to see if updating is required.
     *
     * @return If the element requires update ticks
     */
    public abstract boolean canUpdate();

    /**
     * Called to update the interface
     */
    public abstract void update();

    /**
     * <p>
     * Attempt to box text from a string onto as many pages as is required. The
     * text provided will be added to the tail of the current page and will
     * overflow onto any subsequent pages as is required.
     * </p>
     *
     * @param trace     The debugging tracer object
     * @param writer    The page writer
     * @param format    The default format
     * @param what      The text to write
     * @param uid       The ID of the text block
     * @param alignment The text alignment mode
     * @throws IOException     Any exception which occurs when reading from the text stream
     * @throws LayoutException Any layout problem which prevents the text from being laid
     *                         out correctly
     */
    protected void boxText(ITracer trace, IPageWriter writer, TextFormat format, FormattedString what, String uid, AlignmentMode alignment) throws IOException, LayoutException {
        StackedPushBackStringReader reader = new StackedPushBackStringReader(what.string);
        trace.trace("Element.boxText", "startBox");
        while (reader.available() > 0) {
            what.formatter.addDefaultFormat(format);

            LineWriter stream = new LineWriter(writer, what.formatter, alignment, uid);
            boxText(trace, writer, stream, reader);
            trace.trace("Element.boxText", "streamRemain", reader.available());
            if (reader.available() > 0)
                writer.next();
        }
        trace.trace("Element.boxText", "endBox");
    }

    /**
     * <p>
     * Attempt to box text from a specified stream onto the page in the bounds
     * specified. The text on the stream will be read and written into Line[]
     * objects such that either:
     * </p>
     * <p>
     * <ul>
     * <li>The stream becomes empty; the cursor on the stream is placed at the
     * end.</li>
     * <li>No further text can be fit into the region specified by the bounds;
     * the cursor on the stream is placed at the end of the last full word
     * written onto the region specified.</li>
     * </ul>
     * </p>
     *
     * @param trace      The debugging tracer object
     * @param pageWriter The underlying stream to write onto
     * @param text       The text stream to read from
     * @throws IOException     Any exception which occurs when reading from the text stream
     * @throws LayoutException Any layout problem which prevents the text from being laid
     *                         out correctly
     */
    protected void boxText(ITracer trace, IPageWriter pageWriter, LineWriter lineWriter, StackedPushBackStringReader text) throws IOException, LayoutException {
        main:
        while (text.available() > 0) {
            // Put some words on the writer:
            while (true) {
                // Push the writer so we can back out
                text.pushPosition();

                // Build the word:
                StringBuilder inWord = new StringBuilder();
                while (true) {
                    char cz = text.next();
                    if (cz == 0)
                        break; // okay, end of stream

                    // Skip spaces or tabs;
                    if (cz != ' ' && cz != '\t')
                        inWord.append(cz); // push
                    else if (inWord.length() > 0)
                        break; // okay, consider now
                }

                if (inWord.toString().trim().length() == 0)
                    break;

                // Consider the word:
                trace.trace("Element.boxText", "considerWord", inWord.toString());
                lineWriter.push(inWord.toString());
                ObjectBounds future = lineWriter.pendingBounds();
                IPage current = pageWriter.current();
                trace.trace("Element.boxText", "considerCursor", pageWriter.cursor());

                // If we overflow the page, back out last change to fit:
                if (!current.insidePage(future)) {
                    trace.trace("Element.boxText", "overflowPage", current.getWidth(), current.getHeight(), future, lineWriter.size());
                    lineWriter.pop();
                    text.popPosition();
                    // If there are now no words on the writer, then
                    if (lineWriter.size() == 0)
                        break main; // nothing fits; break the loop
                    else
                        break; // break the local loop
                } else if (current.intersectsElement(future) != null) {
                    // We hit another object, so let's undo
                    trace.trace("Element.boxText", "collideElement", lineWriter.size());
                    IElement e0 = current.intersectsElement(future);
                    trace.trace("Element.boxText", "collideHit", e0.bounds().toString(), future.toString());
                    lineWriter.pop();
                    text.popPosition();
                    if (lineWriter.size() == 0)
                        break main; // Nothing fits at all where we are; break
                    else
                        break; // break the local loop
                } else {
                    // Store our work
                    trace.trace("Element.boxText", "commitWord");
                    text.commitPosition();
                }
            }

            // Writer now contains a list of words which fit, so do something
            // useful with that line
            Line line = lineWriter.emit();
            trace.trace("Element.boxText", "emitLine", line.line);
            pageWriter.write(line);
        }
    }

}
