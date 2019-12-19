package com.sxs.item.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @Author: a797s
 * @Date: 2019/12/11 14:02
 * @Desc: 崩溃捕捉页面
 */
public class CrashActivity extends BaseActivity implements OnClickListener {

    private CaocConfig mConfig;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crash;
    }

    @Override
    protected void initView() {
        // 重新启动
        findViewById(R.id.btn_crash_restart).setOnClickListener(this);
        // 错误日志
        findViewById(R.id.btn_crash_log).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mConfig = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (mConfig == null) {
            // 这种情况永远不会发生，只要完成该活动就可以避免递归崩溃。
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crash_restart:
                CustomActivityOnCrash.restartApplication(CrashActivity.this, mConfig);
                break;
            case R.id.btn_crash_log:
                AlertDialog dialog = new AlertDialog.Builder(CrashActivity.this)
                        .setTitle(R.string.crash_error_details)
                        .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent()))
                        .setPositiveButton(R.string.crash_close, null)
                        .setNeutralButton(R.string.crash_copy_log, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                copyErrorToClipboard();
                            }


                        })
                        .show();
                TextView textView = dialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 复制报错信息到剪贴板
     */
    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent());
        ContextCompat.getSystemService(this, ClipboardManager.class).setPrimaryClip(ClipData.newPlainText(getString(R.string.crash_error_info), errorInformation));
    }
}
