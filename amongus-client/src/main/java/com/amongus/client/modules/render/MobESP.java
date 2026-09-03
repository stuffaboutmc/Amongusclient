package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class MobESP extends Module {
    public MobESP() {
        super("MobESP", Keyboard.KEY_NONE, Category.RENDER, "Highlights mobs.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glColor4f(1, 0.5F, 0, 0.6F);
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityMob) {
                EntityMob mob = (EntityMob) obj;
                double x = mob.posX - mc.getRenderManager().viewerPosX;
                double y = mob.posY - mc.getRenderManager().viewerPosY;
                double z = mob.posZ - mc.getRenderManager().viewerPosZ;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(x-0.3, y, z-0.3); GL11.glVertex3d(x+0.3, y, z-0.3); GL11.glVertex3d(x+0.3, y, z+0.3); GL11.glVertex3d(x-0.3, y, z+0.3); GL11.glVertex3d(x-0.3, y, z-0.3);
                GL11.glEnd();
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}