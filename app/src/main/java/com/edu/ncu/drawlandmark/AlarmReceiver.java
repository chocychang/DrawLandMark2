package com.edu.ncu.drawlandmark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  Log.d("定時接收","定時接收");
        Intent i=new Intent(context,TimeService.class);
        context.startService(i);//开启AlarmService服务
    }
}