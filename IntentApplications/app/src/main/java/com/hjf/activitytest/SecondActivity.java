package com.hjf.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

//        Intent intent = getIntent();
        // 从 Intent 中取出数据
//        String data = intent.getStringExtra("extra_data");
//        Log.d("SecondActivity", data);

        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 暂存数据到 Intent 中
                intent.putExtra("data_return", "Hello FirstActivity");
                // 向上一个活动返回数据，第一个参数一般为 RESULT_OK
                setResult(RESULT_OK, intent);
                // 销毁当前活动
                finish();
            }
        });
    }

    // 绑定按下返回键时的逻辑，与按钮点击的逻辑一致
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK, intent);
        finish();
    }
}
