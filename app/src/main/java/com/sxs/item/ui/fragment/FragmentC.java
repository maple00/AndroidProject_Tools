package com.sxs.item.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sxs.item.R;
import com.sxs.item.base.BaseFragment;
import com.sxs.tools.permission.OnPermission;
import com.sxs.tools.permission.Permission;
import com.sxs.tools.permission.XXPermissions;
import com.sxs.tools.statusbar.StatusBarUtil;

import java.util.List;
import java.util.Random;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:32
 * @des: 项目中常用的框架展示
 */
public class FragmentC extends BaseFragment implements View.OnClickListener {

    private ImageView mImageView;

    @Override
    protected int initLayout() {
        return R.layout.fragment_mes;
    }

    //  初始化控件
    @Override
    protected void initView(final View view) {
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(getActivity());
        if (!StatusBarUtil.setStatusBarDarkTheme(getActivity(), true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(getActivity(), 0x55000000);
        }


        mImageView = view.findViewById(R.id.iv_show_image);
        // 正常显示图片
        view.findViewById(R.id.btn_normal_image).setOnClickListener(this);
        // 加载圆形图片
        view.findViewById(R.id.btn_round_image).setOnClickListener(this);
        // 加载圆角图片
        view.findViewById(R.id.btn_corner_image).setOnClickListener(this);
        // 选择一张图片
        view.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        // 请求一次权限
        view.findViewById(R.id.btn_request_permission).setOnClickListener(this);
        // 设置状态栏黑色字体
        view.findViewById(R.id.btn_set_state_black).setOnClickListener(this);
        // 设置状态栏白色字体
        view.findViewById(R.id.btn_set_state_white).setOnClickListener(this);
        // 设置状态栏背景随机颜色
        view.findViewById(R.id.btn_set_state_random).setOnClickListener(this);

    }

    // 初始化数据
    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_normal_image:
                mImageView.setVisibility(View.VISIBLE);
                Glide.with(view).load("https://www.baidu.com/img/bd_logo.png").into(mImageView);
                break;
            case R.id.btn_round_image:
                mImageView.setVisibility(View.VISIBLE);
                Glide.with(view)
                        .load("https://www.baidu.com/img/bd_logo.png")
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                        .into(mImageView);
                break;
            case R.id.btn_corner_image:
                mImageView.setVisibility(View.VISIBLE);
                //设置图片圆角角度
//                RoundedCorners roundedCorners = new RoundedCorners(100);
//                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//                // RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
//                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

                Glide.with(view).load("https://www.baidu.com/img/bd_logo.png")
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)).override(300, 300))
                        .into(mImageView);
                break;
            case R.id.btn_choose_img:
                toast("暂未实现");
                break;
            case R.id.btn_request_permission:
                XXPermissions.with(getActivity())
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        // 支持6.0 悬浮窗权限 8.0 安装权限
//                        .permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                        // 不指定权限则自动获取清单中的危险权限
                        .permission(Permission.CAMERA)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    toast("获取权限成功");
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
            case R.id.btn_set_state_black:
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
                break;
            case R.id.btn_set_state_white:
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
                break;
            case R.id.btn_set_state_random:
                Random random = new Random();
                int color = 0xff000000 | random.nextInt(0xffffff);
                StatusBarUtil.setStatusBarColor(getActivity(), color);
                break;
        }
    }


}
