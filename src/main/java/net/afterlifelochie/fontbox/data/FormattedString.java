package net.afterlifelochie.fontbox.data;

import net.afterlifelochie.fontbox.document.formatting.TextFormat;
import net.afterlifelochie.fontbox.layout.components.TextFormatter;

public class FormattedString
{
    public final String string;
    public final TextFormatter formatter;

    public FormattedString(String string)
    {
        this.string = string;
        this.formatter = new TextFormatter();
    }

    public FormattedString applyFormat(TextFormat format, int index)
    {
        this.formatter.addFormatting(index, format);
        return this;
    }
}
