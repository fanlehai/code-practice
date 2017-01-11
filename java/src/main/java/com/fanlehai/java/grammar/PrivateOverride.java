// Trying to override a private method.
package com.fanlehai.java.grammar;

import static com.fanlehai.java.util.Print.*;

public class PrivateOverride {
	private void f() {
		print("private f()");
	}

	public static void main(String[] args) {
		PrivateOverride po = new Derived();
		po.f();
	}
}

class Derived extends PrivateOverride {
	public void f() {
		print("public f()");
	}
} /*
	 * Output: private f()
	 */// :~
