package com.fanlehai.java.container;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class DataView<T> implements Iterable<T>{
	List<T> list = new LinkedList<>();
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public T next() {
				// TODO Auto-generated method stub
				return list.get(new Random().nextInt(list.size()));
			}
		};
	}
	
	void add(T data){
		list.add(data);
	}
}

class DataViewCol<T> implements Collection<T>{

	List<T> list = new LinkedList<>();
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public T next() {
				// TODO Auto-generated method stub
				return list.get(new Random().nextInt(list.size()));
			}
		};
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(T e) {
		// TODO Auto-generated method stub
		list.add(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
}

public class ForEachTest {

	public static void main(String[] args) {
		DataView<String> dataView = new DataView<>();
		dataView.add("1");
		dataView.add("2");
		dataView.add("3");
		for(String string : dataView){
			System.out.println(string);
		}
		
		DataViewCol<String> dataViewCol = new DataViewCol<>();
		dataViewCol.add("a");
		dataViewCol.add("b");
		dataViewCol.add("c");
		for(String string : dataViewCol){
			System.out.println(string);
		}
	}
}
