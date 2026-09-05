package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", "Draws lines from crosshair to players", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender3D(float partialTicks) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0F);

        double px = mc.getRenderManager().viewerPosX;
        double py = mc.getRenderManager().viewerPosY + mc.thePlayer.getEyeHeight();
        double pz = mc.getRenderManager().viewerPosZ;

        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer && obj != mc.thePlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (player.isDead || player.getHealth() <= 0) continue;

                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - px;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - py;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - pz;

                float dist = (float) Math.sqrt(x * x + y * y + z * z);
                float r = Math.min(1.0F, dist / 64.0F);
                float g = 1.0F - r;

                GL11.glColor4f(r, g, 0.0F, 0.6F);
                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex3d(0, 0, 0);
                GL11.glVertex3d(x, y, z);
                GL11.glEnd();
            }
        }

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
