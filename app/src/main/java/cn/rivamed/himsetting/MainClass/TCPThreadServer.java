package cn.rivamed.himsetting.MainClass;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import cn.rivamed.himsetting.Utils.AccessNumber;


public class TCPThreadServer implements Serializable {

	private static Socket socket;

	private   static final String TAG="TCPThreadServer";

	private static HashMap<Integer,Socket> socketstore=new HashMap<Integer,Socket>();

	public static HashMap<Integer, Socket> getSocketstore() {
		return socketstore;
	}

	public static void instanceHMIText(ServerSocket server) {
		try {
			Log.d(TAG, "instanceHMIText: 开始接受");
			int i=0;
			int j=0;
			while (true){
			socket = server.accept();
				Log.d(TAG, "instanceHMIText: 接收到数据"+j++);
			if (socket != null) {
				Log.d(TAG, "instanceHMIText: 接收到数据"+i++);
				int accessnumber=AccessNumber.getAccessNumber().getNumber();
				Log.d(TAG, "instanceHMIText: 编号为："+accessnumber);
				TCPClinet clinet = new TCPClinet(socket, accessnumber);
				socketstore.put(accessnumber,socket);
				new Thread(clinet).start();
			}else {
				Log.d(TAG, "instanceHMIText: 没有接收到串口屏信息");
			}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
