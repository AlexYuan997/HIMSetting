package cn.rivamed.himsetting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.Dataset;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.himsetting.MainClass.LCDinfo;
import cn.rivamed.himsetting.MainClass.TCPClinet;


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
                    boolean b=isContantNumber(num);
                    if (t0!=null&&t1!=null&&num!=0&&b==true){
                        setSettingText1(num,t0,t1);
                    }else if (b==false){
                        Toast.makeText(MainActivity.this,"编号不存在",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"输入的内容为空,请输入正确的内容",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:break;
            }
        }
    };



    //判断是否有这个编号
    public boolean isContantNumber(int num){
        boolean b=false;

        List<LCDinfo> lcDinfoList= DataSupport.findAll(LCDinfo.class);

        for (LCDinfo lcd:lcDinfoList
                ) {
            if (lcd.getNumber()==num){
                b=true;
                break;
            }
        }


        return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //软件启动的时候启动服务


        number=findViewById(R.id.number);

        text0=findViewById(R.id.editText1);

        text1=findViewById(R.id.editText2);

        setbutton=findViewById(R.id.settingHIM);

        clearbutton=findViewById(R.id.clearinfo);

        setbutton.setOnClickListener(this);
        clearbutton.setOnClickListener(this);

        //先初始化数据库，先往里面存点无用的数据,不然子线程中会出错
        LCDinfo lcDinfo=new LCDinfo();
        lcDinfo.setSerialnumber("dasdsadsad");
        lcDinfo.save();

        Intent intent=new Intent(MainActivity.this,MyService.class);
        startService(intent);





//    DataSupport.deleteAll(LCDinfo.class);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settingHIM:
                Log.d(TAG, "onClick: 进入设置数据btn");
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



   public void    clearAllData(){
        DataSupport.deleteAll(LCDinfo.class);
    }


    public  void setSettingText1(int number,String txt0,String txt1){
        HashMap<Integer,Socket> socketstore=new HashMap<Integer,Socket>();
        socketstore= TCPClinet.getSocketstore();

        TCPClinet tcpClinet=new TCPClinet();

        for(Map.Entry<Integer,Socket> entry: socketstore.entrySet())
        {
            Log.d(TAG, "setSettingText1:包含的编号 "+entry.getKey());
            if (number==entry.getKey()){
                Socket socket=entry.getValue();
                try {
                    //将传进来的数据输出到LCD屏幕上
                    OutputStream outputStream=socket.getOutputStream();
                    tcpClinet.writeTxt(txt0,outputStream);
                    tcpClinet.writeTxt1(txt1,outputStream,entry.getKey());

                    //存储想要存入的两个文本
                    LCDinfo lcDinfo=new LCDinfo();
                    lcDinfo.setFristtxt(txt0);
                    lcDinfo.setSecondtxt(txt1);
                    lcDinfo.updateAll("number=?",String.valueOf(number));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("Key: "+ entry.getKey()+ " Value: "+entry.getValue());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_app:
                Intent intent1=new Intent(MainActivity.this,Manul.class);
                startActivity(intent1);
                break;
            case R.id.clear_data:
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("清空数据库");
                dialog.setMessage("您确定清空数据库吗，清空后数据将不可恢复");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllData();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
        return true;
    }
}
