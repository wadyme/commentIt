package cn.ls.integrator.core.thread;

import java.util.HashMap;
import java.util.Map;

public class TestThread {

	private static Map<Foo, Foo> map;
	
	public static void main(String[] args) {
		
		new TestThread().test();
	}
	
	void test(){
		map = new HashMap<Foo, Foo>();
		Foo foo1 = new Foo("zhang"); 
		map.put(foo1, foo1);
		Foo foo2 = new Foo("wang"); 
		map.put(foo2, foo2);
		new MyThread("zhang").start();
		new MyThread("wang").start();
		new MyThread("zhang").start();
		new MyThread("wang").start();
	}
	
	class MyThread extends Thread{
		
		Foo name;
		
		MyThread(String name){
			this.name = new Foo(name);
		}
		
		@Override
		public void run() {
//			Foo value = new Foo(map.get(name).name);
			Foo value = map.get(name);
			synchronized (value) {
				System.out.println(value.name);
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class Foo{
		String name;
		Foo(String name){
			this.name = name;
		}
		@Override
		public boolean equals(Object obj) {
			if( !(obj instanceof Foo)){
				return false;
			}
			Foo objFoo = (Foo) obj;
			return this.name.equals(objFoo.name);
		}
		
		public int hashCode() {
			return name.length();
		}
	}
}
