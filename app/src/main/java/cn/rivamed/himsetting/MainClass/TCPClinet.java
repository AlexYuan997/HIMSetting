package cn.rivamed.himsetting.MainClass;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import cn.rivamed.himsetting.Utils.HIMInfoStore;
import cn.rivamed.himsetting.Utils.UtilsFields;

public class TCPClinet implements Runnable {

	private   static final String TAG="TCPClinet";

	private Socket socket;
	private int accessnumber;
	private byte[] b = UtilsFields.getThree0XFF();

	public void run() {
		sendCommnd();
	}

	public TCPClinet(Socket socket,int accessnumber) {
		this.accessnumber=accessnumber;
		this.socket = socket;
	}
	
	public TCPClinet() {
	}

	public void sendCommnd() {
		try {
            OutputStream outputStream = socket.getOutputStream();
			outputStream.write(b);
			String command = UtilsFields.CONNECT;
			byte[] bs = command.getBytes(UtilsFields.GB2312);
			outputStream.write(UtilsFields.arrysCombine(bs, b));


			byte[] len = new byte[1024];
			InputStream inputStream = socket.getInputStream();
			inputStream.read(len, 0, len.length);
			String leninfotostring = new String(len, Charset.forName(UtilsFields.GB2312));


			if (leninfotostring.contains(UtilsFields.COMOK)) {
				Log.d(TAG,"获取的屏幕信息"+ leninfotostring);
				String[] info = leninfotostring.split(",");
				if (info[5] != null) {
					HIMInfoStore himInfoStore = new HIMInfoStore(info[5], accessnumber, this);
					Log.d(TAG, "sendCommnd: 存储的信息"+himInfoStore.toString());
					UtilsFields.getHimInfoStores().add(himInfoStore);
					writeTxt(accessnumber+"",outputStream);
				}
			} else {
				Log.d(TAG, "sendCommnd: 错误的屏幕信息包");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	//写第二个信息
	public  void writeTxt1(String txt1,OutputStream outputStream,int accessnumber){
		byte[] byte1;
		byte[] bcs = null;
		String s="            编号："+accessnumber;
		String string = String.format("t1.txt=\"%S\"", txt1+s);
		try {
			byte1 = string.getBytes(UtilsFields.GB2312);
			bcs = UtilsFields.arrysCombine(byte1, b);
			outputStream.write(b);
			outputStream.write(bcs);
		}catch (IOException e){
			e.printStackTrace();
			Log.d(TAG, "writeTxt1:无法写入HIM屏幕 ");
		}
		Log.d(TAG, new String(bcs, Charset.forName(UtilsFields.GB2312)));
	}

	//写第一个信息
	public  void writeTxt(String txt0,OutputStream outputStream){
		byte[] byte1;
		byte[] bcs = null;
		String string = String.format("t0.txt=\"%S\"", txt0);
		try {
			byte1 = string.getBytes(UtilsFields.GB2312);
			bcs = UtilsFields.arrysCombine(byte1, b);
			outputStream.write(b);
			outputStream.write(bcs);
		}catch (IOException e){
			e.printStackTrace();
			Log.d(TAG, "writeTxt1:无法写入HIM屏幕 ");
		}
		Log.d(TAG, new String(bcs, Charset.forName(UtilsFields.GB2312)));
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getAccessnumber() {
		return accessnumber;
	}

	public void setAccessnumber(int accessnumber) {
		this.accessnumber = accessnumber;
	}

}
