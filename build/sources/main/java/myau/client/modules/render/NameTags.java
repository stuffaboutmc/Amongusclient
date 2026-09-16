package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", "Enhanced name tags with health info", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender3D(float partialTicks) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer && obj != mc.thePlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (player.isDead || player.getHealth() <= 0) continue;
                if (mc.thePlayer.getDistanceToEntity(player) > 64) continue;

                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

                float health = player.getHealth();
                float absorption = player.getAbsorptionAmount();

                String name = player.getName();
                String healthStr = EnumChatFormatting.RED + " " + String.format("%.1f", health);
                if (absorption > 0) {
                    healthStr += EnumChatFormatting.YELLOW + "+" + String.format("%.1f", absorption);
                }

                String display = name + healthStr;
                float scale = 0.025F;
                float dist = (float) mc.thePlayer.getDistanceToEntity(player);
                scale *= dist;
                if (scale < 0.01F) scale = 0.01F;
                if (scale > 0.06F) scale = 0.06F;

                GL11.glPushMatrix();
                GL11.glTranslated(x, y + 2.2, z);
                GL11.glRotatef(-mc.getRenderManager().playerViewY, 0, 1, 0);
                GL11.glRotatef(mc.getRenderManager().playerViewX, 1, 0, 0);
                GL11.glScalef(scale, -scale, scale);

                GlStateManager.disableDepth();
                GlStateManager.disableLighting();

                int textWidth = mc.fontRendererObj.getStringWidth(display) / 2;
                GlStateManager.disableTexture2D();
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex3d(-textWidth - 1, -1, 0);
                GL11.glVertex3d(textWidth + 1, -1, 0);
                GL11.glVertex3d(textWidth + 1, 9, 0);
                GL11.glVertex3d(-textWidth - 1, 9, 0);
                GL11.glEnd();
                GlStateManager.enableTexture2D();

                mc.fontRendererObj.drawStringWithShadow(display, -textWidth, -1, 0xFFFFFF);

                GlStateManager.enableDepth();
                GlStateManager.enableLighting();
                GL11.glPopMatrix();
            }
        }
    }
}
