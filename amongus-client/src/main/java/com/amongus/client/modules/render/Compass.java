package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class Compass extends Module {
    public Compass() {
        super("Compass", Keyboard.KEY_NONE, Category.RENDER, "Shows direction.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer == null) return;
        float yaw = mc.thePlayer.rotationYaw % 360;
        if (yaw < 0) yaw += 360;
        String dir = "S";
        if (yaw > 315 || yaw <= 45) dir = "S";
        else if (yaw > 45 && yaw <= 135) dir = "W";
        else if (yaw > 135 && yaw <= 225) dir = "N";
        else if (yaw > 225 && yaw <= 315) dir = "E";
        mc.fontRendererObj.drawStringWithShadow(dir + " (" + (int)yaw + ")", 5, 30, new Color(255, 255, 255).getRGB());
    }
}