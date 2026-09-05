package myau.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import myau.client.core.*;
import myau.client.gui.ClickGUI;
import myau.client.gui.font.CustomFont;
import myau.client.config.ConfigManager;
import myau.client.command.CommandManager;
import myau.client.alt.AltManager;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = "amongusclient", name = "Among Us Client", version = "1.0.0", clientSideOnly = true)
public class AmongUsMod {
    @Mod.Instance("amongusclient")
    public static AmongUsMod instance;
    
    private boolean rightShiftWasDown = false;
    private int notificationTimer = 0;
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModuleManager.init();
        KeybindManager.init();
        ConfigManager.init();
        AltManager.init();
        CommandManager.init();
        MinecraftForge.EVENT_BUS.register(this);
        notificationTimer = 100;
        System.out.println("[Among Us Client] Loaded " + ModuleManager.getModules().size() + " modules");
    }
    
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        KeybindManager.update();
        
        boolean rShiftDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        if (rShiftDown && !rightShiftWasDown) {
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(ClickGUI.getInstance());
            }
        }
        rightShiftWasDown = rShiftDown;
        
        ModuleManager.onTick();
        ModuleManager.onUpdate();
        
        if (notificationTimer > 0) notificationTimer--;
    }
    
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        
        ModuleManager.onRender2D(event.partialTicks);
        
        if (notificationTimer > 0) {
            renderNotification(mc, event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
        }
    }
    
    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onGuiOpen(net.minecraftforge.client.event.GuiOpenEvent event) {
        if (event.gui instanceof net.minecraft.client.gui.GuiMainMenu) {
            event.gui = new myau.client.gui.MainMenu();
        }
    }

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
    }
    
    private void renderNotification(net.minecraft.client.Minecraft mc, int scaledWidth, int scaledHeight) {
        FontRenderer fr = mc.fontRendererObj;
        String text = "\u00a7a\u00a7l[\u00a7eamong us client\u00a7a\u00a7l] \u00a7r\u00a77Loaded " + ModuleManager.getModules().size() + " modules \u00a77| \u00a7eRight Shift = ClickGUI";
        int textWidth = fr.getStringWidth(text);
        int boxW = textWidth + 20;
        int boxH = 18;
        int x = (scaledWidth - boxW) / 2;
        int y = scaledHeight - boxH - 10;
        
        float alpha = Math.min(1.0f, notificationTimer / 20.0f);
        
        drawRect(x, y, x + boxW, y + boxH, (int)(0xDD * alpha) << 24 | 0x1A1A1A);
        drawRect(x, y, x + 3, y + boxH, (int)(0xFF * alpha) << 24 | 0xE60000);
        fr.drawStringWithShadow(text, x + 10, y + 5, (int)(0xFF * alpha) << 24 | 0xFFFFFF);
    }
    
    private void drawRect(int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.renderer.GlStateManager.disableTexture2D();
        net.minecraft.client.renderer.GlStateManager.enableBlend();
        net.minecraft.client.renderer.GlStateManager.blendFunc(770, 771);
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        org.lwjgl.opengl.GL11.glColor4f(r, g, b, a);
        org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_QUADS);
        org.lwjgl.opengl.GL11.glVertex3d(x1, y2, 0);
        org.lwjgl.opengl.GL11.glVertex3d(x2, y2, 0);
        org.lwjgl.opengl.GL11.glVertex3d(x2, y1, 0);
        org.lwjgl.opengl.GL11.glVertex3d(x1, y1, 0);
        org.lwjgl.opengl.GL11.glEnd();
        net.minecraft.client.renderer.GlStateManager.enableTexture2D();
    }
}
