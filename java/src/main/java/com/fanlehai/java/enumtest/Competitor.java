// Switching one enum on another.
package com.fanlehai.java.enumtest;

public interface Competitor<T extends Competitor<T>> {
	Outcome compete(T competitor);
} /// :~
