package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.block.BlockCrops;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
public class AutoFarm extends Module {
    public AutoFarm() {
        super("AutoFarm", Keyboard.KEY_NONE, Category.PLAYER, "Harvests crops automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockCrops) {
                mc.playerController.clickBlock(pos, mc.objectMouseOver.sideHit);
            }
        }
    }
}