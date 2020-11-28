package com.example.facebooklogin;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class NotificationReceiver extends BroadcastReceiver {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String chatroom_id;
    int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String answer = null;
        String habbit_name =null;

        //接收HabitSetRemindTimet 傳來的Bundle
        Bundle bData = intent.getExtras();
        chatroom_id = bData.getString("chatroom_id");

        //---------------------------------------------------------
        //把提醒回應寫入setContentText
        try {
            String response=new AsyncPostReturn_notice().execute(chatroom_id).get();
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(response);
                answer = jsonObject.getString("answer");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //把habbit_name寫入setContentTitle
        try {
            String response=new AsyncPostGetHabbitName().execute(chatroom_id).get();
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(response);
                habbit_name = jsonObject.getString("habbit_name");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //---------------------------------------------------------

        Intent myIntent = new Intent(context, ChatActivity.class);
        PendingIntent myPendingIntent = PendingIntent.getActivity(context, m, myIntent, 0);



        Intent accomplishIntent = new Intent(context, ReturnrNoticeActivity.class);
        accomplishIntent.putExtra("return_chitchatAnswer", answer);
        accomplishIntent.putExtra("chatroom_id", chatroom_id);
        PendingIntent accomplishPendingIntent = PendingIntent.getActivity(context, m, accomplishIntent, 0);
        


        /*
        //多行通知
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle("打卡通知");
        style.bigText(answer);
        //style.setSummaryText(context.getString(R.string.app_name));

         */

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, chatroom_id)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.birdsound))
                .setSmallIcon(R.drawable.ic_iconsvg_white_01)
                .setTicker("您有訊息!")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(habbit_name+" 打卡通知")
                .setContentText(answer)
                .addAction(R.drawable.ic_baseline_emoji_emotions_24, "完成", accomplishPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                //.setStyle(style)
                .setAutoCancel(true)
                .setContentIntent(myPendingIntent)
                .setColor(Color.BLUE);
        //.setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(m, builder.build());




    }

    //--------------------------------------------------------------------------
    //-------------------------------------
    //POST 打卡提醒
    //-------------------------------------
    static class AsyncPostReturn_notice   extends AsyncTask<String,Void,String>
    {
        HttpURLConnection conn;
        URL url = null;
        /*
        private String answer;

        private String chatroom_id;
        */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/return_notice.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("chatroom_id", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return(String.valueOf(result));
                }else{
                    return("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }

        }
        @Override
        public void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                //answer = jsonObject.getString("answer");
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //--------------------------------------------------------------------------
    //-------------------------------------
    //POST 獲得習慣名稱
    //-------------------------------------
    static class AsyncPostGetHabbitName   extends AsyncTask<String,Void,String>
    {
        HttpURLConnection conn;
        URL url = null;
        //private String habbit_name;
        //private String chatroom_id;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/getHabbitName.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("chatroom_id", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return(String.valueOf(result));
                }else{
                    return("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }

        }
        @Override
        public void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

    }






}