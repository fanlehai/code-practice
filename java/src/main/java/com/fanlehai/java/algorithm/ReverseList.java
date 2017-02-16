package com.fanlehai.java.algorithm;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import com.fanlehai.java.util.Print;

// 反转一个list的方向：

class ListTest<T> {
	private String name;
	private ListTest<T> next;

	public ListTest(String str, ListTest<T> next) {
		// TODO Auto-generated constructor stub
		name = str;
		this.next = next;
	}

	ListTest<T> getNext() {
		return next;
	}

	void setNext(ListTest<T> next) {
		this.next = next;
	}

	String getName() {
		return name;
	}
}

public class ReverseList {

	ListTest<String> listA;

	@Before
	public void Init() {
		ListTest<String> listE = new ListTest<>("E", null);
		ListTest<String> listD = new ListTest<>("D", listE);
		ListTest<String> listC = new ListTest<>("C", listD);
		ListTest<String> listB = new ListTest<>("B", listC);
		listA = new ListTest<>("A", listB);
	}

	public <T> ListTest<T> reverse(ListTest<T> list) {
		if (list == null) {
			throw new NullPointerException();
		}

		if (list.getNext() == null) {
			return list;
		}

		ListTest<T> next = list.getNext();
		list.setNext(null);

		ListTest<T> tmp = reverse(next);
		next.setNext(list);

		return tmp;
	}
	
	@Test
	public void reverseTest(){
		Print.print("*****反转前：*****");
		ListTest<String> listIndex = listA;
		while (listIndex!=null) {
			Print.printnb(listIndex.getName());
			Print.printnb(" -->  ");
			listIndex = listIndex.getNext();
		}
		Print.printnb("null");
		
		listIndex = reverse(listA);
		Print.print();
		Print.print("*****反转后：*****");
		while (listIndex!=null) {
			Print.printnb(listIndex.getName());
			Print.printnb(" -->  ");
			listIndex = listIndex.getNext();
		}
		Print.printnb("null");
	}
}
