package com.hjf.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.hjf.tally.R;

import java.util.Date;

/**
 * 在记录页面弹出时间对话框
 * @author hjf
 * @create 2020-12-25 0:48
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private DatePicker datePicker;

    private TimePicker timePicker;

    private Button ensureBtn, cancelBtn;

    private OnEnsureListener onEnsureListener;

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public TimePicker getTimePicker() {
        return timePicker;
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public interface OnEnsureListener {
        void onEnsure(String time);
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        datePicker = findViewById(R.id.dialog_time_dp);
        timePicker = findViewById(R.id.dialog_time_tp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        datePicker.setMaxDate(new Date().getTime());

        //这句隐藏头布局
        ((LinearLayout) ((ViewGroup)datePicker.getChildAt(0)).getChildAt(0)).setVisibility(View.GONE);

        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);  //设置点击事件不弹键盘
        timePicker.setIs24HourView(true);   //设置时间显示为24小时
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear();  //选择年份
                int month = datePicker.getMonth()+1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if (month<10){
                    monthStr = "0"+month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    dayStr="0"+dayOfMonth;
                }

                //获取输入的小时和分钟
                String hourStr = String.valueOf(timePicker.getHour());
                String minuteStr = String.valueOf(timePicker.getMinute());
//                if (TextUtils.isEmpty(hourStr) || TextUtils.isEmpty(minuteStr)) {
//                    Toast.makeText(getContext(), "小时和分钟不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                int hour = Integer.parseInt(hourStr);
                int minute = Integer.parseInt(minuteStr);
                minuteStr=String.valueOf(minute);
                if (hour<10){
                    hourStr="0"+hour;
                }
                if (minute<10){
                    minuteStr="0"+minute;
                }
                String timeFormat = year+"年"+monthStr+"月"+dayStr+"日 "+hourStr+":"+minuteStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat);
                }
                break;
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
        }
    }
}
