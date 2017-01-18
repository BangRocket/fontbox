package net.afterlifelochie.demo;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.FontException;
import net.afterlifelochie.fontbox.api.font.GLFontBuilder;
import net.afterlifelochie.fontbox.api.font.IGLFontBuilder;
import net.afterlifelochie.fontbox.api.tracer.PrintOutputTracer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FontboxClient extends FontboxServer {
    @GLFontBuilder
    public static IGLFontBuilder fontBuilder;
    private static FontboxManager manager;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        book.initModel();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        try {
            manager = new FontboxManager();
            manager.setTracer(new PrintOutputTracer());
            fontBuilder.fromSpriteFont(manager, "Daniel", new ResourceLocation("fontbox", "fonts/daniel.png"), new ResourceLocation("fontbox", "fonts/daniel.metrics.xml"));
            fontBuilder.fromTTF(manager, 22.0f, new ResourceLocation("fontbox", "fonts/notethis.ttf"));
            fontBuilder.fromTTF(manager, 22.0f, new ResourceLocation("fontbox", "fonts/ampersand.ttf"));
        } catch (FontException f0) {
            f0.printStackTrace();
        }
    }

    public static FontboxManager getManager() {
        return manager;
    }
}
