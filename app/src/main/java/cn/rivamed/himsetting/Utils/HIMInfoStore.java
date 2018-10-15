package cn.rivamed.himsetting.Utils;

import java.io.Serializable;

import cn.rivamed.himsetting.MainClass.TCPClinet;

public class HIMInfoStore {
	

	private String serialnumber;
	private int singlenumber;
	private TCPClinet tcpClinet;
	
	
	
	public HIMInfoStore() {
		super();
	}
	
	public HIMInfoStore(String serialnumber, int singlenumber, TCPClinet tcpClinet) {
		super();
		this.serialnumber = serialnumber;
		this.singlenumber = singlenumber;
		this.tcpClinet = tcpClinet;
	}
	
	
	@Override
	public String toString() {
		return "HIMInfoStore [serialnumber=" + serialnumber + ", singlenumber=" + singlenumber + ", tcpClinet="
				+ tcpClinet + "]";
	}

	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public int getSinglenumber() {
		return singlenumber;
	}
	public void setSinglenumber(int singlenumber) {
		this.singlenumber = singlenumber;
	}
	public TCPClinet getTcpClinet() {
		return tcpClinet;
	}
	public void setTcpClinet(TCPClinet tcpClinet) {
		this.tcpClinet = tcpClinet;
	}
	
}
