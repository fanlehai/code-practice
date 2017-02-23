package com.fanlehai.java;

import java.util.jar.Attributes.Name;

import org.junit.Test;

/**
 * Hello world!
 *
 */
//only public, abstract & final are permitted
class StaticTest{
}

//only public, abstract & final are permitted
public  class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    public void name() {
		
	}
    
    // The class StaticTest can be either abstract or final, not both
    public static class StaticTest{
    	void test(){
    	}
    	
    	String string;
    }

    
}
