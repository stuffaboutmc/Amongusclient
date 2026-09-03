package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color AUGUSTUS_BG = new Color(25, 25, 25, 230);
    private static final Color AUGUSTUS_HEADER = new Color(35, 35, 35, 255);
    private static final Color AUGUSTUS_OUTLINE = new Color(60, 60, 60, 255);
    private static final Color AUGUSTUS_ENABLED = new Color(0, 255, 128, 255);
    private static final Color AUGUSTUS_DISABLED = new Color(180, 180, 180, 255);
    private static final Color IMPOSTER_RED = new Color(255, 0, 0, 255);
    private static final Color TOOLTIP_BG = new Color(10, 10, 10, 200);
    private static final Color TOOLTIP_TEXT = new Color(220, 220, 220, 255);

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER, "Opens the panel menu.");
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new AugustusGuiScreen());
        toggle();
    }

    public class AugustusGuiScreen extends GuiScreen {
        private List<Panel> panels = new ArrayList<>();
        private Panel draggingPanel;
        private int dragOffsetX, dragOffsetY;

        public AugustusGuiScreen() {
            Panel combat = new Panel("Combat", 20, 20);
            Panel movement = new Panel("Movement", 145, 20);
            Panel render = new Panel("Render", 270, 20);
            Panel player = new Panel("Player", 395, 20);
            Panel misc = new Panel("Misc", 520, 20);
            for (Module m : AmongusClient.moduleManager.getModules()) {
                switch (m.getCategory()) {
                    case COMBAT: combat.modules.add(m); break;
                    case MOVEMENT: movement.modules.add(m); break;
                    case RENDER: render.modules.add(m); break;
                    case PLAYER: player.modules.add(m); break;
                    case MISC: misc.modules.add(m); break;
                }
            }
            panels.add(combat);
            panels.add(movement);
            panels.add(render);
            panels.add(player);
            panels.add(misc);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawRect(0, 0, width, height, new Color(0, 0, 0, 160).getRGB());
            for (Panel panel : panels) panel.draw(mouseX, mouseY);
            ScaledResolution sr = new ScaledResolution(mc);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            mc.fontRendererObj.drawStringWithShadow("Amongus", 6, sr.getScaledHeight() - 13, IMPOSTER_RED.getRGB());
            int ww = mc.fontRendererObj.getStringWidth("Amongus");
            mc.fontRendererObj.drawStringWithShadow("v1.0", 8 + ww, sr.getScaledHeight() - 12, new Color(200, 200, 200, 255).getRGB());
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            if (draggingPanel = null) {
                draggingPanel.x = mouseX - dragOffsetX;
                draggingPanel.y = mouseY - dragOffsetY;
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            for (Panel panel : panels) {
                if (mouseButton == 0 && panel.isHoveringHeader(mouseX, mouseY)) {
                    draggingPanel = panel;
                    dragOffsetX = mouseX - panel.x;
                    dragOffsetY = mouseY - panel.y;
                    return;
                }
                if (mouseButton == 0) {
                    for (Module m : panel.modules) {
                        int mi = panel.modules.indexOf(m);
                        int my = panel.y + panel.headerHeight + (mi * panel.moduleHeight);
                        if (mouseX >= panel.x && mouseX <= panel.x + panel.width && mouseY >= my && mouseY <= my + panel.moduleHeight) {
                            m.toggle();
                            return;
                        }
                    }
                }
            }
        }

        @Override
        protected void mouseReleased(int mouseX, int mouseY, int state) {
            draggingPanel = null;
        }

        @Override
        public boolean doesGuiPauseGame() { return false; }
    }

    public class Panel {
        public String title;
        public int x, y;
        public int width = 110;
        public int headerHeight = 18;
        public int moduleHeight = 15;
        public List<Module> modules = new ArrayList<>();

        public Panel(String title, int x, int y) { this.title = title; this.x = x; this.y = y; }

        public boolean isHoveringHeader(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight;
        }

        public void draw(int mouseX, int mouseY) {
            int th = headerHeight + (modules.size() * moduleHeight);
            drawRect(x, y, x + width, y + th, AUGUSTUS_BG.getRGB());
            drawRect(x, y, x + width, y + headerHeight, AUGUSTUS_HEADER.getRGB());
            drawRect(x, y, x + width, y + 1, AUGUSTUS_OUTLINE.getRGB());
            drawRect(x, y + th - 1, x + width, y + th, AUGUSTUS_OUTLINE.getRGB());
            drawRect(x, y, x + 1, y + th, AUGUSTUS_OUTLINE.getRGB());
            drawRect(x + width - 1, y, x + width, y + th, AUGUSTUS_OUTLINE.getRGB());
            String ht = title.toUpperCase();
            int tw = mc.fontRendererObj.getStringWidth(ht);
            mc.fontRendererObj.drawStringWithShadow(ht, x + (width - tw) / 2, y + (headerHeight - 8) / 2, new Color(255, 255, 255, 255).getRGB());
            String ti = modules.stream().anyMatch(m -> m.isEnabled()) ? "+" : "-";
            mc.fontRendererObj.drawStringWithShadow(ti, x + width - 10, y + (headerHeight - 8) / 2, new Color(150, 150, 150, 255).getRGB());
            Module hovered = null;
            for (Module m : modules) {
                int my = y + headerHeight + (modules.indexOf(m) * moduleHeight);
                if (mouseX >= x && mouseX <= x + width && mouseY >= my && mouseY <= my + moduleHeight) {
                    drawRect(x, my, x + width, my + moduleHeight, new Color(50, 50, 50, 180).getRGB());
                    hovered = m;
                }
                Color mc2 = m.isEnabled() ? AUGUSTUS_ENABLED : AUGUSTUS_DISABLED;
                mc.fontRendererObj.drawStringWithShadow(m.getName(), x + 6, my + 4, mc2.getRGB());
                if (m.getKey() = Keyboard.KEY_NONE) {
                    String kn = Keyboard.getKeyName(m.getKey());
                    int kw = mc.fontRendererObj.getStringWidth(kn);
                    mc.fontRendererObj.drawStringWithShadow(kn, x + width - kw - 6, my + 4, new Color(120, 120, 120, 255).getRGB());
                }
            }
            if (hovered = null) drawTooltip(hovered, mouseX, mouseY);
        }

        private void drawTooltip(Module m, int mouseX, int mouseY) {
            String[] words = m.getDescription().split(" ");
            List<String> lines = new ArrayList<>();
            String current = "";
            for (String word : words) {
                if (mc.fontRendererObj.getStringWidth(current + word) < 150) {
                    current += word + " ";
                } else {
                    lines.add(current.trim());
                    current = word + " ";
                }
            }
            if (current.trim().isEmpty()) lines.add(current.trim());
            int tw = 0;
            for (String line : lines) tw = Math.max(tw, mc.fontRendererObj.getStringWidth(line));
            tw += 12;
            int th = lines.size() * 10 + 8;
            int tx = mouseX + 10;
            int ty = mouseY + 10;
            if (tx + tw > mc.displayWidth / 2) tx = mouseX - tw - 10;
            if (ty + th > mc.displayHeight / 2) ty = mouseY - th - 10;
            drawRect(tx, ty, tx + tw, ty + th, TOOLTIP_BG.getRGB());
            for (int i = 0; i < lines.size(); i++) {
                mc.fontRendererObj.drawStringWithShadow(lines.get(i), tx + 6, ty + 4 + i * 10, TOOLTIP_TEXT.getRGB());
            }
        }
    }
}
