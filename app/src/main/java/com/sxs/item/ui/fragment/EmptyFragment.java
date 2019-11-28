package com.sxs.item.ui.fragment;

import android.content.Context;
import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseFragment;


/**
 * @anthor :  shearson
 * @time : 2019/11/27 13:38
 * @des: 占位 fragment
 */
public class EmptyFragment extends BaseFragment {

    @Override
    protected int initLayout() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(Context mContext) {

    }
}
