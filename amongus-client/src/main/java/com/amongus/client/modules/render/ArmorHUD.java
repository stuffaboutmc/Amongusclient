package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class ArmorHUD extends Module {
    public ArmorHUD() {
        super("ArmorHUD", Keyboard.KEY_NONE, Category.RENDER, "Shows armor durability.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer == null) return;
        int y = 110;
        for (ItemStack armor : mc.thePlayer.inventory.armorInventory) {
            if (armor != null) {
                int durability = armor.getMaxDamage() - armor.getItemDamage();
                mc.fontRendererObj.drawStringWithShadow(armor.getDisplayName() + ": " + durability, 5, y, new Color(255, 255, 255).getRGB());
                y += 10;
            }
        }
    }
}