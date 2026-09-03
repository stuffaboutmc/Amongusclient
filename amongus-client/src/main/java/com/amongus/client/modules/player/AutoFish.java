package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemFishingRod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoFish extends Module {
    private long lastCast = 0;
    public AutoFish() {
        super("AutoFish", Keyboard.KEY_NONE, Category.PLAYER, "Fishes automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
            if (mc.thePlayer.fishEntity == null && System.currentTimeMillis() - lastCast > 2000) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                lastCast = System.currentTimeMillis();
            }
            if (mc.thePlayer.fishEntity != null && mc.thePlayer.fishEntity.motionY < -0.5) {
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            }
        }
    }
}