package com.sxs.item.common;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sxs.inject.view.ViewBind;
import com.sxs.item.R;
import com.sxs.item.helper.ActivityStackManager;
import com.sxs.statusbar.StatusBarUtil;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:23
 * @des: Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      //禁止横屏
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        init();
    }

    protected void init() {
        // View 绑定
        ViewBind.inject(this);
        // 沉浸式状态栏
        setStatusBar();
        // 初始视图
        initView();
        // 初始化数据
        initData();
        // 将activity 入栈
        ActivityStackManager.getInstance().addActivity(this);
    }

    /**
     * 状态栏适配
     * 魅族，小米适配需要单独设置
     * setFUI
     * setMiUI
     */
    private void setStatusBar() {
        //  当fistsSystemWindows 设置为true时，会在屏幕的最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        // 一般的手机的状态栏文字和图标都是白色的，如果应用是纯白的，则会导致状态栏文字看不清
        // 如果是纯白的状态栏背景，则设置状态栏使用深灰色图标风格，否则可以选择性注释掉if中的内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        /*
         * 设置状态栏透明，黑色字体
         */
//        StatusBarUtil.setTranslucentStatus(this);
//        StatusBarUtil.setStatusBarDarkTheme(this, true);

           /*
           白色字体
            */
//           StatusBarUtil.setStatusBarDarkTheme(this, false);

        /*
        设置白色字体，其他背景
         */
//        StatusBarUtil.setStatusBarDarkTheme(this, false);
//        StatusBarUtil.setStatusBarColor(this, Color.parseColor("#58C087"));

    }

    /**
     * 布局ID
     *
     * @return 布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    @Override
    protected void onDestroy() {
        ActivityStackManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}