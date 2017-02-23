package com.fanlehai.java.grammar;

public class HelloWorldAnonymousClasses {
	interface HelloWorld {
		public void greet();

		public void greetSomeone(String someone);
	}

	public void sayHello() {
		int nTest= 10;

		class EnglishGreeting implements HelloWorld {
			String name = "world";

			public void greet() {
				greetSomeone("world");
			}

			public void greetSomeone(String someone) {
				name = someone;
				System.out.println("Hello " + name);
			}
		}

		HelloWorld englishGreeting = new EnglishGreeting();

		HelloWorld frenchGreeting = new HelloWorld() {
			String name = "tout le monde";

			public void greet() {
				greetSomeone("tout le monde");
			}

			public void greetSomeone(String someone) {
				name = someone;
				System.out.println("Salut " + name);
			}
		};

		HelloWorld spanishGreeting = new HelloWorld() {
			String name = "mundo";
			public final static String str = "ster";
			public void greet() {
				greetSomeone("mundo");
				name = str;
			}

			public void greetSomeone(String someone) {
				name = someone;
				System.out.println("Hola, " + name);
			}
			
			
		};
		englishGreeting.greet();
		frenchGreeting.greetSomeone("Fred");
		spanishGreeting.greet();
	}

	public final static void test(){
		
	}
	public static void main(String... args) {
		HelloWorldAnonymousClasses myApp = new HelloWorldAnonymousClasses();
		myApp.sayHello();
	}
}
