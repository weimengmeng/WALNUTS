package com.njjd.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

/**
 * Created by mrwim on 17/8/22.
 */

public class NotificationUtils {
    private static NotificationManager mNotificationManager;
    private static Notification notification;
    private static MediaPlayer mMediaPlayer;

    /**
     * 创建通知，  * 请在调用此方法时开启子线程
     *
     * @param context    上下文
     * @param icon       通知图片
     * @param tickerText 通知未拉开的内容
     * @param title      通知标题
     * @param content    通知主内容
     * @param intent     意图
     * @param id
     */
    public static void createNotif(Context context, int icon, String tickerText, String title, String content, Intent intent, int id) {
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setTicker(tickerText)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(icon);
        notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(id, notification);
    }

    public static void virbrate(Context mContext) {
        mMediaPlayer = MediaPlayer.create(mContext,
                RingtoneManager.TYPE_NOTIFICATION);
        mMediaPlayer.setLooping(false);
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                    }
                });
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}
