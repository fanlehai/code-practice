package com.fanlehai.java.string;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnagramTestTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
		
		
		assertTrue(AnagramTest.isAnagram("word", "wrdo")); 
		assertTrue(AnagramTest.isAnagram("mary", "army")); 
		assertTrue(AnagramTest.isAnagram("stop", "tops")); 
		assertTrue(AnagramTest.isAnagram("boat", "btoa")); 
		assertFalse(AnagramTest.isAnagram("pure", "in")); 
		assertFalse(AnagramTest.isAnagram("fill", "fil")); 
		assertFalse(AnagramTest.isAnagram("b", "bbb")); 
		assertFalse(AnagramTest.isAnagram("ccc", "ccccccc")); 
		assertTrue(AnagramTest.isAnagram("a", "a")); 
		assertFalse(AnagramTest.isAnagram("sleep", "slep"));

	}

}
