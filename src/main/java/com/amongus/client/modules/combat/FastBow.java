package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemBow;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class FastBow extends Module {
    public FastBow() {
        super("FastBow", Keyboard.KEY_NONE, Category.COMBAT, "Shoots bow instantly.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("ChargeTicks", 1, 20, 20, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            if (mc.thePlayer.isUsingItem()) {
                mc.thePlayer.itemInUseCount = (int) getSetting("ChargeTicks").getDoubleValue();
            }
        }
    }
}