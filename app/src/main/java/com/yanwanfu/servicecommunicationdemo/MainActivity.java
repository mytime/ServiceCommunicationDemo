package com.yanwanfu.servicecommunicationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 使用Intent与Service进行通信
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_text;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_text = (EditText) findViewById(R.id.et_text);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);

        i = new Intent(MainActivity.this,MyService.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                //获取新内容
                i.putExtra("data",et_text.getText().toString());
                startService(i);
                break;
            case R.id.btn_stop:
                stopService(i);
                break;
        }

    }
}
