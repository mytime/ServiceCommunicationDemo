package com.yanwanfu.servicecommunicationdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 使用Intent与Service进行通信
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private EditText et_text;
    private TextView tv_out;
    private Intent i;

    private MyService.Binder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_text = (EditText) findViewById(R.id.et_text);
        tv_out = (TextView) findViewById(R.id.tv_out);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.sync_data).setOnClickListener(this);

        i = new Intent(MainActivity.this, MyService.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //获取新内容
                i.putExtra("data", et_text.getText().toString());
                startService(i);
                break;
            case R.id.btn_stop:
                stopService(i);
                break;
            case R.id.btn_bind:
                bindService(i, this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                unbindService(this);
                break;
            case R.id.sync_data:
                if (binder != null) {
                    binder.setData(et_text.getText().toString());
                }
                break;
        }

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //service : 是由MyService.onBind return出来的Binder实例对象
        binder = (MyService.Binder) service;
        //实现一个匿名接口
        binder.getService().setCallBack(new MyService.CallBack() {
            @Override
            public void onDataChanage(String data) {

                Message msg = new Message();         // 开始一个新的消息
                Bundle dataBundle = new Bundle();      // 开始一个新的数据包

                dataBundle.putString("data", data);   // 先把获取到的数据用键值对封装
                msg.setData(dataBundle);               // 再封装到Message
                handler.sendMessage(msg);           // 最后用Handler把msg发送出去


            }
        });

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    //使用Handler 异步处理UI
    //开始一个新的UI处理器
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = msg.getData().getString("data"); //接受msg数据包并拆开数据包，获取key=data的值，
            tv_out.setText(str);                          //改变tv_out的显示文字
        }
    };
}
