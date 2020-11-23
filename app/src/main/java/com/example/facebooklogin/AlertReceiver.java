package com.example.facebooklogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        com.example.facebooklogin.NotificationHelper notificationHelper = new com.example.facebooklogin.NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
//        setNormalAlertDialog();
    }
//
//    public void setNormalAlertDialog(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.getChatActivityContext());
//        alertDialog.setTitle("To user");
//        alertDialog.setMessage("今天喝水了嗎?");
//        /*一樣，不熟的用這個打就OK了*/
//        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        alertDialog.setNegativeButton("沒有...",(dialog, which) -> {
//
//        });
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }
}
