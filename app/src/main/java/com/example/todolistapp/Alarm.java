package com.example.todolistapp;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.view.KeyEventDispatcher;

import java.io.IOException;

import static androidx.legacy.content.WakefulBroadcastReceiver.completeWakefulIntent;
import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

public class Alarm extends BroadcastReceiver {

    public Alarm(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (uriAlarm == null) {
            uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.ring);
        mediaPlayer.start();
//        Intent intent1 = new Intent();
//        intent1.setClassName("com.example.todolistapp","com.example.todolistapp.SnoozeSet");
        Intent intent1 = new Intent(context,SnoozeSet.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
//        Ringtone ringtone = RingtoneManager.getRingtone(context, uriAlarm);
//        ringtone.play();
//        String s = intent.getExtras().get("M").toString();
        ComponentName componentName = new ComponentName(context.getPackageName(), AlarmServices.class.getName());
        AlarmServices.enqueueWork(context,componentName,1,intent);

        setResultCode(Activity.RESULT_OK);
    }

    public static class AlarmServices extends JobIntentService {
        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        private String CHANNEL_ID = "Alarm_Notification";


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onHandleWork(@NonNull Intent intent) {
            sendNotification(intent.getStringExtra("M"));

        }



        @RequiresApi(api = Build.VERSION_CODES.O)
        private void sendNotification(String message) {
            Intent intentNotification = new Intent(this,Alarm.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intentNotification,
            PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentIntent(pendingIntent).setContentTitle("Alarm")
                    .setSmallIcon(android.R.drawable.btn_star_big_on).setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"Alarm Notified",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
            manager.notify(1, builder.build());
            Log.e("NO","Notification send");
        }
    }
}
