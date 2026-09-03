package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class CustomFOV extends Module {
    private float oldFOV;
    public CustomFOV() {
        super("CustomFOV", Keyboard.KEY_NONE, Category.RENDER, "Custom field of view.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("FOV", 30, 180, 90, 5));
    }
    @Override
    public void onEnable() {
        oldFOV = mc.gameSettings.fovSetting;
        mc.gameSettings.fovSetting = (float) getSetting("FOV").getDoubleValue();
    }
    @Override
    public void onDisable() {
        mc.gameSettings.fovSetting = oldFOV;
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.gameSettings.fovSetting = (float) getSetting("FOV").getDoubleValue();
    }
}