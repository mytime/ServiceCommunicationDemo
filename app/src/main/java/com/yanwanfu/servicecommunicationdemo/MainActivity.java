package com.yanwanfu.servicecommunicationdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 使用Intent与Service进行通信
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private EditText et_text;
    private Intent i;

    private MyService.Binder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_text = (EditText) findViewById(R.id.et_text);

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

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
