package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Color;

public class ESP extends Module {
    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER, "Shows players through walls.");
        addSetting(new Setting("Mode", new String[]{"None", "Glow", "Box", "Outline", "Box+Glow"}, "Glow"));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;
            if ((entity instanceof EntityPlayer)) continue;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - RenderManager.renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - RenderManager.renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - RenderManager.renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            if (mode.contains("Glow")) drawGlow(x, y, z, entity.width, entity.height, new Color(255, 50, 50, 120));
            if (mode.contains("Box")) drawBox(x, y, z, entity.width, entity.height, new Color(255, 50, 50, 200));
            if (mode.contains("Outline")) drawBox(x, y, z, entity.width, entity.height, new Color(255, 50, 50, 255));
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
    }

    private void drawGlow(double x, double y, double z, float w, float h, Color c) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F, c.getAlpha()/255F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        GL11.glVertex3d(-w/2, h, -w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    private void drawBox(double x, double y, double z, float w, float h, Color c) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor4f(c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F, c.getAlpha()/255F);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(-w/2, h, -w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, -w/2);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
