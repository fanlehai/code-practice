package com.fanlehai.java.jvm;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

class ContainValue {
	public String value;

	ContainValue(String str) {
		value = str;
	}

	@Override
	protected void finalize() {
		System.out.println("垃圾回收对象：" + value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContainValue) {
			return value.equals(((ContainValue) obj).value);
		} else {
			return false;
		}
	}
}

public class WeakReferenceTest {

	@Test
	public void weakReferenceTest() {
		System.out.println("***** weakReferenceTest *****");

		ContainValue referent = new ContainValue("just for testing");
		WeakReference<ContainValue> weakReference = new WeakReference<ContainValue>(referent);
		referent = null;

		ContainValue referent1 = new ContainValue("just for testing1");
		LinkedList<WeakReference<ContainValue>> list = new LinkedList<>();
		list.add(new WeakReference<ContainValue>(referent1));
		referent1 = null;

		System.gc();
		// 上面referent1 = null;这样在43行new的对象，就没有强引用了，list中的WeakReference设置成null
		Assert.assertNull(list.peek().get());
	}

	@Test
	public void normalReferenceTest() {
		System.out.println("***** normalReferenceTest *****");
		LinkedList<ContainValue> data = new LinkedList<>();

		ContainValue expect = new ContainValue("test normal");
		data.push(expect);

		expect = null;
		System.gc();
		// 这里测试失败，因为peekvalue = null只是把peekvalue变量设置为null，跟data中数据无关
		Assert.assertNotNull(data.peek());
		Assert.assertEquals(new ContainValue("test normal"), data.peek());
	}

}
