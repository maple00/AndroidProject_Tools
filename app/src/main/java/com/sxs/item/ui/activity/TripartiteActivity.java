package com.sxs.item.ui.activity;

import android.view.View;

import com.sxs.item.R;
import com.sxs.item.common.BaseActivity;

/**
 * @Author: a797s
 * @Date: 2019/12/11 11:19
 * @Desc: 三方集成案例
 */
public class TripartiteActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tripartite;
    }

    @Override
    protected void initView() {
        // 返回上一侧Activity
        findViewById(R.id.btn_back).setOnClickListener(this);
        // 个推集成Demo
        findViewById(R.id.btn_getui_integration).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_getui_integration:
                openActivity(GeTuiActivity.class);
                break;
            default:
                break;
        }
    }
}
