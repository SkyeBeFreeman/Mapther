package com.example.administrator.mapther;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private List<Fragment> fragments;
    private BottomNavigationBar bottomNavigationBar;
    static private String my_name = "";
    static private String super_admin = "ZWG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        my_name = sharedPreferences.getString("MY_NAME", "");
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "社区")).setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.mipmap.ic_find_replace_white_24dp, "工具")).setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.mipmap.ic_favorite_white_24dp, "我的")).setActiveColor(R.color.colorPrimary)
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

    }

    // 设置默认的初始Fragment
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, fragments.get(0));
        transaction.commit();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(SocialFragment.newInstance("Map"));
        fragments.add(ToolFragment.newInstance("Weather"));
        fragments.add(MyFragment.newInstance("My"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.frame_layout, fragment);
                } else {
                    ft.add(R.id.frame_layout, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }

    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    static public void setMy_name(String user_name) {
        my_name = user_name;
    }

    static public String getMy_name() {
        return my_name;
    }

    public static String getSuper_admin() {
        return super_admin;
    }
}
