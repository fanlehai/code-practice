package com.fanlehai.java.generics;

import java.util.*;

interface TimeStampedMix {
	long getStamp();
}

class TimeStampedImp implements TimeStampedMix {
	private final long timeStamp;

	public TimeStampedImp() {
		timeStamp = new Date().getTime();
	}

	public long getStamp() {
		return timeStamp;
	}
}

interface SerialNumberedMix {
	long getSerialNumber();
}

class SerialNumberedImp implements SerialNumberedMix {
	private static long counter = 1;
	private final long serialNumber = counter++;

	public long getSerialNumber() {
		return serialNumber;
	}
}

interface BasicMix {
	public void set(String val);

	public String get();
}

class BasicImp implements BasicMix {
	private String value;

	public void set(String val) {
		value = val;
	}

	public String get() {
		return value;
	}
}

class Mixin extends BasicImp implements TimeStampedMix, SerialNumberedMix {
	private TimeStampedMix timeStamp = new TimeStampedImp();
	private SerialNumberedMix serialNumber = new SerialNumberedImp();

	public long getStamp() {
		return timeStamp.getStamp();
	}

	public long getSerialNumber() {
		return serialNumber.getSerialNumber();
	}
}

public class Mixins {
	public static void main(String[] args) {
		Mixin mixin1 = new Mixin(), mixin2 = new Mixin();
		mixin1.set("test string 1");
		mixin2.set("test string 2");
		System.out.println(mixin1.get() + " " + mixin1.getStamp() + " " + mixin1.getSerialNumber());
		System.out.println(mixin2.get() + " " + mixin2.getStamp() + " " + mixin2.getSerialNumber());
	}
}

/*
 * Output: (Sample) test string 1 1132437151359 1 test string 2 1132437151359 2
 */// :~
