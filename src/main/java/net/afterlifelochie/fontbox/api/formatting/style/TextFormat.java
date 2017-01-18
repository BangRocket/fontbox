package net.afterlifelochie.fontbox.api.formatting.style;

import net.afterlifelochie.fontbox.api.font.IGLFont;

import java.util.EnumSet;

public class TextFormat implements Cloneable {
    public final EnumSet<DecorationStyle> decorations;
    public final IGLFont font;
    public final ColorFormat color;

    public TextFormat(IGLFont font) {
        this(font, EnumSet.noneOf(DecorationStyle.class), null);
    }

    public TextFormat(IGLFont font, EnumSet<DecorationStyle> decorations) {
        this(font, decorations, null);
    }

    public TextFormat(IGLFont font, ColorFormat color) {
        this(font, EnumSet.noneOf(DecorationStyle.class), color);
    }

    public TextFormat(IGLFont font, EnumSet<DecorationStyle> decorations, ColorFormat color) {
        this.decorations = decorations;
        this.font = font;
        this.color = color;
    }

    @Override
    public TextFormat clone() {
        EnumSet<DecorationStyle> style = EnumSet.noneOf(DecorationStyle.class);
        style.addAll(decorations);
        return new TextFormat(font, style, (color != null) ? color.clone() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TextFormat))
            return false;
        TextFormat that = (TextFormat) o;
        if (!font.equals(that.font) || !decorations.equals(that.decorations))
            return false;
        if ((that.color != null && color == null) || (that.color == null && color != null))
            return false;
        if (that.color != null && color != null)
            if (!color.equals(that.color))
                return false;
        return true;
    }
}
