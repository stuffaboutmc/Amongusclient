package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class BowAimBot extends Module {
    public BowAimBot() {
        super("BowAimBot", Keyboard.KEY_NONE, Category.COMBAT, "Aims your bow at the nearest target.");
        addSetting(new Setting("Range", 10, 100, 50, 5));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Prediction", new String[]{"Off","On"}, "On"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) return;
        if (!mc.thePlayer.isUsingItem()) return;
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");
        EntityLivingBase target = RotationUtils.raycastEntity(getSetting("Range").getDoubleValue());
        if (target != null) {
            float[] rot = RotationUtils.getRotations(target);
            RotationUtils.applyRotations(rot[0], rot[1]);
        }
    }
}