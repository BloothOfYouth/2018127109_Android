package com.hjf.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hjf.tally.*;

/**
 * @author hjf
 * @create 2020-12-27 16:58
 */
public class MoreDialog extends Dialog implements View.OnClickListener{

    private Button aboutBtn, settingBtn, recordBtn, detailBtn;

    private ImageView cancelIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);   //设置对话框显示布局
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        recordBtn = findViewById(R.id.dialog_more_btn_record);
        detailBtn = findViewById(R.id.dialog_more_btn_detail);
        cancelIv = findViewById(R.id.dialog_more_iv_cancel);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        detailBtn.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.dialog_more_btn_about:
                intent = new Intent(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent = new Intent(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_detail:
                intent = new Intent(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
        }
        cancel();
    }

    /**
     * 设置Dialog的尺寸和屏幕尺寸一致，以及位置
     */
    public void setDialogSize() {
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        attributes.width = (int)(display.getWidth());   //对话框窗口为屏幕宽度
        attributes.gravity = Gravity.BOTTOM;    //设置位置为底部
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}
