package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP", Keyboard.KEY_NONE, Category.RENDER, "Highlights dropped items.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glColor4f(0, 1, 1, 0.7F);
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityItem) {
                EntityItem item = (EntityItem) obj;
                double x = item.posX - mc.getRenderManager().viewerPosX;
                double y = item.posY - mc.getRenderManager().viewerPosY;
                double z = item.posZ - mc.getRenderManager().viewerPosZ;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(x-0.2, y, z); GL11.glVertex3d(x+0.2, y, z); GL11.glVertex3d(x+0.2, y+0.4, z); GL11.glVertex3d(x-0.2, y+0.4, z); GL11.glVertex3d(x-0.2, y, z);
                GL11.glEnd();
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}