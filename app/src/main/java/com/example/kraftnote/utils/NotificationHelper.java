package com.example.kraftnote.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.kraftnote.R;
import com.example.kraftnote.services.NotificationReceiver;

import java.util.Date;

public class NotificationHelper {

    public static final String CHANNEL_ID = "61827";
    private static boolean initialized = false;

    private static void init(Context context) {

        if (initialized) return;

        CharSequence name = context.getString(R.string.note_reminder);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        initialized = true;
    }

    public static void scheduleNotification(Context context, Date time, int requestId, String text) {
        init(context);

        cancelNotification(context, requestId);

        Log.d("Note", DateHelper.toFormattedStringAlt(DateHelper.toDate(time.getTime())));

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", context.getString(R.string.note_reminder));
        intent.putExtra("text", text);
        PendingIntent pending = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schedule notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTime(), pending);


    }

    public static void cancelNotification(Context context, int requestId) {
        init(context);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Cancel notification
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }
}
