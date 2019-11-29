package com.sxs.item.ui.activity;

import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseActivity;
import com.sxs.item.other.BaseDialog;
import com.sxs.item.ui.dialog.MessageDialog;

/**
 * @Author: shearson
 * @time: 2019/11/29 16:05
 * @des: 对话框使用案例
 */
public class DialogActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initView() {
        // 消息对话框
        this.findViewById(R.id.btn_dialog_message).setOnClickListener(this);
        // 弹出输入对话框

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_message:
                new MessageDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle("我是标题")
                        // 内容必须要填写
                        .setMessage("我是内容")
                        // 确定文本按钮
                        .setConfirm(getString(R.string.common_confirm))
                        // 取消按钮，设置null之后不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置点击按钮后不关闭对话框
                        .setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                toast("确定了");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
        }
    }
}
