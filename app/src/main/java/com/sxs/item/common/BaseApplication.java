package com.sxs.item.common;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.sxs.item.helper.ActivityStackManager;
import com.sxs.item.ui.activity.CrashActivity;
import com.sxs.item.ui.activity.HomeActivity;
import com.sxs.tools.toast.ToastInterceptor;
import com.sxs.tools.toast.ToastUtils;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Application 基类
 */
public class BaseApplication extends Application {


    /**
     * 是否调试
     */
    private boolean isDebug = false;

    private static BaseApplication application;

    public static BaseApplication getInstance() {
        return application;
    }

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

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);


        // 本地异常捕获
        // Crash 捕捉界面
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(HomeActivity.class)
                // 错误的 Activity
                .errorActivity(CrashActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();

    }

    /**
     * 是否是调试环境
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    public boolean isDetermineNetwork() {
        return true;
    }


}
