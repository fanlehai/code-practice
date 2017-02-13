package com.fanlehai.java.nio;

import java.util.prefs.*;
import static com.fanlehai.java.util.Print.*;

class PreferenceTest {
	private Preferences prefs;

	public void setPreference()throws Exception {
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node(this.getClass().getName());
		String ID1 = "Test1";
		String ID2 = "Test2";
		String ID3 = "Test3";

		// First we will get the values
		// Define a boolean value
		System.out.println(prefs.getBoolean(ID1, true));
		// Define a string with default "Hello World
		System.out.println(prefs.get(ID2, "Hello World"));
		// Define a integer with default 50
		System.out.println(prefs.getInt(ID3, 50));

		// now set the values
		prefs.putBoolean(ID1, false);
		prefs.put(ID2, "Hello Europa");
		prefs.putInt(ID3, 45);

		// Delete the preference settings for the first value
		prefs.remove(ID1);
		print("--------------------------");
		for (String key : prefs.keys())
			print(key + ": " + prefs.get(key, null));

	}
}

public class PreferencesDemo {
	public static void main(String[] args) throws Exception {

		// userNodeForPackage使用用户偏好的配置；
		// 也可以使用systemNodeForPackage()，这个是用于通用安装配置
		Preferences prefs = Preferences.userNodeForPackage(PreferencesDemo.class);
		// prefs.put("Location", "Oz");
		// prefs.put("Footwear", "Ruby Slippers");
		// prefs.putInt("Companions", 4);
		// prefs.putBoolean("Are there witches?", true);
		// int usageCount = prefs.getInt("UsageCount", 0);
		// usageCount++;
		// prefs.putInt("UsageCount", usageCount);
		for (String key : prefs.keys())
			print(key + ": " + prefs.get(key, null));
		// You must always provide a default value:
		print("How many companions does Dorothy have? " + prefs.getInt("Companions", 0));
		
		
		
		// 使用方法二：
		 PreferenceTest test = new PreferenceTest();
		 test.setPreference();
	}
} /*
	 * Output: (Sample) Location: Oz Footwear: Ruby Slippers Companions: 4 Are
	 * there witches?: true UsageCount: 53 How many companions does Dorothy
	 * have? 4
	 */// :~
