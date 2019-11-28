package com.sxs.item.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sxs.inject.view.ViewBind;
import com.sxs.statusbar.StatusBarUtil;

import static com.sxs.statusbar.StatusBarUtil.getStatusBarHeight;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Fragment 基类
 */
public abstract class BaseFragment extends Fragment {
    //获取TAG的fragment名称
    protected final String TAG = this.getClass().getSimpleName();

    /** 全局ViewGroup */
    private ViewGroup rootView;
    /** 状态栏 */
    private View mStatusBarView;
    /** 上下文对象 */
    public Context context;
    /** 封装Toast对象 */
    private static Toast toast;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        context = ctx;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = (ViewGroup) inflater.inflate(initLayout(), container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null){
            parent.removeAllViews();
        }

        initView(rootView);
        initData(context);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 预留一个状态栏高度
        addStatusBar();
    }

    /**
     * 给 fragment 预留一个状态栏高度
     */
    private void addStatusBar(){
        if (mStatusBarView == null) {
            mStatusBarView = new View(getContext());
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(getActivity());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, statusBarHeight);
            mStatusBarView.setLayoutParams(params);
            mStatusBarView.requestLayout();
            if (rootView != null)
                rootView.addView(mStatusBarView, 0);
        }
    }

    /**
     * 初始化布局
     * @return 布局id
     */
    protected abstract int initLayout();

    /**
     * 初始化控件
     * @param view 布局View
     */
    protected abstract void initView(final View view);

    /**
     * 初始化、绑定数据
     * @param mContext 上下文
     */
    protected abstract void initData(Context mContext);

    /**
     * 保证同一按钮在1秒内只响应一次点击事件
     */
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮的最小间隔，目前为1000
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View v) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(v);
            }
        }
    }

    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }
    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

