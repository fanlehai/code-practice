package com.fanlehai.java.junit.powermock;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fanlehai.java.util.New;

@RunWith(PowerMockRunner.class)
public class ClassUnderTestTest {

	/*
	 * 1.普通Mock不需要加@RunWith和@PrepareForTest注解
	 */
	@Test
	public void testCallArgumentInstance() {
		// 用mock创建一个File的模拟对象，拥有File的所有功能，但是默认不执行File的真实方法
		File file = PowerMockito.mock(File.class);
		ClassUnderTest underTest = new ClassUnderTest();
		// 为file.exists()方法进行stub，就是设定此方法固定返回值
		PowerMockito.when(file.exists()).thenReturn(true);
		// PowerMockito.doNothing().when(file).exists();
		// PowerMockito.doNothing().when(file.exists());
		Assert.assertTrue(underTest.callArgumentInstance(file));
	}

	/*
	 * 2.Mock方法内部new出来的对象
	 * 当使用PowerMockito.whenNew方法时，必须加注解@PrepareForTest和@RunWith。
	 * 注解@PrepareForTest里写的类是需要mock的new对象代码所在的类。
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
	 * 3.Mock普通对象的final方法 当需要mock final方法的时候，必须加注解@PrepareForTest和@RunWith。注解@
	 * PrepareForTest里写的类是final方法所在的类。
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
	 * 4.Mock普通类的静态方法 当需要mock静态方法的时候，必须加注解@PrepareForTest和@RunWith。注解@
	 * PrepareForTest里写的类是静态方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassDependency.class)
	public void testCallStaticMethod() {
		ClassUnderTest underTest = new ClassUnderTest();
		PowerMockito.mockStatic(ClassDependency.class);
		PowerMockito.when(ClassDependency.isExist()).thenReturn(true);
		// PowerMockito.doNothing().when(ClassDependency.isExist());
		Assert.assertTrue(underTest.callStaticMethod());
	}

	/*
	 * 5.Mock 私有方法
	 * 和Mock普通方法一样，只是需要加注解@PrepareForTest(ClassUnderTest.class)，注解里写的类是私有方法所在的类。
	 */
	@Test
	@PrepareForTest(ClassUnderTest.class)
	public void testCallPrivateMethod() throws Exception {
		ClassUnderTest underTest = PowerMockito.mock(ClassUnderTest.class);
		PowerMockito.when(underTest.callPrivateMethod()).thenCallRealMethod();
		PowerMockito.when(underTest, "isExist").thenReturn(true);
		Assert.assertTrue(underTest.callPrivateMethod());

		PowerMockito.verifyPrivate(underTest).invoke("isExist");
	}

	/*
	 * 6.Mock系统类的静态和final方法 和Mock普通对象的静态方法、final方法一样，只不过注解@PrepareForTest里写的类不一样
	 * ，注解里写的类是需要调用系统方法所在的类。
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
		ClassDependency.isExist();
		// PowerMockito.verifyStatic();// 默认是验证方法被调用1次
		PowerMockito.verifyStatic(Mockito.times(2)); // 被调用2次
		ClassDependency.isVerify();

		// 验证
		Map mock = Mockito.mock(Map.class);
		PowerMockito.when(mock.get("city")).thenReturn("广州");
		// 关注参数有否传入
		Mockito.verify(mock).get(Matchers.eq("city"));
		// 关注调用的次数,次数还有下面方法可以使用
		// Mockito.never();Mockito.atLeast(minNumberOfInvocations);Mockito.atMost(maxNumberOfInvocations);
		Mockito.verify(mock, Mockito.times(2)).get("city");

	}

	/*
	 * 8.方法调用顺序测试
	 */
	@Test
	@PrepareForTest(ClassUnderTest.class)
	public void testOrder() {

		ClassUnderTest test = PowerMockito.mock(ClassUnderTest.class);
		test.order1(); // 1st
		test.order2(); // 2nd
		test.order3(); // 3rd

		InOrder inOrder = Mockito.inOrder(test);

		// 验证下面的调用顺序
		inOrder.verify(test).order1();
		inOrder.verify(test).order2();
		inOrder.verify(test).order3();

		// 验证没有其他方法被调用，在order3后，这个测试可以通过
		inOrder.verifyNoMoreInteractions();

		// 检查是否所有的方法都通过顺序测试，这里order1么有测试，所以失败
		Mockito.verifyNoMoreInteractions(test);

	}

