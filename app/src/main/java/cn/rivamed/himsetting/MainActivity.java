package cn.rivamed.himsetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import cn.rivamed.himsetting.MainClass.TCPClinet;
import cn.rivamed.himsetting.MainClass.TCPThreadServer;
import cn.rivamed.himsetting.Utils.HIMInfoStore;
import cn.rivamed.himsetting.Utils.UtilsFields;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText number;
    private EditText text0;
    private EditText text1;
    private Button setbutton;
    private Button clearbutton;
    private   static final String TAG="MainActivity";
    private static final int CLEAR_TXT=1;
    private static final int SET_TXT=2;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CLEAR_TXT:
                    number.setText(null);
                    text1.setText(null);
                    text0.setText(null);
                    break;
                case SET_TXT:
                    int num= Integer.parseInt(number.getText().toString());
                    Log.d(TAG, "handleMessage: 编号为："+num);
                    String t0=text0.getText().toString();
                    Log.d(TAG, "onClick: 第一行信息"+t0);
                    String t1=text1.getText().toString();
                    Log.d(TAG, "onClick: 第一行信息"+t1);
                    setSettingText1(num,t0,t1);
                    break;
                    default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number=findViewById(R.id.number);

        text0=findViewById(R.id.editText1);

        text1=findViewById(R.id.editText2);

        setbutton=findViewById(R.id.settingHIM);

        clearbutton=findViewById(R.id.clearinfo);

        setbutton.setOnClickListener(this);
        clearbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settingHIM:
                Log.d(TAG, "onClick: 进入设置数据btn");
/*               int num= Integer.parseInt(number.getText().toString());
                String t0=text0.getText().toString();
                Log.d(TAG, "onClick: 第一行信息"+t0);
                String t1=text1.getText().toString();
                Log.d(TAG, "onClick: 第一行信息"+t1);
                setSettingText(num,t0,t1);*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=SET_TXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            case R.id.clearinfo:
                Log.d(TAG, "onClick: 进入清空数据btn");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=CLEAR_TXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
                default:
                    break;
        }
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }


    public  void setSettingText1(int number,String txt0,String txt1){
        HashMap<Integer,Socket> socketstore=new HashMap<Integer,Socket>();
        socketstore= TCPThreadServer.getSocketstore();
        TCPClinet tcpClinet=new TCPClinet();

        for(Map.Entry<Integer,Socket> entry: socketstore.entrySet())
        {
            if (number==entry.getKey()){
                Socket socket=entry.getValue();
                try {
                OutputStream outputStream=socket.getOutputStream();
                tcpClinet.writeTxt(txt0,outputStream);
                tcpClinet.writeTxt1(txt1,outputStream,entry.getKey());
            }catch (Exception e){
                    e.printStackTrace();
                }
            }
/*            else if (number!=entry.getKey()){
//                Toast.makeText(MainActivity.this,"没有这个编号",Toast.LENGTH_SHORT).show();
            }*/
            System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.set_item:
                Intent intent=new Intent(MainActivity.this,SettingNumber.class);
                startActivity(intent);
                break;
            case R.id.about_app:
                Intent intent1=new Intent(MainActivity.this,Manul.class);
                startActivity(intent1);
                break;
                default:
                    break;
        }
        return true;
    }
}
