package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
public class Blink extends Module {
    private List<double[]> positions = new ArrayList<>();
    public Blink() {
        super("Blink", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleport back to previous position.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("MaxDistance", 1, 100, 20, 5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ});
        if (mc.gameSettings.keyBindSneak.isKeyDown() && !positions.isEmpty()) {
            double[] pos = positions.get(0);
            mc.thePlayer.setPosition(pos[0], pos[1], pos[2]);
            positions.clear();
        }
    }
}