	/*
	 * 9.方法参数匹配一
	 */
	@Test
	@PrepareForTest(EmployeeService.class)
	public void shouldFindEmployeeByEmail() {

		final EmployeeService mock = PowerMockito.mock(EmployeeService.class);
		final Employee employee = new Employee();
		PowerMockito.when(mock.findEmployeeByEmail(Mockito.startsWith("deep"))).thenReturn(employee);
		final EmployeeController employeeController = new EmployeeController(mock);

		assertSame(employee, employeeController.findEmployeeByEmail("deep@gitshah.com"));
		assertSame(employee, employeeController.findEmployeeByEmail("deep@packtpub.com"));
		assertNull(employeeController.findEmployeeByEmail("noreply@packtpub.com"));
	}

	/*
	 * 9.方法参数匹配二
	 */
	@Test
	@PrepareForTest(EmployeeService.class)
	public void shouldReturnNullIfNoEmployeeFoundByEmail() {

		final EmployeeService mock = PowerMockito.mock(EmployeeService.class);
		PowerMockito.when(mock.findEmployeeByEmail(Mockito.anyString())).thenReturn(null);
		final EmployeeController employeeController = new EmployeeController(mock);
		assertNull(employeeController.findEmployeeByEmail("deep@gitshah.com"));
		assertNull(employeeController.findEmployeeByEmail("deep@packtpub.com"));
		assertNull(employeeController.findEmployeeByEmail("noreply@packtpub.com"));
	}

	/*
	 * 10.用answer接口实现return值，不同返回
	 */
	@Test
	public void shouldFindEmployeeByEmailUsingTheAnswerInterface() {

		final EmployeeService mock = PowerMockito.mock(EmployeeService.class);
		final Employee employee = new Employee();

		PowerMockito.when(mock.findEmployeeByEmail(Mockito.anyString())).then(new Answer<Employee>() {

			public Employee answer(InvocationOnMock invocation) throws Throwable {
				final String email = (String) invocation.getArguments()[0];
				if (email == null)
					return null;
				if (email.startsWith("deep"))
					return employee;
				if (email.endsWith("packtpub.com"))
					return employee;
				return null;
			}
		});

		final EmployeeController employeeController = new EmployeeController(mock);
		assertSame(employee, employeeController.findEmployeeByEmail("deep@gitshah.com"));
		assertSame(employee, employeeController.findEmployeeByEmail("deep@packtpub.com"));
		assertNull(employeeController.findEmployeeByEmail("hello@world.com"));
	}

	/*
	 * 10.用answer接口实现return值，不同返回(二)
	 */
	@Test
	public void shouldReturnCountOfEmployeesFromTheServiceWithDefaultAnswer() {
		Employee employee = new Employee();
		EmployeeService mock = PowerMockito.mock(EmployeeService.class, new Answer() {

			public Object answer(InvocationOnMock invocation) {
				if (invocation.getMethod().getName().equals("findEmployeeByEmail")) {
					// return new Employee();
					return employee;
				} else {
					return 10;
				}
			}
		});

		EmployeeController employeeController = new EmployeeController(mock);
		assertEquals(10, employeeController.getProjectedEmployeeCount());
		assertEquals(employee, employeeController.findEmployeeByEmail("test"));
	}

	/*
	 * 11.spy使用
	 */
	@Test
	public void shouldInvokeTheCreateEmployeeMethodWhileSavingANewEmployee() {

		final EmployeeService spy = PowerMockito.spy(new EmployeeService());
		final Employee employeeMock = PowerMockito.mock(Employee.class);
		PowerMockito.when(employeeMock.isNew()).thenReturn(true);
		PowerMockito.doNothing().when(spy).createEmployee(employeeMock);
		spy.saveEmployee(employeeMock);
		Mockito.verify(spy).createEmployee(employeeMock);

		List<String> list = new LinkedList<String>();
		List<String> spyList = PowerMockito.spy(list);

		// Impossible: real method is called so spy.get(0) throws
		// IndexOutOfBoundsException (the list is yet empty)
		// when(spyList.get(0)).thenReturn("foo");

		// You have to use doReturn() for stubbing
		PowerMockito.doReturn("foo").when(spyList).get(0);
		assertEquals("foo", spyList.get(0));

	}
}
