package com.fanlehai.java.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class MapExample {
	
	private static class MyHashMap<K,V> extends HashMap<K,V>
	{  
	    public V put(K key, V value)  
	    {  
	        //如果已经存在key，不覆盖原有key对应的value  
	       // if(this.containsKey(key))  
	            return super.put(key, value);  
	       // return null;  
	    }  
	}  
	
	
	private static class MyHashMap1 extends HashMap  
	{  
	    @Override  
	    public Object put(Object key, Object value)  
	    {  
	        //如果已经存在key，不覆盖原有key对应的value  
	        if(!this.containsKey(key))  
	            return super.put(key, value);  
	          
	        return null;  
	    }  
	}  

	

	public static void mapTest(){
		MyHashMap<String, String> map = new MyHashMap<String, String>();
		
		map.put("key4", "key4");
		map.put("key5", "key5");
		map.put("key1", "key1");
		map.put("key2", "key2");
		map.put("key3", "key3");
		map.put("key1", "key11");

		for(Map.Entry<String,String> entry : map.entrySet()){
			System.out.println("key : " + entry.getKey() +"   value : "+entry.getValue());
		}
	}

	public static void main(String[] args) {

		/*TreeMap<String, String> map = new TreeMap<String, String>();

		map.put("key4", "key4");
		map.put("key5", "key5");
		map.put("key1", "key1");
		map.put("key2", "key2");
		map.put("key3", "key3");
		map.put("key1", "key11");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("key : " + entry.getKey() + "   value : " + entry.getValue());
		}
*/
		
		//mapTest();
		
		
	 
		return;
	}

}
