package com.sxs.item.ui.activity;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;
import com.sxs.item.other.BaseDialog;
import com.sxs.item.ui.dialog.MenuDialog;

/**
 * @Author: shearson
 * @time: 2019/12/3 13:52
 * @des: 加载使用案例
 */
public class StatusActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status;
    }

    @Override
    protected void initView() {
        // 页面返回
        this.findViewById(R.id.btn_back).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        new MenuDialog.Builder(this)
                .setCancelable(false)
                // 设置点击按钮后不关闭对话框
                .setList("加载中", "请求错误", "空数据提示", "自定义提示")
                .setListener(new MenuDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, Object o) {
                        switch (position) {
                            case 0:
                                showLoading();
                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showComplete();
                                    }
                                }, 2000);
                                break;
                            case 1:
                                showError();
                                break;
                            case 2:
                                showEmpty();
                                break;
                            case 3:
                                showLayout(ContextCompat.getDrawable(getActivity(), R.drawable.icon_hint_address), "还没有添加地址");
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {

                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                // 销毁aty在内存中占有的空间
                // onDestroy();
                // finish 将aty出栈
                finish();
                break;
        }
    }


}
