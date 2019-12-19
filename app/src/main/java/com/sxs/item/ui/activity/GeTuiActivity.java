package com.sxs.item.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;
import com.sxs.item.base.BaseApplication;
import com.sxs.item.other.getui.GetuiSdkHttpPost;
import com.sxs.item.service.getui.GeTuiIntentService;
import com.sxs.item.service.getui.GeTuiPushService;
import com.sxs.tools.viewinject.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2019/12/11 14:33
 * @Desc: 个推集成Demo
 * PushManager为对外接口，所有的调用均是通过PushManager.getInstance() 日志过滤器输入 PushManager， 在接口调用失败会有对应的Error级别
 * log 提示
 */
public class GeTuiActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = GeTuiActivity.class.getSimpleName();
    private static final String MASTERSECRET = "ItQSEF1Hgw7V0q95csarI1";

    @ViewById(R.id.btn_clear)
    private Button clearBtn;
    @ViewById(R.id.btn_service)
    private Button serviceBtn;
    @ViewById(R.id.btn_bind_alias)
    private Button bindAliasBtn;
    @ViewById(R.id.btn_unbind_alias)
    private Button unBindAliasBtn;
    @ViewById(R.id.btnAddTag)
    private Button addTagBtn;
    @ViewById(R.id.btnVersion)
    private Button versionBtn;
    @ViewById(R.id.btnSilentime)
    private Button silentimeBtn;
    @ViewById(R.id.btnGetCid)
    private Button getCidBtn;
    // 透传测试
    @ViewById(R.id.btn_pmsg)
    private Button pmsgBtn;
    // 通知测试
    @ViewById(R.id.btn_psmsg)
    private Button psmsgBtn;

    @ViewById(R.id.tvclientid)
    public static TextView clientidTV;
    @ViewById(R.id.tvappkey)
    private TextView appkeyTV;
    @ViewById(R.id.tvappsecret)
    private TextView appsecretTV;
    @ViewById(R.id.tvmastersecret)
    private TextView mastersecretTV;
    @ViewById(R.id.tvappid)
    private TextView appidTV;
    @ViewById(R.id.tvlog)
    public static EditText logTV;

    // SDK服务是否启动
    private boolean isServiceRunning = true;
    private Context mContext;

    private String appkey = "";
    private String appsecret = "";
    private String appid = "";

    private static final int REQUEST_PERMISSION = 0;

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = GeTuiPushService.class;


    @Override
    protected int getLayoutId() {
        return R.layout.acivity_getui_main;
    }

    @Override
    protected void initView() {
        mContext = this;
        isServiceRunning = true;
        BaseApplication.mGeTuiActivity = this;


        clearBtn.setOnClickListener(this);
        serviceBtn.setOnClickListener(this);
        bindAliasBtn.setOnClickListener(this);
        unBindAliasBtn.setOnClickListener(this);
        addTagBtn.setOnClickListener(this);
        versionBtn.setOnClickListener(this);
        silentimeBtn.setOnClickListener(this);
        getCidBtn.setOnClickListener(this);
        pmsgBtn.setOnClickListener(this);
        psmsgBtn.setOnClickListener(this);

        logTV.setInputType(InputType.TYPE_NULL);
        logTV.setSingleLine(false);
        logTV.setHorizontallyScrolling(false);

        // 获取Manifests 中的配置参数
        parseManifests();

        // appkeyTV,appsecretTV,mastersecretTV
        appkeyTV.setText(String.format("%s", getResources().getString(R.string.appkey) + appkey));
        appsecretTV.setText(String.format("%s", getResources().getString(R.string.appsecret) + appsecret));
        mastersecretTV.setText(String.format("%s", getResources().getString(R.string.mastersecret) + MASTERSECRET));
        appidTV.setText(String.format("%s", getResources().getString(R.string.appid) + appid));

        Log.d(TAG, "initializing sdk...");

        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && (!sdCardWritePermission || !phoneSatePermission)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION);
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }

        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);

        // 应用未启动, 个推 service已经被唤醒,显示该时间段内离线消息
        if (BaseApplication.payloadData != null) {
            logTV.append(BaseApplication.payloadData);
        }

        // cpu 架构
        Log.d(TAG, "cpu arch = " + (Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0]));

        // 检查 so 是否存在
        File file = new File(this.getApplicationInfo().nativeLibraryDir + File.separator + "libgetuiext3.so");
        Log.e(TAG, "libgetuiext2.so exist = " + file.exists());

    }

    @Override
    public void onClick(View v) {
        if (v == clearBtn) {
            logTV.setText("");
            BaseApplication.payloadData.delete(0, BaseApplication.payloadData.length());
        } else if (v == serviceBtn) {
            if (isServiceRunning) {
                Log.d(TAG, "stopping sdk...");
                PushManager.getInstance().stopService(this.getApplicationContext());
                clientidTV.setText(getResources().getString(R.string.no_clientid));
                serviceBtn.setText(getResources().getString(R.string.stop));
                isServiceRunning = false;
            } else {
                Log.d(TAG, "reinitializing sdk...");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
                serviceBtn.setText(getResources().getString(R.string.start));
                isServiceRunning = true;
            }
        } else if (v == bindAliasBtn) {
            bindAlias();
            // PushManager.getInstance().turnOnPush(this.getApplicationContext());
        } else if (v == unBindAliasBtn) {
            unBindAlias();
            // PushManager.getInstance().turnOffPush(this.getApplicationContext());
        } else if (v == pmsgBtn) {
            showTransmission();
        } else if (v == psmsgBtn) {
            showNotification();
        } else if (v == addTagBtn) {
            addTag();
        } else if (v == getCidBtn) {
            getCid();
        } else if (v == silentimeBtn) {
            setSilentime();
        } else if (v == versionBtn) {
            getVersion();
        }
    }

    private void bindAlias() {
        final EditText editText = new EditText(GeTuiActivity.this);
        new AlertDialog.Builder(GeTuiActivity.this).setTitle(R.string.bind_alias).setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getEditableText() != null) {
                            String alias = editText.getEditableText().toString();
                            if (alias.length() > 0) {
                                PushManager.getInstance().bindAlias(GeTuiActivity.this, alias);
                                Log.d(TAG, "bind alias = " + editText.getEditableText().toString());
                            }
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).show();
    }

    private void unBindAlias() {
        final EditText editText = new EditText(GeTuiActivity.this);
        new AlertDialog.Builder(GeTuiActivity.this).setTitle(R.string.unbind_alias).setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String alias = editText.getEditableText().toString();
                        if (alias.length() > 0) {
                            PushManager.getInstance().unBindAlias(GeTuiActivity.this, alias, false);
                            Log.d(TAG, "unbind alias = " + editText.getEditableText().toString());
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).show();
    }

    private void parseManifests() {
        String packageName = getApplicationContext().getPackageName();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID");
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
                appkey = appInfo.metaData.getString("PUSH_APPKEY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification() {
        if (isNetworkConnected()) {
            // !!!!!!注意：以下为个推服务端API1.0接口，仅供测试。不推荐在现网系统使用1.0版服务端接口，请参考最新的个推服务端API接口文档，使用最新的2.0版接口
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("action", "pushSpecifyMessage"); // pushSpecifyMessage为接口名，注意大小写
            /*---以下代码用于设定接口相应参数---*/
            param.put("appkey", appkey);
            param.put("type", 2); // 推送类型： 2为消息
            param.put("pushTitle", getResources().getString(R.string.push_notification_title)); // pushTitle请填写您的应用名称

            // 推送消息类型，有TransmissionMsg、LinkMsg、NotifyMsg三种，此处以LinkMsg举例
            param.put("pushType", "LinkMsg");
            param.put("offline", true); // 是否进入离线消息
            param.put("offlineTime", 72); // 消息离线保留时间
            param.put("priority", 1); // 推送任务优先级

            List<String> cidList = new ArrayList<String>();
            cidList.add(clientidTV.getText().toString()); // 您获取的ClientID
            param.put("tokenMD5List", cidList);
            param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));// 生成Sign值，用于鉴权，需要MasterSecret，请务必填写

            // LinkMsg消息实体
            Map<String, Object> linkMsg = new HashMap<String, Object>();
            linkMsg.put("linkMsgIcon", "push.png"); // 消息在通知栏的图标
            linkMsg.put("linkMsgTitle", getResources().getString(R.string.push_notification_msg_title)); // 推送消息的标题
            linkMsg.put("linkMsgContent", getResources().getString(R.string.push_notification_msg_content)); // 推送消息的内容
            linkMsg.put("linkMsgUrl", "http://www.igetui.com/"); // 点击通知跳转的目标网页
            param.put("msg", linkMsg);
            GetuiSdkHttpPost.httpPost(param);

        } else {
            Toast.makeText(this, R.string.network_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void showTransmission() {
        if (isNetworkConnected()) {
            // !!!!!!注意：以下为个推服务端API1.0接口，仅供测试。不推荐在现网系统使用1.0版服务端接口，请参考最新的个推服务端API接口文档，使用最新的2.0版接口
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("action", "pushmessage"); // pushmessage为接口名，注意全部小写
            /*---以下代码用于设定接口相应参数---*/
            param.put("appkey", appkey);
            param.put("appid", appid);
            // 注：透传内容后面需用来验证接口调用是否成功，假定填写为hello girl~
            param.put("data", getResources().getString(R.string.push_transmission_data));
            // 当前请求时间，可选
            param.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
            param.put("clientid", clientidTV.getText().toString()); // 您获取的ClientID
            Log.d("sxs--->", clientidTV.getText().toString());
            param.put("expire", 3600); // 消息超时时间，单位为秒，可选
            param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));// 生成Sign值，用于鉴权

            GetuiSdkHttpPost.httpPost(param);
        } else {
            Toast.makeText(this, R.string.network_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断网络是否连接.
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo ni : info) {
                    if (ni.getState() == NetworkInfo.State.CONNECTED) {
                        Log.d(TAG, "type = " + (ni.getType() == 0 ? "mobile" : ((ni.getType() == 1) ? "wifi" : "none")));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 测试addTag接口.
     */
    private void addTag() {
        final View view = new EditText(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.add_tag).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TextView tagText = (TextView) view;

                Log.d(TAG, "setTag input tags = " + tagText.getText().toString());

                String[] tags = tagText.getText().toString().split(",");
                Tag[] tagParam = new Tag[tags.length];
                for (int i = 0; i < tags.length; i++) {
                    Tag t = new Tag();
                    t.setName(tags[i]);
                    tagParam[i] = t;
                }

                int i = PushManager.getInstance().setTag(mContext, tagParam, "" + System.currentTimeMillis());
                int text = R.string.add_tag_unknown_exception;

                // 这里的返回结果仅仅是接口调用是否成功, 不是真正成功, 真正结果见{
                // com.getui.demo.DemoIntentService.setTagResult 方法}
                switch (i) {
                    case PushConsts.SETTAG_SUCCESS:
                        text = R.string.add_tag_success;
                        break;

                    case PushConsts.SETTAG_ERROR_COUNT:
                        text = R.string.add_tag_error_count;
                        break;

                    case PushConsts.SETTAG_ERROR_FREQUENCY:
                        text = R.string.add_tag_error_frequency;
                        break;

                    case PushConsts.SETTAG_ERROR_NULL:
                        text = R.string.add_tag_error_null;
                        break;

                    default:
                        break;
                }

                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setView(view);
        alertBuilder.create().show();
    }


    private void getVersion() {
        String version = PushManager.getInstance().getVersion(this);
        Toast.makeText(this, getResources().getString(R.string.show_version) + version, Toast.LENGTH_SHORT).show();
    }

    private void getCid() {
        String cid = PushManager.getInstance().getClientid(this);
        Toast.makeText(this, getResources().getString(R.string.show_cid) + cid, Toast.LENGTH_LONG).show();
        Log.d(TAG, getResources().getString(R.string.show_cid) + cid);
        Log.e("sxs", cid);
    }

    private void setSilentime() {
        final View view = LayoutInflater.from(this).inflate(R.layout.silent_setting, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.set_silenttime).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TextView beginText = view.findViewById(R.id.beginText);
                TextView durationText = view.findViewById(R.id.durationText);

                try {
                    int beginHour = Integer.valueOf(String.valueOf(beginText.getText()));
                    int durationHour = Integer.valueOf(String.valueOf(durationText.getText()));

                    boolean result = PushManager.getInstance().setSilentTime(mContext, beginHour, durationHour);

                    if (result) {
                        Toast.makeText(mContext, "begin = " + beginHour + ", duration = " + durationHour, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "setSilentime, begin = " + beginHour + ", duration = " + durationHour);
                    } else {
                        Toast.makeText(mContext, "setSilentime failed, value exceeding", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "setSilentime failed, value exceeding");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }).setView(view);
        alertBuilder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            } else {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("GetuiSdkDemo", "onDestroy()");
        BaseApplication.payloadData.delete(0, BaseApplication.payloadData.length());
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d("GetuiSdkDemo", "onStop()");
        super.onStop();
    }

    private static int rebackFlag = -1;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if (rebackFlag < 0){
                toast("再摁一次退出到桌面");
                rebackFlag++;
                return false;
                // openActivity(GeTuiActivity.class);
            }else {
                rebackFlag = -1;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
