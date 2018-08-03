package com.porpit.minecamera.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyLoader {

	public static KeyBinding cameralock;

    public KeyLoader()
    {
		KeyLoader.cameralock = new KeyBinding("key.minecamera.cameralock", Keyboard.KEY_LCONTROL, "key.categories.mincamera");
        
        ClientRegistry.registerKeyBinding(KeyLoader.cameralock);
    }
}
