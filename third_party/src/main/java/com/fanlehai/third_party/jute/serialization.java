package com.fanlehai.third_party.jute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;
import org.apache.jute.InputArchive;
import org.apache.jute.OutputArchive;
import org.apache.jute.Record;
import org.junit.Assert;
import org.junit.Test;

class TestBean implements Record {

	private int intV;
	private String stringV;

	int getIntV() {
		return intV;
	}

	String getStringV() {
		return stringV;
	}

	public TestBean(int intV, String stringV) {
		this.intV = intV;
		this.stringV = stringV;
	}

	@Override
	public void serialize(OutputArchive archive, String tag) throws IOException {
		// TODO Auto-generated method stub
		archive.startRecord(this, tag);
		archive.writeInt(intV, "intV");
		archive.writeString(stringV, "stringV");
		archive.endRecord(this, tag);

	}

	@Override
	public void deserialize(InputArchive archive, String tag) throws IOException {
		// TODO Auto-generated method stub
		archive.startRecord(tag);
		this.intV = archive.readInt("intV");
		this.stringV = archive.readString("stringV");
		archive.endRecord(tag);
	}
}

public class serialization {

	/*
	 * 序列化到内存： 
	 * 1. 实现Record接口的类TestBean,serialize和deserialize是实际序列化和反序列化方法；
	 * 2.通过ByteArrayOutputStream和BinaryOutputArchive把TestBean类序列化到内存;
	 * 3.通过ByteArrayInputStream和BinaryInputArchive把2中序列化到内存的字节，反序列化成TestBean对象；
	 */
	@Test
	public void testSerialize() throws IOException {
		// 序列化：
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BinaryOutputArchive binaryOutputArchive = BinaryOutputArchive.getArchive(byteArrayOutputStream);
		TestBean tBeanOut = new TestBean(100, "testString");
		tBeanOut.serialize(binaryOutputArchive, "serializationData");

		// 反序列化
		ByteArrayInputStream byteArrayInoutStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		InputArchive inputArchive = BinaryInputArchive.getArchive(byteArrayInoutStream);
		TestBean testBeanIn = new TestBean(0, "");
		testBeanIn.deserialize(inputArchive, "serializationData");

		// 测试结果
		Assert.assertEquals(100, testBeanIn.getIntV());
		Assert.assertEquals("testString", testBeanIn.getStringV());
		
		byteArrayOutputStream.close();
		byteArrayInoutStream.close();
	}
	
	/*
	 * 序列化到文件：
	 * 1. 实现Record接口的类TestBean,serialize和deserialize是实际序列化和反序列化方法；
	 * 2.通过FileOutputStream和BinaryOutputArchive把TestBean类序列化到内存;
	 * 3.通过FileInputStream和BinaryInputArchive把2中序列化到内存的字节，反序列化成TestBean对象；
	 */
	@Test
	public void testSerializeFile() throws IOException {
		File file = new File("src/main/resources/jute.file");
		// 序列化：
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		BinaryOutputArchive binaryOutputArchive = BinaryOutputArchive.getArchive(fileOutputStream);
		TestBean tBeanOut = new TestBean(200, "testFileString");
		tBeanOut.serialize(binaryOutputArchive, "serializationDataFile");

		// 反序列化
		FileInputStream fileInputStream = new FileInputStream(file);
		InputArchive inputArchive = BinaryInputArchive.getArchive(fileInputStream);
		TestBean testBeanIn = new TestBean(0, "");
		testBeanIn.deserialize(inputArchive, "serializationDataFile");

		// 测试结果
		Assert.assertEquals(200, testBeanIn.getIntV());
		Assert.assertEquals("testFileString", testBeanIn.getStringV());
		
		fileOutputStream.close();
		fileInputStream.close();
	}
}
