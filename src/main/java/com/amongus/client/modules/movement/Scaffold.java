package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Scaffold extends Module {
    private int blocksSinceJump = 0;
    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT, "Places blocks under you.");
        addSetting(new Setting("Mode", new String[]{"None","Telly","Normal","Godbridge","Snap"}, "Normal"));
        addSetting(new Setting("Rotation", new String[]{"Legit","Custom","Snap","Smooth","Instant"}, "Legit"));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("MoveFix", 0, 100, 100, 1));
        addSetting(new Setting("Tower", new String[]{"Off","On"}, "Off"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        RotationUtils.rotationMode = getSetting("Rotation").getValue();
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");
        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
            if (mode.equals("Godbridge")) {
                blocksSinceJump++;
                if (blocksSinceJump >= 9) { mc.thePlayer.jump(); blocksSinceJump = 0; return; }
            }
            if (getSetting("Tower").getValue().equals("On")) {
                mc.thePlayer.motionY = 0.42;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            }
            float[] rot = RotationUtils.getRotations(new Vec3(below.getX() + 0.5, below.getY() + 0.5, below.getZ() + 0.5));
            RotationUtils.applyRotations(rot[0], rot[1]);
            ItemStack held = mc.thePlayer.getHeldItem();
            if (held != null && held.getItem() instanceof ItemBlock) {
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, held, below, EnumFacing.UP, new Vec3(below.getX() + 0.5, below.getY() + 0.5, below.getZ() + 0.5));
                mc.thePlayer.swingItem();
            }
        }
        if (getSetting("MoveFix").getDoubleValue() > 50) { mc.thePlayer.motionX *= 0.95; mc.thePlayer.motionZ *= 0.95; }
    }
}