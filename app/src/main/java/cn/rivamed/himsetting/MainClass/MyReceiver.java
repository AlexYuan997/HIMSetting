package cn.rivamed.himsetting.MainClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.rivamed.himsetting.MyService;

public class MyReceiver extends BroadcastReceiver {
    String TAG="MyReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
            Intent  intent1=new Intent(context,MyService.class);
            context.startService(intent1);
        Toast.makeText(context,"LCD屏显示工具已经启动",Toast.LENGTH_LONG).show();
    }
}
