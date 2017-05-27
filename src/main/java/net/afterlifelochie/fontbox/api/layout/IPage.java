package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.api.formatting.PageProperties;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A page of {@link IElement}s
 */
public interface IPage extends IContainer, Iterable<IElement> {
    /**
     * @return all elements on the page
     */
    Iterable<IElement> allElements();

    /**
     * Dynamic rendering means that the results should be saved to a display list
     *
     * @return all elements that require dynamic rendering
     */
    Iterable<IElement> dynamicElements();

    /**
     * Static elements can/should be save to a display list for performance reasons
     * See {@link net.minecraft.client.renderer.GlStateManager#glNewList(int, int)}
     * and {@link GlStateManager#glEndList()} for more info
     *
     * @return all elements that can be statically rendered
     */
    Iterable<IElement> staticElements();

    PageProperties getProperties();

    /**
     * Determine if the provided bounding box intersects with an existing
     * element on the page. Returns true if an intersection occurs, false if
     * not.
     *
     * @param bounds The bounding box to check
     * @return If an intersection occurs
     */
     IElement intersectsElement(ObjectBounds bounds);

    /**
     * Determine if the provided bounding box fits entirely on the page. Returns
     * true if the bounding box fits inside the page, false if not.
     *
     * @param bounds The bounding box to check
     * @return If the bounding box fits inside the page
     */
     boolean insidePage(ObjectBounds bounds);

    @Override
    default Iterator<IElement> iterator(){
        List<IElement> list = new LinkedList<>();
        allElements().forEach(list::add);
        return list.iterator();
    }
}
