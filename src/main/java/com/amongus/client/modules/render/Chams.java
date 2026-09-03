package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
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
        GL11.glColor4f((float)getSetting("Red").getDoubleValue()/255F, (float)getSetting("Green").getDoubleValue()/255F, (float)getSetting("Blue").getDoubleValue()/255F, 0.5F);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }
}
