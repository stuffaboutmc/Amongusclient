package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class Radar extends Module {
    public Radar() {
        super("Radar", Keyboard.KEY_NONE, Category.RENDER, "Shows nearby players.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Size", 50, 200, 100, 10));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;
        int size = (int) getSetting("Size").getDoubleValue();
        int centerX = 10 + size / 2;
        int centerY = 10 + size / 2;
        GuiScreen.drawRect(10, 10, 10 + size, 10 + size, new Color(0, 0, 0, 150).getRGB());
        mc.fontRendererObj.drawStringWithShadow("N", centerX - 4, 10, -1);
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead) continue;
            double dx = entity.posX - mc.thePlayer.posX;
            double dz = entity.posZ - mc.thePlayer.posZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > 50) continue;
            int px = centerX + (int)(dx / 50 * (size / 2));
            int py = centerY + (int)(dz / 50 * (size / 2));
            GuiScreen.drawRect(px - 1, py - 1, px + 1, py + 1, new Color(255, 0, 0).getRGB());
        }
        GuiScreen.drawRect(centerX - 1, centerY - 1, centerX + 1, centerY + 1, new Color(0, 255, 128).getRGB());
    }
}