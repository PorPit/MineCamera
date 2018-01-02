package com.porpit.minecamera.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.porpit.minecamera.MineCamera;

import net.minecraft.client.Minecraft;

public class VideoMemoryCleaner extends Thread {
	public static Set<Integer> activeTexture = new HashSet<Integer>();

	public VideoMemoryCleaner() {
	}

	@Override
	public void run() {
		while (true) {
			// 5分钟自动清理一次
			// this.sleep(300000);
			try {
				this.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("[MineCamera]清理显存开始");
			Iterator<Map.Entry<String, Integer>> entries = PictureFactory.loadedPicture.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, Integer> entry = entries.next();
				if (!activeTexture.contains(entry.getValue())) {
					System.out.println("清理显存：" + entry.getKey());
					entries.remove();
				}
			}
			System.out.println("[MineCamera]清理显存结束");
			activeTexture.clear();
		}
	}
}
