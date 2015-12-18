package com.elbbbird.android.elbbbird.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by zhanghailong-ms on 2015/9/24.
 * 系统调用相关API
 */
public class SystemApi {

    /**
     * 发邮件
     *
     * @param context
     * @param email   邮箱地址
     * @param title   标题
     */
    public static void sendEmail(Context context, String email, String title) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + email));
        data.putExtra(Intent.EXTRA_SUBJECT, title);
        context.startActivity(data);
    }
}
