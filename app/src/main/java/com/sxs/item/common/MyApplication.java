package com.sxs.item.common;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.sxs.item.helper.ActivityStackManager;
import com.sxs.toast.ToastInterceptor;
import com.sxs.toast.ToastUtils;

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


    }


}
