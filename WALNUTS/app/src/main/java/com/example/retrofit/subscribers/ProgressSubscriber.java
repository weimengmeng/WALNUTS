package com.example.retrofit.subscribers;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.mywidget.LoadingDialog;
import com.njjd.utils.LogUtils;
import com.njjd.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.WMM on 2016/8/25
 */
public class ProgressSubscriber<T> extends Subscriber<T> {
    //    回调接口
    private HttpOnNextListener mSubscriberOnNextListener;
    //    弱引用反正内存泄露
    private WeakReference<Context> mActivity;
    //加载框是否显示
    private boolean isShow = false;
    //    是否能取消请求
    private boolean cancel = false;
    //    加载框可自己定义
    private LoadingDialog pd;

    /**
     * 是否显示加载框和是否可以取消请求
     *
     * @param mSubscriberOnNextListener
     * @param context
     * @param isShow
     * @param cancel
     */
    public ProgressSubscriber(HttpOnNextListener mSubscriberOnNextListener, Context context, boolean isShow, boolean cancel) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<>(context);
        this.cancel = cancel;
        this.isShow = isShow;
        initProgressDialog();
    }

    /**
     * 初始化加载框
     */
    private void initProgressDialog() {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new LoadingDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }

    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (isShow)
            showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if (isShow)
            dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Context context = mActivity.get();
        if (context == null) return;
        if (e instanceof SocketTimeoutException) {
            ToastUtils.showShortToast(context, "网络中断，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            ToastUtils.showShortToast(context, "网络中断，请检查您的网络状态");
        } else {
            if (e.toString().contains("暂无")||e.toString().contains("NO")) {

            } else {
                ToastUtils.showShortToast(context, e.toString().split(":").length < 1 ? e.toString() : e.toString().split(":")[1]);
            }
        }
        if (isShow)
            dismissProgressDialog();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}