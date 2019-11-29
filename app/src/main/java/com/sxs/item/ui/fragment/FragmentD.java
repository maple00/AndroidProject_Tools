package com.sxs.item.ui.fragment;

import android.content.Context;
import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseFragment;
import com.sxs.item.ui.activity.DialogActivity;

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

        // 对话框界面
        view.findViewById(R.id.btn_dialog_surface).setOnClickListener(this);
    }

    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_dialog_surface:
                startActivity(DialogActivity.class);
                break;
        }
    }
}
