package com.sxs.item.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.MixPushConfig;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.sxs.item.R;
import com.sxs.item.helper.ActivityStackManager;
import com.sxs.item.ui.activity.CrashActivity;
import com.sxs.item.ui.activity.GeTuiActivity;
import com.sxs.item.ui.activity.HomeActivity;
import com.sxs.tools.toast.ToastInterceptor;
import com.sxs.tools.toast.ToastUtils;

import java.io.IOException;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Application 基类
 */
public class BaseApplication extends Application {


    /**
     * 是否调试
     */
    private boolean isDebug = false;

    public static BaseApplication getInstance() {
        return Instance.INSTANCE;
    }

    private static class Instance {
        static BaseApplication INSTANCE = new BaseApplication();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // initActivity 初始化Activity 栈管理
        initActivity();

        // 初始化工具类
        initTools();

        // 初始化三方的框架
        initSDK();

    }

    /**
     * 初始化活动
     */
    private void initActivity() {
        ActivityStackManager.getInstance().register(this);
    }

    /**
     * 初始化一些三方框架
     */
    private void initTools() {

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);

        // 本地异常捕获
        // Crash 捕捉界面
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(HomeActivity.class)
                // 错误的 Activity
                .errorActivity(CrashActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();
    }

    /**
     * 初始化三方的框架
     */
    // 个推
    private static geTuiHandler geTuiHanlder;
    public static GeTuiActivity mGeTuiActivity;
    // 应用未启动，个推的service 已经被唤醒，保存在该时间段内的离线消息（此时 mGeTuiActivity.tLogView == null）
    public static StringBuilder payloadData = new StringBuilder();

    private void initSDK() {
        /*
         初始化个推
         */
        if (geTuiHanlder == null) {
            geTuiHanlder = new geTuiHandler();
        }

        /*
        初始化网易云信
         */
        NIMClient.init(this, loginInfo(), options());

        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下的操作必须在主线程中进行
            // 1、UI相关的初始化操作
            NimUIKit.init(this);
            // 2、相关Service调用
        }

    }

    /**
     * 是否是调试环境
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    public boolean isDetermineNetwork() {
        return true;
    }

    public static void sendMessage(Message msg) {
        geTuiHanlder.sendMessage(msg);
    }

    /**
     * Class：个推Handler
     */
    public static class geTuiHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    payloadData.append((String) msg.obj);
                    payloadData.append("\n");
                    if (mGeTuiActivity != null) {
                        if (GeTuiActivity.logTV != null) {
                            GeTuiActivity.logTV.append(msg.obj + "\n");
                        }
                    }
                    break;
                case 1:
                    if (mGeTuiActivity != null) {
                        if (GeTuiActivity.logTV != null) {
                            GeTuiActivity.clientidTV.setText((String) msg.obj);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 如果已经存在用户的登录信息，则 返回LoginInfo， 否则返回null即可
     *
     * @return
     */
    private LoginInfo loginInfo() {
        return null;
    }

    /**
     * 如果返回值为null， 则全部使用默认参数
     *
     * @return
     */
    private SDKOptions options() {
        MixPushConfig mixPushConfig = new MixPushConfig();
        mixPushConfig.hwCertificateName = "C1:DE:25:DB:F4:11:BB:74:15:37:E4:CD:5F:B4:51:EE:DA:F9:82:FC:B0:18:67:EB:88:CC:2C:93:5C:1C:6E:F1";
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知托管给SDK管理，则需要添加如下的配置，反之无需设置
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // config.notificationEntrance = T.class;  // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.home;       // logo
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;
        options.mixPushConfig = mixPushConfig;

        // 配置保存图片，文件，log等数据的目录
        // 如果options中没有设置目录的值，SDK将使用SDK默认的文件数据目录
        // 该目录目前包含有6哥目录：log，file，image，audio，video，thumb
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";   // 可以不设置，那么将采用默认路径
        // 如果第三方的App需要清理缓存的功能，清理这个目录下的子目录的内容即可
        options.sdkStorageRootPath = sdkPath;
        // 配置是否需要下载附件缩略图，默认为true
        options.preloadAttach = true;

        // 配置附件缩略图的大小，表示向服务器请求缩略图的大小
        // 该值一般根据屏幕的尺寸来确定，一般是Screen.width / 2
        options.thumbnailSize = 480 / 2;
        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }
        };

        return options;
    }

}
