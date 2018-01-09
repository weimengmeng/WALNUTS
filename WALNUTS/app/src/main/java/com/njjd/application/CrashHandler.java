package com.njjd.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.njjd.utils.LogUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.LoginActivity;
import com.njjd.walnuts.WelcomeActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by mrwim on 17/7/19.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler{
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    private static CrashHandler INSTANCE ;
    private Context context;

    //1.私有化构造方法
    private CrashHandler(){

    }

    public static synchronized CrashHandler getInstance(){
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    public void init(Context context){
        this.context = context;
    }


    public void uncaughtException(Thread arg0, Throwable arg1) {
        if(arg1.toString().contains("SSLSocketFactory")){
           return;
        }
        MobclickAgent.reportError(context,arg1);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("crash", true);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 0.5秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        System.gc();
    }
}
