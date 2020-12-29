package com.hjf.tally.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author hjf
 * @create 2020-12-28 18:05
 */
public class ChartVPAdapter extends FragmentPagerAdapter {

    private List<Fragment> chartFragList;

    public ChartVPAdapter(@NonNull FragmentManager fm, List<Fragment> chartFragList) {
        super(fm);
        this.chartFragList = chartFragList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return chartFragList.get(position);
    }

    @Override
    public int getCount() {
        return chartFragList.size();
    }
}
