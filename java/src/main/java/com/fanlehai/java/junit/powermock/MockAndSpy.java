package com.fanlehai.java.junit.powermock;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

class Jerry {
	public void goHome() {
		System.out.println("goHome");
		doSomeThingA();
		doSomeThingB();
	}

	// real invoke it.
	public void doSomeThingB() {
		System.out.println("good day");

	}

	// auto mock method by mockito
	public void doSomeThingA() {
		System.out.println("you should not see this message.");

	}
}

public class MockAndSpy {

	@Test
	public void callRealMethodTest() {
		Jerry jerry = mock(Jerry.class);
		
		//doCallRealMethod().when(jerry).goHome();
		//doCallRealMethod().when(jerry).doSomeThingB();

		jerry.goHome();
		//Mockito.verify(jerry).goHome();
		//Mockito.verify(jerry).doSomeThingA();
		//Mockito.verify(jerry).doSomeThingB();
	}
	
	@Test
	public void callSpyMethodTest() {
		Jerry jerry = PowerMockito.spy(new Jerry());

		doNothing().when(jerry).goHome();
		//doNothing().when(jerry).doSomeThingB();

		jerry.goHome();

		Mockito.verify(jerry).goHome();
		//Mockito.verify(jerry).doSomeThingB();
		//Mockito.verify(jerry).doSomeThingA();
	}
	
	
	
	
}
