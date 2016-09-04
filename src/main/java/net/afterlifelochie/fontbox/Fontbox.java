package net.afterlifelochie.fontbox;

import net.afterlifelochie.fontbox.api.DocumentBuilder;
import net.afterlifelochie.fontbox.api.IDocumentBuilder;
import net.afterlifelochie.fontbox.api.font.GLFontBuilder;
import net.afterlifelochie.fontbox.api.font.IGLFontBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Set;

@Mod(name = Fontbox.NAME, modid = Fontbox.ID, version="@VERSION@")
public class Fontbox
{
    public static final String NAME = "Fontbox";
    public static final String ID = "fontbox";

    private static final IDocumentBuilder documentBuilder = new net.afterlifelochie.fontbox.document.DocumentBuilder();
    private static final IGLFontBuilder fontBuilder = new net.afterlifelochie.fontbox.font.GLFontBuilder();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Logger log = event.getModLog();
        log.info("Providing FontBuilders...");
        injectIntoFields(event.getModLog(), event.getAsmData(), GLFontBuilder.class, IGLFontBuilder.class, fontBuilder);
        log.info("Providing DocumentBuilders...");
        injectIntoFields(event.getModLog(), event.getAsmData(), DocumentBuilder.class, IDocumentBuilder.class, documentBuilder);
    }

    public static <T> void injectIntoFields(Logger log, ASMDataTable asmDataTable, Class annotation, Class<T> type, T instance) {
        String annotationClassName = annotation.getCanonicalName();
        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(annotationClassName);
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class clazz = Class.forName(asmData.getClassName());
                Field field = clazz.getField(asmData.getObjectName());
                if (field.getType() == type)
                    field.set(null, instance);
            } catch (ClassNotFoundException e)
            {
                log.warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            } catch (NoSuchFieldException e)
            {
                log.warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            } catch (IllegalAccessException e)
            {
                log.warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            }
        }
    }
}