package com.sxs.item.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseFragment;
import com.sxs.tools.statusbar.StatusBarUtil;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:32
 * @des: Fragment A
 */
public class FragmentA extends BaseFragment {


    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        // 设置状态栏背景
        StatusBarUtil.setStatusBarColor(getActivity(), Color.parseColor("#82B1FF"));
    }

    @Override
    protected void initData(Context mContext) {

    }


}
