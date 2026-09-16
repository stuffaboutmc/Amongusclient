package myau.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import myau.client.alt.AltManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class AltManagerScreen extends GuiScreen {

    private GuiTextField usernameField;
    private int panelX, panelY, panelW = 400, panelH = 300;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private int hoveredIndex = -1;
    private int mouseX, mouseY;
    private String statusMessage = "";
    private int statusTimer = 0;

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        panelX = sw / 2 - panelW / 2;
        panelY = sh / 2 - panelH / 2;

        usernameField = new GuiTextField(0, mc.fontRendererObj, panelX + 20, panelY + 40, panelW - 40, 20);
        usernameField.setMaxStringLength(32);
        usernameField.setFocused(true);
        usernameField.setEnableBackgroundDrawing(true);
        usernameField.setText("");
        updateMaxScroll();
    }

    private void updateMaxScroll() {
        List<AltManager.AltEntry> alts = AltManager.getAlts();
        int contentH = alts.size() * 26;
        int visibleH = panelH - 120;
        maxScroll = Math.max(0, contentH - visibleH);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        panelX = sw / 2 - panelW / 2;
        panelY = sh / 2 - panelH / 2;

        ClickGuiRenderer.drawRect(0, 0, sw, sh, 0xCC000000);

        drawRoundedRect(panelX, panelY, panelW, panelH, 10, 0xE01A1A1A);

        if (statusTimer > 0) {
            statusTimer--;
            int statusAlpha = Math.min(255, statusTimer * 4);
            mc.fontRendererObj.drawStringWithShadow(statusMessage,
                sw / 2f - mc.fontRendererObj.getStringWidth(statusMessage) / 2f,
                panelY + panelH + 8, (statusAlpha << 24) | 0xFF44FF44);
        }

        mc.fontRendererObj.drawStringWithShadow("ALT MANAGER",
            sw / 2f - mc.fontRendererObj.getStringWidth("ALT MANAGER") / 2f,
            panelY + 14, 0xFFE60000);

        if (usernameField != null) {
            usernameField.xPosition = panelX + 20;
            usernameField.yPosition = panelY + 40;
            usernameField.drawTextBox();
        }

        int addBtnX = panelX + panelW - 70;
        int addBtnY = panelY + 38;
        int addBtnW = 50;
        int addBtnH = 22;
        boolean addHovered = mouseX >= addBtnX && mouseX <= addBtnX + addBtnW
            && mouseY >= addBtnY && mouseY <= addBtnY + addBtnH;
        drawRoundedRect(addBtnX, addBtnY, addBtnW, addBtnH, 4, addHovered ? 0xFFE60000 : 0xFFAA0000);
        mc.fontRendererObj.drawStringWithShadow("Add",
            addBtnX + addBtnW / 2f - mc.fontRendererObj.getStringWidth("Add") / 2f,
            addBtnY + 7, 0xFFFFFFFF);

        List<AltManager.AltEntry> alts = AltManager.getAlts();
        int contentY = panelY + 68;
        int contentH = panelH - 110;
        hoveredIndex = -1;

        GlStateManager.enableBlend();
        ScaledResolution clipSr = new ScaledResolution(mc);
        float scale = clipSr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        org.lwjgl.opengl.GL11.glScissor(
            (int) ((panelX + 4) * scale),
            (int) ((sh / clipSr.getScaleFactor() - (contentY + contentH)) * scale),
            (int) ((panelW - 8) * scale),
            (int) (contentH * scale)
        );

        for (int i = 0; i < alts.size(); i++) {
            int rowY = contentY + i * 26 - scrollOffset;
            if (rowY + 26 < contentY || rowY > contentY + contentH) continue;

            AltManager.AltEntry entry = alts.get(i);
            boolean rowHovered = mouseX >= panelX + 10 && mouseX <= panelX + panelW - 10
                && mouseY >= rowY && mouseY <= rowY + 22;
            if (rowHovered) hoveredIndex = i;

            int rowBg = rowHovered ? 0xFF2A2A2A : 0xFF222222;
            drawRoundedRect(panelX + 10, rowY, panelW - 20, 22, 4, rowBg);

            mc.fontRendererObj.drawStringWithShadow(entry.name != null ? entry.name : "Unknown",
                panelX + 18, rowY + 7, entry.cracked ? 0xFFAAAAAA : 0xFFFFFFFF);

            int loginBtnX = panelX + panelW - 140;
            int loginBtnW = 50;
            boolean loginHovered = mouseX >= loginBtnX && mouseX <= loginBtnX + loginBtnW
                && mouseY >= rowY + 3 && mouseY <= rowY + 19;
            drawRoundedRect(loginBtnX, rowY + 3, loginBtnW, 16, 3, loginHovered ? 0xFF228822 : 0xFF1A6B1A);
            mc.fontRendererObj.drawStringWithShadow("Login",
                loginBtnX + loginBtnW / 2f - mc.fontRendererObj.getStringWidth("Login") / 2f,
                rowY + 7, 0xFFFFFFFF);

            int removeBtnX = panelX + panelW - 80;
            int removeBtnW = 50;
            boolean removeHovered = mouseX >= removeBtnX && mouseX <= removeBtnX + removeBtnW
                && mouseY >= rowY + 3 && mouseY <= rowY + 19;
            drawRoundedRect(removeBtnX, rowY + 3, removeBtnW, 16, 3, removeHovered ? 0xFFCC2222 : 0xFF881111);
            mc.fontRendererObj.drawStringWithShadow("Remove",
                removeBtnX + removeBtnW / 2f - mc.fontRendererObj.getStringWidth("Remove") / 2f,
                rowY + 7, 0xFFFFFFFF);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        int backBtnX = panelX + 10;
        int backBtnY = panelY + panelH - 36;
        int backBtnW = 70;
        int backBtnH = 24;
        boolean backHovered = mouseX >= backBtnX && mouseX <= backBtnX + backBtnW
            && mouseY >= backBtnY && mouseY <= backBtnY + backBtnH;
        drawRoundedRect(backBtnX, backBtnY, backBtnW, backBtnH, 4, backHovered ? 0xFF444444 : 0xFF333333);
        mc.fontRendererObj.drawStringWithShadow("Back",
            backBtnX + backBtnW / 2f - mc.fontRendererObj.getStringWidth("Back") / 2f,
            backBtnY + 8, 0xFFAAAAAA);

        String countStr = alts.size() + " alt(s)";
        mc.fontRendererObj.drawStringWithShadow(countStr,
            panelX + panelW - mc.fontRendererObj.getStringWidth(countStr) - 14,
            panelY + panelH - 30, 0xFF666666);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawRoundedRect(int x, int y, int w, int h, int r, int color) {
        ClickGuiRenderer.drawRect(x + r, y, x + w - r, y + h, color);
        ClickGuiRenderer.drawRect(x, y + r, x + w, y + h - r, color);
        ClickGuiRenderer.drawRect(x, y, x + r, y + r, color);
        ClickGuiRenderer.drawRect(x + w - r, y, x + w, y + r, color);
        ClickGuiRenderer.drawRect(x, y + h - r, x + r, y + h, color);
        ClickGuiRenderer.drawRect(x + w - r, y + h - r, x + w, y + h, color);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (usernameField != null) {
            usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (mouseButton != 0) return;

        List<AltManager.AltEntry> alts = AltManager.getAlts();

        int contentY = panelY + 68;
        for (int i = 0; i < alts.size(); i++) {
            int rowY = contentY + i * 26 - scrollOffset;

            int loginBtnX = panelX + panelW - 140;
            int loginBtnW = 50;
            if (mouseX >= loginBtnX && mouseX <= loginBtnX + loginBtnW
                && mouseY >= rowY + 3 && mouseY <= rowY + 19) {
                loginAlt(i);
                return;
            }

            int removeBtnX = panelX + panelW - 80;
            int removeBtnW = 50;
            if (mouseX >= removeBtnX && mouseX <= removeBtnX + removeBtnW
                && mouseY >= rowY + 3 && mouseY <= rowY + 19) {
                AltManager.remove(i);
                updateMaxScroll();
                scrollOffset = Math.min(scrollOffset, maxScroll);
                showStatus("Alt removed");
                return;
            }
        }

        int addBtnX = panelX + panelW - 70;
        int addBtnY = panelY + 38;
        int addBtnW = 50;
        int addBtnH = 22;
        if (mouseX >= addBtnX && mouseX <= addBtnX + addBtnW
            && mouseY >= addBtnY && mouseY <= addBtnY + addBtnH) {
            addAlt();
            return;
        }

        int backBtnX = panelX + 10;
        int backBtnY = panelY + panelH - 36;
        int backBtnW = 70;
        int backBtnH = 24;
        if (mouseX >= backBtnX && mouseX <= backBtnX + backBtnW
            && mouseY >= backBtnY && mouseY <= backBtnY + backBtnH) {
            mc.displayGuiScreen(new MainMenu());
            return;
        }
    }

    private void addAlt() {
        String username = usernameField != null ? usernameField.getText().trim() : "";
        if (username.isEmpty()) {
            showStatus("Enter a username");
            return;
        }
        AltManager.add(username, "", true);
        usernameField.setText("");
        updateMaxScroll();
        showStatus("Added: " + username);
    }

    private void loginAlt(int index) {
        List<AltManager.AltEntry> alts = AltManager.getAlts();
        if (index < 0 || index >= alts.size()) return;
        AltManager.AltEntry entry = alts.get(index);
        String newUsername = entry.name;
        try {
            net.minecraft.client.Minecraft mcInst = Minecraft.getMinecraft();
            net.minecraft.util.Session session = mcInst.getSession();

            java.lang.reflect.Field usernameField = null;
            String[] fieldNames = {"field_74286_b", "username", "field_146421_c", "playerUsername"};
            for (String name : fieldNames) {
                try {
                    usernameField = net.minecraft.util.Session.class.getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException ignored) {}
            }

            if (usernameField == null) {
                showStatus("Could not find username field");
                return;
            }

            usernameField.setAccessible(true);

            try {
                java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(usernameField, usernameField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            } catch (Exception ignored) {}

            usernameField.set(session, newUsername);
            showStatus("Logged in as: " + newUsername + " (rejoin to apply)");
        } catch (Exception e) {
            showStatus("Login failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private void showStatus(String msg) {
        statusMessage = msg;
        statusTimer = 100;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (usernameField != null && usernameField.isFocused()) {
            usernameField.textboxKeyTyped(typedChar, keyCode);
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new MainMenu());
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = org.lwjgl.input.Mouse.getDWheel();
        if (dWheel != 0) {
            scrollOffset -= dWheel > 0 ? 26 : -26;
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (usernameField != null) {
            usernameField.updateCursorCounter();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
