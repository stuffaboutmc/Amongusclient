package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class PlayerESP extends Module {
    public PlayerESP() {
        super("PlayerESP", Keyboard.KEY_NONE, Category.RENDER, "Highlights players only.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glColor4f(1, 0, 0, 0.6F);
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || player.isDead) continue;
            double x = player.posX - mc.getRenderManager().viewerPosX;
            double y = player.posY - mc.getRenderManager().viewerPosY;
            double z = player.posZ - mc.getRenderManager().viewerPosZ;
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x-0.3, y, z-0.3); GL11.glVertex3d(x+0.3, y, z-0.3); GL11.glVertex3d(x+0.3, y, z+0.3); GL11.glVertex3d(x-0.3, y, z+0.3); GL11.glVertex3d(x-0.3, y, z-0.3);
            GL11.glVertex3d(x-0.3, y+1.8, z-0.3); GL11.glVertex3d(x+0.3, y+1.8, z-0.3); GL11.glVertex3d(x+0.3, y+1.8, z+0.3); GL11.glVertex3d(x-0.3, y+1.8, z+0.3); GL11.glVertex3d(x-0.3, y+1.8, z-0.3);
            GL11.glEnd();
        }
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}