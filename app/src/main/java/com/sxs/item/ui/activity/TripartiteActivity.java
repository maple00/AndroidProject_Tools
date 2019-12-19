package com.sxs.item.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;
import com.sxs.tools.permission.OnPermission;
import com.sxs.tools.permission.Permission;
import com.sxs.tools.permission.XXPermissions;
import com.sxs.tools.viewinject.ViewById;

import java.util.List;

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

    @ViewById(R.id.tv_title)
    private TextView tvTitle;

    @Override
    protected void initView() {
        // 返回上一侧Activity
        findViewById(R.id.btn_back).setOnClickListener(this);
        tvTitle.setText("三方集成");
        // 个推集成Demo
        findViewById(R.id.btn_getui_integration).setOnClickListener(this);
        // 网易云信集成Demo
        findViewById(R.id.btn_wangyiyun_integration).setOnClickListener(this);
        // 文件在线预览Demo
        findViewById(R.id.btn_read_ol).setOnClickListener(this);
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
            case R.id.btn_wangyiyun_integration:
                // 需要先获取到权限之后再打开, 这个权限是为了用户提现，必须打开才能进行
                XXPermissions.with(this)
                        .constantRequest()
                        .permission(Permission.RECORD_AUDIO)
                        .permission(Permission.Group.STORAGE)
                        .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            toast("获取权限成功");
                            openActivity(LoginActivity.class);
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
                break;
            case R.id.btn_read_ol:
                toast("待完成");
                break;
            default:
                break;
        }
    }
}
