package com.amongus.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final List<Notification> notifications = new ArrayList<>();
    private static final int MAX_NOTIFICATIONS = 5;
    private static final int DURATION_MS = 3000; // 3 seconds
    private static final int FADE_MS = 500;

    static {
        MinecraftForge.EVENT_BUS.register(new NotificationManager());
    }

    public static void addNotification(String message) {
        notifications.add(new Notification(message, System.currentTimeMillis()));
        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(0);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (notifications.isEmpty()) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        // Position: bottom right, stacked upward
        int x = screenWidth - 5;
        int y = screenHeight - 10;
        int notificationWidth = 180;
        int notificationHeight = 22;
        int gap = 5;

        // Remove expired notifications
        Iterator<Notification> iter = notifications.iterator();
        long currentTime = System.currentTimeMillis();
        while (iter.hasNext()) {
            Notification n = iter.next();
            if (currentTime - n.timeCreated > DURATION_MS) {
                iter.remove();
            }
        }

        // Draw notifications from oldest to newest (so newest at bottom)
        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification n = notifications.get(i);
            long elapsed = currentTime - n.timeCreated;
            float alpha = 1.0f;
            if (elapsed > DURATION_MS - FADE_MS) {
                alpha = 1.0f - (float)(elapsed - (DURATION_MS - FADE_MS)) / FADE_MS;
            }
            alpha = Math.max(0.0f, Math.min(1.0f, alpha));

            int notifX = x - notificationWidth;
            int notifY = y - notificationHeight - (i * (notificationHeight + gap));

            // Background with rounded corners (approximated with rectangles)
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();

            // Shadow
            Gui.drawRect(notifX - 2, notifY - 2, notifX + notificationWidth + 2, notifY + notificationHeight + 2,
                    new Color(0, 0, 0, (int)(80 * alpha)).getRGB());
            // Main background
            Gui.drawRect(notifX, notifY, notifX + notificationWidth, notifY + notificationHeight,
                    new Color(30, 30, 35, (int)(220 * alpha)).getRGB());
            // Accent line
            Gui.drawRect(notifX, notifY, notifX + 2, notifY + notificationHeight,
                    new Color(0, 200, 120, (int)(255 * alpha)).getRGB());

            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();

            // Text
            String text = n.message;
            int textWidth = mc.fontRendererObj.getStringWidth(text);
            int textX = notifX + 8;
            int textY = notifY + (notificationHeight - 8) / 2;
            mc.fontRendererObj.drawStringWithShadow(text, textX, textY,
                    new Color(255, 255, 255, (int)(255 * alpha)).getRGB());
        }
    }

    private static class Notification {
        String message;
        long timeCreated;

        Notification(String message, long timeCreated) {
            this.message = message;
            this.timeCreated = timeCreated;
        }
    }
}
