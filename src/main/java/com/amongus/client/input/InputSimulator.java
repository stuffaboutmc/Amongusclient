package com.amongus.client.input; 
import java.awt.*; 
import java.awt.event.InputEvent; 
import java.awt.event.KeyEvent; 
import java.util.concurrent.ConcurrentLinkedQueue; 
 
public class InputSimulator { 
    private static final Robot robot; 
    private static final ConcurrentLinkedQueue<Runnable> inputQueue = new ConcurrentLinkedQueue<>(); 
    private static final Thread dispatcher; 
 
    static { 
        try { robot = new Robot(); } catch (AWTException e) { throw new RuntimeException(e); } 
        dispatcher = new Thread(() -> { 
            while (true) { 
                Runnable task = inputQueue.poll(); 
                if (task != null) task.run(); 
                try { Thread.sleep(1); } catch (InterruptedException ignored) {} 
            } 
        }); 
        dispatcher.setDaemon(true); 
        dispatcher.start(); 
    } 
 
    public static void pressKey(int keyCode) { 
        if (GraphicsEnvironment.isHeadless()) return; 
        inputQueue.add(() -> { robot.keyPress(keyCode); robot.delay(10); robot.keyRelease(keyCode); }); 
    } 
 
    public static void holdKey(int keyCode, long ms) { 
        inputQueue.add(() -> { robot.keyPress(keyCode); robot.delay((int) ms); robot.keyRelease(keyCode); }); 
    } 
 
    public static void clickLeft() { 
        inputQueue.add(() -> { robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); robot.delay(5); robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); }); 
    } 
 
    public static void clickRight() { 
        inputQueue.add(() -> { robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); robot.delay(5); robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); }); 
    } 
 
    public static void moveMouse(int x, int y) { inputQueue.add(() -> robot.mouseMove(x, y)); } 
 
    public static void moveMouseRelative(int dx, int dy) { 
        Point p = MouseInfo.getPointerInfo().getLocation(); 
        moveMouse(p.x + dx, p.y + dy); 
    } 
} 
