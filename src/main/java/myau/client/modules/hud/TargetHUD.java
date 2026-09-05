package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class TargetHUD extends Module {
    private EntityPlayer lastTarget = null;

    public TargetHUD() {
        super("TargetHUD", "Shows target info in combat", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        EntityPlayer target = findTarget();
        if (target == null) return;

        int centerX = mc.displayWidth / 8;
        int centerY = mc.displayHeight / 4;

        Gui.drawRect(centerX - 50, centerY - 30, centerX + 50, centerY + 30, 0x80000000);

        mc.fontRendererObj.drawStringWithShadow(target.getName(), centerX - mc.fontRendererObj.getStringWidth(target.getName()) / 2, centerY - 20, 0xFFFFFF);
        mc.fontRendererObj.drawStringWithShadow("HP: " + String.format("%.1f", target.getHealth()), centerX - 30, centerY - 5, 0xFF5555);
        mc.fontRendererObj.drawStringWithShadow("Dist: " + String.format("%.1f", mc.thePlayer.getDistanceToEntity(target)), centerX - 30, centerY + 8, 0x55FF55);

        float healthPct = target.getHealth() / target.getMaxHealth();
        int barWidth = 80;
        int barHeight = 4;
        int barX = centerX - barWidth / 2;
        int barY = centerY + 18;
        Gui.drawRect(barX, barY, barX + barWidth, barY + barHeight, 0xFF333333);
        int fillColor = healthPct > 0.5F ? 0xFF00FF00 : (healthPct > 0.25F ? 0xFFFFFF00 : 0xFFFF0000);
        Gui.drawRect(barX, barY, barX + (int)(barWidth * healthPct), barY + barHeight, fillColor);
    }

    private EntityPlayer findTarget() {
        EntityPlayer closest = null;
        double closestDist = 6.0;
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer && obj != mc.thePlayer) {
                EntityPlayer p = (EntityPlayer) obj;
                if (p.isDead || p.getHealth() <= 0) continue;
                double dist = mc.thePlayer.getDistanceToEntity(p);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = p;
                }
            }
        }
        return closest;
    }
}
