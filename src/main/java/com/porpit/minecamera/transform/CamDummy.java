package com.porpit.minecamera.transform;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class CamDummy extends DummyModContainer {
	public static final String modid = "MineCameraDummy";
	public static final String version = "1.0.0";

	public CamDummy() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = modid;
		meta.name = "MineCameraDummy";
		meta.version = version;
		meta.credits = "none";
		meta.authorList.add("none");
		meta.description = "";
		meta.url = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {

		bus.register(this);
		return true;
	}
}