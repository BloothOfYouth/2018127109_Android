package com.hjf.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实例化 Intent，第一个参数为当前上下文，第二个参数为要启动的目标活动
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);

                // 实例化 Intent，并设置行为是 ACTION_START，不设置分类则默认分类是 DEFAULT
//                Intent intent = new Intent("com.hjf.activitytest.ACTION_START");
                // 为 Intent 设置分类是 MY_CATEGORY
//                intent.addCategory("com.hjf.activitytest.MY_CATEGORY");

                // 实例化 Intent，并设置行为是 ACTION_VIEW
//                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 设置 Intent 的 Uri
//                intent.setData(Uri.parse("https://www.baidu.com"));
                // 启动活动，会打开系统浏览器访问设置的网址

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));

//                String data = "Hello SecondActivity";
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                // 暂存数据到 Intent 中
//                intent.putExtra("extra_data", data);

                // 启动目标活动
//                startActivity(intent);

                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                // 启动活动，并接受活动销毁时返回的数据，第二个参数为请求码，用于判断数据的来源
                startActivityForResult(intent, 1);
            }
        });

    }


    // 下一个活动被销毁时的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 判断数据来源
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
                break;
            default:
        }
    }
}
