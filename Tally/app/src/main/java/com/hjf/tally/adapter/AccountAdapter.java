package com.hjf.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjf.tally.R;
import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.bean.TypeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author hjf
 * @create 2020-12-26 15:02
 */
public class AccountAdapter extends BaseAdapter {

    private Context context;

    private List<AccountBean> accountList;

    private String currentTime;

    public AccountAdapter(Context context, List<AccountBean> accountList) {
        this.context = context;
        if (accountList != null) {
            this.accountList = accountList;
        }else {
            this.accountList = new ArrayList<>();
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        currentTime = dateFormat.format(date);
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Override
    public Object getItem(int i) {
        return accountList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_main_lv, viewGroup, false);

        TextView title = view.findViewById(R.id.item_main_lv_title);
        TextView detail = view.findViewById(R.id.item_main_lv_detail);
        TextView time = view.findViewById(R.id.item_main_lv_time);
        ImageView image = view.findViewById(R.id.item_main_lv);
        TextView money = view.findViewById(R.id.item_main_lv_money);

        //设置布局当中的控件的值
        AccountBean accountBean = accountList.get(i);
        String timeOfAccount = accountBean.getTime();;
        if (timeOfAccount.contains(currentTime)) {
            time.setText("今天 "+accountBean.getTimeOfDay());
        }else {
            //yyyy年MM月dd日 HH:mm
            time.setText(accountBean.getTime());
        }

        title.setText(accountBean.getTypename());
        detail.setText(accountBean.getNote());
        image.setImageResource(accountBean.getSimageId());
        if (accountBean.getKind() == TypeBean.KIND_INCOME) {
            //收入
            money.setText("+ ￥ "+String.valueOf(accountBean.getMoney()));
        }else {
            //支出
            money.setText("- ￥ "+String.valueOf(accountBean.getMoney()));
        }
        return view;
    }
}
