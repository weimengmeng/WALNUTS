package com.njjd.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.graphics.Typeface;

import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Field;

/**
 * Created by mrwim on 17/7/13.
 */

public class AppAplication extends Application {
    protected static Context context;
    protected static String appName = "核桃";
    private RefWatcher mRefWatcher;
    String fontPath = "fonts/NotoSansHans-Regular.ttf";
    private PushAgent pushAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        mRefWatcher = LeakCanary.install(this);
        replaceSystemDefaultFont(this, fontPath);
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
        /**
         * 友盟登录、分享
         */
        Config.DEBUG=true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxaaa88f9a47ec1f98", "35a9eef61c384087a3028686789f2900");
        PlatformConfig.setQQZone("1106091328", "7XJSCwws0c8wFtFx");
        PlatformConfig.setSinaWeibo("3031185497", "ecfbe63de14dabaf1d089e31e9e24144", "http://sns.whalecloud.com");
        /**
         *  友盟推送
         */
        pushAgent=PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtils.d("device token------->"+s);
                SPUtils.put(context,"deviceToken",s);
                initNotification();
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }
    public static Context getContext(){
        return  context;
    }
    private void initNotification(){
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtils.d("收到通知啦－－－－－－－》"+msg.toString());
                return  null;
            }
        };
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
}
