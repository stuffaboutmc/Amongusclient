package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class CriticalsPlus extends Module {
    public CriticalsPlus() {
        super("CriticalsPlus", Keyboard.KEY_NONE, Category.COMBAT, "Enhanced criticals.");
        addSetting(new Setting("Mode", new String[]{"None","Jump","Packet","Both"}, "Both"));
        addSetting(new Setting("Chance", 0, 100, 100, 1));
    }
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (Math.random() * 100 > getSetting("Chance").getDoubleValue()) return;
        if (mode.equals("Jump") || mode.equals("Both")) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump();
        }
        if (mode.equals("Packet") || mode.equals("Both")) {
            mc.thePlayer.onGround = false;
        }
    }
}