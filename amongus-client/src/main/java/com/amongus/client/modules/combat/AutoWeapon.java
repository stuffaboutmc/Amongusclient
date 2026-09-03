package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoWeapon extends Module {
    public AutoWeapon() {
        super("AutoWeapon", Keyboard.KEY_NONE, Category.COMBAT, "Switches to best weapon.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSword) {
                mc.thePlayer.inventory.currentItem = i;
                break;
            }
        }
    }
}