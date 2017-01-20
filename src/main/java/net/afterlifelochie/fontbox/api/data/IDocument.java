package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.layout.CompilerHint;
import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public interface IDocument {
    void addHeading(String uid, FormattedString text);

    void addParagraph(FormattedString text);

    void addParagraph(FormattedString text, AlignmentMode align);

    void addImage(ResourceLocation location, int width, int height);

    void addImage(ResourceLocation location, int width, int height, AlignmentMode align);

    void addImage(ResourceLocation location, int width, int height, FloatMode floating);

    void addImage(ResourceLocation location, int width, int height, AlignmentMode align, FloatMode floating);

    void addItemStack(ItemStack itemStack, int width, int height);

    void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align);

    void addItemStack(ItemStack itemStack, int width, int height, FloatMode floating);

    void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align, FloatMode floating);

    void addCompilerHint(CompilerHint hint);

    void pageBreak();

    void floatBreak();

    GuiScreen createBookGui(FontboxManager manager, IBookProperties bookProperties) throws IOException, LayoutException;

    IBook createBook(FontboxManager manager, IBookProperties bookProperties) throws IOException, LayoutException;
}
