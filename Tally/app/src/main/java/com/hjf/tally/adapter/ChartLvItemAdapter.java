package com.hjf.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjf.tally.R;
import com.hjf.tally.bean.ChartLvItemBean;
import com.hjf.tally.utils.FloatUtils;

import java.util.List;

/**
 * @author hjf
 * @create 2020-12-28 18:58
 */
public class ChartLvItemAdapter extends BaseAdapter {

    private Context context;

    private List<ChartLvItemBean> chartLvItemList;

    public ChartLvItemAdapter(Context context, List<ChartLvItemBean> chartLvItemList) {
        this.context = context;
        this.chartLvItemList = chartLvItemList;
    }

    @Override
    public int getCount() {
        return chartLvItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return chartLvItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_chartfrag_lv, viewGroup, false);

        ImageView chartSImage = view.findViewById(R.id.item_chartfrag_iv);
        TextView chartTypename = view.findViewById(R.id.item_chartfrag_tv_type);
        TextView chartTotalMoney = view.findViewById(R.id.item_chartfrag_tv_sum);
        TextView chartRatio = view.findViewById(R.id.item_chartfrag_tv_pert);

        ChartLvItemBean chartLvItemBean = chartLvItemList.get(i);
        chartSImage.setBackgroundResource(chartLvItemBean.getsImageId());
        chartTypename.setText(chartLvItemBean.getTypename());
        chartTotalMoney.setText("￥ "+chartLvItemBean.getTotalMoney());
        String percentage = FloatUtils.decimalToPercentage(chartLvItemBean.getRatio());
        chartRatio.setText(percentage);

        return view;
    }
}
