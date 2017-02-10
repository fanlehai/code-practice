package com.fanlehai.java.generics;
import java.lang.reflect.*;
import java.util.*;
import com.fanlehai.java.util.*;
import static com.fanlehai.java.util.Tuple.*;

class MixinProxy implements InvocationHandler {
	Map<String, Object> delegatesByMethod;

	public MixinProxy(TwoTuple<Object, Class<?>>... pairs) {
		delegatesByMethod = new HashMap<String, Object>();
		for (TwoTuple<Object, Class<?>> pair : pairs) {
			for (Method method : pair.second.getMethods()) {
				String methodName = method.getName();
				// The first interface in the map
				// implements the method.
				if (!delegatesByMethod.containsKey(methodName))
					delegatesByMethod.put(methodName, pair.first);
			}
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		Object delegate = delegatesByMethod.get(methodName);
		return method.invoke(delegate, args);
	}

	@SuppressWarnings("unchecked")
	public static Object newInstance(TwoTuple... pairs) {
		Class[] interfaces = new Class[pairs.length];
		for (int i = 0; i < pairs.length; i++) {
			interfaces[i] = (Class) pairs[i].second;
		}
		ClassLoader cl = pairs[0].first.getClass().getClassLoader();
		return Proxy.newProxyInstance(cl, interfaces, new MixinProxy(pairs));
	}
}

public class DynamicProxyMixin {
	public static void main(String[] args) {
		Object mixin = MixinProxy.newInstance(
				tuple(new BasicImp(), BasicMix.class),
				tuple(new TimeStampedImp(), TimeStampedMix.class), 
				tuple(new SerialNumberedImp(), SerialNumberedMix.class));
		BasicMix b = (BasicMix) mixin;
		TimeStampedMix t = (TimeStampedMix) mixin;
		SerialNumberedMix s = (SerialNumberedMix) mixin;
		b.set("Hello");
		System.out.println(b.get());
		System.out.println(t.getStamp());
		System.out.println(s.getSerialNumber());
	}
} 

/* Output: (Sample)
Hello
1132519137015
1
*///:~
