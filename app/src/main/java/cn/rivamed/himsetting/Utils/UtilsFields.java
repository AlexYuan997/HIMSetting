package cn.rivamed.himsetting.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class UtilsFields implements Serializable {
	
	
	public final static  String COMOK="comok";
	
	public final static  String GB2312="GB2312";
	
	public final static  String CONNECT="connect";

	private static  HashSet<HIMInfoStore> himInfoStores=new HashSet<HIMInfoStore>();


	
	
	
	public static byte[] getThree0XFF() {
		byte[] b = new byte[3];
		b[0] = (byte) 0XFF;
		b[1] = (byte) 0XFF;
		b[2] = (byte) 0XFF;
		return b;
	}
	
	public static byte[] arrysCombine(byte[] bs,byte[]b){
		byte[] bcs=new byte[bs.length+b.length];
		System.arraycopy(bs, 0, bcs, 0, bs.length);
		System.arraycopy(b, 0, bcs, bs.length,b.length);
		return bcs;
	}
	

	
	public static String Byte2String(byte[] buf) {
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < buf.length; i++) {
			int v = buf[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}
	
	
	public static synchronized HashSet<HIMInfoStore> getHimInfoStores() {
		return himInfoStores;
	}


}
