package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class ClientSpoofer extends Module {
    public ClientSpoofer() {
        super("ClientSpoofer", Keyboard.KEY_NONE, Category.MISC, "Spoofs client brand.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Client", new String[]{"Vanilla","Forge","Lunar","Badlion"}, "Vanilla"));
    }
}