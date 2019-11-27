package com.sxs.item.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sxs.inject.view.ViewById;
import com.sxs.item.R;
import com.sxs.item.common.BaseActivity;
import com.sxs.item.ui.fragment.FragmentA;
import com.sxs.item.ui.fragment.FragmentB;
import com.sxs.item.ui.fragment.FragmentC;
import com.sxs.item.ui.fragment.FragmentD;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:17
 * @des: App 首页
 */
public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @ViewById(R.id.bottomNavigationView)
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);//设置导航栏监听器
        mBottomNavigationView.setSelectedItemId(R.id.tab_one);//设置默认选择的导航栏子项tab_one即首页
        mBottomNavigationView.setItemIconTintList(null);//取消导航栏子项图片的颜色覆盖
    }

    /**
     * 处理导航栏子项的点击事件
     *
     * @param menuItem 导航栏子项
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // 获取点击位置以及对应的id
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.tab_one:
                replaceFragment(new FragmentA());   //id 为tab_one 则第一项被点击，用Fragment替换空的Fragment
                menuItem.setChecked(true);
                break;
            case R.id.tab_two:
                replaceFragment(new FragmentB());
                menuItem.setChecked(true);
                break;
            case R.id.tab_three:
                replaceFragment(new FragmentC());
                menuItem.setChecked(true);
                break;
            case R.id.tab_four:
                replaceFragment(new FragmentD());
                menuItem.setChecked(true);
                break;
        }

        return false;
    }


    // 替换Fragment 的方法
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_empty, fragment);
        transaction.commit();
    }
}
