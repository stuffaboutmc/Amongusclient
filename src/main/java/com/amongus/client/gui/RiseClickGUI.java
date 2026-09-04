package com.amongus.client.gui; 
import net.minecraft.client.gui.GuiScreen; 
import java.awt.*; 
public class RiseClickGUI extends GuiScreen { 
    @Override 
    public void drawScreen(int mx, int my, float pt) { 
        Graphics2D g = (Graphics2D) mc.getTextureManager().getGraphics(); 
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
    } 
} 
