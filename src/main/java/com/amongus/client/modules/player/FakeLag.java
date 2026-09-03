package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.LinkedList;
import java.util.Queue;
public class FakeLag extends Module {
    private Queue<net.minecraft.network.Packet> packetQueue = new LinkedList<>();
    private long lastFlush = 0;
    public FakeLag() {
        super("FakeLag", Keyboard.KEY_NONE, Category.PLAYER, "Simulates lag.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 100, 2000, 500, 50));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastFlush >= (int) getSetting("Delay").getDoubleValue()) {
            while (!packetQueue.isEmpty()) {
                mc.getNetHandler().addToSendQueue(packetQueue.poll());
            }
            lastFlush = System.currentTimeMillis();
        }
    }
}