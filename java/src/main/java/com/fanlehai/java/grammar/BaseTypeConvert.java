package com.fanlehai.java.grammar;

import java.io.UnsupportedEncodingException;

public class BaseTypeConvert {

	/*
	 * String Convert
	 */
	public static void StringToInt() {
		String number = "123456";
		int num1 = Integer.parseInt(number);// 使用Integer的parseInt方法
		int num2 = new Integer(number);// 强制转换
		int num3 = Integer.valueOf(number).intValue();// 先转Integer类型，再调用intValue()转为int
	}

	public static void StringToLong() {
		String number = "1234567890";
		long num1 = Long.parseLong(number);// 调用Long类型的parseLong方法
		long num2 = new Long(number);// 强制转换
		long num3 = Long.valueOf(number).longValue();// 先转换Long类型，再使用longValue方法转为long
	}

	public static void StringToFloat() {
		String number = "1234.202";
		float num1 = Float.parseFloat(number);// 调用Float的parseFloat方法
		float num2 = new Float(number);// 强制转换
		float num3 = Float.valueOf(number).floatValue();// 先转为Float类型再使用floatValue转为float
	}

	public static void StringToByte() {
		byte[] num = new byte[200];
		String number = "1234567890";
		num = number.getBytes();
	}

	/*
	 * Long Convert
	 */
	public static void LongToString() {
		long number = 1234567890l;
		String num1 = Long.toString(number);// Long的tostring方法
		String num2 = String.valueOf(number);// 使用String的valueOf方法
		String num3 = "" + number;// 这个应该属于强制转换吧

	}

	public static void LongToInt() {
		long number = 121121121l;
		int num1 = (int) number;// 强制类型转换
		int num2 = new Long(number).intValue();// 调用intValue方法
		int num3 = Integer.parseInt(String.valueOf(number));// 先把long转换位字符串String，然后转换为Integer

	}

	// long转换byte数组:将64位的long值放到8字节的byte数组
	public static byte[] longToByteArray(long num) {
		byte[] result = new byte[8];
		result[0] = (byte) (num >>> 56);// 取最高8位放到0下标
		result[1] = (byte) (num >>> 48);// 取最高8位放到0下标
		result[2] = (byte) (num >>> 40);// 取最高8位放到0下标
		result[3] = (byte) (num >>> 32);// 取最高8位放到0下标
		result[4] = (byte) (num >>> 24);// 取最高8位放到0下标
		result[5] = (byte) (num >>> 16);// 取次高8为放到1下标
		result[6] = (byte) (num >>> 8); // 取次低8位放到2下标
		result[7] = (byte) (num); // 取最低8位放到3下标
		return result;
	}

	/*
	 * Byte[] Convert
	 */
	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	public static void putDouble(byte[] bb, double x, int index) {
		// byte[] b = new byte[8];
		long l = Double.doubleToLongBits(x);
		for (int i = 0; i < 4; i++) {
			bb[index + i] = new Long(l).byteValue();
			l = l >> 8;
		}
	}

	public static short getShort(byte[] b, int index) {
		return (short) ((b[index + 1] << 8) | (b[index + 0] & 0xff));
	}

	public static int byteArrayToInt(byte[] b) {
		byte[] a = new byte[4];
		int i = a.length - 1, j = b.length - 1;
		for (; i >= 0; i--, j--) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= 0)
				a[i] = b[j];
			else
				a[i] = 0;// 如果b.length不足4,则将高位补0
		}
		int v0 = (a[0] & 0xff) << 24;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		int v1 = (a[1] & 0xff) << 16;
		int v2 = (a[2] & 0xff) << 8;
		int v3 = (a[3] & 0xff);
		return v0 + v1 + v2 + v3;
	}

	public static long byteArrayToLong(byte[] byteArray) {
		byte[] a = new byte[8];
		int i = a.length - 1, j = byteArray.length - 1;
		for (; i >= 0; i--, j--) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= 0)
				a[i] = byteArray[j];
			else
				a[i] = 0;// 如果b.length不足4,则将高位补0
		}
		// 注意此处和byte数组转换成int的区别在于，下面的转换中要将先将数组中的元素转换成long型再做移位操作，
		// 若直接做位移操作将得不到正确结果，因为Java默认操作数字时，若不加声明会将数字作为int型来对待，此处必须注意。
		long v0 = (long) (a[0] & 0xff) << 56;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		long v1 = (long) (a[1] & 0xff) << 48;
		long v2 = (long) (a[2] & 0xff) << 40;
		long v3 = (long) (a[3] & 0xff) << 32;
		long v4 = (long) (a[4] & 0xff) << 24;
		long v5 = (long) (a[5] & 0xff) << 16;
		long v6 = (long) (a[6] & 0xff) << 8;
		long v7 = (long) (a[7] & 0xff);
		return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
	}

	// 通过byte数组取得float
	public static float getFloat(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	// 通过byte数组取得double
	public static double getDouble(byte[] b, int index) {
		long l;
		l = b[0];
		l &= 0xff;
		l |= ((long) b[1] << 8);
		l &= 0xffff;
		l |= ((long) b[2] << 16);
		l &= 0xffffff;
		l |= ((long) b[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[7] << 56);
		return Double.longBitsToDouble(l);
	}
	
	public static String byteArray2String(byte[] bytes) throws UnsupportedEncodingException{
	
		return new String(bytes, "UTF-8");
	}
	
	

	/*
	 * float convert
	 * 
	 */

	// float转成byte数组
	public static void putFloat(byte[] bb, float x, int index) {
		// byte[] b = new byte[4];
		int l = Float.floatToIntBits(x);
		for (int i = 0; i < 4; i++) {
			bb[index + i] = new Integer(l).byteValue();
			l = l >> 8;
		}
	}

	/*
	 * int convert
	 * 
	 */
	public static void IntToString() {
		int number = 121121;
		String num1 = Integer.toString(number);// 使用Integer的toString方法
		String num2 = String.valueOf(number);// 使用String的valueOf方法
		String num3 = "" + number;// 也是强制转换吧

	}

	public static void IntToLong() {
		int number = 123111;
		long num1 = (long) number;// 强制
		long num2 = Long.parseLong(new Integer(number).toString());// 先转String再进行转换
		long num3 = Long.valueOf(number);

	}
	@SuppressWarnings( "unuse" )
	public static void IntToInterge() {
		int number = 123456;
		Integer num1 = Integer.valueOf(number);
		Integer num2 = new Integer(number);

	}

	// 将32位的int值放到4字节的byte数组
	public static byte[] intToByteArray(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) (num >>> 24);// 取最高8位放到0下标
		result[1] = (byte) (num >>> 16);// 取次高8为放到1下标
		result[2] = (byte) (num >>> 8); // 取次低8位放到2下标
		result[3] = (byte) (num); // 取最低8位放到3下标
		return result;
	}

	public static void main(String[] args) {

		byte[] byteTest = new byte[7];
		for (int i =0; i <byteTest.length; ++i) {
			byteTest[i] = (byte)i;
		}
		byteArrayToInt(byteTest);
	}
}
