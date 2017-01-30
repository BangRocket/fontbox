package net.afterlifelochie.fontbox.api.layout;

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
    Iterable<? extends IElement> allElements();

    /**
     * Dynamic rendering means that the results should be saved to a display list
     *
     * @return all elements that require dynamic rendering
     */
    Iterable<? extends IElement> dynamicElements();

    /**
     * Static elements can/should be save to a display list for performance reasons
     * See {@link net.minecraft.client.renderer.GlStateManager#glNewList(int, int)}
     * and {@link GlStateManager#glEndList()} for more info
     *
     * @return all elements that can be statically rendered
     */
    Iterable<? extends IElement> staticElements();

    @Override
    default Iterator<IElement> iterator(){
        List<IElement> list = new LinkedList<>();
        allElements().forEach(list::add);
        return list.iterator();
    }
}
