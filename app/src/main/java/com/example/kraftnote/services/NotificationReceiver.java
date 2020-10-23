package com.example.kraftnote.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.provider.Settings;

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
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .build();
        // Show notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(
                intent.getIntExtra(NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID), notification
        );
    }
}
