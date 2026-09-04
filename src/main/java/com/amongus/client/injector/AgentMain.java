package com.amongus.client.injector; 
import java.lang.instrument.Instrumentation; 
public class AgentMain { 
    public static void premain(String args, Instrumentation inst) {} 
    public static void agentmain(String args, Instrumentation inst) { premain(args, inst); } 
} 
