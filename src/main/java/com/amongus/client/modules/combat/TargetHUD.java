package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class TargetHUD extends Module {
    public TargetHUD() {
        super("TargetHUD", Keyboard.KEY_NONE, Category.COMBAT, "Shows target info.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            String name = target.getName();
            String health = String.valueOf((int) target.getHealth());
            mc.fontRendererObj.drawStringWithShadow(name, 10, 10, new Color(255, 255, 255).getRGB());
            mc.fontRendererObj.drawStringWithShadow("HP: " + health, 10, 20, new Color(255, 50, 50).getRGB());
        }
    }
}