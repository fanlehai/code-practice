package com.fanlehai.java.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RepeatingAnnotations {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Cars {
		Manufacturer[] value() default {};
	}

	@Manufacturer("Mercedes Benz")
	@Manufacturer("Toyota")
	@Manufacturer("BMW")
	@Manufacturer("Range Rover")
	public interface Car {

	}

	@Repeatable(value = Cars.class)
	public @interface Manufacturer {
		String value();

	};

	public static void main(String[] args) {
		Manufacturer[] a = Car.class.getAnnotationsByType(Manufacturer.class);
		System.out.println("Number of car manufacturers is " + a.length);

		System.out.println("\n-------Printing out Car Manufacturers--------");

		Cars cars = Car.class.getAnnotation(Cars.class);
		for (Manufacturer car : cars.value()) {
			System.out.println(car.value());
		}
	}
}
