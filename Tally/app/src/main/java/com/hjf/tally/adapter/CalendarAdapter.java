package com.hjf.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hjf.tally.R;

import java.util.List;

/**
 * @author hjf
 * @create 2020-12-27 21:02
 */
public class CalendarAdapter extends BaseAdapter {

    private Context context;

    private List<Integer> monthList;

    private int selectPosition;

    private int year;

    public int getYear() {
        return year;
    }

    public CalendarAdapter(Context context, List<Integer> monthList, int selectPosition, int year) {
        this.context = context;
        this.monthList = monthList;
        this.selectPosition = selectPosition;
        this.year = year;
    }

    public CalendarAdapter(Context context, List<Integer> monthList, int year) {
        this.context = context;
        this.monthList = monthList;
        this.year = year;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object getItem(int i) {
        return monthList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, viewGroup, false);
        TextView textView = view.findViewById(R.id.item_dialogcal_gv_tv);
        Integer month = monthList.get(i);
        textView.setText(year+"/"+month);
        if (selectPosition == i) {
            textView.setBackgroundResource(R.color.green_006400);
            textView.setTextColor(Color.WHITE);
        }else {
            textView.setBackgroundResource(R.color.grey_f3f3f3);
            textView.setTextColor(Color.BLACK);
        }
        return view;
    }
}
