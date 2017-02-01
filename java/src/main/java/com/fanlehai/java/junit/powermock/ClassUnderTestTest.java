package com.fanlehai.java.junit.powermock;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ClassUnderTestTest {

	/*
	 * 1.普通Mock不需要加@RunWith和@PrepareForTest注解
	 */
	@Test
	public void testCallArgumentInstance() {
		// 用mock创建一个File的模拟对象，拥有File的所有功能
		File file = PowerMockito.mock(File.class);
		ClassUnderTest underTest = new ClassUnderTest();
		// 为file.exists()方法进行stub，就是设定此方法固定返回值
		PowerMockito.when(file.exists()).thenReturn(true);
		//PowerMockito.doNothing().when(file).exists();
		PowerMockito.doNothing().when(file.exists());
		Assert.assertTrue(underTest.callArgumentInstance(file));
	}

	/* 
	 * 2.Mock方法内部new出来的对象
	 * 	 当使用PowerMockito.whenNew方法时，必须加注解@PrepareForTest和@RunWith。
	 * 	 注解@PrepareForTest里写的类是需要mock的new对象代码所在的类。
	 */
	@Test
	@PrepareForTest(ClassUnderTest.class)
	public void testCallInternalInstance() throws Exception {
		File file = PowerMockito.mock(File.class);
		ClassUnderTest underTest = new ClassUnderTest();
		PowerMockito.whenNew(File.class).withArguments("bbb").thenReturn(file);
		PowerMockito.when(file.exists()).thenReturn(true);
		Assert.assertTrue(underTest.callInternalInstance("bbb"));
	}

	/*
	 * 3.Mock普通对象的final方法
	 *   当需要mock final方法的时候，必须加注解@PrepareForTest和@RunWith。注解@PrepareForTest里写的类是final方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassDependency.class)
	public void testCallFinalMethod() {
		ClassDependency depencency = PowerMockito.mock(ClassDependency.class);
		ClassUnderTest underTest = new ClassUnderTest();
		PowerMockito.when(depencency.isAlive()).thenReturn(true);
		Assert.assertTrue(underTest.callFinalMethod(depencency));
	}

	/*
	 * 4.Mock普通类的静态方法
	 *   当需要mock静态方法的时候，必须加注解@PrepareForTest和@RunWith。注解@PrepareForTest里写的类是静态方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassDependency.class)
	public void testCallStaticMethod() {
		ClassUnderTest underTest = new ClassUnderTest();
		PowerMockito.mockStatic(ClassDependency.class);
		PowerMockito.when(ClassDependency.isExist()).thenReturn(true);
		//PowerMockito.doNothing().when(ClassDependency.isExist());
		Assert.assertTrue(underTest.callStaticMethod());
	}

	/*
	 * 5.Mock 私有方法
	 *   和Mock普通方法一样，只是需要加注解@PrepareForTest(ClassUnderTest.class)，注解里写的类是私有方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassUnderTest.class)
	public void testCallPrivateMethod() throws Exception {
		ClassUnderTest underTest = PowerMockito.mock(ClassUnderTest.class);
		PowerMockito.when(underTest.callPrivateMethod()).thenCallRealMethod();
		PowerMockito.when(underTest, "isExist").thenReturn(true);
		Assert.assertTrue(underTest.callPrivateMethod());
	}

	/*
	 * 6.Mock系统类的静态和final方法
	 *   和Mock普通对象的静态方法、final方法一样，只不过注解@PrepareForTest里写的类不一样 ，注解里写的类是需要调用系统方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassUnderTest.class)
	public void testCallSystemStaticMethod() {
		ClassUnderTest underTest = new ClassUnderTest();
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getProperty("aaa")).thenReturn("bbb");
		Assert.assertEquals("bbb", underTest.callSystemStaticMethod("aaa"));
	}
	
	/*
	 * 7.验证方法是否被调用
	 */
	@Test
	@PrepareForTest(ClassDependency.class)
	public void testVerify() {
		
		// 测试普通的方法是否被调用
		ClassUnderTest test = PowerMockito.mock(ClassUnderTest.class);
		test.callSystemFinalMethod("test");
		Mockito.verify(test).callSystemFinalMethod("test");
		
		// 测试静态方法是否被调用
		PowerMockito.mockStatic(ClassDependency.class);
		ClassDependency.isVerify();
		ClassDependency.isVerify();
		//PowerMockito.verifyStatic();// 默认是验证方法被调用1次
		PowerMockito.verifyStatic(Mockito.times(2)); //  被调用2次  
		ClassDependency.isVerify();
		
	}
}
