package com.njjd.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;

import com.ios.dialog.AlertDialog;
import com.njjd.db.DBHelper;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.LoginActivity;
import com.njjd.walnuts.MainActivity;
import com.njjd.walnuts.WelcomeActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import retrofit.http.Url;

/**
 * Created by mrwim on 17/7/13.
 */

public class AppAplication extends Application {
    protected static Context context;
    protected static String appName = "核桃";
    private RefWatcher mRefWatcher;
    String fontPath = "fonts/NotoSansHans-Regular.ttf";
    private PushAgent pushAgent;
    /**
     * 0 系统通知
     * 1 回答通知 你的问题被回答  ret{ article_id}
     * 2 评论通知 有人评论了你    ret{}
     * 3 关注通知
     */
    private int notifyType = 0;
    Handler mainHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
            } else if (msg.what == 2) {
            } else if (msg.what == 3) {
            } else if (msg.what == 0) {
            }
        }

        ;
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        mRefWatcher = LeakCanary.install(this);
        replaceSystemDefaultFont(this, fontPath);
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(handler);
        CommonUtils.init(context);
        /**
         * 友盟登录、分享
         */
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxaaa88f9a47ec1f98", "35a9eef61c384087a3028686789f2900");
        PlatformConfig.setQQZone("1106091328", "7XJSCwws0c8wFtFx");
        PlatformConfig.setSinaWeibo("3031185497", "ecfbe63de14dabaf1d089e31e9e24144", "http://sns.whalecloud.com");
        /**
         *  友盟推送
         */
        pushAgent = PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                SPUtils.put(context, "deviceToken", s);
                initNotification();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.d("device_token" + s + "  " + s1);
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    private void initNotification() {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtils.d("收到通知啦－－－－－－－》" + msg.getRaw().toString());
                return null;
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("收到通知啦－－－－－－－》" + msg.getRaw().toString());
                        JSONObject object=msg.getRaw();
                        try {
                            JSONObject object1=object.getJSONObject("extra");
                            JSONObject object2=object1.getJSONObject("param");
                            if(object2.getString("action").contains("logout")){
                                new AlertDialog(MyActivityManager.getInstance().getLastActivity()).builder().setTitle("异常提示").setMsg("您的账号在另外一台设备登陆，请重新登陆")
                                        .setPositiveButton("确定", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                MyActivityManager.getInstance().finishAllActivity();
                                                Intent intent=new Intent(context, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setCancelable(false).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                if (!isRunning()) {
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    JSONObject object = msg.getRaw();
                    try {
                        JSONObject object2 = object.getJSONObject("extra");
                        if ((object2.isNull("type") ? "" : object2
                                .getString("type")).equals("0")) {
                            //系统通知
                            super.launchApp(context, msg);
                        } else if ((object2.isNull("type") ? "" : object2
                                .getString("type")).equals("1")) {
                            //回答通知
                            super.launchApp(context, msg);
                        } else if ((object2.isNull("type") ? "" : object2
                                .getString("type")).equals("2")) {
                            //评论通知
                        } else if ((object2.isNull("type") ? "" : object2
                                .getString("type")).equals("3")) {
                            //关注通知
                        } else {
                            super.launchApp(context, msg);
                            Intent intent = new Intent();
                            intent.putExtra("data", "1");
                            intent.setAction("attenation"); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                            sendBroadcast(intent); // 发送广播
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        pushAgent.setNotificationClickHandler(notificationClickHandler);
        pushAgent.setMessageHandler(messageHandler);
    }

    public void replaceSystemDefaultFont(Context context, String fontPath) {
        replaceTypefaceField("MONOSPACE", createTypeface(context, fontPath));
    }

    private Typeface createTypeface(Context context, String fontPath) {

        return Typeface.createFromAsset(context.getAssets(), fontPath);

    }

    private void replaceTypefaceField(String fieldName, Object value) {
        try {
            Field defaultField = Typeface.class.getDeclaredField(fieldName);
            defaultField.setAccessible(true);
            defaultField.set(null, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    private boolean isRunning() {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.njjd.walnuts";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }
}
