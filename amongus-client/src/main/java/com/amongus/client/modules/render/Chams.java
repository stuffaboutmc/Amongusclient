package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class Chams extends Module {
    public Chams() {
        super("Chams", Keyboard.KEY_NONE, Category.RENDER, "See players through walls with color.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Red", 0, 255, 255, 1));
        addSetting(new Setting("Green", 0, 255, 50, 1));
        addSetting(new Setting("Blue", 0, 255, 50, 1));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead) continue;
            GL11.glColor4f(getSetting("Red").getDoubleValue()/255F, getSetting("Green").getDoubleValue()/255F, getSetting("Blue").getDoubleValue()/255F, 0.5F);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }
}