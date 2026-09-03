package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import java.util.List;
public class Breadcrumbs extends Module {
    private List<double[]> positions = new ArrayList<>();
    public Breadcrumbs() {
        super("Breadcrumbs", Keyboard.KEY_NONE, Category.RENDER, "Shows where you've been.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("MaxPoints", 10, 200, 50, 10));
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ});
        int max = (int) getSetting("MaxPoints").getDoubleValue();
        while (positions.size() > max) positions.remove(0);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GL11.glColor4f(1, 0, 0, 0.8F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (double[] pos : positions) {
            GL11.glVertex3d(pos[0] - mc.getRenderManager().viewerPosX, pos[1] - mc.getRenderManager().viewerPosY, pos[2] - mc.getRenderManager().viewerPosZ);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}