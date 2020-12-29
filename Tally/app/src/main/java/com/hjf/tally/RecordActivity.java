package com.hjf.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hjf.tally.adapter.RecordPagerAdapter;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.frag_record.IncomeFragment;
import com.hjf.tally.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1、查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        //2、设置ViewPager加载页面
        initPager();
    }

    private void initPager() {
        //初始化ViewPager页面的集合
        List<Fragment> fragmentList = new ArrayList<>();
        //创建收入和支出页面，放置在Fragment当中
        OutcomeFragment outcomeFragment = new OutcomeFragment(TypeBean.KIND_OUTCOME);    //支出
        IncomeFragment incomeFragment = new IncomeFragment(TypeBean.KIND_INCOME);       //收入
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        //创建适配器
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置适配器
        viewPager.setAdapter(recordPagerAdapter);

        //将TabLayout和ViewPager进行关联
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_back:
                finish();   //返回MainActivity
                break;
        }
    }
}
