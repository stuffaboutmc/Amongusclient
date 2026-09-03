package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
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
        addSetting(new Setting("Mode", new String[]{"None","Glow","Box","Outline","Box+Glow"}, "Glow"));
        addSetting(new Setting("Players", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Mobs", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Invisible", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("ThroughWalls", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Red", 0, 255, 255, 1));
        addSetting(new Setting("Green", 0, 255, 50, 1));
        addSetting(new Setting("Blue", 0, 255, 50, 1));
        addSetting(new Setting("Opacity", 0, 255, 120, 5));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;

        boolean drawPlayers = getSetting("Players").getValue().equals("On");
        boolean drawMobs = getSetting("Mobs").getValue().equals("On");
        boolean includeInvisible = getSetting("Invisible").getValue().equals("On");
        boolean throughWalls = getSetting("ThroughWalls").getValue().equals("On");

        double rx = mc.getRenderManager().viewerPosX;
        double ry = mc.getRenderManager().viewerPosY;
        double rz = mc.getRenderManager().viewerPosZ;

        int red = (int) getSetting("Red").getDoubleValue();
        int green = (int) getSetting("Green").getDoubleValue();
        int blue = (int) getSetting("Blue").getDoubleValue();
        int opacity = (int) getSetting("Opacity").getDoubleValue();
        Color c = new Color(red, green, blue, opacity);

        for (Object obj : mc.theWorld.loadedEntityList) {
            if (!(obj instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase) obj;
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;
            if (entity instanceof EntityPlayer && !drawPlayers) continue;
            if (!(entity instanceof EntityPlayer) && !drawMobs) continue;
            if (!includeInvisible && entity.isInvisible()) continue;

            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - rx;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - ry;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - rz;

            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            if (throughWalls) GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();

            if (mode.contains("Glow")) {
                drawGlow(x, y, z, entity.width, entity.height, c);
            }
            if (mode.contains("Box") || mode.contains("Outline")) {
                drawBox(x, y, z, entity.width, entity.height, c);
            }

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
        // Front
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        GL11.glVertex3d(-w/2, h, -w/2);
        // Back
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        // Left
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, -w/2);
        // Right
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        // Top
        GL11.glVertex3d(-w/2, h, -w/2);
        GL11.glVertex3d(w/2, h, -w/2);
        GL11.glVertex3d(w/2, h, w/2);
        GL11.glVertex3d(-w/2, h, w/2);
        // Bottom
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
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
        // Bottom
        GL11.glVertex3d(-w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, -w/2);
        GL11.glVertex3d(w/2, 0, w/2);
        GL11.glVertex3d(-w/2, 0, w/2);
        GL11.glVertex3d(-w/2, 0, -w/2);
        // Sides
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
