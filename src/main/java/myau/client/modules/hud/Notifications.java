package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notifications extends Module {
    private static List<Notification> notifications = new ArrayList<>();

    public Notifications() {
        super("Notifications", "Shows notification popups", Category.HUD, Keyboard.KEY_NONE);
    }

    public static void addNotification(String text) {
        notifications.add(new Notification(text, System.currentTimeMillis()));
    }

    @Override
    public void onRender2D(float partialTicks) {
        long now = System.currentTimeMillis();
        int y = mc.displayHeight / 4;

        Iterator<Notification> it = notifications.iterator();
        while (it.hasNext()) {
            Notification n = it.next();
            long elapsed = now - n.time;
            if (elapsed > 3000) {
                it.remove();
                continue;
            }

            float alpha = 1.0F;
            if (elapsed > 2000) {
                alpha = 1.0F - ((elapsed - 2000) / 1000.0F);
            }

            int width = mc.fontRendererObj.getStringWidth(n.text) + 10;
            int centerX = mc.displayWidth / 4 - width / 2;

            net.minecraft.client.renderer.GlStateManager.enableBlend();
            net.minecraft.client.renderer.GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F * alpha);
            Gui.drawRect(centerX - 2, y - 2, centerX + width + 2, y + 12, 0x80000000);
            net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            mc.fontRendererObj.drawStringWithShadow(n.text, centerX, y, 0xFFFFFF);
            y += 16;
        }
    }

    private static class Notification {
        String text;
        long time;

        Notification(String text, long time) {
            this.text = text;
            this.time = time;
        }
    }
}
