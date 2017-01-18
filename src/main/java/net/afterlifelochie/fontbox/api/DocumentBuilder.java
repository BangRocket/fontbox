package net.afterlifelochie.fontbox.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Add to field of type {@link IDocumentBuilder}
 * Used for building {@link net.afterlifelochie.fontbox.api.data.IDocument}s
 * Provided during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 */
@Target(ElementType.FIELD)
public @interface DocumentBuilder {
}
