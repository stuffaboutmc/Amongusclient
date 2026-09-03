package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.HashSet;
import java.util.Set;
public class AntiBot extends Module {
    private Set<EntityPlayer> bots = new HashSet<>();
    public AntiBot() {
        super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT, "Ignores fake players.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("CheckHealth", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("CheckName", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckPing", new String[]{"Off","On"}, "Off"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            if (mode.equals("Basic")) { if (player.getUniqueID().version() == 0) bots.add(player); }
            else if (mode.equals("Advanced")) {
                if (getSetting("CheckHealth").getValue().equals("On") && (player.getHealth() > 40 || player.getMaxHealth() > 40)) bots.add(player);
                if (getSetting("CheckName").getValue().equals("On") && player.getName().contains("§")) bots.add(player);
            }
        }
    }
    public boolean isBot(EntityPlayer player) { return bots.contains(player); }
}