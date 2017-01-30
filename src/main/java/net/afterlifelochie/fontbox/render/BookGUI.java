package net.afterlifelochie.fontbox.render;

import net.afterlifelochie.fontbox.api.data.IBookProperties;
import net.afterlifelochie.fontbox.api.formatting.PageMode;
import net.afterlifelochie.fontbox.api.formatting.layout.Layout;
import net.afterlifelochie.fontbox.api.layout.IElement;
import net.afterlifelochie.fontbox.api.layout.IPage;
import net.afterlifelochie.fontbox.api.layout.IPageIndex;
import net.afterlifelochie.fontbox.api.tracer.ITracer;
import net.afterlifelochie.fontbox.layout.DocumentProcessor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Tuple;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookGUI extends GuiScreen {
    /**
     * The renderer's PageMode
     */
    private final PageMode mode;

    /**
     * The list of pages
     */
    private List<? extends IPage> pages;
    /**
     * The data index
     */
    private IPageIndex index;
    /**
     * The current page pointer
     */
    private int ptr = 0;

    /**
     * The current opengl display list state
     */
    private boolean useDisplayList = false;
    /**
     * The current opengl buffer list
     */
    private int[] glDisplayLists;
    /**
     * The current buffer dirty state
     */
    private boolean glBufferDirty[];
    /**
     * The underlying bookProperties properties
     */
    private final IBookProperties bookProperties;

    /**
     * <p>
     * Create a new Book rendering context on top of the existing Minecraft GUI
     * system. The bookProperties rendering properties are set through the constructor and
     * control how many and where pages are rendered.
     * </p>
     *
     * @param bookProperties The underlying {@link IBookProperties}
     */
    public BookGUI(IBookProperties bookProperties, ITracer tracer) {
        if (bookProperties == null)
            throw new IllegalArgumentException("IBookProperties cannot be null!");
        if (bookProperties.getPageMode() == null)
            throw new IllegalArgumentException("Mode cannot be null!");
        if (bookProperties.getPageMode().layouts == null)
            throw new IllegalArgumentException("Layout cannot be null!");
        this.bookProperties = bookProperties;
        this.mode = bookProperties.getPageMode();
        prepareGraphics(tracer);
    }

    /**
     * <p>
     * Updates the pages currently being rendered.
     * </p>
     * <p>
     * If the page read pointer is currently beyond the end of the new page
     * count (ie, the number of pages has reduced), the pointer will be reset to
     * the beginning of the bookProperties.
     * </p>
     *
     * @param pages The new list of pages
     * @param index The new page index
     */
    public void changePages(List<? extends IPage> pages, IPageIndex index) {
        if (ptr >= pages.size()) {
            ptr = 0;
            internalOnPageChanged(this, ptr);
        }
        this.pages = pages;
        this.index = index;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        if (useDisplayList) {
            useDisplayList = false;
            GlStateManager.glDeleteLists(glDisplayLists[0], glDisplayLists.length);
            for (int i = 0; i < glDisplayLists.length; i++)
                glDisplayLists[i] = -1;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mx, int my, float frames) {
        super.drawScreen(mx, my, frames);
        drawBackground(mx, my, frames);
        try {
            if (pages != null) {
                List<Tuple<Layout, IPage>> toRender = new ArrayList<>(2);
                int i;
                for (i = 0; i < mode.pages; i++) {
                    int what = ptr + i;
                    if (pages.size() <= what)
                        break;
                    toRender.add(new Tuple<>(mode.layouts[i], pages.get(ptr + i)));
                }
                i = 0;
                for (Tuple<Layout, IPage> page : toRender)
                    if (useDisplayList)
                        renderPageStaticsBuffered(i++, page.getSecond(), page.getFirst().x, page.getFirst().y, zLevel, mx, my, frames);
                    else
                        renderPageStaticsImmediate(page.getSecond(), page.getFirst().x, page.getFirst().y, zLevel, mx, my, frames);
                for (Tuple<Layout, IPage> page : toRender)
                    renderPageDynamics(page.getSecond(), page.getFirst().x, page.getFirst().y, zLevel, mx, my, frames);
            }
        } catch (RenderException err) {
            err.printStackTrace();
        }
        drawForeground(mx, my, frames);
    }

    /**
     * <p>
     * Draw the background layer of the interface. You must leave the opengl
     * state such that the layout (0, 0) will be drawn in the current place.
     * </p>
     *
     * @param mx    The mouse x-coordinate
     * @param my    The mouse y-coordinate
     * @param frame The partial frames rendered
     */
    public void drawBackground(int mx, int my, float frame) {
        bookProperties.drawBackground(width, height, mx, my, frame, zLevel);
    }

    /**
     * <p>
     * Draw the foreground layer of the interface. The opengl state is such that
     * the layout coordinates (0, 0) are in the top-left corner of the written
     * text.
     * </p>
     *
     * @param mx    The mouse x-coordinate
     * @param my    The mouse y-coordinate
     * @param frame The partial frames rendered
     */
    public void drawForeground(int mx, int my, float frame) {
        bookProperties.drawForeground(width, height, mx, my, frame, zLevel);
    }

    /**
     * Called internally when the page is changed.
     *
     * @param gui     The bookProperties GUI
     * @param whatPtr The new page pointer
     */
    private void internalOnPageChanged(BookGUI gui, int whatPtr) {
        for (int i = 0; i < mode.pages; i++)
            glBufferDirty[i] = true;
        bookProperties.onPageChanged(gui, whatPtr);
    }

    /**
     * Called internally to set up the display lists.
     */
    protected void prepareGraphics(ITracer tracer) {
        try {
            Util.checkGLError();
        } catch (OpenGLException glex) {
            tracer.warn("BookGUI.prepareGraphics", "Bad OpenGL operation detected, check GL history!");
            glex.printStackTrace();
            return;
        }
        glDisplayLists = new int[mode.pages];
        glBufferDirty = new boolean[mode.pages];
        int glList = GlStateManager.glGenLists(glDisplayLists.length);

        try {
            Util.checkGLError();
        } catch (OpenGLException glex) {
            tracer.warn("BookGUI.prepareGraphics",
                "Unable to allocate display-list buffers, using immediate mode.");
            return;
        }

        if (glList <= 0)
            tracer.warn("BookGUI.prepareGraphics", "No display-lists available, using immediate mode.");
        else {
            for (int i = 0; i < glDisplayLists.length; i++) {
                glDisplayLists[i] = glList + i;
                glBufferDirty[i] = true;
            }
            tracer.trace("BookGUI.prepareGraphics", "Displaylist initialized.", glList, glDisplayLists.length);
            useDisplayList = true;
        }
    }

    /**
     * Advance to the next page
     */
    protected void next() {
        if (ptr + mode.pages < pages.size()) {
            ptr += mode.pages;
            internalOnPageChanged(this, ptr);
        }
    }

    /**
     * Go to a page in the index; if the item doesn't exist, no navigation
     * occurs
     *
     * @param id The ID of the anchor to go to
     */
    protected void go(String id) {
        int where = index.find(id);
        if (where != -1)
            go(where);
    }

    /**
     * Go to a page
     *
     * @param where The page pointer
     */
    protected void go(int where) {
        where = where - (where % mode.pages);
        if (ptr != where && 0 <= where - mode.pages && where + mode.pages < pages.size()) {
            ptr = where;
            internalOnPageChanged(this, ptr);
        }
    }

    /**
     * Reverse to the previous page
     */
    protected void previous() {
        if (0 <= ptr - mode.pages) {
            ptr -= mode.pages;
            internalOnPageChanged(this, ptr);
        }
    }

    @Override
    protected void keyTyped(char val, int code) throws IOException {
        super.keyTyped(val, code);
        if (code == Keyboard.KEY_LEFT)
            previous();
        if (code == Keyboard.KEY_RIGHT)
            next();
    }

    @Override
    protected void mouseClicked(int mx, int my, int button) throws IOException {
        super.mouseClicked(mx, my, button);
        for (int i = 0; i < mode.pages; i++) {
            Layout where = mode.layouts[i];
            int which = ptr + i;
            if (pages.size() <= which)
                break;
            IPage page = pages.get(ptr + i);
            int mouseX = mx - where.x, mouseY = my - where.y;
            if (mouseX >= 0 && mouseY >= 0 && mouseX <= page.getWidth() && mouseY <= page.getHeight()) {
                IElement elem = DocumentProcessor.getElementAt(page, mouseX, mouseY);
                if (elem != null)
                    elem.clicked(this, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mx, int my, int button, long ticks) {
        super.mouseClickMove(mx, my, button, ticks);
    }

    private void renderPageDynamics(IPage page, float x, float y, float z, int mx, int my, float frame) throws RenderException {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderElementGroupImmediate(page.dynamicElements(), mx, my, frame);
        GlStateManager.popMatrix();
    }

    private void renderPageStaticsImmediate(IPage page, float x, float y, float z, int mx, int my, float frame) throws RenderException {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderElementGroupImmediate(page.staticElements(), mx, my, frame);
        GlStateManager.popMatrix();
    }

    private void renderPageStaticsBuffered(int index, IPage page, float x, float y, float z, int mx, int my, float frame) throws RenderException {
        if (glBufferDirty[index]) {
            GlStateManager.glNewList(glDisplayLists[index], GL11.GL_COMPILE);
            renderPageStaticsImmediate(page, x, y, z, mx, my, frame);
            GlStateManager.glEndList();
            glBufferDirty[index] = false;
        }
        GlStateManager.callList(glDisplayLists[index]);
    }

    private void renderElementGroupImmediate(Iterable<? extends IElement> elements, int mx, int my, float frame) throws RenderException {
        for (IElement element : elements)
            element.render(this, mx, my, frame);
    }
}
