package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;
public class FullBright extends Module {
    private float oldGamma;
    public FullBright() {
        super("FullBright", Keyboard.KEY_NONE, Category.RENDER, "Makes everything bright.");
        addSetting(new Setting("Mode", new String[]{"None","Gamma","Potion","Both"}, "Both"));
    }
    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("Gamma") || mode.equals("Both")) mc.gameSettings.gammaSetting = 100.0F;
        if (mode.equals("Potion") || mode.equals("Both")) mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), Integer.MAX_VALUE, 1));
    }
    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
    }
}