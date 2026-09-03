package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Nametags extends Module {
    public Nametags() {
        super("Nametags", Keyboard.KEY_NONE, Category.RENDER, "Shows nametags through walls.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Scale", 1, 5, 2, 0.5));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead) continue;
            GlStateManager.pushMatrix();
            GlStateManager.translate(entity.posX - mc.getRenderManager().viewerPosX, entity.posY + entity.height + 0.5 - mc.getRenderManager().viewerPosY, entity.posZ - mc.getRenderManager().viewerPosZ);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
            float scale = (float) getSetting("Scale").getDoubleValue() * 0.01F;
            GlStateManager.scale(-scale, -scale, scale);
            String name = entity.getName();
            int width = mc.fontRendererObj.getStringWidth(name) / 2;
            mc.fontRendererObj.drawStringWithShadow(name, -width, 0, -1);
            GlStateManager.popMatrix();
        }
    }
}