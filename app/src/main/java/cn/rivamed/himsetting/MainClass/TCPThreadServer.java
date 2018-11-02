package cn.rivamed.himsetting.MainClass;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import cn.rivamed.himsetting.Utils.AccessNumber;


public class TCPThreadServer  {

	private static Socket socket;

	private   static final String TAG="TCPThreadServer";

	public static void instanceHMIText(ServerSocket server) {
		try {

			socket = server.accept();
			if (socket!=null) {
				TCPClinet clinet = new TCPClinet(socket);
				new Thread(clinet).start();
			}else {
				Log.d(TAG, "instanceHMIText: socke是关闭的");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
