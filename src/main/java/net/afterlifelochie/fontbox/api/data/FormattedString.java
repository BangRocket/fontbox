package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormatter;

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
