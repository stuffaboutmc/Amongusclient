package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Keyboard.KEY_NONE, Category.RENDER, "No camera shake on hit.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.thePlayer.hurtTime = 0;
        mc.thePlayer.maxHurtTime = 0;
    }
}