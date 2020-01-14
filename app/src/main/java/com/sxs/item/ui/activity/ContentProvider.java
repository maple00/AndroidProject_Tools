package com.sxs.item.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.sxs.item.R;
import com.sxs.item.base.BaseActivity;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @Author: a797s
 * @Date: 2020/1/14 9:46
 * @Desc: 日历内容提供者
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public final class ContentProvider extends BaseActivity {

    private static final String TAG = "CalendarProvider";

    public static final int PERMISSION_REQUEST_CODE = 0xA1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar_provider;
    }

    @Override
    protected void initView() {
        // 给日历添加事件
        CalendarProvider();

        // 获取联系人信息
        ContactsProvider();
    }

    /**
     * 获取联系人信息
     */
    private void ContactsProvider() {

    }

    /**
     * 读取联系人点击事件
     * @param view
     */
    public void getContactsInfo(View view){

    }

    /**
     * 给日历添加事件
     */
    private void CalendarProvider() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            queryCalendarsPermission();
        }
        queryCalendars();
    }

    /**
     * 添加日历的事件提醒的点击事件
     * @param view
     */
    public void addAlertEvents(View view) {
        // 日历Provider：_id = 1
        long calID = 1;
        // 设置日历事件的开始时间和结束时间
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2020, 0, 15, 0, 0);
        long beginMillis = beginTime.getTimeInMillis();
        // 结束时间
        Calendar endTime = Calendar.getInstance();
        endTime.set(2020, 0, 15, 23, 59);
        long endMillis = endTime.getTimeInMillis();
        // 事件内容
        String timeZone = TimeZone.getDefault().getID();
        Log.d(TAG, "timeZone ---> " + timeZone);
        ContentValues eventValues = new ContentValues();
        // 设置时区
        eventValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        eventValues.put(CalendarContract.Events.DTSTART, beginMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        eventValues.put(CalendarContract.Events.TITLE, "双十一购物疯狂开抢");
        eventValues.put(CalendarContract.Events.DESCRIPTION, "尽量把自己想买的东西一口气全部买完，毫不犹豫");
        eventValues.put(CalendarContract.Events.EVENT_LOCATION, "重庆");
        // 插入数据
        Uri eventUri = CalendarContract.Events.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        // 权限检查
        if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Uri resultUri = contentResolver.insert(eventUri, eventValues);
        Log.d(TAG, "resultUri: " + resultUri);

        String id = resultUri.getLastPathSegment();

        // 加入提醒--- 提醒方式有：邮件，短信，闹钟
        ContentValues remiderValues = new ContentValues();
        remiderValues.put(CalendarContract.Reminders.EVENT_ID, id);
        remiderValues.put(CalendarContract.Reminders.MINUTES, 15);
        remiderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);      // 提醒方式--闹钟
        Uri remiderUri = CalendarContract.Reminders.CONTENT_URI;
        contentResolver.insert(remiderUri, remiderValues);

    }

    /**
     * 请求权限
     */
    private void queryCalendarsPermission() {
        // 1、先检查权限
        int readPermission = checkSelfPermission(Manifest.permission.READ_CALENDAR);
        int writePermission = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);
        if (readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED) {
            // 表示有权限

        } else {
            Log.d(TAG, "requestPermissions");
            // 去获取权限
            // 有个提示，用户点击了确定再去调用权限，
            // 如果点击了不再提示，就不再获取了，再根据产品经理的交互去写。
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 判断结果
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 有权限
                Log.d(TAG, "has permissions");
            } else {
                Log.d(TAG, "no permissions");
                // 没有权限
                finish();
            }
        }
    }

    /**
     * 查询日历
     */
    private void queryCalendars() {
        ContentResolver contentResolver = getContentResolver();
        //Uri uri = Uri.parse("content://" + "com.android.calendar/" + "calendars");
        Uri uri = Uri.parse("content://" + CalendarContract.Calendars.CONTENT_URI + "/calendars");
        @SuppressLint("Recycle")
        Cursor query = contentResolver.query(uri, null, null, null, null);
        String[] columnNames = new String[0];
        if (query != null) {
            columnNames = query.getColumnNames();
        }
        if (query != null) {
            while (query.moveToNext()) {
                Log.d(TAG, "=======================");
                for (String columnName : columnNames) {
                    Log.d(TAG, columnName + " ========= " + query.getString(query.getColumnIndex(columnName)));
                }
                Log.d(TAG, "=====================");
            }
        }
    }
}
