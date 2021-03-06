package com.sxs.item.ui.fragment;

import android.content.Context;
import android.view.View;

import com.sxs.item.R;
import com.sxs.item.base.BaseFragment;
import com.sxs.item.ui.activity.DialogActivity;
import com.sxs.item.ui.activity.StatusActivity;
import com.sxs.item.ui.activity.TripartiteActivity;
import com.sxs.item.ui.activity.provide.CalendarProvider;
import com.sxs.tools.statusbar.StatusBarUtil;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:32
 * @des: 项目常用界面跳转示例
 */
public class FragmentD extends BaseFragment implements View.OnClickListener {


    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(getActivity());
        if (!StatusBarUtil.setStatusBarDarkTheme(getActivity(), true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(getActivity(), 0x55000000);
        }


        // 对话框界面
        view.findViewById(R.id.btn_dialog_surface).setOnClickListener(this);
        // 界面状态
        view.findViewById(R.id.btn_surface_hint).setOnClickListener(this);
        // 三方集成界面
        view.findViewById(R.id.btn_tripartite).setOnClickListener(this);
        // 内容提供者 provide
        view.findViewById(R.id.btn_provide).setOnClickListener(this);
    }

    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_surface:   // 对话框使用案例
                startActivity(DialogActivity.class);
                break;
            case R.id.btn_surface_hint: // 页面状态布局(网络错误，异常错误，空数据等)
                startActivity(StatusActivity.class);
                break;
            case R.id.btn_tripartite:   // 三方集成界面
                startActivity(TripartiteActivity.class);
                break;
            case R.id.btn_provide:
                startActivity(CalendarProvider.class);
                break;
        }
    }


}
