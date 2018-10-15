package cn.rivamed.himsetting.Utils;

import java.io.Serializable;

public class AccessNumber {
	
	private static int i=0;
	private static final AccessNumber a=new AccessNumber();
	
	
	private AccessNumber(){
	}
	
	
	public static AccessNumber getAccessNumber(){
		return a;
	}
	
	
	public synchronized int getNumber() {
	    i++;
		return i;
	}
	
	

}
