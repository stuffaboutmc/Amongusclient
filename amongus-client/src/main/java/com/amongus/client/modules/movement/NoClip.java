package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoClip extends Module {
    public NoClip() {
        super("NoClip", Keyboard.KEY_NONE, Category.MOVEMENT, "Fly through blocks.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @Override
    public void onEnable() { mc.thePlayer.noClip = true; }
    @Override
    public void onDisable() { mc.thePlayer.noClip = false; }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.thePlayer.noClip = true;
    }
}