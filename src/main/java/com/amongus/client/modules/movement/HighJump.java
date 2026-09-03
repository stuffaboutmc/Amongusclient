package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class HighJump extends Module {
    public HighJump() {
        super("HighJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Jump higher.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Height", 1, 10, 3, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround) {
            mc.thePlayer.motionY = getSetting("Height").getDoubleValue() * 0.42;
        }
    }
}