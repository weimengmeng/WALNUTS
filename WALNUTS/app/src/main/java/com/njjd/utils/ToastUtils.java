package com.njjd.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.njjd.walnuts.R;

/**
 * Created by mrwim on 17/7/13.
 */

public class ToastUtils {
    private static Toast toast;

    private static View view;
    private static TextView mTextView;

    private ToastUtils() {
    }

    private static void getToast(Context context,CharSequence msg) {
        if (toast == null) {
            toast = new Toast(context);
        }
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.toast, null);
            //初始化布局控件
            mTextView = (TextView) view.findViewById(R.id.message);
            //为控件设置属性
            mTextView.setText(msg);
        }
        mTextView.setText(msg);
        toast.setView(view);
    }

    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, CharSequence msg, int duration) {
        try {
            getToast(context,msg);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    private static void showToast(Context context, int resId, int duration) {
        try {
            if (resId == 0) {
                return;
            }
            getToast(context,"");
            toast.setText(resId);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
