package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT, "Always crits.");
        addSetting(new Setting("Mode", new String[]{"None","Packet","MiniJump","Jump","Both"}, "Packet"));
    }
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mode.equals("Packet")) { mc.thePlayer.onGround = false; }
        else if (mode.equals("MiniJump")) { if (mc.thePlayer.onGround) { mc.thePlayer.motionY = 0.1; mc.thePlayer.onGround = false; } }
        else if (mode.equals("Jump")) { if (mc.thePlayer.onGround) mc.thePlayer.jump(); }
        else if (mode.equals("Both")) { if (mc.thePlayer.onGround) { mc.thePlayer.jump(); mc.thePlayer.onGround = false; } }
    }
}
