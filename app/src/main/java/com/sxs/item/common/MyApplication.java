package com.sxs.item.common;

import android.app.Application;

import com.sxs.item.helper.ActivityStackManager;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Application 基类
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initActivity 初始化Activity 栈管理
        initActivity();

        // 初始化三方的框架
        initSDK();

    }

    /**
     * 初始化活动
     */
    private void initActivity() {
        ActivityStackManager.getInstance().register(this);
    }

    /**
     * 初始化一些三方框架
     */
    private void initSDK() {

    }

}
