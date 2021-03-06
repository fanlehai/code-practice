package com.fanlehai.java.grammar;
// Overloading a base-class method name in a derived
// class does not hide the base-class versions.
import static com.fanlehai.java.util.Print.*;

class Homer {
	char doh(char c) {
		print("doh(char)");
		return 'd';
	}
	
	float doh(float f) {
		print("doh(float)");
		return 1.0f;
	}
}

class Milhouse {
}

class Bart extends Homer {
	@Override
	float doh(float f) {
		print(" Bart doh(float)");
		return 1.0f;
	}
	
	void doh(Milhouse m) {
		print("doh(Milhouse)");
	}
}

class BartEx extends Bart{
	float doh(float f) {
		print(" Bart doh(float)");
		return 1.0f;
	}
}

public class Hide {
	public static void main(String[] args) {
		Bart b = new Bart();
		b.doh(1);
		b.doh('x');
		b.doh(1.0f);
		b.doh(new Milhouse());
	}
} /* Output:
 Bart doh(float)
doh(char)
 Bart doh(float)
doh(Milhouse)
*///:~
