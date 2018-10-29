package cn.rivamed.himsetting;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

import cn.rivamed.himsetting.MainClass.TCPThreadServer;
import cn.rivamed.himsetting.Utils.AccessNumber;

public class SettingNumber extends AppCompatActivity implements View.OnClickListener {
    private   static final String TAG="SettingNumber";
    private Button button;
    private static int s=0;
    ProgressBar processBar;
    private  static final int UPDATE_BAR=1;
    private  static final int START_SERVER=2;
    private ServerSocket server;

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_BAR:
                    processBar.setVisibility(View.INVISIBLE);
                break;
                    default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_number);
        processBar=findViewById(R.id.probar);

        button=findViewById(R.id.setbnt);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (s == 0) {
            s++;

            processBar.setVisibility(View.VISIBLE);

            try {
                server = new ServerSocket(8014);
                if (server.isClosed()) {
                    Toast.makeText(SettingNumber.this, "连接关闭！", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "ip:" + server.getLocalSocketAddress() + "----port: " + server.getLocalPort());
                Log.d(TAG, "服务器搭建成功************");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: 启动服务器");
                        TCPThreadServer.instanceHMIText(server);
                    }
                }).start();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.d(TAG, "onClick：接受信息失败 ");
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000*60);
                        Message message = new Message();
                        message.what = UPDATE_BAR;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
