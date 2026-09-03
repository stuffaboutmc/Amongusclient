package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
public class Backtrack extends Module {
    private Map<EntityPlayer, LinkedList<Vec3>> positionHistory = new HashMap<>();
    public Backtrack() {
        super("Backtrack", Keyboard.KEY_NONE, Category.COMBAT, "Tracks player positions. Hit them where they were.");
        addSetting(new Setting("Latency", 50, 500, 150, 10));
        addSetting(new Setting("Mode", new String[]{"None","Simple","Advanced"}, "Simple"));
        addSetting(new Setting("MaxTargets", 1, 20, 5, 1));
        addSetting(new Setting("OnlyPlayers", new String[]{"Off","On"}, "On"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity == mc.thePlayer) return;
        if (mc.theWorld == null) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            if (!positionHistory.containsKey(player)) positionHistory.put(player, new LinkedList<>());
            LinkedList<Vec3> history = positionHistory.get(player);
            history.add(new Vec3(player.posX, player.posY, player.posZ));
            int maxSize = Math.max(1, (int) getSetting("Latency").getDoubleValue() / 50);
            while (history.size() > maxSize) history.pollFirst();
        }
    }
    public Vec3 getBacktrackedPosition(EntityPlayer player) {
        LinkedList<Vec3> history = positionHistory.get(player);
        if (history == null || history.isEmpty()) return null;
        return history.getFirst();
    }
}