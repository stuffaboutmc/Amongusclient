package com.user.utilitymod.gui;

import com.user.utilitymod.gui.component.Frame;
import com.user.utilitymod.gui.component.ModuleComponent;
import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Setting;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends GuiScreen {

    // ARGB colors
    private static final int COLOR_BG = 0xE0161616;
    private static final int COLOR_HEADER = 0xF02A2A2A;
    private static final int COLOR_ACCENT = 0xFF7F5CFF;
    private static final int COLOR_ROW = 0xD01E1E1E;
    private static final int COLOR_ROW_ON = 0xD02E2350;
    private static final int COLOR_TEXT = 0xFFE6E6E6;
    private static final int COLOR_TEXT_DIM = 0xFFA0A0A0;

    private final List<Frame> frames = new ArrayList<>();

    public ClickGui() {
        double startX = 20;
        double startY = 20;
        double spacing = 130;
        int i = 0;
        for (Category cat : Category.values()) {
            frames.add(new Frame(cat, startX + i * spacing, startY));
            i++;
        }
    }

    @Override
    public void initGui() {
        // Frames retain position across opens because they're stored on the instance;
        // if you want persistence across GUI re-creation, save x/y to a config file here.
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        for (Frame frame : frames) {
            drawFrame(frame, mouseX, mouseY);
        }

        drawCenteredString(fontRendererObj, "Amongus", width / 2, 4, COLOR_TEXT);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawFrame(Frame frame, int mouseX, int mouseY) {
        double x = frame.x;
        double y = frame.y;
        double w = frame.width;

        drawRect((int) x, (int) y, (int) (x + w), (int) (y + frame.getHeaderHeight()), COLOR_HEADER);
        drawRect((int) (x + w - 3), (int) y, (int) (x + w), (int) (y + frame.getHeaderHeight()), COLOR_ACCENT);

        fontRendererObj.drawString(frame.category.getDisplayName(), (int) x + 4, (int) y + 3, COLOR_TEXT);

        if (frame.collapsed) return;

        double rowY = y + frame.getHeaderHeight();
        drawRect((int) x, (int) rowY, (int) (x + w), (int) (y + frame.getTotalHeight()), COLOR_BG);

        for (ModuleComponent comp : frame.components) {
            double rowH = comp.getRowHeight();
            int rowColor = comp.module.isEnabled() ? COLOR_ROW_ON : COLOR_ROW;
            drawRect((int) x, (int) rowY, (int) (x + w), (int) (rowY + rowH), rowColor);

            int textColor = comp.module.isEnabled() ? COLOR_TEXT : COLOR_TEXT_DIM;
            fontRendererObj.drawString(comp.module.getName(), (int) x + 4, (int) (rowY + 2), textColor);

            String suffix = comp.module.getSuffix();
            if (suffix != null) {
                int sw = fontRendererObj.getStringWidth(suffix);
                fontRendererObj.drawString(suffix, (int) (x + w - sw - 12), (int) (rowY + 2), COLOR_TEXT_DIM);
            }

            // Arrow indicator for expandable settings
            if (!comp.module.getSettings().isEmpty()) {
                String arrow = comp.settingsOpen ? "-" : "+";
                fontRendererObj.drawString(arrow, (int) (x + w - 8), (int) (rowY + 2), COLOR_TEXT_DIM);
            }

            rowY += rowH;

            if (comp.settingsOpen) {
                for (Setting setting : comp.module.getSettings()) {
                    drawSettingRow(setting, x, rowY, w, comp.getSettingRowHeight());
                    rowY += comp.getSettingRowHeight();
                }
            }
        }
    }

    private void drawSettingRow(Setting setting, double x, double y, double w, double h) {
        drawRect((int) x, (int) y, (int) (x + w), (int) (y + h), 0xC0121212);

        if (setting.getType() == Setting.Type.TOGGLE) {
            int boxColor = setting.isEnabled() ? COLOR_ACCENT : 0xFF3A3A3A;
            drawRect((int) (x + w - 14), (int) (y + 2), (int) (x + w - 4), (int) (y + h - 2), boxColor);
            fontRendererObj.drawString(setting.getName(), (int) x + 8, (int) (y + 2), COLOR_TEXT_DIM);
        } else {
            fontRendererObj.drawString(setting.getName(), (int) x + 8, (int) (y + 2), COLOR_TEXT_DIM);

            double barX = x + 8;
            double barW = w - 16;
            double barY = y + h - 3;
            drawRect((int) barX, (int) barY, (int) (barX + barW), (int) (barY + 2), 0xFF3A3A3A);

            double percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
            drawRect((int) barX, (int) barY, (int) (barX + barW * percent), (int) (barY + 2), COLOR_ACCENT);

            String valStr = String.format("%.2f", setting.getValue());
            int vw = fontRendererObj.getStringWidth(valStr);
            fontRendererObj.drawString(valStr, (int) (x + w - vw - 8), (int) (y + 2), COLOR_TEXT_DIM);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Frame frame : frames) {
            if (frame.isMouseOverHeader(mouseX, mouseY)) {
                if (mouseButton == 1) {
                    frame.collapsed = !frame.collapsed;
                } else {
                    frame.startDrag(mouseX, mouseY);
                }
                return;
            }

            if (frame.collapsed) continue;

            double rowY = frame.y + frame.getHeaderHeight();
            for (ModuleComponent comp : frame.components) {
                double rowH = comp.getRowHeight();
                if (isOver(mouseX, mouseY, frame.x, rowY, frame.width, rowH)) {
                    boolean clickedArrow = mouseX >= frame.x + frame.width - 10 && !comp.module.getSettings().isEmpty();
                    if (clickedArrow) {
                        comp.settingsOpen = !comp.settingsOpen;
                    } else {
                        comp.module.toggle();
                    }
                    return;
                }
                rowY += rowH;

                if (comp.settingsOpen) {
                    for (Setting setting : comp.module.getSettings()) {
                        double sh = comp.getSettingRowHeight();
                        if (isOver(mouseX, mouseY, frame.x, rowY, frame.width, sh)) {
                            handleSettingClick(setting, mouseX, frame.x, frame.width);
                            if (setting.getType() == Setting.Type.SLIDER) {
                                comp.draggingSlider = true;
                                comp.activeDragSetting = setting;
                            }
                            return;
                        }
                        rowY += sh;
                    }
                }
            }
        }
    }

    private void handleSettingClick(Setting setting, int mouseX, double frameX, double frameW) {
        if (setting.getType() == Setting.Type.TOGGLE) {
            setting.toggle();
        } else {
            updateSliderFromMouse(setting, mouseX, frameX, frameW);
        }
    }

    private void updateSliderFromMouse(Setting setting, int mouseX, double frameX, double frameW) {
        double barX = frameX + 8;
        double barW = frameW - 16;
        double percent = (mouseX - barX) / barW;
        percent = Math.max(0, Math.min(1, percent));
        double value = setting.getMin() + percent * (setting.getMax() - setting.getMin());
        // snap to increment
        double inc = setting.getIncrement();
        if (inc > 0) value = Math.round(value / inc) * inc;
        setting.setValue(value);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (Frame frame : frames) {
            if (frame.dragging) {
                frame.updateDrag(mouseX, mouseY);
                return;
            }
            for (ModuleComponent comp : frame.components) {
                if (comp.draggingSlider && comp.activeDragSetting != null) {
                    updateSliderFromMouse(comp.activeDragSetting, mouseX, frame.x, frame.width);
                    return;
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Frame frame : frames) {
            frame.stopDrag();
            for (ModuleComponent comp : frame.components) {
                comp.draggingSlider = false;
                comp.activeDragSetting = null;
            }
        }
    }

    private boolean isOver(double mouseX, double mouseY, double x, double y, double w, double h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
