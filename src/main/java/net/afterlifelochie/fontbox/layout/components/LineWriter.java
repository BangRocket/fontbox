package net.afterlifelochie.fontbox.layout.components;

import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.font.IGLGlyphMetric;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;
import net.afterlifelochie.fontbox.api.layout.ObjectBounds;
import net.afterlifelochie.fontbox.layout.PageWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineWriter {
    /**
     * The writer stream
     */
    private final PageWriter writer;
    /**
     * The alignment writing in
     */
    private final AlignmentMode alignment;

    /**
     * The list of words on the stack currently
     */
    private final List<String> words;
    /**
     * The formatter for the text
     */
    private final TextFormatter formatter;
    /**
     * The current computed bounds of the stack's words
     */
    private ObjectBounds bounds;
    /**
     * The current size of the spaces between the stack's words
     */
    private int spaceSize;
    /**
     * Offset for current line
     */
    private int lineOffset;
    /**
     * Current lines uid
     */
    private final String uid;

    /**
     * Construct a new line writing utility. The underlying stream and the
     * writing font must be specified and cannot be null.
     *
     * @param writer    The underlying stream to operate on.
     * @param formatter The text formatter.
     * @param alignment The alignment to paginate in.
     * @param uid       The lines uid.
     */
    public LineWriter(PageWriter writer, TextFormatter formatter, AlignmentMode alignment, String uid) {
        this.writer = writer;
        this.alignment = alignment;
        this.words = new ArrayList<>();
        this.formatter = formatter;
        this.uid = uid;
    }

    private void update() throws LayoutException, IOException {
        int width = 0, height = 0;

        Page page = writer.current();

        int offset = lineOffset;
        int wordsWidth = 0;
        for (String word : words) {
            char[] chars = word.toCharArray();
            for (char cz : chars) {
                TextFormat format = formatter.getFormat(offset);
                IGLGlyphMetric cm = format.font.getMetric().getGlyphs().get((int) cz);
                if (cm == null)
                    throw new LayoutException(String.format("Glyph %s not supported by font %s.", cz,
                        format.font.getName()));
                wordsWidth += cm.getWidth();
                if (cm.getAscent() > height)
                    height = cm.getAscent();
                offset++;
            }
            offset++;
        }

        int blankWidth = page.width - page.properties.margin_left - page.properties.margin_right - wordsWidth;
        spaceSize = page.properties.min_space_size;
        int x = writer.cursor().x(), y = writer.cursor().y();

        switch (alignment) {
            case CENTER:
                float halfBlank = blankWidth / 2.0f;
                x += (int) Math.floor(halfBlank);
                break;
            case JUSTIFY:
                float density = (float) wordsWidth / (float) page.width;
                if (density >= page.properties.min_line_density) {
                    int extra_px_per_space = (int) Math.floor(blankWidth / words.size());
                    if (extra_px_per_space > page.properties.min_space_size)
                        spaceSize = extra_px_per_space;
                }
                break;
            case LEFT:
            /* Do nothing */
                break;
            case RIGHT:
                x += blankWidth;
                break;
        }

        width = wordsWidth + Math.max(words.size() - 2, 0) * spaceSize;
        bounds = new ObjectBounds(x, y, width, Math.max(height, page.properties.line_height_size), FloatMode.NONE);
    }

    /**
     * Called to emit the stack's contents to a Line element. The contents of
     * the stack are automatically cleared and zerored on invocation.
     *
     * @return The formatted line. The stack, properties and other values
     * associated with generating the line are reset on the self object.
     */
    public Line emit() {
        StringBuilder words = new StringBuilder();
        for (int i = 0; i < this.words.size(); i++) {
            String what = this.words.get(i);
            words.append(what);
            if (i < this.words.size() - 1)
                words.append(" ");
        }
        int offset = 0;
        for (String word : this.words)
            offset += word.length() + 1;
        Line what = new Line(words.toString().toCharArray(), formatter.getFormatter(lineOffset, offset), uid, bounds, spaceSize);
        bounds = null;
        spaceSize = 0;
        lineOffset += offset;
        this.words.clear();
        return what;
    }

    /**
     * Get the current pending bounding box of the words on the writer.
     *
     * @return The pending bounding box of the words on the writer.
     */
    public ObjectBounds pendingBounds() {
        return bounds;
    }

    /**
     * Pushes the word onto the writer stack. The word is placed on the end of
     * the stack and the dimensions of the stack are recomputed automatically.
     *
     * @param word The word to place on the end of the stack
     * @throws IOException     Any exception which occurs when reading from the page writing
     *                         stream underlying this writer
     * @throws LayoutException Any exception which occurs when updating the potentially
     *                         paginated text
     */
    public void push(String word) throws LayoutException, IOException {
        words.add(word);
        update();
    }

    /**
     * Removes the word from the end of the writer stack. The word removed is
     * returned and then dimensions of the stack are recomputed automatically.
     *
     * @return The word which was removed from the end of the stack
     * @throws IOException     Any exception which occurs when reading from the page writing
     *                         stream underlying this writer
     * @throws LayoutException Any exception which occurs when updating the potentially
     *                         paginated text
     */
    public String pop() throws LayoutException, IOException {
        String word = words.remove(words.size() - 1);

        int offset = 0;
        for (String wd : words)
            offset += wd.length() + 1;

        formatter.cleanAfter(offset);

        update();
        return word;
    }

    /**
     * Get the size (number of elements) on the stack at the current time.
     *
     * @return The number of elements currently on the writer stack at the time
     * of invocation.
     */
    public int size() {
        return words.size();
    }

}
