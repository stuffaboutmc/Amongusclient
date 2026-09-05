package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class BlockESP extends Module {
    private List<Block> targetBlocks = new ArrayList<>();

    public BlockESP() {
        super("BlockESP", "Highlights specific blocks in world", Category.RENDER, Keyboard.KEY_NONE);
        targetBlocks.add(Blocks.diamond_ore);
        targetBlocks.add(Blocks.gold_ore);
        targetBlocks.add(Blocks.iron_ore);
        targetBlocks.add(Blocks.coal_ore);
        targetBlocks.add(Blocks.emerald_ore);
        targetBlocks.add(Blocks.lapis_ore);
        targetBlocks.add(Blocks.chest);
    }

    @Override
    public void onRender3D(float partialTicks) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        int range = 64;
        int px = (int) mc.thePlayer.posX;
        int py = (int) mc.thePlayer.posY;
        int pz = (int) mc.thePlayer.posZ;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(2.0F);

        for (int x = px - range; x <= px + range; x++) {
            for (int y = py - range; y <= py + range; y++) {
                for (int z = pz - range; z <= pz + range; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (targetBlocks.contains(block)) {
                        double dx = pos.getX() - mc.getRenderManager().viewerPosX;
                        double dy = pos.getY() - mc.getRenderManager().viewerPosY;
                        double dz = pos.getZ() - mc.getRenderManager().viewerPosZ;

                        if (block == Blocks.diamond_ore) GL11.glColor4f(0.0F, 1.0F, 1.0F, 0.5F);
                        else if (block == Blocks.gold_ore) GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.5F);
                        else if (block == Blocks.emerald_ore) GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.5F);
                        else if (block == Blocks.chest) GL11.glColor4f(1.0F, 0.5F, 0.0F, 0.5F);
                        else GL11.glColor4f(0.8F, 0.8F, 0.8F, 0.5F);

                        AxisAlignedBB bb = new AxisAlignedBB(dx, dy, dz, dx + 1, dy + 1, dz + 1);
                        drawOutlinedBox(bb);
                    }
                }
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private void drawOutlinedBox(AxisAlignedBB bb) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glEnd();
    }
}
