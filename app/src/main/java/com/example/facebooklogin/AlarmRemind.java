package com.example.facebooklogin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class AlarmRemind extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        if(bData.get("title").equals("activity_app"))
        {
            //主要執行的程式
            
        }
    }

    /***    加入(與系統註冊)鬧鐘    ***/
    public static void add_alarm(Context context, Calendar cal) {
        Log.d(TAG, "alarm add time: " + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + " " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));

        Intent intent = new Intent(context, AlarmRemind.class);
        // 以日期字串組出不同的 category 以添加多個鬧鐘
        intent.addCategory("ID." + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + "-" + String.valueOf((cal.get(Calendar.HOUR_OF_DAY) )) + "." + String.valueOf(cal.get(Calendar.MINUTE)) + "." + String.valueOf(cal.get(Calendar.SECOND)));
        String AlarmTimeTag = "Alarmtime " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(cal.get(Calendar.MINUTE)) + ":" + String.valueOf(cal.get(Calendar.SECOND));

        intent.putExtra("title", "activity_app");
        intent.putExtra("time", AlarmTimeTag);

        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);       //註冊鬧鐘
    }

    /***    取消(與系統註冊的)鬧鐘    ***/
    private static void cancel_alarm(Context context, Calendar cal) {
        Log.d(TAG, "alarm cancel time: " + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + " " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));

        Intent intent = new Intent(context, AlarmRemind.class);
        // 以日期字串組出不同的 category 以添加多個鬧鐘
        intent.addCategory("ID." + String.valueOf(cal.get(Calendar.MONTH)) + "." + String.valueOf(cal.get(Calendar.DATE)) + "-" + String.valueOf((cal.get(Calendar.HOUR_OF_DAY) )) + "." + String.valueOf(cal.get(Calendar.MINUTE)) + "." + String.valueOf(cal.get(Calendar.SECOND)));
        String AlarmTimeTag = "Alarmtime " + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(cal.get(Calendar.MINUTE)) + ":" + String.valueOf(cal.get(Calendar.SECOND));

        intent.putExtra("title", "activity_app");
        intent.putExtra("time", AlarmTimeTag);

        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pi);    //取消鬧鐘，只差在這裡
    }

}
