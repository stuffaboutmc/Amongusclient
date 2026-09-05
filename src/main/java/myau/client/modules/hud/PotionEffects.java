package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

import java.util.Collection;

public class PotionEffects extends Module {
    public PotionEffects() {
        super("PotionEffects", "Shows active potion effects", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        int y = 46;
        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = potion.getName();
            int amplifier = effect.getAmplifier();
            int duration = effect.getDuration();

            String durationStr = "";
            int seconds = duration / 20;
            if (seconds >= 60) {
                durationStr = (seconds / 60) + "m " + (seconds % 60) + "s";
            } else {
                durationStr = seconds + "s";
            }

            String text = name + " " + roman(amplifier) + " " + durationStr;
            int color = potion.getLiquidColor();
            mc.fontRendererObj.drawStringWithShadow(text, 4, y, color);
            y += 10;
        }
    }

    private String roman(int level) {
        switch (level) {
            case 0: return "";
            case 1: return "II";
            case 2: return "III";
            case 3: return "IV";
            case 4: return "V";
            default: return String.valueOf(level + 1);
        }
    }
}
