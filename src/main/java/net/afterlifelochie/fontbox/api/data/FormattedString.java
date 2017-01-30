package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;

/**
 * A formatted {@link String}
 */
public class FormattedString {
    public final String string;
    public final TextFormatter formatter;

    /**
     * Construct a new unformatted string
     *
     * @param string the text
     */
    public FormattedString(String string) {
        this.string = string;
        this.formatter = new TextFormatter();
    }

    /**
     * Construct a new {@link FormattedString} with given {@link TextFormat}
     *
     * @param string the text
     * @param format the {@link TextFormat} for the text
     */
    public FormattedString(String string, TextFormat format) {
        this(string);
        applyFormat(format);
    }

    /**
     * Apply a {@link TextFormat} to text from index zero
     *
     * @param format the {@link TextFormat} to apply
     * @return this object
     */
    public FormattedString applyFormat(TextFormat format) {
        return applyFormat(format, 0);
    }

    /**
     * Apply a {@link TextFormat} to text from a given index
     *
     * @param format the {@link TextFormat} to apply
     * @param index  the index it starts from
     * @return this object
     */
    public FormattedString applyFormat(TextFormat format, int index) {
        this.formatter.addFormatting(index, format);
        return this;
    }
}
