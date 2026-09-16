package myau.client.gui;

import myau.client.core.Category;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
import myau.client.gui.font.CustomFont;
import myau.client.gui.font.CustomFontRenderer;
import myau.client.gui.GuiStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClickGUI extends GuiScreen {

    private static ClickGUI instance;

    private final Category[] categories = Category.values();
    private int selectedCategoryIndex = 0;
    private List<Module> currentModules = new ArrayList<>();
    private Module selectedModule = null;

    private int scrollOffset = 0;
    private boolean draggingSlider = false;
    private String draggingSliderName = null;

    private static final int TITLE_HEIGHT = 30;
    private static final int TAB_HEIGHT = 22;
    private static final int HEADER_HEIGHT = TITLE_HEIGHT + TAB_HEIGHT;
    private static final int SIDEBAR_WIDTH = 155;
    private static final int MODULE_HEIGHT = 18;
    private static final int MODULE_PAD = 2;

    private GuiStyle currentStyle = GuiStyle.Rise;
    private float guiScale = 1.0f;
    private boolean firstTimeDone = false;

    public ClickGUI() {
        instance = this;
    }

    public static ClickGUI getInstance() {
        if (instance == null) instance = new ClickGUI();
        return instance;
    }

    public GuiStyle getStyle() { return currentStyle; }
    public void setStyle(GuiStyle style) { this.currentStyle = style; }
    public float getGuiScale() { return guiScale; }
    public void setGuiScale(float scale) { this.guiScale = scale; }
    public boolean isFirstTimeDone() { return firstTimeDone; }
    public void setFirstTimeDone(boolean done) { this.firstTimeDone = done; }

    @Override
    public void initGui() {
        super.initGui();
        if (CustomFont.TITLE == null) CustomFont.init();
        if (ModuleManager.getModules().isEmpty()) {
            ModuleManager.init();
        }
        currentModules = ModuleManager.getModulesByCategory(categories[selectedCategoryIndex]);
        currentModules.sort(Comparator.comparing(Module::getName));
        scrollOffset = 0;
        selectedModule = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int w = width;
        int h = height;

        drawRect(0, 0, w, h, 0x90000000);

        drawTitleBar(w);
        drawCategoryTabs(w, mouseX, mouseY);
        drawModuleList(mouseX, mouseY);
        drawSettingsPanel(mouseX, mouseY, w, h);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTitleBar(int w) {
        drawRect(0, 0, w, TITLE_HEIGHT, 0xFF111111);
        drawRect(0, TITLE_HEIGHT - 1, w, TITLE_HEIGHT, 0xFF333333);
        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString("CLICKGUI", 10, 7, 0xFFFFFFFF);
        } else {
            fontRendererObj.drawStringWithShadow("CLICKGUI", 10, 7, 0xFFFFFFFF);
        }
    }

    private void drawCategoryTabs(int w, int mouseX, int mouseY) {
        drawRect(0, TITLE_HEIGHT, w, HEADER_HEIGHT, 0xFF1A1A1A);
        drawRect(0, HEADER_HEIGHT - 1, w, HEADER_HEIGHT, 0xFF444444);

        int x = 10;
        for (int i = 0; i < categories.length; i++) {
            Category cat = categories[i];
            int textW = getStringWidth(cat.displayName);
            boolean hovered = mouseX >= x && mouseX <= x + textW && mouseY >= TITLE_HEIGHT && mouseY <= HEADER_HEIGHT;
            boolean selected = i == selectedCategoryIndex;

            if (selected) {
                drawRect(x - 2, TITLE_HEIGHT + 2, x + textW + 2, HEADER_HEIGHT - 2, 0x40FFFFFF);
            } else if (hovered) {
                drawRect(x - 2, TITLE_HEIGHT + 2, x + textW + 2, HEADER_HEIGHT - 2, 0x20FFFFFF);
            }

            int textColor = selected ? 0xFFFFFFFF : 0xFF888888;
            drawString(cat.displayName, x, TITLE_HEIGHT + 4, textColor);
            x += textW + 16;
        }
    }

    private void drawModuleList(int mouseX, int mouseY) {
        drawRect(0, HEADER_HEIGHT, SIDEBAR_WIDTH, height, 0xE0111111);
        drawRect(SIDEBAR_WIDTH - 1, HEADER_HEIGHT, SIDEBAR_WIDTH, height, 0xFF444444);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scale = mc.gameSettings.guiScale;
        int scissorX = 0;
        int scissorY = (mc.displayHeight - (HEADER_HEIGHT + (height - HEADER_HEIGHT))) * scale;
        int scissorW = SIDEBAR_WIDTH * scale;
        int scissorH = (height - HEADER_HEIGHT) * scale;
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);

        int y = HEADER_HEIGHT + 4 - scrollOffset;
        int itemH = MODULE_HEIGHT + MODULE_PAD;

        for (int i = 0; i < currentModules.size(); i++) {
            Module m = currentModules.get(i);
            if (y + itemH < HEADER_HEIGHT || y > height) {
                y += itemH;
                continue;
            }

            boolean hovered = mouseX >= 4 && mouseX <= SIDEBAR_WIDTH - 4 && mouseY >= y && mouseY <= y + MODULE_HEIGHT;
            boolean isActive = m == selectedModule;

            if (isActive) {
                drawRect(3, y, SIDEBAR_WIDTH - 3, y + MODULE_HEIGHT, 0x30FFFFFF);
            } else if (hovered) {
                drawRect(3, y, SIDEBAR_WIDTH - 3, y + MODULE_HEIGHT, 0x15FFFFFF);
            }

            int nameColor = m.isEnabled() ? 0xFFFFFF00 : 0xFFCCCCCC;
            drawString(m.getName(), 8, y + 4, nameColor);
            y += itemH;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawSettingsPanel(int mouseX, int mouseY, int w, int h) {
        int panelX = SIDEBAR_WIDTH + 4;
        int panelW = w - SIDEBAR_WIDTH - 8;
        int panelY = HEADER_HEIGHT + 4;
        int panelH = h - HEADER_HEIGHT - 8;

        if (selectedModule == null) {
            drawRect(panelX, panelY, panelX + panelW, panelY + panelH, 0xE0111111);
            drawRect(panelX, panelY, panelX + panelW, panelY + 1, 0xFF444444);
            drawRect(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF444444);
            drawRect(panelX, panelY, panelX + 1, panelY + panelH, 0xFF444444);
            drawRect(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF444444);
            String msg = "Select a module";
            int msgW = getStringWidth(msg);
            drawString(msg, panelX + panelW / 2 - msgW / 2, panelY + panelH / 2 - 4, 0xFF666666);
            return;
        }

        drawRect(panelX, panelY, panelX + panelW, panelY + panelH, 0xE0111111);
        drawRect(panelX, panelY, panelX + panelW, panelY + 1, 0xFF444444);
        drawRect(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, 0xFF444444);
        drawRect(panelX, panelY, panelX + 1, panelY + panelH, 0xFF444444);
        drawRect(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, 0xFF444444);

        drawRect(panelX + 1, panelY + 1, panelX + panelW - 1, panelY + 35, 0xFF161616);

        String moduleName = selectedModule.getName().toUpperCase();
        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString(moduleName, panelX + 10, panelY + 10, 0xFFFFFFFF);
        } else {
            fontRendererObj.drawStringWithShadow(moduleName, panelX + 10, panelY + 10, 0xFFFFFFFF);
        }

        drawRect(panelX + 1, panelY + 35, panelX + panelW - 1, panelY + 36, 0xFF444444);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scale = mc.gameSettings.guiScale;
        int scissorX = (panelX + 1) * scale;
        int scissorY = (mc.displayHeight - (panelY + panelH - 1)) * scale;
        int scissorW = (panelW - 2) * scale;
        int scissorH = (panelH - 37) * scale;
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);

        int y = panelY + 42;
        for (Module.Setting s : selectedModule.getSettings()) {
            if (y > panelY + panelH) break;

            if (s.getType() == Module.SettingType.BOOLEAN) {
                y = drawBooleanSetting(s, panelX + 8, y, panelW - 16, mouseX, mouseY);
            } else if (s.getType() == Module.SettingType.NUMBER) {
                y = drawSliderSetting(s, panelX + 8, y, panelW - 16, mouseX, mouseY);
            } else if (s.getType() == Module.SettingType.MODE) {
                y = drawModeSetting(s, panelX + 8, y, panelW - 16, mouseX, mouseY);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private int drawBooleanSetting(Module.Setting s, int x, int y, int w, int mouseX, int mouseY) {
        int h = 16;
        boolean val = s.getBoolean();
        drawString(s.getName() + ": ", x, y + 2, 0xFFBBBBBB);
        int nameW = getStringWidth(s.getName() + ": ");
        drawString(String.valueOf(val), x + nameW, y + 2, val ? 0xFF55FF55 : 0xFFFF5555);
        return y + h;
    }

    private int drawSliderSetting(Module.Setting s, int x, int y, int w, int mouseX, int mouseY) {
        int h = 22;
        drawString(s.getName() + ":", x, y + 2, 0xFFBBBBBB);

        int barY = y + 14;
        int barH = 5;
        drawRect(x, barY, x + w, barY + barH, 0xFF333333);

        float min = ((Number) s.getMin()).floatValue();
        float max = ((Number) s.getMax()).floatValue();
        float val = ((Number) s.getValue()).floatValue();
        float percent = max != min ? (val - min) / (max - min) : 0;
        percent = Math.max(0, Math.min(1, percent));
        drawRect(x, barY, x + (int) (w * percent), barY + barH, 0xFF3388FF);

        drawString(String.format("%.1f", val), x + w + 4, y + 2, 0xFFBBBBBB);
        return y + h;
    }

    private int drawModeSetting(Module.Setting s, int x, int y, int w, int mouseX, int mouseY) {
        drawString(s.getName() + ": ", x, y + 2, 0xFFBBBBBB);
        int nameW = getStringWidth(s.getName() + ": ");
        List<String> modes = s.getModes();
        int currentIdx = s.getModeIndex();

        int modesX = x + nameW;
        for (int i = 0; i < modes.size(); i++) {
            String mode = modes.get(i);
            boolean isCurrent = i == currentIdx;
            boolean hovered = mouseX >= modesX && mouseX <= modesX + getStringWidth(mode + ", ")
                    && mouseY >= y && mouseY <= y + 12;
            int color = isCurrent ? 0xFFFFFF00 : (hovered ? 0xFFAAAAAA : 0xFF777777);
            drawString(mode, modesX, y + 2, color);
            modesX += getStringWidth(mode + ", ");
            if (i < modes.size() - 1) {
                drawString(", ", modesX - getStringWidth(", "), y + 2, 0xFF555555);
            }
        }

        int lines = 1;
        int totalW = nameW;
        for (String mode : modes) {
            totalW += getStringWidth(mode + ", ");
            if (totalW > w && lines < 3) {
                lines++;
                totalW = getStringWidth(mode + ", ");
            }
        }
        return y + 12 + (lines - 1) * 12;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseY >= TITLE_HEIGHT && mouseY <= HEADER_HEIGHT) {
            int x = 10;
            for (int i = 0; i < categories.length; i++) {
                int textW = getStringWidth(categories[i].displayName);
                if (mouseX >= x && mouseX <= x + textW) {
                    selectedCategoryIndex = i;
                    currentModules = ModuleManager.getModulesByCategory(categories[i]);
                    currentModules.sort(Comparator.comparing(Module::getName));
                    scrollOffset = 0;
                    selectedModule = null;
                    return;
                }
                x += textW + 16;
            }
        }

        if (mouseY > HEADER_HEIGHT && mouseX < SIDEBAR_WIDTH) {
            int y = HEADER_HEIGHT + 4 - scrollOffset;
            int itemH = MODULE_HEIGHT + MODULE_PAD;
            for (Module m : currentModules) {
                if (mouseX >= 4 && mouseX <= SIDEBAR_WIDTH - 4 && mouseY >= y && mouseY <= y + MODULE_HEIGHT) {
                    if (mouseButton == 0) {
                        m.toggle();
                    } else if (mouseButton == 1) {
                        selectedModule = m;
                    }
                    return;
                }
                y += itemH;
            }
            selectedModule = null;
            return;
        }

        if (selectedModule != null && mouseX > SIDEBAR_WIDTH) {
            int panelX = SIDEBAR_WIDTH + 4;
            int panelW = width - SIDEBAR_WIDTH - 8;
            int y = HEADER_HEIGHT + 42;

            for (Module.Setting s : selectedModule.getSettings()) {
                if (s.getType() == Module.SettingType.BOOLEAN) {
                    if (mouseX >= panelX + 8 && mouseX <= panelX + 8 + panelW - 16
                            && mouseY >= y && mouseY <= y + 16) {
                        s.toggle();
                        return;
                    }
                    y += 16;
                } else if (s.getType() == Module.SettingType.NUMBER) {
                    if (mouseX >= panelX + 8 && mouseX <= panelX + 8 + panelW - 16
                            && mouseY >= y && mouseY <= y + 22) {
                        float min = ((Number) s.getMin()).floatValue();
                        float max = ((Number) s.getMax()).floatValue();
                        float percent = (float) (mouseX - (panelX + 8)) / (panelW - 16);
                        percent = Math.max(0, Math.min(1, percent));
                        float val = min + percent * (max - min);
                        s.setValue(Math.round(val / ((Number) s.getInc()).floatValue()) * ((Number) s.getInc()).floatValue());
                        draggingSlider = true;
                        draggingSliderName = s.getName();
                        return;
                    }
                    y += 22;
                } else if (s.getType() == Module.SettingType.MODE) {
                    if (mouseX >= panelX + 8 && mouseX <= panelX + 8 + panelW - 16
                            && mouseY >= y && mouseY <= y + 12) {
                        int modesX = panelX + 8 + getStringWidth(s.getName() + ": ");
                        List<String> modes = s.getModes();
                        for (int i = 0; i < modes.size(); i++) {
                            String mode = modes.get(i);
                            int modeW = getStringWidth(mode + ", ");
                            if (mouseX >= modesX && mouseX <= modesX + modeW) {
                                s.setModeIndex(i);
                                return;
                            }
                            modesX += modeW;
                        }
                    }
                    int lines = 1;
                    int totalW = getStringWidth(s.getName() + ": ");
                    for (String mode : s.getModes()) {
                        totalW += getStringWidth(mode + ", ");
                        if (totalW > panelW - 16) {
                            lines++;
                            totalW = getStringWidth(mode + ", ");
                        }
                    }
                    y += 12 + (lines - 1) * 12;
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (draggingSlider && selectedModule != null && draggingSliderName != null) {
            int panelX = SIDEBAR_WIDTH + 4;
            int panelW = width - SIDEBAR_WIDTH - 8;
            int y = HEADER_HEIGHT + 42;

            for (Module.Setting s : selectedModule.getSettings()) {
                if (s.getType() == Module.SettingType.BOOLEAN) {
                    y += 16;
                } else if (s.getType() == Module.SettingType.NUMBER) {
                    if (s.getName().equals(draggingSliderName)) {
                        float min = ((Number) s.getMin()).floatValue();
                        float max = ((Number) s.getMax()).floatValue();
                        float percent = (float) (mouseX - (panelX + 8)) / (panelW - 16);
                        percent = Math.max(0, Math.min(1, percent));
                        float val = min + percent * (max - min);
                        s.setValue(Math.round(val / ((Number) s.getInc()).floatValue()) * ((Number) s.getInc()).floatValue());
                        return;
                    }
                    y += 22;
                } else if (s.getType() == Module.SettingType.MODE) {
                    int lines = 1;
                    int totalW = getStringWidth(s.getName() + ": ");
                    for (String mode : s.getModes()) {
                        totalW += getStringWidth(mode + ", ");
                        if (totalW > panelW - 16) {
                            lines++;
                            totalW = getStringWidth(mode + ", ");
                        }
                    }
                    y += 12 + (lines - 1) * 12;
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggingSlider = false;
        draggingSliderName = null;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            if (Mouse.getEventX() < SIDEBAR_WIDTH * mc.gameSettings.guiScale) {
                scrollOffset -= wheel / 120 * 15;
                int maxScroll = currentModules.size() * (MODULE_HEIGHT + MODULE_PAD) - (height - HEADER_HEIGHT);
                if (maxScroll < 0) maxScroll = 0;
                scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == 0x9F) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().setIngameFocus();
            }
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private int getStringWidth(String text) {
        if (CustomFont.TITLE != null) {
            return CustomFont.TITLE.getStringWidth(text);
        }
        return fontRendererObj.getStringWidth(text);
    }

    private void drawString(String text, int x, int y, int color) {
        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString(text, x, y, color);
        } else {
            fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
