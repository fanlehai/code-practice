package com.fanlehai.java.junit;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fanlehai.java.junit.powermock.ClassUnderTest;

@RunWith(PowerMockRunner.class)
public class BatchCheckUtilTest {

	/*
	 * 用反射的方式获取静态方法，调用
	 */
	@Test
	public void validInt_when_input_not_int() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String input = "test";
		Method validInt = BatchCheckUtil.class.getDeclaredMethod("validInt", String.class);
		validInt.setAccessible(true);
		// 把BatchCheckUtil.class换成对象的实例就是调用普通方法
		Object object = validInt.invoke(BatchCheckUtil.class, input);
		assertFalse((Boolean) object);
	}
	
	/*
	 * 对private的静态方法进行mock，让其返回自定义的值
	 */
	@Test
	@PrepareForTest(BatchCheckUtil.class)
	public void testStaticMethod() throws Exception{
		PowerMockito.mockStatic(BatchCheckUtil.class);
		PowerMockito.doReturn(true).when(BatchCheckUtil.class,"validInt", "13");
		
		BatchCheckUtil batchCheckUtil = new BatchCheckUtil();
		assertTrue(batchCheckUtil.testStatic("13"));
		//PowerMockito.verifyPrivate(BatchCheckUtil.class).invoke("validInt","14");
		
		// 确定static方法运行几次，此static需要可访问到，不能是private
		//PowerMockito.verifyStatic();
		//ClassWithStaticMethod.someStaticMethod("some arg");
	}

}
