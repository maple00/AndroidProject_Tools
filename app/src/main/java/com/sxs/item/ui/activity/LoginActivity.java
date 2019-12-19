package com.sxs.item.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;
import com.sxs.tools.view.ClearEditText;
import com.sxs.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2019/12/19 13:33
 * @Desc: 集成网易登录登出
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nim_login;
    }

    @ViewById(R.id.et_account)
    private ClearEditText account;
    @ViewById(R.id.et_passwords)
    private ClearEditText pwd;
    @ViewById(R.id.btn_login)
    private Button btnLogin;


    @Override
    protected void initView() {
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    /**
     * 通过SDK的方式进行登录
     */
    private void login() {
        LoginInfo loginInfo = new LoginInfo(account.getText().toString(), pwd.getText().toString());

        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {

            @Override
            public void onSuccess(LoginInfo param) {
                // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                toast("登录成功");
                // 登录成功之后，跳转到单聊功能 --- 默认的nimUI 中的音频没有权限,需要在某个地方开启权限
                NimUIKit.setAccount(param.getAccount());
                NimUIKit.startP2PSession(LoginActivity.this, param.getAccount());
                // 登录成功之后，跳转到历史聊天人的页面

            }

            @Override
            public void onFailed(int code) {
                toast("登录失败");
            }

            @Override
            public void onException(Throwable exception) {
                toast("登录异常");
            }
        };
        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
    }
}
