package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class Minimap extends Module {
    public Minimap() {
        super("Minimap", Keyboard.KEY_NONE, Category.RENDER, "Shows terrain minimap.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Size", 50, 200, 100, 10));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        int size = (int) getSetting("Size").getDoubleValue();
        int x = mc.displayWidth / 4 - size - 10;
        int y = 10;
        GuiScreen.drawRect(x, y, x + size, y + size, new Color(0, 0, 0, 150).getRGB());
        mc.fontRendererObj.drawStringWithShadow("Map", x + size / 2 - 10, y + size / 2 - 4, new Color(200, 200, 200).getRGB());
    }
}