package net.afterlifelochie.fontbox;

import net.afterlifelochie.fontbox.api.DocumentBuilder;
import net.afterlifelochie.fontbox.api.IDocumentBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Field;
import java.util.Set;

@Mod(name = Fontbox.NAME, modid = Fontbox.ID)
public class Fontbox
{
    public static final String NAME = "Fontbox";
    public static final String ID = "fontbox";

    private static final IDocumentBuilder builder = new net.afterlifelochie.fontbox.document.DocumentBuilder();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        event.getModLog().info("Providing DocumentBuilders...");
        String annotationClassName = DocumentBuilder.class.getCanonicalName();
        Set<ASMDataTable.ASMData> asmDataSet = event.getAsmData().getAll(annotationClassName);
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class clazz = Class.forName(asmData.getClassName());
                Field field = clazz.getField(asmData.getObjectName());
                if (field.getType() == IDocumentBuilder.class)
                    field.set(null, builder);
            } catch (ClassNotFoundException e)
            {
                event.getModLog().warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            } catch (NoSuchFieldException e)
            {
                event.getModLog().warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            } catch (IllegalAccessException e)
            {
                event.getModLog().warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            }
        }
    }
}