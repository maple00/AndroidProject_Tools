package com.sxs.item.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.sxs.tools.statusbar.StatusBarUtil;
import com.sxs.tools.toast.ToastUtils;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Fragment 基类
 */
public abstract class BaseFragment<A extends BaseActivity> extends Fragment {

    //获取TAG的fragment名称
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 全局ViewGroup
     */
    private ViewGroup rootView;

    /**
     * 状态栏
     */
    private View mStatusBarView;

    /**
     * 上下文对象
     */
    public Context mContext;

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
        mContext = requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = (ViewGroup) inflater.inflate(initLayout(), container, false);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        initView(rootView);
        initData(mContext);

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
    private void addStatusBar() {
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
     *
     * @return 布局id
     */
    protected abstract int initLayout();

    /**
     * 初始化控件
     *
     * @param view 布局View
     */
    protected abstract void initView(final View view);

    /**
     * 初始化、绑定数据
     *
     * @param mContext 上下文
     */
    protected abstract void initData(Context mContext);

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * startActivity 方法优化
     */

    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(mContext, cls));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

