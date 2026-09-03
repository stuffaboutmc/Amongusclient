package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class SilentAim extends Module {
    public SilentAim() {
        super("SilentAim", Keyboard.KEY_NONE, Category.COMBAT, "Aims without moving camera.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Range", 3, 10, 6, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        RotationUtils.silentRotations = true;
        EntityLivingBase target = RotationUtils.raycastEntity(getSetting("Range").getDoubleValue());
        if (target != null) {
            float[] rot = RotationUtils.getRotations(target);
            RotationUtils.applyRotations(rot[0], rot[1]);
        }
    }
}