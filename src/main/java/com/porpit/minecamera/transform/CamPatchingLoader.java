package com.porpit.minecamera.transform;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.10.2")
public class CamPatchingLoader implements IFMLLoadingPlugin {

	public static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { CamTransformer.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return CamDummy.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
		TransformerNames.obfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}