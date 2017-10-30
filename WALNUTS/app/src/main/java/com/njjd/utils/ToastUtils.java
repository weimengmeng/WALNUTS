package com.njjd.utils;

import android.content.Context;
import android.widget.Toast;

import com.njjd.walnuts.BaseActivity;

/**
 * Created by mrwim on 17/7/13.
 */

public class ToastUtils {
    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
    }
    private static void showToast(Context context, CharSequence msg, int duration) {
        BaseActivity.showToast(msg.toString());
    }

    private static void showToast(Context context, int resId, int duration) {
        BaseActivity.showToast("");
    }
}
