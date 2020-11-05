package com.example.facebooklogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetUserData extends AppCompatActivity {
    private TextView txtUserName,txtUserBirthday;
    private Button btn_next,btn_datePicker;
    private Spinner spinnerUserGender;

    //post
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    //userId,userName
    String UserID,UserName;

    //生日
    Calendar c = Calendar.getInstance();
    int cday, cmonth, cyear;
    String UserBirthday,UserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_data);
        txtUserName=findViewById(R.id.userName);
        txtUserBirthday=findViewById(R.id.userBirthday);
        btn_next = findViewById(R.id.btn_next);
        //spinner設定
        spinnerUserGender = findViewById(R.id.spinnerUserGender);
        final String[] item = {"男性♂", "女性♀"};
        ArrayAdapter<String> itemlist = new ArrayAdapter<>(this,R.layout.myspinner,item);
        spinnerUserGender.setAdapter(itemlist);

        //-----------------------------------------------------
        //接收從getUserName的bundle
        Bundle bundle = getIntent().getExtras();
        UserID = bundle.getString("UserID");
        UserName = bundle.getString("UserName");
        txtUserName.setText("Hello!"+UserName);

        //設定生日
        GregorianCalendar calendar=new GregorianCalendar();
        Button btn_datePicker = (Button) findViewById(R.id.btn_datePicker);
        txtUserBirthday = (TextView) findViewById(R.id.userBirthday);
        btn_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new DatePickerDialog(GetUserData.this, d,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 按下下一步按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("".equals(txtUserBirthday.getText().toString())))
                {
                    UserGender = spinnerUserGender.getSelectedItem().toString();
                    //Toast.makeText(GetUserData.this,UserID+UserBirthday+UserGender , Toast.LENGTH_SHORT).show();
                    new AsyncPost().execute(UserID,UserBirthday,UserGender);
                }
                else {
                    Toast.makeText(GetUserData.this, "請輸入生日", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //生日顯示
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            cday = dayOfMonth;
            cmonth = monthOfYear + 1;
            cyear = year;
            txtUserBirthday.setText(cyear + "/" + cmonth + "/" + cday);
            UserBirthday =txtUserBirthday.getText().toString();
        }
    };



    //-------------------------------------
    //post
    //-------------------------------------
    private class AsyncPost   extends AsyncTask<String,Void,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/addUserInfromation.php");

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
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("birthday", params[1])
                        .appendQueryParameter("gender", params[2]);
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
                String result = jsonObject.getString("result");
                int resultValue=Integer.parseInt(result);
                if (resultValue==0){
                    //post 成功，取出 data
                    String data = jsonObject.getString("data");
                    //傳入成功，跳至"歡迎頁面"
                    Toast.makeText(GetUserData.this, result+data, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(GetUserData.this,startdescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserID",UserID);
                    bundle.putString("UserName",UserName);
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent);
                }else{
                    String data = jsonObject.getString("data");
                    String message1 = jsonObject.getString("message 1");
                    String message2 = jsonObject.getString("message 2");
                    Toast.makeText(GetUserData.this, result+data+message1+message2, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
