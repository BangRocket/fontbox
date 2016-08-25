package net.afterlifelochie.fontbox.api.formatting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TextFormatter
{
    private Map<Integer, TextFormat> formatting;

    public TextFormatter()
    {
        formatting = new HashMap<Integer, TextFormat>();
    }

    public TextFormatter(TextFormat defaultFormat)
    {
        this();
        addDefaultFormat(defaultFormat);
    }

    public void addDefaultFormat(TextFormat defaultFormat)
    {
        if (!formatting.containsKey(0))
            formatting.put(0, defaultFormat);
    }

    public void addFormatting(int index, TextFormat format)
    {
        formatting.put(index, format);
    }

    public TextFormat getFormat(int index)
    {
        while (!formatting.containsKey(index))
            index--;
        return formatting.get(index);
    }

    public void cleanAfter(int index)
    {
        Iterator<Map.Entry<Integer, TextFormat>> itr = formatting.entrySet().iterator();
        while (itr.hasNext())
        {
            Map.Entry<Integer, TextFormat> entry = itr.next();
            if (entry.getKey() >= index && entry.getKey() != 0)
                itr.remove();
        }
    }

    public TextFormatter getFormatter(int start, int length)
    {
        TextFormatter formatter = new TextFormatter(getFormat(start));

        for (Map.Entry<Integer, TextFormat> entry : formatting.entrySet())
            if (entry.getKey() >= start && entry.getKey() <= start + length)
                formatter.addFormatting(entry.getKey() - start, entry.getValue());

        return formatter;
    }
}
