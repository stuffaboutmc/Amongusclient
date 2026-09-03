package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;
public class Waypoints extends Module {
    private List<BlockPos> waypoints = new ArrayList<>();
    public Waypoints() {
        super("Waypoints", Keyboard.KEY_NONE, Category.RENDER, "Shows saved locations.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (!waypoints.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GL11.glColor4f(0, 1, 0, 0.8F);
            for (BlockPos wp : waypoints) {
                double x = wp.getX() - mc.getRenderManager().viewerPosX;
                double y = wp.getY() - mc.getRenderManager().viewerPosY;
                double z = wp.getZ() - mc.getRenderManager().viewerPosZ;
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex3d(x, y, z); GL11.glVertex3d(x, y+1, z);
                GL11.glEnd();
            }
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
    }
    public void addWaypoint(BlockPos pos) { waypoints.add(pos); }
}