package com.fanlehai.java.compile;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GramaTest {

	public static void main(String[] args) {
		System.out.println("实例1：泛型、自动装箱、自动拆箱、遍历循环、变长参数：");
		{
			List<Integer> list = Arrays.asList(1, 2, 3, 4);
			// 如果在JDK 1.7中，还有另外一颗语法糖 ，
			// 能让上面这句代码进一步简写成List<Integer> list = [1, 2, 3, 4];
			int sum = 0;
			for (int i : list) {
				sum += i;
			}
			System.out.println(sum);
		}

		System.out.println("实例2：编译后的变化：");
		{
			List list = Arrays.asList(
					new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4) });

			int sum = 0;
			for (Iterator localIterator = list.iterator(); localIterator.hasNext();) {
				int i = ((Integer) localIterator.next()).intValue();
				sum += i;
			}
			System.out.println(sum);
		}

		System.out.println("实例3：语法糖的误区：");
		{
			Integer a = 1;
			Integer b = 2;
			Integer c = 3;
			Integer d = 3;
			Integer e = 321;
			Integer f = 321;
			Long g = 3L;
			System.out.println(c == d);
			
			// 下面这个语句返回false，原因是Integer会缓存-128到127的数字，不在这个范围会new Integer；
			//public static Integer valueOf(int i) {
			//     if (i >= IntegerCache.low && i <= IntegerCache.high)
			//         return IntegerCache.cache[i + (-IntegerCache.low)];
			//     return new Integer(i);
			//}
			System.out.println(e == f);
			
			
			System.out.println(c == (a + b));
			System.out.println(c.equals(a + b));
			System.out.println(g == (a + b));
			
			// equals不会处理不同类型的关系
			System.out.println(g.equals(a + b));
		}
	}

}
