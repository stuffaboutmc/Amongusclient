package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
public class AutoClicker extends Module {
    private long lastClick = 0;
    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT, "Clicks automatically.");
        addSetting(new Setting("CPS", 1, 20, 10, 1));
        addSetting(new Setting("Mode", new String[]{"None","Hold","Always","Toggle"}, "Hold"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        int cps = (int) getSetting("CPS").getDoubleValue();
        long delay = Math.max(50, 1000 / cps);
        boolean shouldClick = mode.equals("Always") || mode.equals("Toggle") || (mode.equals("Hold") && Mouse.isButtonDown(0));
        if (shouldClick && System.currentTimeMillis() - lastClick >= delay) {
            mc.thePlayer.swingItem();
            if (mc.objectMouseOver != null) mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
            lastClick = System.currentTimeMillis();
        }
    }
}
