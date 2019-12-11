package com.sxs.item.ui.fragment;

import android.content.Context;
import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseFragment;
import com.sxs.tools.statusbar.StatusBarUtil;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:32
 * @des: 自定义控件展示
 */
public class FragmentB extends BaseFragment {


    @Override
    protected int initLayout() {
        return R.layout.fragment_kind;
    }

    @Override
    protected void initView(View view) {
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(getActivity());
        if (!StatusBarUtil.setStatusBarDarkTheme(getActivity(), true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(getActivity(), 0x55000000);
        }
    }

    @Override
    protected void initData(Context mContext) {

    }


}
