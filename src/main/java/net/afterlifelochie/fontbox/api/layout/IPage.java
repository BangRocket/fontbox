package net.afterlifelochie.fontbox.api.layout;

public interface IPage extends IContainer {
    Iterable<? extends IElement> allElements();

    Iterable<? extends IElement> dynamicElements();

    Iterable<? extends IElement> staticElements();
}
