package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Keyboard.KEY_NONE, Category.RENDER, "Highlights storage blocks.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glColor4f(0.8F, 0.8F, 0, 0.5F);
        for (TileEntity te : mc.theWorld.loadedTileEntityList) {
            double x = te.getPos().getX() - mc.getRenderManager().viewerPosX;
            double y = te.getPos().getY() - mc.getRenderManager().viewerPosY;
            double z = te.getPos().getZ() - mc.getRenderManager().viewerPosZ;
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex3d(x, y, z); GL11.glVertex3d(x+1, y, z); GL11.glVertex3d(x+1, y, z+1); GL11.glVertex3d(x, y, z+1); GL11.glVertex3d(x, y, z);
            GL11.glEnd();
        }
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}