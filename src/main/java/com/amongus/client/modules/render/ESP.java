package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
public class ESP extends Module {
    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER, "Shows players through walls.");
        addSetting(new Setting("Mode", new String[]{"None","Glow","Box","Outline","Box+Glow"}, "Glow"));
        addSetting(new Setting("Red", 0, 255, 255, 1));
        addSetting(new Setting("Green", 0, 255, 50, 1));
        addSetting(new Setting("Blue", 0, 255, 50, 1));
        addSetting(new Setting("ThroughWalls", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Players", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Mobs", new String[]{"Off","On"}, "Off"));
    }
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        double rx = mc.getRenderManager().viewerPosX;
        double ry = mc.getRenderManager().viewerPosY;
        double rz = mc.getRenderManager().viewerPosZ;
        Color c = new Color((int)getSetting("Red").getDoubleValue(), (int)getSetting("Green").getDoubleValue(), (int)getSetting("Blue").getDoubleValue(), 120);
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - rx;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - ry;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - rz;
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            if (getSetting("ThroughWalls").getValue().equals("On")) GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            if (mode.contains("Glow")) {
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glColor4f(c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F, c.getAlpha()/255F);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex3d(-0.3, 0, -0.3); GL11.glVertex3d(0.3, 0, -0.3); GL11.glVertex3d(0.3, 1.8, -0.3); GL11.glVertex3d(-0.3, 1.8, -0.3);
                GL11.glVertex3d(-0.3, 0, 0.3); GL11.glVertex3d(0.3, 0, 0.3); GL11.glVertex3d(0.3, 1.8, 0.3); GL11.glVertex3d(-0.3, 1.8, 0.3);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
            }
            if (mode.contains("Box") || mode.contains("Outline")) {
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glColor4f(c.getRed()/255F, c.getGreen()/255F, c.getBlue()/255F, 0.78F);
                GL11.glLineWidth(2.0F);
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(-0.3, 0, -0.3); GL11.glVertex3d(0.3, 0, -0.3); GL11.glVertex3d(0.3, 0, 0.3); GL11.glVertex3d(-0.3, 0, 0.3);
                GL11.glVertex3d(-0.3, 0, -0.3); GL11.glVertex3d(-0.3, 1.8, -0.3); GL11.glVertex3d(0.3, 1.8, -0.3); GL11.glVertex3d(0.3, 1.8, 0.3);
                GL11.glVertex3d(-0.3, 1.8, 0.3); GL11.glVertex3d(-0.3, 1.8, -0.3);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
    }
}