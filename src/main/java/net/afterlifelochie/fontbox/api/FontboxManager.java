package net.afterlifelochie.fontbox.api;

import net.afterlifelochie.fontbox.api.font.IGLFont;
import net.afterlifelochie.fontbox.font.GLFont;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.api.tracer.VoidTracer;

import java.util.HashMap;

/**
 * Manger used for passing around commonly used objects
 * and doing some debug checks and prints
 */
public class FontboxManager
{
    /**
     * The system tracer
     */
    private ITracer tracer = new VoidTracer();

    /**
     * Perform a protected assertion
     *
     * @param condition The condition
     * @param reason    The error message to raise if the condition is not true
     */
    public void doAssert(boolean condition, String reason)
    {
        if (!condition && (tracer == null || tracer.enableAssertion()))
            throw new AssertionError(reason);
    }

    /**
     * Get the current system tracer.
     *
     * @return The current system tracer
     */
    public ITracer tracer()
    {
        return tracer;
    }

    /**
     * Set the current system tracer.
     *
     * @param tracer The new tracer
     */
    public void setTracer(ITracer tracer)
    {
        this.tracer = tracer;
    }

    /**
     * The map of all font names to fonts
     */
    private HashMap<String, IGLFont> fonts = new HashMap<String, IGLFont>();

    /**
     * Allocate a font on the font record heap. The font can later be referenced
     * using {@link FontboxManager#fromName(String)}.
     *
     * @param font The font object
     */
    public void allocateFont(IGLFont font)
    {
        fonts.put(font.getName(), font);
    }

    /**
     * Delete a font on the font record heap. The font is de-registered from the
     * heap and can no longer be referenced using
     * {@link FontboxManager#fromName(String)}. Note that this doesn't de-allocate the
     * resources associated with the font.
     *
     * @param font The font to de-register
     * @see GLFont#delete(FontboxManager) ()
     */
    public void deleteFont(IGLFont font)
    {
        fonts.remove(font.getName());
    }

    /**
     * Get a font from the font record heap. If the font hasn't been loaded or
     * doesn't exist, null will be returned.
     *
     * @param name The name of the font, case sensitive
     * @return The game font associated with the name, or null if the font
     * hasn't been loaded or doesn't exist.
     */
    public IGLFont fromName(String name)
    {
        return fonts.get(name);
    }
}
