package net.afterlifelochie.fontbox.api.formatting;

/**
 * The page-up mode.
 */
public class PageMode
{
    /**
     * The number of pages in this mode
     */
    public final int pages;

    /**
     * The page layouts for this mode
     */
    public final Layout[] layouts;

    public PageMode(Layout... layouts)
    {
        this.pages = layouts.length;
        this.layouts = layouts;
    }
}
