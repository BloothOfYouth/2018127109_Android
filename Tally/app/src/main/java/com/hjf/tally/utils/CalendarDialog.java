package com.hjf.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;

import com.hjf.tally.R;
import com.hjf.tally.adapter.CalendarAdapter;
import com.hjf.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjf
 * @create 2020-12-27 19:42
 */
public class CalendarDialog extends Dialog implements View.OnClickListener{

    private LinearLayout linearLayout;

    private GridView gridView;

    private ImageView calendarCancel;

    private List<TextView> hsvList;

    private List<Integer> yearList;

    private TextView calendarEmpty;

    private CalendarAdapter calendarAdapter;
    /**
     * 表示正在被点击的年份
     */
    private int selectYearPos = -1;
    /**
     * 表示正在被点击的月份
     */
    private int selectMonthPos = -1;

    private int yearOfTextView;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void initCalendar(int selectYearPosition, int selectMonthPosition) {
        if (selectYearPosition != -1 && selectMonthPosition != -1) {
            selectYearPos = selectYearPosition;
            selectMonthPos = selectMonthPosition;
        }
        //判断是否有YearList
        if (haveYearList()) {
            //向横向的ScrollView当中添加TextView
            initLinearLayout();
            //初始化GridView
            initGridView();
        }
    }

    public interface OnClickListener {
        void onClick(int year, int month, int selectYearPos, int selectMonthPos);
    }

    public CalendarDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        linearLayout = findViewById(R.id.dialog_calendar_ly);
        gridView = findViewById(R.id.dialog_calendar_gv);
        calendarCancel = findViewById(R.id.dialog_calendar_iv);
        calendarEmpty = findViewById(R.id.dialog_calendar_tv_empty);
        calendarCancel.setOnClickListener(this);

    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        Integer year = yearList.get(yearOfTextView);
        List<Integer> monthList = DBManager.getMonthList(year);
        int needSelected = -1;
        if (selectMonthPos == -1) {
            selectMonthPos = monthList.size() - 1;
            needSelected = selectMonthPos;
        }else {
            if (yearOfTextView == selectYearPos) {
                needSelected = selectMonthPos;
            }
        }
        calendarAdapter = new CalendarAdapter(getContext(), monthList, needSelected, year);
        gridView.setAdapter(calendarAdapter);
        setGVListener();
    }

    private void setGVListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                calendarAdapter.setSelectPosition(position);
                calendarAdapter.notifyDataSetChanged();
                if (onClickListener != null) {
                    selectMonthPos = position;
                    selectYearPos = yearOfTextView;
                    int month = (int) calendarAdapter.getItem(position);
                    onClickListener.onClick(calendarAdapter.getYear(), month, selectYearPos, selectMonthPos);
                    cancel();
                }
            }
        });
    }

    private void initLinearLayout() {
        hsvList = new ArrayList<>();    //将添加进入线性布局当中TextView进行统一管理

        for (Integer year : yearList) {
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            linearLayout.addView(view);
            TextView textView = view.findViewById(R.id.item_dialogcal_hsv_tv);
            textView.setText(year+"");
            hsvList.add(textView);
        }
        if (selectYearPos == -1) {
            yearOfTextView = selectYearPos = hsvList.size() - 1; //设置当前被选中的是最近的年份
        }else {
            yearOfTextView = selectYearPos;
        }
        changeYearTvBg(selectYearPos); //修改被选中年份的背景和文字颜色
        setAllHsvListListener();
    }

    private boolean haveYearList() {
        yearList = DBManager.getYearList();
        // 判断是否有数据
        if (yearList.size() == 0) {
            //没有数据就显示无数据的图
            linearLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            calendarEmpty.setVisibility(View.VISIBLE);
            return false;
        }
        linearLayout.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.VISIBLE);
        calendarEmpty.setVisibility(View.GONE);
        return true;
    }

    private void setAllHsvListListener() {
        for (int i = 0; i < hsvList.size(); i++) {
            final int finalI = i;
            hsvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeYearTvBg(finalI);
                    yearOfTextView = finalI;
                    initGridView();
                }
            });
        }
    }

    /**
     * 修改被选中年份的背景和文字颜色
     * @param selectYearPos
     */
    private void changeYearTvBg(int selectYearPos) {
        //先将所有TextView设置成为被选中的样式
        for (TextView textView : hsvList) {
            textView.setBackgroundResource(R.drawable.dialog_btn_bg);
            textView.setTextColor(Color.BLACK);
        }
        //再将被选中的TextView设置成为选中的样式
        hsvList.get(selectYearPos).setBackgroundResource(R.drawable.main_record_btn);
        hsvList.get(selectYearPos).setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
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
        attributes.gravity = Gravity.TOP;    //设置位置为顶部
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}
