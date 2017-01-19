package net.afterlifelochie.fontbox.api.layout;

import net.afterlifelochie.fontbox.document.Element;

public interface IPage extends IContainer {
    Iterable<? extends Element> allElements();

    Iterable<? extends Element> dynamicElements();

    Iterable<? extends Element> staticElements();
}
