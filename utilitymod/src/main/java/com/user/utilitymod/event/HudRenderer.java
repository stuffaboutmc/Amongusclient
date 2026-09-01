package com.user.utilitymod.event;

import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.ModuleManager;
import com.user.utilitymod.module.modules.render.ArmorHud;
import com.user.utilitymod.module.modules.render.Keystrokes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;

public class HudRenderer extends Gui {

    public static HudRenderer INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();

    public HudRenderer() {
        INSTANCE = this;
    }

    // name -> ticks remaining, for the Notifications module popups
    private final Map<String, Integer> activeNotifications = new LinkedHashMap<>();

    public void pushNotification(String text) {
        activeNotifications.put(text, 60); // ~3 seconds at 20 ticks/sec
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.thePlayer == null) return;

        Module armorHud = ModuleManager.getModuleByName("ArmorHUD");
        if (armorHud != null && armorHud.isEnabled()) {
            drawArmorHud();
        }

        Module keystrokes = ModuleManager.getModuleByName("Keystrokes");
        if (keystrokes != null && keystrokes.isEnabled()) {
            drawKeystrokes();
        }

        drawNotifications();

        // Small module list in the corner, "watermark" style, always visible.
        drawModuleList();
    }

    private void drawArmorHud() {
        int x = 10;
        int y = mc.displayHeight / mc.gameSettings.guiScale <= 0 ? 10 : 10;
        // Render armor slots 0-3 (boots..helmet) plus held item as simple icon rows.
        int startY = mc.currentScreen == null ? mc.displayHeight - 200 : 10;
        int drawY = 100;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
            if (stack != null) {
                mc.getRenderItem().renderItemIntoGUI(stack, x, drawY);
                drawY += 20;
            }
        }
        ItemStack held = mc.thePlayer.getHeldItem();
        if (held != null) {
            mc.getRenderItem().renderItemIntoGUI(held, x, drawY);
        }
    }

    private void drawKeystrokes() {
        int size = 20;
        int baseX = mc.displayWidth - (size * 3) - 20;
        int baseY = mc.displayHeight - (size * 2) - 20;

        drawKey("W", baseX + size, baseY, mc.gameSettings.keyBindForward);
        drawKey("A", baseX, baseY + size, mc.gameSettings.keyBindLeft);
        drawKey("S", baseX + size, baseY + size, mc.gameSettings.keyBindBack);
        drawKey("D", baseX + size * 2, baseY + size, mc.gameSettings.keyBindRight);
    }

    private void drawKey(String label, int x, int y, KeyBinding bind) {
        boolean down = bind.isKeyDown();
        int color = down ? 0xFF7F5CFF : 0x80000000;
        drawRect(x, y, x + 18, y + 18, color);
        mc.fontRendererObj.drawString(label, x + 6, y + 5, 0xFFFFFFFF);
    }

    private void drawNotifications() {
        int y = 30;
        Iterator<Map.Entry<String, Integer>> it = activeNotifications.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            int ticksLeft = entry.getValue();

            int width = mc.fontRendererObj.getStringWidth(entry.getKey()) + 10;
            int x = mc.displayWidth - width - 8;
            drawRect(x, y, x + width, y + 12, 0xB0161616);
            mc.fontRendererObj.drawString(entry.getKey(), x + 5, y + 2, 0xFFE6E6E6);
            y += 14;

            ticksLeft--;
            if (ticksLeft <= 0) {
                it.remove();
            } else {
                entry.setValue(ticksLeft);
            }
        }
    }

    private void drawModuleList() {
        int y = 4;
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            String text = m.getName();
            String suffix = m.getSuffix();
            if (suffix != null) text += " [" + suffix + "]";
            mc.fontRendererObj.drawStringWithShadow(text, 4, y, 0xFF7F5CFF);
            y += 10;
        }
    }
}
