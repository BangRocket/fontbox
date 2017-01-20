package net.afterlifelochie.fontbox;

import net.afterlifelochie.fontbox.api.data.IBook;
import net.afterlifelochie.fontbox.api.data.IBookProperties;
import net.afterlifelochie.fontbox.api.formatting.PageMode;
import net.afterlifelochie.fontbox.api.formatting.PageProperties;
import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.layout.IPageIndex;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class Book implements IBook {

    private final List<? extends IPage> pages;
    private final IPageIndex index;
    private final IBookProperties properties;

    public Book(List<? extends IPage> pages, IPageIndex index, IBookProperties properties) {
        this.pages = pages;
        this.index = index;
        this.properties = properties;
    }

    @Override
    public List<? extends IPage> pages() {
        return pages;
    }

    @Override
    public IPageIndex index() {
        return index;
    }

    @Override
    public PageProperties getPageProperties() {
        return properties.getPageProperties();
    }

    @Override
    public PageMode getPageMode() {
        return properties.getPageMode();
    }

    @Override
    public void onPageChanged(GuiScreen gui, int whatPtr) {
        properties.onPageChanged(gui, whatPtr);
    }

    @Override
    public void drawBackground(int width, int height, int mx, int my, float frame, float zLevel) {
        properties.drawBackground(width, height, mx, my, frame, zLevel);
    }

    @Override
    public void drawForeground(int width, int height, int mx, int my, float frame, float zLevel) {
        properties.drawForeground(width, height, mx, my, frame, zLevel);
    }

    @Override
    public GuiScreen getGui(ITracer tracer) {
        BookGUI gui = new BookGUI(properties, tracer);
        gui.changePages(pages, index);
        return gui;
    }
}
