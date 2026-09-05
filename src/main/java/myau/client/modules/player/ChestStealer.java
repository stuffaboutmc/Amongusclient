package myau.client.modules.player;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    private int tickDelay = 0;

    public ChestStealer() {
        super("ChestStealer", "Steals all items from opened chest", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        tickDelay++;
        if (tickDelay < 2) return;
        tickDelay = 0;

        if (mc.currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            ContainerChest container = (ContainerChest) chest.inventorySlots;
            int totalSlots = container.getLowerChestInventory().getSizeInventory();

            for (int i = 0; i < totalSlots; i++) {
                Slot slot = container.getSlot(i);
                if (slot != null && slot.getStack() != null && slot.getHasStack()) {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                }
            }
        }
    }
}
