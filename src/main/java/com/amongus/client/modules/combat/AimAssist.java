package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AimAssist extends Module {
    public AimAssist() {
        super("AimAssist", Keyboard.KEY_NONE, Category.COMBAT, "Gently aims at nearby targets.");
        addSetting(new Setting("Range", 3, 10, 5, 0.5));
        addSetting(new Setting("Strength", 1, 100, 40, 1));
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        EntityLivingBase target = RotationUtils.raycastEntity(getSetting("Range").getDoubleValue());
        if (target == null) return;
        float strength = (float) getSetting("Strength").getDoubleValue() / 100.0F;
        float[] rot = RotationUtils.getRotations(target);
        mc.thePlayer.rotationYaw += (rot[0] - mc.thePlayer.rotationYaw) * strength;
        mc.thePlayer.rotationPitch += (rot[1] - mc.thePlayer.rotationPitch) * strength;
    }
}
