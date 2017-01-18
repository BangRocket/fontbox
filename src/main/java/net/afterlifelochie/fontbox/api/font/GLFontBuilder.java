package net.afterlifelochie.fontbox.api.font;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Apply to field of type {@link IGLFontBuilder}
 * Use this to construct {@link IGLFont}s
 * Provided during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 */
@Target(ElementType.FIELD)
public @interface GLFontBuilder {
}
