package cn.rivamed.himsetting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

import cn.rivamed.himsetting.MainClass.TCPThreadServer;

public class MyService extends Service {
    String TAG="MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 启动服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //调用服务启动监听串口服务器，实现LCD屏幕的输出
        final ServerSocket server ;
        try {
            server = new ServerSocket(8014);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        TCPThreadServer.instanceHMIText(server);
                    }
                } }).start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onStartCommand: 接受客户端有错误");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
