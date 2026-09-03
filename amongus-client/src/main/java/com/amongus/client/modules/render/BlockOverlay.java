package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
public class BlockOverlay extends Module {
    public BlockOverlay() {
        super("BlockOverlay", Keyboard.KEY_NONE, Category.RENDER, "Highlights the block you're looking at.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.objectMouseOver == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GL11.glColor4f(1, 1, 1, 0.3F);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        double x = pos.getX() - mc.getRenderManager().viewerPosX;
        double y = pos.getY() - mc.getRenderManager().viewerPosY;
        double z = pos.getZ() - mc.getRenderManager().viewerPosZ;
        GL11.glVertex3d(x, y, z); GL11.glVertex3d(x+1, y, z); GL11.glVertex3d(x+1, y, z+1); GL11.glVertex3d(x, y, z+1);
        GL11.glVertex3d(x, y, z); GL11.glVertex3d(x, y+1, z); GL11.glVertex3d(x+1, y+1, z); GL11.glVertex3d(x+1, y+1, z+1);
        GL11.glVertex3d(x, y+1, z+1); GL11.glVertex3d(x, y+1, z); GL11.glVertex3d(x+1, y+1, z); GL11.glVertex3d(x+1, y, z);
        GL11.glVertex3d(x+1, y, z+1); GL11.glVertex3d(x+1, y+1, z+1); GL11.glVertex3d(x, y, z+1); GL11.glVertex3d(x, y+1, z+1);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}