package com.edu.ncu.drawlandmark;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


public class TimeService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                Log.d("定時成功","定時成功");
                showTimeUP();

            }}, 1800000);

 /*       new Thread(new Runnable(){
            @Override
            public void run() {//可以在该线程中做需要处理的事
                Log.d("定時成功","定時成功");
                stopSelf();//关闭服务


                //showTimeUP();  //需要再思考
            }
        }).start();

        AlarmManager manger=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(TimeService.this,AlarmReceiver.class);//广播接收
       // Intent i=new Intent(TimeService.this,TimeUpActivity.class);//广播接收
        //PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity2_Text.this, 0, intent, 0);//意图为开启活动
        PendingIntent pendingIntent=PendingIntent.getBroadcast(TimeService.this, 0, i, 0);//意图为开启广播
        long triggerAtTime = SystemClock.elapsedRealtime();//开机至今的时间毫秒数
        triggerAtTime=triggerAtTime+10*1000;//比开机至今的时间增长10秒
        manger.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);//设置为开机至今的模式，时间，PendingIntent

*/
        return super.onStartCommand(intent, flags, startId);
    }

    public void showTimeUP(){
        Intent intent = new Intent(this, TimeUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);

    }


}

