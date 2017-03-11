package com.fanlehai.java.grammar;

class ObjTest {

}

public class LocalClassExample {

	static String regularExpression = "[^0-9]";

	public void validatePhoneNumber(String phoneNumber1, String phoneNumber2, ObjTest obj) {
		// final int numberLength = 10;
		// Valid in JDK 8 and later:

		int numberLength = 10;
		class PhoneNumber {
			String formattedPhoneNumber = null;

			PhoneNumber(String phoneNumber) {
				// numberLength = 7;
				String currentNumber = phoneNumber.replaceAll(regularExpression, "");
				if (currentNumber.length() == numberLength)
					formattedPhoneNumber = currentNumber;
				else
					formattedPhoneNumber = null;
			}

			public String getNumber() {
				return formattedPhoneNumber;
			}

			// Valid in JDK 8 and later:
			public void printOriginalNumbers() {
				System.out.println("Original numbers are " + phoneNumber1 + " and " + phoneNumber2 + obj.toString());
			}
		}

		PhoneNumber myNumber1 = new PhoneNumber(phoneNumber1);
		PhoneNumber myNumber2 = new PhoneNumber(phoneNumber2);

		// Valid in JDK 8 and later:
		myNumber1.printOriginalNumbers();
		// myNumber2.getNumber();
		System.out.println("Second number is " + myNumber2.getNumber());
		// if (myNumber1.getNumber() == null)
		// System.out.println("First number is invalid");
		// else
		// System.out.println("First number is " + myNumber1.getNumber());
		// if (myNumber2.getNumber() == null)
		// System.out.println("Second number is invalid");
		// else
		// System.out.println("Second number is " + myNumber2.getNumber());

	}

	public void sayGoodbyeInEnglish() {
		class EnglishGoodbye {
			public static final String farewell = "Bye bye";
			public void sayGoodbye() {
				System.out.println(farewell);
			}
		}
		EnglishGoodbye myEnglishGoodbye = new EnglishGoodbye();
		myEnglishGoodbye.sayGoodbye();
	}
	
	public void greetInEnglish() {
		// 不能在块中定义interface
		/*interface HelloThere {
	        public void greet();
	     }
        class EnglishHelloThere implements HelloThere {
            public void greet() {
                System.out.println("Hello ");
            }
        }
        HelloThere myGreeting = new EnglishHelloThere();
        myGreeting.greet();*/
    }
	
	public void sayGoodbyeInEnglish1() {
        class EnglishGoodbye {
        	//static methods can only be declared in a static or top level type
            /*public static void sayGoodbye() {
                System.out.println("Bye bye");
            }*/
        }
//        EnglishGoodbye.sayGoodbye();
    }

	public static void main(String... args) {
		ObjTest objTest = new ObjTest();
		LocalClassExample localClassExample = new LocalClassExample();
		localClassExample.validatePhoneNumber("123-456-7890", "456-7890", objTest);
	}
}
