package com.amongus.client; 
import java.util.ArrayList; 
import java.util.List; 
import com.amongus.client.modules.Module; 
 
public class ModuleManager { 
    public static List<Module> modules = new ArrayList<>(); 
    public static void register(Module m) { modules.add(m); } 
} 
