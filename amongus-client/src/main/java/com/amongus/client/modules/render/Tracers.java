package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Keyboard.KEY_NONE, Category.RENDER, "Draws lines to players.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Red", 0, 255, 255, 1));
        addSetting(new Setting("Green", 0, 255, 50, 1));
        addSetting(new Setting("Blue", 0, 255, 50, 1));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        Color c = new Color((int)getSetting("Red").getDoubleValue(), (int)getSetting("Green").getDoubleValue(), (int)getSetting("Blue").getDoubleValue(), 200);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead) continue;
            GL11.glColor4f(c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F, c.getAlpha()/255F);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3d(0, 0, 0);
            GL11.glVertex3d(entity.posX - mc.getRenderManager().viewerPosX, entity.posY - mc.getRenderManager().viewerPosY, entity.posZ - mc.getRenderManager().viewerPosZ);
            GL11.glEnd();
        }
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}