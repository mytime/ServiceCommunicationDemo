package com.yanwanfu.servicecommunicationdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    private boolean running = false;
    private String data = "service默认内容";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回自定义Binder的实例对象，在onServiceConnected 中接收（IBinder service）
        return new Binder();
    }

    //自定义Binder
    public class Binder extends android.os.Binder {
        //set 方法
        public void setData(String data) {
            //重新给data赋值
            MyService.this.data = data;
        }
    }

    //第一次启动startService()执行onCreate()，
    //反复执行startService()时不再执行 onCreate()，而是执行onStartCommand()方法
    @Override
    public void onCreate() {
        super.onCreate();
        iniMsg();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //从intent获取传递过来的数据
        data = intent.getStringExtra("data");

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }


    private void iniMsg() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                running = true;
                //无限循环
                while (running) {
                    System.out.println(data);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
