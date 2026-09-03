package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Clock extends Module {
    public Clock() {
        super("Clock", Keyboard.KEY_NONE, Category.RENDER, "Shows time.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        mc.fontRendererObj.drawStringWithShadow(time, 5, 40, new Color(255, 255, 255).getRGB());
    }
}