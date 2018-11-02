package cn.rivamed.himsetting.MainClass;

import android.util.Log;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import cn.rivamed.himsetting.Utils.AccessNumber;
import cn.rivamed.himsetting.Utils.UtilsFields;

public class TCPClinet implements Runnable {
	private static  HashMap<Integer,Socket> socketstore=new HashMap<Integer,Socket>();

	public static  HashMap<Integer, Socket> getSocketstore() {
		return socketstore;
	}

	private   static final String TAG="TCPClinet";

	private Socket socket;

	private int accessnumber;

	private byte[] b = UtilsFields.getThree0XFF();

	public void run() {
		sendCommnd();
	}

	public TCPClinet(Socket socket) {

		this.socket = socket;
	}

	public TCPClinet() {
	}

	public void sendCommnd() {
		try {
			//先发送3个0xFF
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(b);

			//发送连接指令
			String command = UtilsFields.CONNECT;
			byte[] bs = command.getBytes(UtilsFields.GB2312);
			outputStream.write(UtilsFields.arrysCombine(bs, b));


            //获取LCD屏返回的数据包
			byte[] len = new byte[1024];
			InputStream inputStream = socket.getInputStream();
			inputStream.read(len, 0, len.length);
			String leninfotostring = new String(len, Charset.forName(UtilsFields.GB2312));


			if (leninfotostring.contains(UtilsFields.COMOK)) {
				final String[] info = leninfotostring.split(",");
				Log.d(TAG, "序列号"+info[5] );
				List<LCDinfo> lcDinfoList= DataSupport.findAll(LCDinfo.class);

				boolean b=getBooleanHaveInfo5(lcDinfoList,info[5]);

				if (info[5] != null){

					if (b==false){
						accessnumber=TCPClinet.getNumberAlone(AccessNumber.getAccessNumber().getNumber(),lcDinfoList);
						Log.d(TAG, "sendCommnd:屏幕编号 "+accessnumber);

						LCDinfo lcDinfo=new LCDinfo();
						lcDinfo.setSerialnumber(info[5]);
					    lcDinfo.setNumber(accessnumber);
						lcDinfo.save();
						socketstore.put(accessnumber,socket);
						writeTxt("屏幕编号:"+accessnumber,outputStream);
					}else {
						for (LCDinfo lc:lcDinfoList
								) {
							if (lc.getSerialnumber()!=null){
							if (lc.getSerialnumber().equals(info[5])){
								socketstore.put(lc.getNumber(),socket);
								writeTxt(lc.getFristtxt(),outputStream);
								writeTxt1(lc.getSecondtxt(),outputStream,lc.getNumber());
								break;
							}
						}}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//判断获取的串口屏编号是否存在，如果存在加1，在判断，直到不存在的时候返回

	public static int getNumberAlone(int accessnumber,List<LCDinfo> lcDinfoList1){
		int i=accessnumber;
		boolean b=true;

		List<LCDinfo> lcDinfoList=lcDinfoList1;
		while (b){
		for (LCDinfo lc:lcDinfoList
			 ) {
			if (i==lc.getNumber()){
				i++;
			}else {
				b=false;
				break;
			}
			}
		}
		return i;
	}



	//判断是否已经存储
	public boolean getBooleanHaveInfo5( List<LCDinfo> lcDinfoList,String info){
		boolean b=false;
		if(lcDinfoList!=null){
		for (LCDinfo lcDinfo:lcDinfoList
				) {
			if (lcDinfo.getSerialnumber()!=null){
			if (lcDinfo.getSerialnumber().equals(info)){
				b=true;
				break;
			}
		}}
		}
		return  b;
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



}
