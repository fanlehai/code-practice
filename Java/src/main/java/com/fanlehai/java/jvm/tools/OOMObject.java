package com.fanlehai.java.jvm.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存占位符对象，一个OOMObject大约占64K
 */
public class OOMObject {
	public byte[] placeholder = new byte[64 * 1024];

	public static void fillHeap(int num) throws InterruptedException {
		List<OOMObject> list = new ArrayList<OOMObject>();
		for (int i = 0; i < num; i++) {
			// 稍作延时，令监视曲线的变化更加明显
			Thread.sleep(50);
			list.add(new OOMObject());
		}
		// 此时执行内存没有完全释放，因为，list没有释放，如果把此gc操作放到fillHeap下面即可全部释放
		System.gc();
	}

	public static void main(String[] args) throws Exception {
		fillHeap(1000);
	}

}