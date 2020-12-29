package com.hjf.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjf.tally.R;
import com.hjf.tally.bean.TypeBean;

import java.util.List;

/**
 * @author hjf
 * @create 2020-12-24 18:28
 */
public class TypeBaseAdapter extends BaseAdapter {

    private Context context;

    private List<TypeBean> typeList;

    private int selectPosition;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public TypeBaseAdapter(Context context, List<TypeBean> typeList) {
        this.context = context;
        this.typeList = typeList;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, viewGroup, false);
        //设置布局当中的控件的值
        ImageView iv = view.findViewById(R.id.item_recordfrag_iv);
        TextView tv = view.findViewById(R.id.item_recordfrag_tv);
        //获取指定位置的数据源
        TypeBean typeBean = typeList.get(i);
        tv.setText(typeBean.getTypename());
        if (selectPosition == i) {  //如果是被选中的，就是展示亮图标的样式
            iv.setImageResource(typeBean.getSimageId());
        }else { //如果没被选中，就是展示暗图标的样式
            iv.setImageResource(typeBean.getImageId());
        }
        return view;
    }
}
