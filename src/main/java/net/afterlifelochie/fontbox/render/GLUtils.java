package net.afterlifelochie.fontbox.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GLUtils
{
    public static void useSystemTexture(ResourceLocation loc)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
    }

    public static void useFontboxTexture(String name)
    {
        GLUtils.useSystemTexture(new ResourceLocation("fontbox", "textures/gui/" + name + ".png"));
    }

    public static void drawDefaultRect(double x, double y, double w, double h, double z)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(x, y + h, z).endVertex();
        buffer.pos(x + w, y + h, z).endVertex();
        buffer.pos(x + w, y, z).endVertex();
        buffer.pos(x, y, z).endVertex();
        tessellator.draw();
    }

    public static void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us,
                                          double vs, double z)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + h, z).tex(u, v + vs).endVertex();
        buffer.pos(x + w, y + h, z).tex(u + us, v + vs).endVertex();
        buffer.pos(x + w, y, z).tex(u + us, v).endVertex();
        buffer.pos(x, y, z).tex(u, v).endVertex();
        tessellator.draw();
    }

}
