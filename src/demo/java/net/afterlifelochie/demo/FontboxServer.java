package net.afterlifelochie.demo;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FontboxServer {
    public ItemDemoBook book;

    public void preInit(FMLPreInitializationEvent e) {
        book = new ItemDemoBook();
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

}
