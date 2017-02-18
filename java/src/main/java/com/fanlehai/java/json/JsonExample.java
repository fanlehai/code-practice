package com.fanlehai.java.json;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.*;

/*
 {
    "name": "Alice",
    "age": 20,
    "address": {
        "streetAddress": "100 Wall Street",
        "city": "New York"
    },
    "phoneNumber": [
        {
            "type": "home",
            "number": "212-333-1111"
        },{
            "type": "fax",
            "number": "646-444-2222"
        }
    ]
}
*/

public class JsonExample {

	@Test
	public void readJson() throws IOException {
		String filePath = "src/main/java/com/fanlehai/java/json/example.json";
		FileReader fileReader = new FileReader(filePath);
		char[] cbuf = new char[10];
		StringBuilder stringBuilder = new StringBuilder();
		while (fileReader.read(cbuf) != -1) {
			stringBuilder.append(cbuf);
		}
		fileReader.close();

		JSONObject object = new JSONObject(stringBuilder.toString());
		Assert.assertEquals("Alice", object.get("name"));
		Assert.assertEquals(20, object.get("age"));

		JSONObject objAddress = object.getJSONObject("address");
		Assert.assertEquals("100 Wall Street", objAddress.get("streetAddress"));
		Assert.assertEquals("New York", objAddress.get("city"));

		JSONArray array = object.getJSONArray("phoneNumber");
		for (int i = 0; i < array.length(); ++i) {
			System.out.println("type : " + array.getJSONObject(i).get("type"));
			System.out.println("number : " + array.getJSONObject(i).get("number"));
		}
	}

	@Test
	public void writeJson() throws IOException {
		String filePath = "src/main/java/com/fanlehai/java/json/write.json";
		FileWriter writer = new FileWriter(filePath);
		JSONObject obj = new JSONObject();
		obj.put("name", "john");
		obj.put("age", 28);
		JSONArray array = new JSONArray();
		array.put(0, obj);

		JSONObject objData = new JSONObject();
		objData.put("data", array);
		System.out.println(objData.toString());
	}
}