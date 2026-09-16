package myau.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.audio.PositionedSoundRecord;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenu extends GuiScreen {

    private int currentTheme = 0;
    private static final int THEME_COUNT = 4;
    private static final String[] THEME_NAMES = {"Augustus", "Sunset", "Funny", "Mountain"};

    private float animTime = 0f;
    private long lastTime = System.currentTimeMillis();
    private int screenW, screenH;
    private Random rand = new Random();

    private float transitionAlpha = 0f;
    private int transitionTarget = -1;
    private float transitionProgress = 1f;

    private List<Particle> particles = new ArrayList<>();
    private List<Cloud> clouds = new ArrayList<>();
    private List<Star> stars = new ArrayList<>();
    private List<Snowflake> snowflakes = new ArrayList<>();
    private List<FunnyThing> funnyThings = new ArrayList<>();
    private List<Confetti> confetti = new ArrayList<>();

    private int mouseX, mouseY;
    private float funnyHue = 0f;
    private float sunsetHue = 0f;

    private static final int TITLE_COLOR = 0xFFE60000;

    private static class Particle {
        float x, y, vx, vy, life, maxLife, size;
        int color;
        Particle(float x, float y, float vx, float vy, float life, float size, int color) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy;
            this.life = life; this.maxLife = life; this.size = size; this.color = color;
        }
    }

    private static class Cloud {
        float x, y, speed, width, height;
        Cloud(float x, float y, float speed, float w, float h) {
            this.x = x; this.y = y; this.speed = speed; this.width = w; this.height = h;
        }
    }

    private static class Star {
        float x, y, brightness, twinkleSpeed, size;
        Star(float x, float y) {
            this.x = x; this.y = y;
            this.brightness = 0.3f + new Random().nextFloat() * 0.7f;
            this.twinkleSpeed = 0.5f + new Random().nextFloat() * 2f;
            this.size = 0.5f + new Random().nextFloat() * 1.5f;
        }
    }

    private static class Snowflake {
        float x, y, speed, wobble, size, wobblePhase;
        Snowflake(float x, float y) {
            this.x = x; this.y = y;
            this.speed = 0.2f + new Random().nextFloat() * 0.5f;
            this.wobble = 0.3f + new Random().nextFloat() * 0.7f;
            this.size = 1f + new Random().nextFloat() * 2f;
            this.wobblePhase = (float)(new Random().nextFloat() * Math.PI * 2);
        }
    }

    private static class FunnyThing {
        float x, y, vx, vy, life, maxLife, rotation, rotSpeed, scale;
        String text;
        FunnyThing(float x, float y, String text) {
            this.x = x; this.y = y; this.text = text;
            this.vx = (float)(Math.random() * 2 - 1);
            this.vy = -1f - (float)(Math.random() * 2);
            this.life = 120f + (float)(Math.random() * 120);
            this.maxLife = life;
            this.rotation = (float)(Math.random() * 360);
            this.rotSpeed = (float)(Math.random() * 10 - 5);
            this.scale = 0.5f + (float)(Math.random() * 1.0f);
        }
    }

    private static class Confetti {
        float x, y, vx, vy, life, maxLife, rotation, rotSpeed, size;
        int color;
        Confetti(float x, float y, int color) {
            this.x = x; this.y = y; this.color = color;
            this.vx = (float)(Math.random() * 8 - 4);
            this.vy = -4f - (float)(Math.random() * 6);
            this.life = 60f + (float)(Math.random() * 60);
            this.maxLife = life;
            this.rotation = (float)(Math.random() * 360);
            this.rotSpeed = (float)(Math.random() * 20 - 10);
            this.size = 2f + (float)(Math.random() * 4);
        }
    }

    private MenuButton singleplayerBtn, multiplayerBtn, altManagerBtn, optionsBtn, quitBtn;
    private MenuButton lastHoveredBtn = null;

    private class MenuButton {
        int x, y, w, h;
        String label;
        Runnable action;
        float hoverAnim = 0f;

        MenuButton(int x, int y, int w, int h, String label, Runnable action) {
            this.x = x; this.y = y; this.w = w; this.h = h;
            this.label = label; this.action = action;
        }

        boolean isHovered(int mx, int my) {
            return mx >= x && mx <= x + w && my >= y && my <= y + h;
        }
    }

    public MainMenu() {
        singleplayerBtn = new MenuButton(0, 0, 180, 32, "Singleplayer", () -> {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        });
        multiplayerBtn = new MenuButton(0, 0, 180, 32, "Multiplayer", () -> {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        });
        altManagerBtn = new MenuButton(0, 0, 180, 32, "Alt Manager", () -> {
            mc.displayGuiScreen(new AltManagerScreen());
        });
        optionsBtn = new MenuButton(0, 0, 180, 32, "Options", () -> {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        });
        quitBtn = new MenuButton(0, 0, 180, 32, "Quit", () -> {
            mc.shutdown();
        });

        initParticles();
    }

    private void initParticles() {
        particles.clear();
        clouds.clear();
        stars.clear();
        snowflakes.clear();
        funnyThings.clear();
        confetti.clear();
    }

    private void initThemeParticles() {
        initParticles();
        if (screenW <= 0 || screenH <= 0) return;

        switch (currentTheme) {
            case 0:
                for (int i = 0; i < 60; i++) {
                    particles.add(new Particle(
                        rand.nextFloat() * screenW,
                        rand.nextFloat() * screenH,
                        0f, -0.3f - rand.nextFloat() * 0.7f,
                        300 + rand.nextInt(300),
                        1.5f + rand.nextFloat() * 2.5f,
                        0xFF6C00B4
                    ));
                }
                break;
            case 1:
                for (int i = 0; i < 8; i++) {
                    clouds.add(new Cloud(
                        rand.nextFloat() * screenW,
                        40 + rand.nextFloat() * (screenH * 0.5f),
                        0.15f + rand.nextFloat() * 0.3f,
                        60 + rand.nextFloat() * 100,
                        20 + rand.nextFloat() * 20
                    ));
                }
                break;
            case 2:
                String[] texts = {"XD", "bruh", "skill issue", "gg", "lmao", "sus",
                    "( \u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B",
                    "epic", "no cap", "bussin", "*bwoing*", "amogus", "when the impostor is sus",
                    "100%", "OMG", "wait what", "hol up", "poggers", "sheeeesh"};
                for (int i = 0; i < 12; i++) {
                    funnyThings.add(new FunnyThing(
                        rand.nextFloat() * screenW,
                        rand.nextFloat() * screenH,
                        texts[rand.nextInt(texts.length)]
                    ));
                }
                break;
            case 3:
                for (int i = 0; i < 120; i++) {
                    stars.add(new Star(rand.nextFloat() * screenW, rand.nextFloat() * screenH * 0.7f));
                }
                for (int i = 0; i < 80; i++) {
                    snowflakes.add(new Snowflake(rand.nextFloat() * screenW, rand.nextFloat() * screenH));
                }
                break;
        }
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        screenW = sr.getScaledWidth();
        screenH = sr.getScaledHeight();
        initThemeParticles();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        long now = System.currentTimeMillis();
        float dt = (now - lastTime) / 16.667f;
        lastTime = now;
        animTime += partialTicks * 0.05f;

        ScaledResolution sr = new ScaledResolution(mc);
        screenW = sr.getScaledWidth();
        screenH = sr.getScaledHeight();

        if (transitionProgress < 1f) {
            transitionProgress = Math.min(1f, transitionProgress + partialTicks * 0.06f);
            if (transitionProgress >= 1f && transitionTarget >= 0) {
                currentTheme = transitionTarget;
                transitionTarget = -1;
                initThemeParticles();
            }
        }

        updateButtonPositions();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        switch (currentTheme) {
            case 0: drawAugustusTheme(partialTicks); break;
            case 1: drawSunsetTheme(partialTicks); break;
            case 2: drawFunnyTheme(partialTicks); break;
            case 3: drawMountainTheme(partialTicks); break;
        }

        if (transitionProgress < 1f) {
            int alpha = (int)(255 * (1f - transitionProgress));
            ClickGuiRenderer.drawRect(0, 0, screenW, screenH, (alpha << 24) | 0x000000);
        }

        drawThemeSelector();

        mc.fontRendererObj.drawStringWithShadow("among us client Premium v1.0.0",
            screenW / 2 - mc.fontRendererObj.getStringWidth("among us client Premium v1.0.0") / 2,
            screenH - 14, 0x80FFFFFF);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void updateButtonPositions() {
        int btnW = 180;
        int btnH = 32;
        int spacing = 8;
        int totalH = btnH * 5 + spacing * 4;
        int startY = screenH / 2 - totalH / 2 + 20;
        int btnX = screenW / 2 - btnW / 2;

        singleplayerBtn.x = btnX; singleplayerBtn.y = startY;
        multiplayerBtn.x = btnX; multiplayerBtn.y = startY + btnH + spacing;
        altManagerBtn.x = btnX; altManagerBtn.y = startY + (btnH + spacing) * 2;
        optionsBtn.x = btnX; optionsBtn.y = startY + (btnH + spacing) * 3;
        quitBtn.x = btnX; quitBtn.y = startY + (btnH + spacing) * 4;
    }

    private void drawMenuButtons() {
        MenuButton[] btns = {singleplayerBtn, multiplayerBtn, altManagerBtn, optionsBtn, quitBtn};
        for (MenuButton btn : btns) {
            boolean hovered = btn.isHovered(mouseX, mouseY);

            int bgColor, textColor;
            float bounceY = 0;

            if (currentTheme == 2) {
                int hue = (int)((animTime * 50 + btn.y) % 360);
                bgColor = hsba(hue, 0.7f, 0.4f, 200);
                textColor = 0xFFFFFFFF;
                if (hovered) {
                    bounceY = (float)Math.sin(animTime * 20 + btn.x) * 3;
                    bgColor = hsba(hue, 0.9f, 0.6f, 230);
                }
            } else if (currentTheme == 1) {
                bgColor = hovered ? 0xE0FFFFFF : 0x90FFFFFF;
                textColor = 0xFF333333;
            } else if (currentTheme == 3) {
                bgColor = hovered ? 0xE0C8D8F0 : 0x808899BB;
                textColor = 0xFFE8F0FF;
            } else {
                bgColor = hovered ? 0xE06C00B4 : 0xB03A0060;
                textColor = 0xFFFFFFFF;
            }

            if (hovered) btn.hoverAnim = Math.min(1f, btn.hoverAnim + 0.08f);
            else btn.hoverAnim = Math.max(0f, btn.hoverAnim - 0.08f);

            int drawY = btn.y + (int)bounceY;

            if (currentTheme == 0) {
                drawAugustusButton(btn.x, drawY, btn.w, btn.h, bgColor, btn.hoverAnim);
            } else {
                drawRoundedRect(btn.x, drawY, btn.w, btn.h, 6, bgColor);
            }

            drawCenteredString(mc.fontRendererObj, btn.label,
                btn.x + btn.w / 2, drawY + (btn.h - 8) / 2, textColor);

            if (hovered && !btn.equals(lastHoveredBtn)) {
                mc.getSoundHandler().playSound(
                    PositionedSoundRecord.create(
                        new ResourceLocation("random.button")));
                lastHoveredBtn = btn;
            }
        }
    }

    private void drawAugustusButton(int x, int y, int w, int h, int baseColor, float hover) {
        int radius = 6;
        ClickGuiRenderer.drawRect(x + radius, y, x + w - radius, y + h, baseColor);
        ClickGuiRenderer.drawRect(x, y + radius, x + w, y + h - radius, baseColor);
        ClickGuiRenderer.drawRect(x, y, x + radius, y + radius, baseColor);
        ClickGuiRenderer.drawRect(x + w - radius, y, x + w, y + radius, baseColor);
        ClickGuiRenderer.drawRect(x, y + h - radius, x + radius, y + h, baseColor);
        ClickGuiRenderer.drawRect(x + w - radius, y + h - radius, x + w, y + h, baseColor);

        if (hover > 0.1f) {
            int glowAlpha = (int)(60 * hover);
            int glowColor = (glowAlpha << 24) | 0x9C33FF;
            drawGlowRect(x - 2, y - 2, x + w + 2, y + h + 2, glowColor, 4);
        }
    }

    private void drawGlowRect(int x1, int y1, int x2, int y2, int color, int spread) {
        for (int i = spread; i > 0; i--) {
            int a = (color >> 24 & 255) / (i + 1);
            int c = (a << 24) | (color & 0x00FFFFFF);
            drawRoundedRect(x1 - i, y1 - i, (x2 - x1) + i * 2, (y2 - y1) + i * 2, 8, c);
        }
    }

    private void drawRoundedRect(int x, int y, int w, int h, int r, int color) {
        ClickGuiRenderer.drawRect(x + r, y, x + w - r, y + h, color);
        ClickGuiRenderer.drawRect(x, y + r, x + w, y + h - r, color);
        ClickGuiRenderer.drawRect(x, y, x + r, y + r, color);
        ClickGuiRenderer.drawRect(x + w - r, y, x + w, y + r, color);
        ClickGuiRenderer.drawRect(x, y + h - r, x + r, y + h, color);
        ClickGuiRenderer.drawRect(x + w - r, y + h - r, x + w, y + h, color);
    }

    private void drawThemeSelector() {
        int itemW = 24;
        int itemH = 20;
        int gap = 4;
        int totalH = THEME_COUNT * itemH + (THEME_COUNT - 1) * gap;
        int startX = screenW - 12 - itemW;
        int startY = screenH / 2 - totalH / 2;

        ClickGuiRenderer.drawRect(startX - 6, startY - 6, startX + itemW + 6, startY + totalH + 6, 0x60000000);

        for (int i = 0; i < THEME_COUNT; i++) {
            int iy = startY + i * (itemH + gap);
            boolean isActive = (i == currentTheme);
            boolean isHovered = mouseX >= startX && mouseX <= startX + itemW && mouseY >= iy && mouseY <= iy + itemH;

            int bg;
            switch (i) {
                case 0: bg = isActive ? 0xFF6C00B4 : (isHovered ? 0xFF8B3DC6 : 0xFF3A0060); break;
                case 1: bg = isActive ? 0xFFFF8C42 : (isHovered ? 0xFFFFAA66 : 0xFFCC6633); break;
                case 2: bg = isActive ? hsba((int)(animTime * 100) % 360, 0.8f, 0.5f, 255)
                        : hsba((int)(animTime * 100) % 360, 0.5f, 0.3f, 200); break;
                case 3: bg = isActive ? 0xFF4466AA : (isHovered ? 0xFF5577BB : 0xFF223355); break;
                default: bg = 0xFF333333;
            }

            drawRoundedRect(startX, iy, itemW, itemH, 4, bg);

            if (isActive) {
                ClickGuiRenderer.drawRect(startX - 2, iy + 2, startX, iy + itemH - 2, 0xFFFFFFFF);
            }

            mc.fontRendererObj.drawStringWithShadow(THEME_NAMES[i], startX - mc.fontRendererObj.getStringWidth(THEME_NAMES[i]) - 6, iy + (itemH - 8) / 2f, isActive ? 0xFFFFFFFF : 0xAAFFFFFF);
        }
    }

    private void drawTitle(int color) {
        String title = "AMONG US CLIENT";
        mc.fontRendererObj.drawStringWithShadow(title,
            screenW / 2 - mc.fontRendererObj.getStringWidth(title) / 2f,
            30,
            TITLE_COLOR);
    }

    private void drawScanlines() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        for (int y = 0; y < screenH; y += 3) {
            ClickGuiRenderer.drawRect(0, y, screenW, y + 1, 0x15000000);
        }
        GlStateManager.enableTexture2D();
    }

    private int hsba(int h, float s, float b, int a) {
        float c = b * s;
        float x = c * (1 - Math.abs((h / 60f) % 2 - 1));
        float m = b - c;
        float r = 0, g = 0, bl = 0;
        if (h < 60) { r = c; g = x; }
        else if (h < 120) { r = x; g = c; }
        else if (h < 180) { g = c; bl = x; }
        else if (h < 240) { g = x; bl = c; }
        else if (h < 300) { r = x; bl = c; }
        else { r = c; bl = x; }
        return (a << 24) | ((int)((r + m) * 255) << 16) | ((int)((g + m) * 255) << 8) | (int)((bl + m) * 255);
    }

    // ========== THEME 0: AUGUSTUS ==========
    private void drawAugustusTheme(float pt) {
        drawGradientRect(0, 0, screenW, screenH, 0xFF1A0A2E, 0xFF0D0520);
        drawGradientRect(0, screenH / 2, screenW, screenH, 0xFF0D0520, 0xFF050210);

        List<Particle> toRemove = new ArrayList<>();
        for (Particle p : particles) {
            p.x += p.vx;
            p.y += p.vy;
            p.life -= 1;
            if (p.life <= 0 || p.y < -10) {
                p.x = rand.nextFloat() * screenW;
                p.y = screenH + 10;
                p.life = p.maxLife;
            }
            float alpha = Math.min(1f, p.life / 30f) * 0.6f;
            int a = (int)(alpha * 255);
            int c = (a << 24) | 0x9C33FF;
            float size = p.size * (0.8f + 0.2f * (float)Math.sin(animTime * 3 + p.x));
            ClickGuiRenderer.drawRect(
                (int)(p.x - size / 2), (int)(p.y - size / 2),
                (int)(p.x + size / 2), (int)(p.y + size / 2), c);
        }

        drawTitle(0xFFE60000);
        drawMenuButtons();
        drawScanlines();
    }

    // ========== THEME 1: SUNSET HORIZON ==========
    private void drawSunsetTheme(float pt) {
        sunsetHue += 0.02f;
        if (sunsetHue > 360) sunsetHue -= 360;

        int topColor = hsba((int)(sunsetHue + 20) % 360, 0.6f, 0.3f, 255);
        int midColor = hsba((int)(sunsetHue + 10) % 360, 0.7f, 0.5f, 255);
        int botColor = hsba((int)sunsetHue % 360, 0.8f, 0.4f, 255);
        int deepColor = hsba((int)(sunsetHue + 330) % 360, 0.5f, 0.2f, 255);

        drawGradientRect(0, 0, screenW, screenH / 3, topColor, midColor);
        drawGradientRect(0, screenH / 3, screenW, screenH * 2 / 3, midColor, botColor);
        drawGradientRect(0, screenH * 2 / 3, screenW, screenH, botColor, deepColor);

        int sunGlow = 0x30FFCC44;
        int sunCore = 0xCCFFAA33;
        int sunR = 30;
        int sunX = screenW / 2;
        int sunY = screenH / 4;
        for (int i = sunR + 20; i > 0; i -= 2) {
            int a = Math.min(255, (int)(40 * (1 - (float)i / (sunR + 20))));
            ClickGuiRenderer.drawRect(sunX - i, sunY - i, sunX + i, sunY + i,
                (a << 24) | 0xFFCC44);
        }
        drawRoundedRect(sunX - sunR, sunY - sunR, sunR * 2, sunR * 2, sunR, sunCore);

        for (Cloud c : clouds) {
            c.x += c.speed;
            if (c.x > screenW + c.width) c.x = -c.width;
            drawCloud(c.x, c.y, c.width, c.height);
        }

        for (Particle p : particles) {
            p.x += p.vx + (float)Math.sin(animTime + p.y * 0.01) * 0.3f;
            p.y += p.vy;
            p.life -= 1;
            if (p.life <= 0) {
                p.x = rand.nextFloat() * screenW;
                p.y = screenH + 5;
                p.life = p.maxLife;
            }
            float alpha = Math.min(1f, p.life / 30f) * 0.4f;
            int a = (int)(alpha * 255);
            ClickGuiRenderer.drawRect((int)p.x - 1, (int)p.y - 1, (int)p.x + 1, (int)p.y + 1,
                (a << 24) | 0xFFFFDDAA);
        }

        drawTitle(0xFFE60000);
        drawMenuButtons();
    }

    private void drawCloud(float cx, float cy, float w, float h) {
        int cloudColor = 0x40FFFFFF;
        ClickGuiRenderer.drawRect((int)(cx), (int)(cy), (int)(cx + w), (int)(cy + h), cloudColor);
        ClickGuiRenderer.drawRect((int)(cx + w * 0.1f), (int)(cy - h * 0.4f), (int)(cx + w * 0.5f), (int)(cy + h * 0.6f), cloudColor);
        ClickGuiRenderer.drawRect((int)(cx + w * 0.4f), (int)(cy - h * 0.6f), (int)(cx + w * 0.8f), (int)(cy + h * 0.5f), cloudColor);
    }

    // ========== THEME 2: GOOFY ASS FUNNY ==========
    private void drawFunnyTheme(float pt) {
        funnyHue += 3f;
        if (funnyHue >= 360) funnyHue -= 360;

        for (int y = 0; y < screenH; y += 4) {
            int hue = (int)((funnyHue + y * 0.5f) % 360);
            int c = hsba(hue, 0.8f, 0.5f, 255);
            ClickGuiRenderer.drawRect(0, y, screenW, y + 4, c);
        }

        float shakeX = 0, shakeY = 0;
        if (rand.nextInt(100) < 5) {
            shakeX = rand.nextFloat() * 6 - 3;
            shakeY = rand.nextFloat() * 6 - 3;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(shakeX, shakeY, 0);

        if (rand.nextInt(200) < 3) {
            String[] memes = {"When the", "sus impostor", "amogus", "RED SUS", "\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800\u2800",
                "Get out of my head", "GET OUT OF MY HEAD", "AMOGUS",
                "When you vent in front of the homie"};
            funnyThings.add(new FunnyThing(
                rand.nextFloat() * screenW,
                screenH + 10,
                memes[rand.nextInt(memes.length)]
            ));
        }

        for (FunnyThing ft : funnyThings) {
            ft.x += ft.vx;
            ft.y += ft.vy;
            ft.vy += 0.02f;
            ft.life -= 1;
            ft.rotation += ft.rotSpeed;

            float alpha = Math.min(1f, ft.life / 30f);
            int hue = (int)(Math.random() * 360);
            int a = (int)(alpha * 255);
            int color = hsba(hue, 0.9f, 0.8f, a);

            GlStateManager.pushMatrix();
            GlStateManager.translate(ft.x, ft.y, 0);
            GlStateManager.rotate(ft.rotation, 0, 0, 1);
            GlStateManager.scale(ft.scale, ft.scale, 1);
            mc.fontRendererObj.drawStringWithShadow(ft.text,
                -mc.fontRendererObj.getStringWidth(ft.text) / 2f, -4, color);
            GlStateManager.popMatrix();
        }

        funnyThings.removeIf(f -> f.life <= 0 || f.y > screenH + 50);

        if (rand.nextInt(100) < 2) {
            int confettiColor = hsba(rand.nextInt(360), 1f, 0.8f, 255);
            for (int i = 0; i < 15; i++) {
                confetti.add(new Confetti(
                    mouseX + rand.nextFloat() * 40 - 20,
                    mouseY + rand.nextFloat() * 40 - 20,
                    confettiColor
                ));
            }
        }

        for (Confetti cf : confetti) {
            cf.x += cf.vx;
            cf.y += cf.vy;
            cf.vy += 0.15f;
            cf.life -= 1;
            cf.rotation += cf.rotSpeed;
            float alpha = Math.min(1f, cf.life / 20f);
            int a = (int)(alpha * 255);
            int c = (a << 24) | (cf.color & 0x00FFFFFF);
            GlStateManager.pushMatrix();
            GlStateManager.translate(cf.x, cf.y, 0);
            GlStateManager.rotate(cf.rotation, 0, 0, 1);
            ClickGuiRenderer.drawRect((int)(-cf.size / 2), (int)(-cf.size / 4), (int)(cf.size / 2), (int)(cf.size / 4), c);
            GlStateManager.popMatrix();
        }
        confetti.removeIf(c -> c.life <= 0 || c.y > screenH + 50);

        GlStateManager.popMatrix();

        String distortedTitle = generateFunnyTitle();
        float wobble = (float)(Math.sin(animTime * 8) * 5);
        float sizeWobble = 1f + (float)(Math.sin(animTime * 6) * 0.15f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(screenW / 2f, 35, 0);
        GlStateManager.rotate(wobble, 0, 0, 1);
        GlStateManager.scale(sizeWobble, sizeWobble, 1);
        mc.fontRendererObj.drawStringWithShadow(distortedTitle,
            -mc.fontRendererObj.getStringWidth(distortedTitle) / 2f, -6, TITLE_COLOR);
        GlStateManager.popMatrix();

        drawMenuButtons();

        String[] bottomMemes = {"skill issue", "lmao", "gg ez", "noob", "sus",
            "when the impostor is sus", "bruh moment", "epic gamer", "poggers",
            "sheeeesh", "bussin no cap fr fr", "ratio"};
        String memeText = bottomMemes[(int)(animTime * 2) % bottomMemes.length];
        mc.fontRendererObj.drawStringWithShadow(memeText,
            (int)(screenW - (animTime * 30) % (mc.fontRendererObj.getStringWidth(memeText) + screenW)),
            screenH - 28, hsba((int)(animTime * 40) % 360, 0.8f, 0.9f, 200));
    }

    private String generateFunnyTitle() {
        String base = "AMONG US CLIENT";
        StringBuilder sb = new StringBuilder();
        String[] chaos = {"0", "5", "1", "3", "x", "X", "D", "l", "L", "o", "O"};
        for (int i = 0; i < base.length(); i++) {
            if (rand.nextInt(5) == 0) {
                sb.append(chaos[rand.nextInt(chaos.length)]);
            } else {
                sb.append(base.charAt(i));
            }
        }
        String[] suffixes = {" xD", " lmao", " sus", " LOL", " (\u00af\u25C8\u00af)", "!!1!", ""};
        sb.append(suffixes[rand.nextInt(suffixes.length)]);
        return sb.toString();
    }

    // ========== THEME 3: MOUNTAIN ==========
    private void drawMountainTheme(float pt) {
        int skyTop = 0xFF0A0E2A;
        int skyBot = 0xFF1A1840;
        drawGradientRect(0, 0, screenW, screenH, skyTop, skyBot);

        float parallaxX = (float)(mouseX - screenW / 2) * 0.02f;
        float parallaxY = (float)(mouseY - screenH / 2) * 0.01f;

        int moonX = screenW * 3 / 4 + (int)(parallaxX * 0.3f);
        int moonY = 60 + (int)(parallaxY * 0.3f);
        for (int i = 40; i > 0; i -= 2) {
            int a = Math.min(255, (int)(25 * (1 - (float)i / 40)));
            ClickGuiRenderer.drawRect(moonX - i, moonY - i, moonX + i, moonY + i,
                (a << 24) | 0xCCDDFF);
        }
        drawRoundedRect(moonX - 18, moonY - 18, 36, 36, 18, 0xFFDDE8FF);
        drawRoundedRect(moonX - 12, moonY - 20, 10, 8, 5, 0xFFBCC8DD);
        drawRoundedRect(moonX + 2, moonY - 8, 6, 5, 3, 0xFFBCC8DD);

        for (Star s : stars) {
            s.brightness += (float)(Math.sin(animTime * s.twinkleSpeed * 10) * 0.02f);
            s.brightness = Math.max(0.1f, Math.min(1f, s.brightness));
            int a = (int)(s.brightness * 200);
            int c = (a << 24) | 0xCCDDFF;
            ClickGuiRenderer.drawRect((int)s.x - (int)s.size, (int)s.y - (int)s.size,
                (int)s.x + (int)s.size, (int)s.y + (int)s.size, c);
        }

        drawMountainLayer(0.7f, 0xFF1A1840, parallaxX * 0.5f, parallaxY * 0.5f, screenH * 5 / 8);
        drawMountainLayer(0.5f, 0xFF252360, parallaxX * 0.7f, parallaxY * 0.7f, screenH * 6 / 8);
        drawMountainLayer(0.3f, 0xFF302E70, parallaxX * 1.0f, parallaxY * 1.0f, screenH * 7 / 8);

        for (Snowflake sf : snowflakes) {
            sf.y += sf.speed;
            sf.x += (float)(Math.sin(animTime * 2 + sf.wobblePhase) * sf.wobble);
            if (sf.y > screenH + 5) {
                sf.y = -5;
                sf.x = rand.nextFloat() * screenW;
            }
            int a = 180;
            int c = (a << 24) | 0xCCDDFF;
            ClickGuiRenderer.drawRect((int)sf.x - (int)sf.size, (int)sf.y - (int)sf.size,
                (int)sf.x + (int)sf.size, (int)sf.y + (int)sf.size, c);
        }

        for (int y = screenH - 40; y < screenH; y++) {
            float fogAlpha = (float)(y - (screenH - 40)) / 40f * 0.3f;
            int a = (int)(fogAlpha * 255);
            ClickGuiRenderer.drawRect(0, y, screenW, y + 1, (a << 22) | 0x8899BB);
        }

        drawTitle(0xFFE60000);
        drawMenuButtons();
    }

    private void drawMountainLayer(float heightFactor, int color, float px, float py, int baseY) {
        int segments = 20;
        int segW = screenW / segments + 2;
        float[] heights = {0.7f, 0.5f, 0.9f, 0.4f, 0.8f, 0.3f, 0.6f, 1.0f, 0.45f, 0.75f,
            0.55f, 0.85f, 0.35f, 0.65f, 0.95f, 0.4f, 0.7f, 0.5f, 0.8f, 0.6f};

        for (int i = 0; i < segments; i++) {
            float h1 = heights[i % heights.length] * screenH * heightFactor;
            float h2 = heights[(i + 1) % heights.length] * screenH * heightFactor;
            int x1 = (int)(i * segW + px);
            int x2 = (int)((i + 1) * segW + px);
            int y1Top = baseY - (int)h1 + (int)py;
            int y2Top = baseY - (int)h2 + (int)py;

            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GL11.glBegin(GL11.GL_TRIANGLES);
            float a = (color >> 24 & 255) / 255.0f;
            float r = (color >> 16 & 255) / 255.0f;
            float g = (color >> 8 & 255) / 255.0f;
            float b = (color & 255) / 255.0f;
            GL11.glColor4f(r, g, b, a);
            GL11.glVertex3d(x1, baseY + 50, 0);
            GL11.glVertex3d(x1, y1Top, 0);
            GL11.glVertex3d(x2, y2Top, 0);
            GL11.glVertex3d(x2, baseY + 50, 0);
            GL11.glVertex3d(x1, baseY + 50, 0);
            GL11.glVertex3d(x2, y2Top, 0);
            GL11.glEnd();
            GlStateManager.enableTexture2D();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) return;

        MenuButton[] btns = {singleplayerBtn, multiplayerBtn, altManagerBtn, optionsBtn, quitBtn};
        for (MenuButton btn : btns) {
            if (btn.isHovered(mouseX, mouseY)) {
                playClickSound();
                if (currentTheme == 2) {
                    playFunnySound();
                }
                btn.action.run();
                return;
            }
        }

        int itemW = 24;
        int itemH = 20;
        int gap = 4;
        int totalH = THEME_COUNT * itemH + (THEME_COUNT - 1) * gap;
        int startX = screenW - 12 - itemW;
        int startY = screenH / 2 - totalH / 2;
        for (int i = 0; i < THEME_COUNT; i++) {
            int iy = startY + i * (itemH + gap);
            if (mouseX >= startX && mouseX <= startX + itemW && mouseY >= iy && mouseY <= iy + itemH) {
                if (i != currentTheme && transitionProgress >= 1f) {
                    transitionTarget = i;
                    transitionProgress = 0f;
                    playClickSound();
                }
                return;
            }
        }
    }

    private void playClickSound() {
        mc.getSoundHandler().playSound(
            PositionedSoundRecord.create(
                new ResourceLocation("random.button")));
    }

    private void playFunnySound() {
        String[] sounds = {"note.harp", "note.pling", "note.bass", "note.blip", "note.hat"};
        String sound = sounds[rand.nextInt(sounds.length)];
        mc.getSoundHandler().playSound(
            PositionedSoundRecord.create(
                new ResourceLocation(sound)));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
        }
    }
}
