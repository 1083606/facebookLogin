package com.example.facebooklogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HabitSetRemindTime extends AppCompatActivity implements View.OnClickListener{
    //------------------------------
    LinearLayout layoutList;
    Button buttonAdd;
    Button btn_next;
    //ArrayList<Remindertime> remindertimesList = new ArrayList<>();
    ArrayList<String> remindertimesList = new ArrayList<>();
    Context mContext = this;
    //------------------------------
    String chatroom_id;
    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_set_remind_time);

        //------------------------------
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        //-----------------------

        //接收從HabitMotivationActivity的bundle
        Bundle bundle = getIntent().getExtras();
        chatroom_id = bundle.getString("chatroom_id");

    }

    //------------------------------
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_add:
                addView();
                break;

            case R.id.btn_next:
                if (checkIfValidAndRead()){
                    for (int i = 0; i< remindertimesList.size(); i++) {
                        new AsyncPostaddRemindTime().execute(chatroom_id,remindertimesList.get(i));
                    }
                    GoChatRoom();
                    //Toast.makeText(this, remindertimesList.get(1), Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean checkIfValidAndRead() {
        remindertimesList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){
            String time="0";

            View remindertimeView = layoutList.getChildAt(i);

            TextView text_remindertime = (TextView)remindertimeView.findViewById(R.id.text_remindertime);
            //AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);

            //Remindertime remindertime = new Remindertime();

            if(!text_remindertime.getText().toString().equals("")){
                time=text_remindertime.getText().toString();
                //remindertime.setReminderTime(text_remindertime.getText().toString());
            }else {
                result = false;
                break;
            }
            remindertimesList.add(time);
        }

        if(remindertimesList.size()==0){
            result = false;
            Toast.makeText(this, "請輸入完整", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "請輸入完整", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void addView() {

        final View remindertimeView = getLayoutInflater().inflate(R.layout.row_add_remindertime,null,false);

        TextView text_remindertime = (TextView)remindertimeView.findViewById(R.id.text_remindertime);
        //AppCompatSpinner spinnerTeam = (AppCompatSpinner)remindertimeView.findViewById(R.id.spinner_team);
        ImageView imageClose = (ImageView)remindertimeView.findViewById(R.id.image_remove);

        //ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,teamList);
        //spinnerTeam.setAdapter(arrayAdapter);


        text_remindertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0;
                int minute = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        text_remindertime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(remindertimeView);
            }
        });
        layoutList.addView(remindertimeView);
    }

    private void removeView(View view){
        layoutList.removeView(view);
    }
    //原本設定每日提醒時間 END------
    //------------------------------

    //--------------------------------------------------------------------------
    //-------------------------------------
    //Post createChatRoom
    //-------------------------------------
    public class AsyncPostaddRemindTime   extends AsyncTask<String,Void,String>
    {
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/addRemindTime.php");
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
                //user_id,habbit_id,habbit_name,habbit_status,signed_time,original_intention,goodness,badness,days,nick_name
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("chatroom_id", params[0])
                        .appendQueryParameter("remind_time", params[1]);
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
        protected void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                int result = Integer.valueOf(jsonObject.getString("result")).intValue();
                if (result==0){
                    String data = jsonObject.getString("data");
                    String remind_id=jsonObject.getString("remind_id");
                    //Toast.makeText(HabitSetRemindTime.this, data+chatroom_id, Toast.LENGTH_SHORT).show();

                    /*
                    //post成功，跳至HabitSetCharacterActivity
                    //Toast.makeText(login.this,user_id+"登入成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(HabitSetRemindTime.this,ChatroomActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("chatroom_id",chatroom_id);
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent);

                     */

                }else {
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(HabitSetRemindTime.this, result+data + error, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //去chatRoom
    private void GoChatRoom(){
        Intent intent = new Intent();
        intent.setClass(HabitSetRemindTime.this,ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatroom_id",chatroom_id);
        intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
        startActivity(intent);
    }

}
