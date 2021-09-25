package com.example.todolistapp;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SnoozeSet extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snooze_screen);
        findViewById(R.id.alarm_status_btn);
     }

public void setAlarm(long timeInMillis,long id){
    AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this,Alarm.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this,(int) id,intent,0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
}
}
