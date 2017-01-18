package com.fanlehai.java.compile;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


class GrandFather {
	void thinking() {
		System.out.println("i am grandfather");
	}
}

class Father extends GrandFather {
//	void thinking() {
//		System.out.println("i am father");
//	}
}

class Son extends Father {
	
}



 public class InvokeGrandpa extends Father {

	 void thinking() {
			try {
				MethodType mt = MethodType.methodType(void.class);
				MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
				mh.invoke(this);
			} catch (Throwable e) {
				
				System.err.println(e);
			}
		}

	public static void main(String[] args) {
		//((new InvokeGrandpa()).new Son()).thinking();
		new InvokeGrandpa().thinking();
	}

}
