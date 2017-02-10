package com.fanlehai.java.nio;

import java.util.prefs.*;
import static com.fanlehai.java.util.Print.*;

public class PreferencesDemo {
	public static void main(String[] args) throws Exception {
		
		// userNodeForPackage使用用户偏好的配置；
		// 也可以使用systemNodeForPackage()，这个是用于通用安装配置
		Preferences prefs = Preferences.userNodeForPackage(PreferencesDemo.class);
//		prefs.put("Location", "Oz");
//		prefs.put("Footwear", "Ruby Slippers");
//		prefs.putInt("Companions", 4);
//		prefs.putBoolean("Are there witches?", true);
//		int usageCount = prefs.getInt("UsageCount", 0);
//		usageCount++;
//		prefs.putInt("UsageCount", usageCount);
		for (String key : prefs.keys())
			print(key + ": " + prefs.get(key, null));
		// You must always provide a default value:
		print("How many companions does Dorothy have? " + prefs.getInt("Companions", 0));
	}
} /* Output: (Sample)
Location: Oz
Footwear: Ruby Slippers
Companions: 4
Are there witches?: true
UsageCount: 53
How many companions does Dorothy have? 4
*///:~
