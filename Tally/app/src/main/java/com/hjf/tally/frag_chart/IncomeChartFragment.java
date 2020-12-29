package com.hjf.tally.frag_chart;

import com.hjf.tally.bean.TypeBean;

/**
 * @author hjf
 * @create 2020-12-28 17:58
 */
public class IncomeChartFragment extends BaseChartFragment {
    @Override
    public void onResume() {
        super.onResume();
        loadData(TypeBean.KIND_INCOME);
    }

    public void changeTime(int year, int month) {
        super.changeTime(year, month, TypeBean.KIND_INCOME);
    }
}
