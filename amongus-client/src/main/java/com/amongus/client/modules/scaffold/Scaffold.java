package com.amongus.client.modules.scaffold; 
import com.amongus.client.input.InputSimulator; 
import com.amongus.client.modules.Module; 
import com.amongus.client.modules.Category; 
import java.awt.event.KeyEvent; 
 
public class Scaffold extends Module { 
    public enum RotationMode { NONE, DEFAULT, BACKWARDS, SIDEWAYS, GODBRIDGE, SMOOTH, HYPIXEL, SNAP, 3FMC, SNAP2, HPYX2 } 
    public enum TowerMode { NONE, VANILLA, EXTRA, TELLY } 
    public enum KeepYMode { NONE, VANILLA, EXTRA, TELLY, EXTRATELLY } 
 
    private RotationMode rotationMode = RotationMode.SMOOTH; 
    private TowerMode towerMode = TowerMode.TELLY; 
    private KeepYMode keepY = KeepYMode.TELLY; 
    private boolean tellySafe = true; 
    private int tellyStuckDelay = 4; 
    private boolean hypixeltower = true; 
 
    public Scaffold() { super("Scaffold", Category.MOVEMENT); } 
 
    @Override 
    public void onUpdate() { 
        if (mc.thePlayer == null) return; 
        if (towerMode == TowerMode.TELLY) { 
                InputSimulator.holdKey(KeyEvent.VK_SPACE, 380); 
            } 
        } 
                InputSimulator.clickRight(); 
            } 
        } 
    } 
    @Override public void onRender3D(float pt) {} 
    @Override public void onRender() {} 
} 
