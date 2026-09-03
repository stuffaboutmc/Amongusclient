package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class Projectiles extends Module {
    public Projectiles() {
        super("Projectiles", Keyboard.KEY_NONE, Category.RENDER, "Shows projectile paths.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GL11.glColor4f(1, 1, 1, 0.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double z = mc.thePlayer.posZ;
        double motionX = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 1.5;
        double motionY = -Math.sin(Math.toRadians(mc.thePlayer.rotationPitch)) * 1.5;
        double motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 1.5;
        for (int i = 0; i < 100; i++) {
            GL11.glVertex3d(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
            x += motionX; y += motionY; z += motionZ;
            motionY -= 0.05;
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}