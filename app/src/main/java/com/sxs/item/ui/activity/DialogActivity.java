package com.sxs.item.ui.activity;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxs.item.R;
import com.sxs.item.common.BaseActivity;
import com.sxs.item.common.BaseDialogFragment;
import com.sxs.item.other.BaseDialog;
import com.sxs.item.ui.dialog.AddressDialog;
import com.sxs.item.ui.dialog.DateDialog;
import com.sxs.item.ui.dialog.InputDialog;
import com.sxs.item.ui.dialog.MenuDialog;
import com.sxs.item.ui.dialog.MessageDialog;
import com.sxs.item.ui.dialog.PayPasswordDialog;
import com.sxs.item.ui.dialog.TimeDialog;
import com.sxs.item.ui.dialog.ToastDialog;
import com.sxs.item.ui.dialog.UpdateDialog;
import com.sxs.item.ui.dialog.WaitDialog;
import com.sxs.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        // 页面返回
        findViewById(R.id.btn_back).setOnClickListener(this);
        // 消息对话框
        this.findViewById(R.id.btn_dialog_message).setOnClickListener(this);
        // 弹出输入对话框
        this.findViewById(R.id.btn_dialog_input).setOnClickListener(this);
        // 底部选择框
        this.findViewById(R.id.btn_dialog_bottom_menu).setOnClickListener(this);
        // 选择框居中
        this.findViewById(R.id.btn_dialog_center_menu).setOnClickListener(this);
        // 完成对话框提示
        this.findViewById(R.id.btn_dialog_succeed_toast).setOnClickListener(this);
        // 失败对话框提示
        this.findViewById(R.id.btn_dialog_fail_toast).setOnClickListener(this);
        // 警告提示对话框
        this.findViewById(R.id.btn_dialog_warn_toast).setOnClickListener(this);
        // 弹出加载对话框
        this.findViewById(R.id.btn_dialog_wait).setOnClickListener(this);
        // 支付对话框
        this.findViewById(R.id.btn_dialog_pay).setOnClickListener(this);
        // 地区选择对话框
        this.findViewById(R.id.btn_dialog_address).setOnClickListener(this);
        // 日期对话框
        this.findViewById(R.id.btn_dialog_date).setOnClickListener(this);
        // 时间对话框
        this.findViewById(R.id.btn_dialog_time).setOnClickListener(this);
        // 更新对话框
        this.findViewById(R.id.btn_dialog_update).setOnClickListener(this);
        // 自定义对话框
        this.findViewById(R.id.btn_dialog_custom).setOnClickListener(this);
    }

    @ViewById(R.id.tv_title)
    private TextView mTitle;

    @Override
    protected void initData() {
        super.initData();
        mTitle.setText("对话框的使用");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back: // 页面返回
                finish();
                break;
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
            case R.id.btn_dialog_input:
                // 输入对话框
                new InputDialog.Builder(this)
                        .setTitle("我是标题")
                        .setContent("我是内容")
                        .setHint("我是提示")
                        .setConfirm(R.string.common_confirm)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                toast("确定了" + content);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_bottom_menu:
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data.add("我是数据" + i);
                }

                // 底部选择框
                new MenuDialog.Builder(this)
                        // 设置null 表示不显示取消按钮
                        .setCancel(R.string.common_cancel)
                        // 设置点击按钮后不关闭弹窗
                        .setAutoDismiss(false)
                        // 显示的数据
                        .setList(data)
                        .setListener(new MenuDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String text) {
                                toast("位置：" + position + ", 文本：" + text);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_center_menu:
                List<String> data1 = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data1.add("我是数据" + i);
                }
                // 居中显示框
                new MenuDialog.Builder(this)
                        // 设置居中
                        .setGravity(Gravity.CENTER)
                        // 设置null 表示不显示取消按钮
                        .setCancel(null)
                        // 设置点击按钮后不关闭弹窗
                        .setAutoDismiss(false)
                        // 设置数据
                        .setList(data1)
                        .setListener(new MenuDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String text) {
                                toast("位置：" + position + ", 文本：" + text);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_succeed_toast:
                // 成功对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.FINISH)
                        .setMessage("完成").show();
                break;
            case R.id.btn_dialog_fail_toast:
                // 失败对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.ERROR)
                        .setMessage("错误").show();
                break;
            case R.id.btn_dialog_warn_toast:
                // 警告提示对话框
                new ToastDialog.Builder(this)
                        .setType(ToastDialog.Type.WARN)
                        .setMessage("警告").show();
                break;
            case R.id.btn_dialog_wait:
                // 等待对话框
                final BaseDialog dialog = new WaitDialog.Builder(this)
                        // 文本消息
                        .setMessage("加载中").show();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 2000);
                break;
            case R.id.btn_dialog_pay:
                // 支付密码对话框
                new PayPasswordDialog.Builder(this)
                        .setTitle(R.string.pay_title)
                        .setSubTitle("用于购买一个女盆友")
                        .setMoney("￥ 100.00")
                        // 设置点击按钮之后不关闭弹窗
                        .setAutoDismiss(false)
                        .setListener(new PayPasswordDialog.OnListener() {
                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                toast(password);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
//                                toast("取消了");
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.btn_dialog_address:
                // 地区选择对话框
                new AddressDialog.Builder(this)
                        .setTitle(getString(R.string.address_title))
                        // 设置默认省份
                        //.setProvince("重庆市")
                        // 设置默认城市(必须先设置默认省份)
                        // .setCity("南岸区")
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener(new AddressDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                toast(province + city + area);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_date:
                // 日期选择对话框
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        // 确定文本
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置为null 时表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置日期(可支持2019-12-03， 20191203， 时间戳)
                        // .setDate(20191203)
                        // 设置年份
                        //.setYear(2019)
                        // 设置月份
                        //.setMonth(12)
                        // 设置天数
                        //.setDay(3)
                        // 不选择天数
                        //.setIgnoreDay()
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + "-" + "-" + month + "-" + day);

                                // 如果不指定时分秒则默认为现在的时间
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                // 月份从零开始，所以需要减 1
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_time:
                // 时间选择对话框
                new TimeDialog.Builder(this)
                        .setTitle(getString(R.string.time_title))
                        // 确定文本按钮
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 为 null 的时候表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        // 设置时间
                        //.setTime("23:59:59")
                        //.setTime("235959")
                        // 设置小时
                        //.setHour(23)
                        // 设置分钟
                        //.setMinute(59)
                        // 设置秒数
                        //.setSecond(59)
                        // 不选择秒数
                        //.setIgnoreSecond()
                        .setListener(new TimeDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int hour, int minute, int second) {
                                toast(hour + "时" + minute + "分" + second + " 秒");

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, second);
                                toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();
                break;
            case R.id.btn_dialog_update:
                String updateUrl = "aaa";
                // 升级更新对话框
                new UpdateDialog.Builder(this)
                        // 版本名
                        .setVersionName("v 2.0")
                        // 文件大小
                        .setFileSize("10 M")
                        // 是否强制更新
                        .setForceUpdate(false)
                        // 更新日志
                        .setUpdateLog("到底更新了啥\n更新了啥\n更新了啥\n更新了啥")
                        // 下载地址
                        .setDownloadUrl(updateUrl)
                        .show();
                break;
            case R.id.btn_dialog_custom:
                // 自定义对话框
                new BaseDialogFragment.Builder(this)
                        .setContentView(R.layout.dialog_custom)
                        //.setText(id, "我是预设置的文本")
                        .setOnClickListener(R.id.btn_dialog_custom_ok, new BaseDialog.OnClickListener<ImageView>() {

                            @Override
                            public void onClick(BaseDialog dialog, ImageView view) {
                                dialog.dismiss();
                            }
                        })
                        .addOnShowListener(new BaseDialog.OnShowListener() {
                            @Override
                            public void onShow(BaseDialog dialog) {
                                toast("Dialog  显示了");
                            }
                        })
                        .addOnCancelListener(new BaseDialog.OnCancelListener() {
                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("Dialog 取消了");
                            }
                        })
                        .addOnDismissListener(new BaseDialog.OnDismissListener() {
                            @Override
                            public void onDismiss(BaseDialog dialog) {
                                toast("Dialog 销毁了");
                            }
                        })
                        .setOnKeyListener(new BaseDialog.OnKeyListener() {
                            @Override
                            public boolean onKey(BaseDialog dialog, KeyEvent event) {
                                toast("按键代码：" + event.getKeyCode());
                                return false;
                            }
                        })
                        .show();
                break;
        }
    }

}
