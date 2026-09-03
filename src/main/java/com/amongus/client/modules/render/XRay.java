package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class XRay extends Module {
    private float oldGamma;
    private boolean oldAmbientOcclusion;
    private int oldFog;
    private float oldFogDensity;

    public XRay() {
        super("XRay", Keyboard.KEY_NONE, Category.RENDER, "Highlights ores with extreme brightness and no fog.");
        addSetting(new Setting("Mode", new String[]{"None","Ores","Bright","CaveFinder"}, "Ores"));
        addSetting(new Setting("Gamma", 1, 100, 100, 1));
        addSetting(new Setting("DisableFog", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("AmbientOcclusion", new String[]{"Off","On"}, "Off"));
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        oldAmbientOcclusion = mc.gameSettings.ambientOcclusion != 0;
        oldFog = mc.gameSettings.renderDistanceChunks;
        oldFogDensity = mc.gameSettings.fogDensity;

        applySettings();
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        mc.gameSettings.ambientOcclusion = oldAmbientOcclusion ? 1 : 0;
        mc.gameSettings.renderDistanceChunks = oldFog;
        mc.gameSettings.fogDensity = oldFogDensity;
        mc.renderGlobal.loadRenderers();
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        applySettings();
    }

    private void applySettings() {
        mc.gameSettings.gammaSetting = (float) getSetting("Gamma").getDoubleValue();
        if (getSetting("DisableFog").getValue().equals("On")) {
            mc.gameSettings.renderDistanceChunks = 32;
            mc.gameSettings.fogDensity = 0.0F;
        }
        mc.gameSettings.ambientOcclusion = getSetting("AmbientOcclusion").getValue().equals("On") ? 0 : 1;
    }
}
