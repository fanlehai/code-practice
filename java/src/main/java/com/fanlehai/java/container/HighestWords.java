package com.fanlehai.java.container;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public class HighestWords {

	public static void main(String args[]) {
		Map<String, Integer> wordMap = buildWordMap("src/main/java/com/fanlehai/java/container/HighestWords.java");
		List<Entry<String, Integer>> list = sortByValueInDecreasingOrder(wordMap);
		System.out.println("List of repeated word from file and their count");
		for (Map.Entry<String, Integer> entry : list) {
			if (entry.getValue() > 1) {
				System.out.println(entry.getKey() + " => " + entry.getValue());
			}
		}
	}

	public static Map<String, Integer> buildWordMap(String fileName) {
		// Using diamond operator for clean code
		Map<String, Integer> wordMap = new HashMap<>();
		// Using try-with-resource statement for automatic resource management
		try (FileInputStream fis = new FileInputStream(fileName);
				//DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			// words are separated by whitespace
			Pattern pattern = Pattern.compile("\\s+");
			String line = null;
			while ((line = br.readLine()) != null) {
				// do this if case sensitivity is not required i.e.
				// Java = java
				line = line.toLowerCase();
				String[] words = pattern.split(line);
				for (String word : words) {
					if (wordMap.containsKey(word)) {
						wordMap.put(word, (wordMap.get(word) + 1));
					} else {
						wordMap.put(word, 1);
					}
				}
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return wordMap;
	}

	public static List<Entry<String, Integer>> sortByValueInDecreasingOrder(Map<String, Integer> wordMap) {
		Set<Entry<String, Integer>> entries = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<>(entries);
//		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//				return (o2.getValue()).compareTo(o1.getValue());
//			}
//		});
		// 下面用lambda方法实现与上面注释掉的5行代码，相同意思
		Collections.sort(list,(o1,o2)-> o1.getValue().compareTo(o2.getValue()));
		return list;
	}

}
