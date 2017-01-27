package com.fanlehai.java.container;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private int cacheSize;

	public LRUCache(int cacheSize) {
		super(16, (float) 0.75, true);// 这里的参数true表示LinkedHashMap以访问顺序排序，false表示以插入顺序排序
		this.cacheSize = cacheSize;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() >= cacheSize;
	}
}