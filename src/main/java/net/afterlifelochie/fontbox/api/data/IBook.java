package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.layout.IPageIndex;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public interface IBook extends IBookProperties {
    List<? extends IPage> pages();

    IPageIndex index();

    GuiScreen getGui(ITracer tracer);
}
