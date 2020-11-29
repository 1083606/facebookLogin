package com.example.facebooklogin;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

//-------------------------------
//-------------------------------

public class HabitSetTimeActivity extends AppCompatActivity implements View.OnClickListener,TimePickerDialog.OnTimeSetListener {
    String user_id,habbit_name,habbit_id,original_intention,goodness,badness;

    TextView mTimeTextView;
    TextView mPickPunchTime;
    ImageView imgmPickPunchTime;
    Context mContext = this;
    Button btn_next;
    Spinner spinnerDays;
    Button buttonSubmitList;
    Toolbar toolbar;
    Long timeAtButtonClick;

    Calendar c;

    private NotificationManagerCompat notificationManager;


    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_habit_set_time);

        //返回按钮的监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        user_id=readUserID();
        notificationManager = NotificationManagerCompat.from(this);

        //接收從HabitMotivationActivity的bundle
        Bundle bundle = getIntent().getExtras();
        habbit_name = bundle.getString("habbit_name");
        habbit_id = bundle.getString("habbit_id");
        original_intention =bundle.getString("original_intention");
        goodness=bundle.getString("goodness");
        badness=bundle.getString("badness");

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        //Calendar calendar = Calendar.getInstance();
        btn_next = findViewById(R.id.btn_next);
        spinnerDays=findViewById(R.id.spinnerDays);

        buttonSubmitList = findViewById(R.id.btn_next);
        buttonSubmitList.setOnClickListener(this);

        //final  int hour = calendar.get(calendar.HOUR_OF_DAY);
        //final  int minute = calendar.get(calendar.MINUTE);

        mPickPunchTime = (TextView) findViewById(R.id.mPickPunchTime);
        imgmPickPunchTime = (ImageView) findViewById(R.id.imgmPickPunchTime);
        //-------------------------------
//        mPickPunchTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//            @Override
//            public void onClick(View v) {
//               TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
//                   @Override
//                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                       mPickPunchTime.setText(hourOfDay+":"+minute);
//                   }
//               },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
//               timePickerDialog.show();
//            }
//        });

        mPickPunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        imgmPickPunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


//
//        imgmPickPunchTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int hour = 0;
//                int minute = 0;
//                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        DecimalFormat decimalFormat = new DecimalFormat("00");
//                        mPickPunchTime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
//                    }
//                },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
//                timePickerDialog.show();
//            }
//        });

        btn_next = findViewById(R.id.btn_next);
        // 按下下一步按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("點擊設定".equals(mPickPunchTime.getText().toString()))&& !("".equals(spinnerDays.getSelectedItem().toString())))
                {
                    String habbit_status="0";
                    String signed_time=mPickPunchTime.getText().toString();
                    String days=spinnerDays.getSelectedItem().toString();
                    new AsyncPostcreateChatRoom().execute(user_id,habbit_id,habbit_name,habbit_status,signed_time,original_intention,goodness,badness,days);

                }
                else {
                    Toast.makeText(HabitSetTimeActivity.this, "請入完整!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private String readUserID(){
        SharedPreferences preferences = this.getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        return preferences.getString("UserID","未存任何資料");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        mPickPunchTime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        //String cs = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        //mPickPunchTime.setText(hourOfDay+":"+minute);
        timeAtButtonClick = c.getTimeInMillis();

    }



    //--------------------------------------------------------------------------
    //-------------------------------------
    //Post createChatRoom
    //-------------------------------------
    public class AsyncPostcreateChatRoom   extends AsyncTask<String,Void,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/createChatRoom.php");
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
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("habbit_id", params[1])
                        .appendQueryParameter("habbit_name", params[2])
                        .appendQueryParameter("habbit_status", params[3])
                        .appendQueryParameter("signed_time", params[4])
                        .appendQueryParameter("original_intention", params[5])
                        .appendQueryParameter("goodness", params[6])
                        .appendQueryParameter("badness", params[7])
                        .appendQueryParameter("days", params[8]);
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
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                int result = Integer.valueOf(jsonObject.getString("result")).intValue();
                if (result==0){
                    String data = jsonObject.getString("data");
                    String chatroom_id=jsonObject.getString("chatroom_id");
                    Toast.makeText(HabitSetTimeActivity.this, data+chatroom_id, Toast.LENGTH_SHORT).show();

                    Long alarmTime=timeAtButtonClick;

                    int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                    String mSt= String.valueOf(m);;
                    createNotificationChannels(mSt);
                    //---------------------------------------------
                    //******設置 AlarmManager**********************
                    //---------------------------------------------
                    Intent intent = new Intent(HabitSetTimeActivity.this,NotificationReceiver.class);
                    intent.putExtra("chatroom_id", chatroom_id);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(HabitSetTimeActivity.this,
                            m, intent, 0);
                    AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeAtButtonClick,AlarmManager.INTERVAL_DAY,pendingIntent);
                    //-------------------------------------------------------


                    //post成功，跳至HabitSetCharacterActivity
                    //Toast.makeText(login.this,user_id+"登入成功",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent();
                    intent2.setClass(HabitSetTimeActivity.this,HabitSetCharacterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("chatroom_id",chatroom_id);
                    intent2.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent2);
                }else {
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(HabitSetTimeActivity.this, result+data + error, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //----------------------------------------------------------
    //單獨去設定；比如通知開關、提示音、是否震動或者是重要程度等
    private void createNotificationChannels(String mSt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID= mSt;
            String CHANNEL_NAME= "Default Channel";
            String CHANNEL_DESCRIPTION="this is default channel!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            //初始化 NotificationManager，取得 Notification 服務。
            NotificationManager notificationManager = (NotificationManager)getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
