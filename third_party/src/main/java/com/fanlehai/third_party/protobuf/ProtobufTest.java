package com.fanlehai.third_party.protobuf;

public class ProtobufTest {
	// 待整理 2017年03月12日
	//https://my.oschina.net/OutOfMemory/blog/819471
	//https://www.ibm.com/developerworks/cn/linux/l-cn-gpb/
	
	
	
	
//	public static void main(String[] args) throws InvalidProtocolBufferException {
//		long startTime = System.currentTimeMillis();
//		byte[] result = null;
//		for (int i = 0; i < 50000; i++) {
//			GoodsPicInfo.PicInfo.Builder builder = GoodsPicInfo.PicInfo.newBuilder();
//			builder.setGoodID(100);
//			builder.setGuid("11111-22222-3333-444");
//			builder.setOrder(0);
//			builder.setType("ITEM");
//			builder.setID(10);
//			builder.setUrl("http://xxx.jpg");
//			GoodsPicInfo.PicInfo info = builder.build();
//			result = info.toByteArray();
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println("字节数大小:" + result.length + ",序列化花费时间:" + (endTime - startTime) + "ms");
//
//		for (int i = 0; i < 50000; i++) {
//			GoodsPicInfo.PicInfo newBean = GoodsPicInfo.PicInfo.getDefaultInstance();
//			MessageLite prototype = newBean.getDefaultInstanceForType();
//			newBean = (PicInfo) prototype.newBuilderForType().mergeFrom(result).build();
//		}
//		long endTime2 = System.currentTimeMillis();
//		System.out.println("反序列化花费时间:" + (endTime2 - endTime) + "ms");
//	}
}
