package com.yanwanfu.servicecommunicationdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 执行顺序
 * startService:
 *      -> onCreate
 *      -> onStartCommand
 *      -> onDestroy
 *
 * bindService :
 *      -> onCreate
 *      -> onBind
 *      -> onServicConnected
 *
 */
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

        //向外部公开一个绑定事件的函数
        //绑定的事件是：
        public MyService getService(){
            return MyService.this;
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
        running = true;
        new Thread() {
            @Override
            public void run() {
                super.run();

                int i = 0;
                //无限循环
                while (running) {
                    i++;
                    String str = i+":"+data;
                    System.out.println(str);
                    //把数据发送到服务的外部
                    //由public MyService getService()向外部公开数据
                    if (callBack != null){
                        callBack.onDataChanage(str);
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    //接口
    public static interface CallBack{
        void onDataChanage(String data);
    }

    //CallBack类型的变量
    private CallBack callBack = null;
    // 向外部公开 设置和获取callBack变量
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    public CallBack getCallBack() {
        return callBack;
    }
}
