package net.afterlifelochie.fontbox.document;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.data.FormattedString;
import net.afterlifelochie.fontbox.api.data.IBook;
import net.afterlifelochie.fontbox.api.data.IDocument;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.CompilerHint;
import net.afterlifelochie.fontbox.api.formatting.FloatMode;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.afterlifelochie.fontbox.layout.PageWriter;
import net.afterlifelochie.fontbox.render.BookGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class DocumentWrapper implements IDocument
{
    private Document document;

    public DocumentWrapper()
    {
        document = new Document();
    }

    @Override
    public void addHeading(String uid, FormattedString text)
    {
        document.push(new Heading(uid, text));
    }

    @Override
    public void addParagraph(FormattedString text)
    {
        document.push(new Paragraph(text));
    }

    @Override
    public void addParagraph(FormattedString text, AlignmentMode align)
    {
        document.push(new Paragraph(text, align));
    }

    @Override
    public void addImage(ResourceLocation location, int width, int height)
    {
        document.push(new Image(location, width, height));
    }

    @Override
    public void addImage(ResourceLocation location, int width, int height, AlignmentMode align)
    {
        document.push(new Image(location, width, height, align));
    }

    @Override
    public void addImage(ResourceLocation location, int width, int height, FloatMode floating)
    {
        document.push(new Image(location, width, height, floating));
    }

    @Override
    public void addImage(ResourceLocation location, int width, int height, AlignmentMode align, FloatMode floating)
    {
        document.push(new Image(location, width, height, align, floating));
    }

    @Override
    public void addItemStack(ItemStack itemStack, int width, int height)
    {
        document.push(new ImageItemStack(itemStack, width, height));
    }

    @Override
    public void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align)
    {
        document.push(new ImageItemStack(itemStack, width, height, align));
    }

    @Override
    public void addItemStack(ItemStack itemStack, int width, int height, FloatMode floating)
    {
        document.push(new ImageItemStack(itemStack, width, height, floating));
    }

    @Override
    public void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align, FloatMode floating)
    {
        document.push(new ImageItemStack(itemStack, width, height, align, floating));
    }

    @Override
    public void addCompilerHint(CompilerHint hint)
    {
        document.push(new CompilerHintElement(hint));
    }

    @Override
    public void pageBreak()
    {
        addCompilerHint(CompilerHint.PAGE_BREAK);
    }

    @Override
    public void floatBreak()
    {
        addCompilerHint(CompilerHint.FLOAT_BREAK);
    }

    @Override
    public GuiScreen createBookGui(FontboxManager manager, IBook book) throws IOException, LayoutException
    {
        PageWriter writer = new PageWriter(book.getPageProperties(), manager);
        DocumentProcessor.generatePages(manager.tracer(), document, writer);
        writer.close();
        BookGUI gui = new BookGUI(book, manager.tracer());
        gui.changePages(writer.pages(), writer.index());
        return gui;
    }
}
