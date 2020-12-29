package com.hjf.tally.frag_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hjf.tally.R;
import com.hjf.tally.adapter.ChartLvItemAdapter;
import com.hjf.tally.bean.BarChartItemBean;
import com.hjf.tally.bean.ChartLvItemBean;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单详情页面中的图表的基础模块（用于支出和收入模块继承）
 */
public class BaseChartFragment extends Fragment {

    private ListView listView;

    private List<ChartLvItemBean> chartLvItemList = new ArrayList<>();

    private ChartLvItemAdapter chartLvItemAdapter;

    private View view;

    private BarChart barChart;

    private TextView chartTv;

    private int year, month, kind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_chart, container, false);
        listView = view.findViewById(R.id.frag_chart_lv);
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        chartLvItemAdapter = new ChartLvItemAdapter(getContext(), this.chartLvItemList);
        listView.setAdapter(chartLvItemAdapter);
        addLvHeaderView();
        return view;
    }

    protected void loadDataToLV(int kind) {
        this.kind = kind;
        List<ChartLvItemBean> list = DBManager.getChartLvItemList(year, month, this.kind);
        chartLvItemList.clear();
        chartLvItemList.addAll(list);
        chartLvItemAdapter.notifyDataSetChanged();
    }

    protected void loadData(int kind) {
        loadDataToLV(kind);
        loadDataToChart();
    }

    private void loadDataToChart() {
        //设置坐标轴
        setAxis();
        //添加数据
        setAxisData();
    }

    private void setAxisData() {
        List<IBarDataSet> sets = new ArrayList<>();
        //获取这个月每天的支出总金额，
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);
            //设置有多少根柱子
            List<BarEntry> barEntries1 = new ArrayList<>();

            int maxDayOfMonth = getMaxDayOfMonth();
            for (int i = 0; i < maxDayOfMonth; i++) {
                //初始化每一根柱子，添加到柱状图当中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();   //获取日期
                // 根据天数，获取x轴的位置
                int xIndex = day-1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSumMoney());
            }
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSet1.setValueTextSize(8f); // 值的大小

            if (kind == TypeBean.KIND_INCOME) {
                barDataSet1.setColor(Color.parseColor("#006400")); // 柱子的颜色
            }else if (kind == TypeBean.KIND_OUTCOME) {
                barDataSet1.setColor(Color.RED); // 柱子的颜色
            }

            // 设置柱子上数据显示的格式
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // 此处的value默认保存一位小数
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });
            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    private void addLvHeaderView() {
        view = getLayoutInflater().inflate(R.layout.item_chartfrag_lv_top, null);
        listView.addHeaderView(view);
        barChart = view.findViewById(R.id.item_chartfrag_top_chart);
        chartTv = view.findViewById(R.id.item_chartfrag_top_tv);
        //设置柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        //设置柱状图的内边图
        barChart.setExtraOffsets(20, 20, 20, 20);
    }

    /**
     * 设置柱状图坐标轴的显示
     */
    private void setAxis() {
        setXAxis();
        setYAxis();
    }

    /**
     * 设置y轴
     */
    private void setYAxis() {
        //获取本月收入最高的一天为多少，将他设定为y轴的最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);   // 将最大金额向上取整
        //设置y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        //设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    /**
     * 设置x轴
     */
    private void setXAxis() {
        //设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //设置x轴显示在下方
        xAxis.setDrawGridLines(true);  //设置绘制该轴的网格线

        int labelCount = getMaxDayOfMonth();
        //设置x轴标签的个数
        xAxis.setLabelCount(labelCount);

        xAxis.setTextSize(12f);  //x轴标签的大小
        //设置X轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month+"-1";
                }
                if (val==14) {
                    return month+"-15";
                }
                //根据不同的月份，显示最后一天的位置
                if (month==2) {
                    if (year%4==0&&year%100!=0||year%400==0) {
                        //是闰年
                        if (val == 28) {
                            return month+"-29";
                        }
                    }else {
                        //不是闰年
                        if (val == 27) {
                            return month+"-28";
                        }
                    }
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";
            }
        });
        xAxis.setYOffset(10); // 设置标签对x轴的偏移量，垂直方向
    }

    private int getMaxDayOfMonth() {
        int labelCount = 0;
        if (month==2) {
            if (year%4==0&&year%100!=0||year%400==0) {
                //是闰年
                labelCount = 29;
            }else {
                //不是闰年
                labelCount = 28;
            }
        }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
            labelCount = 31;
        }else if(month==4||month==6||month==9||month==11){
            labelCount = 30;
        }
        return labelCount;
    }


    public void changeTime(int year, int month, int kind) {
        this.year = year;
        this.month = month;
        // 清空柱状图当中的数据
        barChart.clear();
        barChart.invalidate();
        loadData(kind);
    }
}
