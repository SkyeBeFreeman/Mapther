package com.example.administrator.mapther;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhtian on 2016/12/8.
 */

public class MyPagerAdapter extends PagerAdapter {

    //数据源
    private List<ImageView> list;

    public MyPagerAdapter(List<ImageView> list) {
        this.list = list;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position % list.size()));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position % list.size()));
        return list.get(position % list.size());
    }

}