package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class PotionHUD extends Module {
    public PotionHUD() {
        super("PotionHUD", Keyboard.KEY_NONE, Category.RENDER, "Shows active potions.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer == null) return;
        int y = 80;
        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
            String name = effect.getEffectName();
            String duration = String.valueOf(effect.getDuration() / 20);
            mc.fontRendererObj.drawStringWithShadow(name + " " + duration + "s", 5, y, new Color(200, 200, 255).getRGB());
            y += 10;
        }
    }
}