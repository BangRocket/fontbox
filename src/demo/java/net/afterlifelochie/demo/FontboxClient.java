package net.afterlifelochie.demo;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.FontException;
import net.afterlifelochie.fontbox.api.font.GLFont;
import net.afterlifelochie.fontbox.api.tracer.PrintOutputTracer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FontboxClient extends FontboxServer
{
    private static FontboxManager manager;

    @Override
    public void preInit(FMLPreInitializationEvent e)
    {
        super.preInit(e);
        book.initModel();
    }

    @Override
    public void init(FMLInitializationEvent e)
    {
        super.init(e);
        try
        {
            manager = new FontboxManager();
            manager.setTracer(new PrintOutputTracer());
            GLFont.fromSpriteFont(manager, "Daniel", new ResourceLocation("fontbox", "fonts/daniel.png"), new ResourceLocation("fontbox", "fonts/daniel.metrics.xml"));
            GLFont.fromTTF(manager, 22.0f, new ResourceLocation("fontbox", "fonts/notethis.ttf"));
            GLFont.fromTTF(manager, 22.0f, new ResourceLocation("fontbox", "fonts/ampersand.ttf"));
        } catch (FontException f0)
        {
            f0.printStackTrace();
        }
    }

    public static FontboxManager getManager()
    {
        return manager;
    }
}
