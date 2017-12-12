package com.porpit.minecamera.client;

import com.porpit.minecamera.block.BlockLoader;
import com.porpit.minecamera.item.ItemLoader;

public class ItemRenderLoader {
	public ItemRenderLoader() {
		BlockLoader.registerRenders();
		ItemLoader.registerRenders();
	}
}