package com.hjf.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hjf.tally.adapter.ChartVPAdapter;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.db.DBManager;
import com.hjf.tally.frag_chart.IncomeChartFragment;
import com.hjf.tally.frag_chart.OutcomeChartFragment;
import com.hjf.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    private TextView chartDataTv, chartOutTv, chartInTv;

    private Button chartOutBtn, chartInBtn;

    private ViewPager chartVp;

    private int year, month;

    private int selectYearPosition = -1, selectMonthPosition = -1;

    private List<Fragment> chartFragList;

    private IncomeChartFragment incomeChartFragment;

    private OutcomeChartFragment outcomeChartFragment;

    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        getCurrentTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initOutAndInData();
        initFrag();
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);

        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);

        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);
    }

    private void initOutAndInData() {
        setTextOfChartDataTv();

        float sumIncome = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_INCOME);
        int countItemIncome = DBManager.getCountItemOneMonth(year, month, TypeBean.KIND_INCOME);
        chartInTv.setText("共"+countItemIncome+"笔收入，￥ "+sumIncome);

        float sumOutcome = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_OUTCOME);
        int countItemOutcome = DBManager.getCountItemOneMonth(year, month, TypeBean.KIND_OUTCOME);
        chartOutTv.setText("共"+countItemOutcome+"笔支出，￥ "+sumOutcome);
    }

    private void setTextOfChartDataTv() {
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        chartDataTv.setText(year+"年"+monthStr+"月账单");
    }

    private void getCurrentTime() {
        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
    }

    private void initView() {
        chartDataTv = findViewById(R.id.chart_iv_date);
        chartOutTv = findViewById(R.id.chart_iv_out);
        chartInTv = findViewById(R.id.chart_iv_in);
        chartOutBtn = findViewById(R.id.chart_btn_out);
        chartInBtn = findViewById(R.id.chart_btn_in);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_calendar:
                CalendarDialog calendarDialog = new CalendarDialog(this);
                calendarDialog.show();
                calendarDialog.initCalendar(selectYearPosition, selectMonthPosition);
                calendarDialog.setDialogSize();

                calendarDialog.setOnClickListener(new CalendarDialog.OnClickListener() {
                    @Override
                    public void onClick(int changeYear, int changeMonth, int selectYearPos, int selectMonthPos) {
                        year = changeYear;
                        month = changeMonth;
                        initOutAndInData();
                        selectYearPosition = selectYearPos;
                        selectMonthPosition = selectMonthPos;

                        incomeChartFragment.changeTime(year, month);
                        outcomeChartFragment.changeTime(year, month);
                    }
                });
                break;
            case R.id.chart_btn_out:
                setButtonStyle(TypeBean.KIND_OUTCOME);
                chartVp.setCurrentItem(0);
                break;
            case R.id.chart_btn_in:
                setButtonStyle(TypeBean.KIND_INCOME);
                chartVp.setCurrentItem(1);
                break;
        }
    }

    private void setButtonStyle(int kind) {
        if (kind == TypeBean.KIND_OUTCOME) {
            chartOutBtn.setBackgroundResource(R.drawable.main_record_btn);
            chartOutBtn.setTextColor(Color.WHITE);
            chartInBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            chartInBtn.setTextColor(Color.BLACK);
        }else if (kind == TypeBean.KIND_INCOME) {
            chartOutBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            chartOutBtn.setTextColor(Color.BLACK);
            chartInBtn.setBackgroundResource(R.drawable.main_record_btn);
            chartInBtn.setTextColor(Color.WHITE);
        }
    }
}
