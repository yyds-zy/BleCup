package com.bluetooth.cup.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bluetooth.cup.R;
import com.bluetooth.cup.app.BleCupApplication;
import com.bluetooth.cup.ui.activity.MainActivity;

/**
 * Created by 阿飞の小蝴蝶 on 2023/7/31
 * Describe:
 */
public class NotifyUtil {
    private boolean mNotifyState;
    private static NotifyUtil instance;
    private NotifyUtil() {}

    public static NotifyUtil getInstance(){
        if (instance == null) {
            synchronized (NotifyUtil.class) {
                if (instance == null) {
                    instance = new NotifyUtil();
                }
            }
        }
        return instance;
    }

    public void setNotifyState(boolean notifyState) {
        mNotifyState = notifyState;
    }

    public void sendNotify(Context context,String msg,int id) {
        if (!mNotifyState) return;
        NotificationManager manager = (NotificationManager) BleCupApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            Intent clickIntent = new Intent(context, MainActivity.class);
            PendingIntent clickPI = PendingIntent.getActivity(context, 1, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(msg)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentIntent(clickPI)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelID = "bleNotification";
                String channelName = "onSessionExpired";
                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channelID);
            }

            Notification notification = builder.build();
            manager.notify(id, notification);
        }
    }
}
