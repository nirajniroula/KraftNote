package com.example.kraftnote.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.kraftnote.R;
import com.example.kraftnote.utils.NotificationHelper;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_ID = "notification-id";
    private static final int DEFAULT_NOTIFICATION_ID = 87346;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Build notification based on Intent
        Notification notification = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("text"))
                .build();
        // Show notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(
                intent.getIntExtra(NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID), notification
        );
    }
}
