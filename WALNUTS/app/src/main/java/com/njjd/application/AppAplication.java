package com.njjd.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ios.dialog.AlertDialog;
import com.njjd.db.DBHelper;
import com.njjd.domain.QuestionEntity;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.LoginActivity;
import com.njjd.walnuts.MainActivity;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.WelcomeActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrwim on 17/7/13.
 */

public class AppAplication extends Application {
    protected static Context context;
    protected static String appName = "核桃";
    private PushAgent pushAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        replaceSystemDefaultFont(this);
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        /**
         * 友盟登录、分享
         */
//        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxaaa88f9a47ec1f98", "35a9eef61c384087a3028686789f2900");
        PlatformConfig.setQQZone("1106091328", "7XJSCwws0c8wFtFx");
        PlatformConfig.setSinaWeibo("3031185497", "ecfbe63de14dabaf1d089e31e9e24144", "http://sns.whalecloud.com");
        /**
         *  友盟推送
         */
        pushAgent = PushAgent.getInstance(this);
        MobclickAgent.openActivityDurationTrack(false);
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
        EMOptions options = new EMOptions();
        options.setAutoLogin(false);
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
        MultiDex.install(this);
        CommonUtils.init(context);
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=59df0a58");
    }
    public static Context getContext() {
        return context;
    }

    @Override
    public Resources getResources() {
        Resources resources=super.getResources();
        Configuration configuration=new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return resources;
    }

    private void initNotification() {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                Intent intent = new Intent();
                intent.setAction(ConstantsVal.NEW_INFORM);
                sendBroadcast(intent);
                return null;
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = msg.getRaw();
                        try {
                            JSONObject object1 = object.getJSONObject("extra");
                            JSONObject object2 = object1.getJSONObject("param");
                            if (object2.getString("action").equals("logout")) {
                                new AlertDialog(MyActivityManager.getInstance().getLastActivity()).builder().setTitle("下线通知").setMsg("你的账号已在其他地方登录，本地已下线。如非本人登录，建议立即修改密码")
                                        .setPositiveButton("确定", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                EMClient.getInstance().logout(true);
                                                MyActivityManager.getInstance().finishAllActivity();
                                                Intent intent = new Intent(context, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setCancelable(false).show();
                            }
                            if(object2.getString("action").equals("notice_show")){
                                Intent intent = new Intent();
                                intent.setAction(ConstantsVal.NEW_INFORM);
                                sendBroadcast(intent);
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
                LogUtils.d("huan"+msg.getRaw().toString());
                if (!isRunning()) {
                    Intent intent = new Intent(context, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if(SPUtils.get(context,"isLogin","0").toString().equals("0")){
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    try {
                        JSONObject object = msg.getRaw();
                        JSONObject object1 = object.getJSONObject("extra");
                        JSONObject object2 = object1.getJSONObject("param");
                        if (object2.getString("action").equals("follow_article")) {
                            Intent intent = new Intent();
                            intent.setAction(ConstantsVal.NEW_INFORM);
                            sendBroadcast(intent);
                        }
                        if (object2.getString("action").equals("follow_user")) {
                            Intent intent = new Intent(context, PeopleInfoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("uid",object2.getString("uid"));
                            startActivity(intent);
                        }
                        if (object2.getString("action").equals("answer")) {
                            if(object2.toString().contains("device_token")){
//                                if(object2.getString("type").equals("1.0")||object2.getString("type").equals("1")){
                                    //此处是评论推送
                                    Intent intent=new Intent(context, IndexDetailActivity.class);
                                    QuestionEntity entity= DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(object2.getString("article_id").contains(".0")?object2.getString("article_id"):object2.getString("article_id")+".0");
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("question", entity);
                                    intent.putExtra("question", bundle);
                                    intent.putExtra("type","2");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("comment_id",String.valueOf(Float.valueOf(object2.getJSONObject("comment").getString("answer_id"))));
                                    startActivity(intent);
//                                }else{
//                                    Intent intent = new Intent(context, MainActivity.class);
//                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                                    intent.setAction(Intent.ACTION_MAIN);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                                    startActivity(intent);
//                                }
                            }else{
                                //此处是回答推送
                                Intent intent=new Intent(context, IndexDetailActivity.class);
                                QuestionEntity entity= DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(object2.getString("article_id")+".0");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("question", entity);
                                intent.putExtra("question", bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("type","1");
                                context.startActivity(intent);
                            }
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

    public void replaceSystemDefaultFont(Context context) {
        replaceTypefaceField("MONOSPACE", createTypeface());
    }

    private Typeface createTypeface() {
        return Typeface.SANS_SERIF;
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
