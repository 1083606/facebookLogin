package com.example.facebooklogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity{

    EditText userInput;
    RecyclerView recyclerView;
    ChatAdapter messageAdapter;
    List<Response> responseMessageList;
    //TextView mTxtAnswer;
    Handler handler;
    TextView remind;
    private static Context context;

    //post
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //返回按钮的监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_chat);
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new ChatAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);
        ChatActivity.context = ChatActivity.this;


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    Response responseMessage = new Response(userInput.getText().toString(), true,false);
                    responseMessageList.add(responseMessage);
                    //AnswerPost(userInput.getText().toString());
                    String input = userInput.getText().toString();
                    userInput.setText(null);
                    new AsyncPostGetAnswer().execute(input);
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                        userInput.setText(null);

                }
                return false;
            }
        });
    }



    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }


    //--------------------------------------------------------------------------
    //-------------------------------------
    //POST
    //-------------------------------------
    public class AsyncPostGetAnswer   extends AsyncTask<String,Void,String>
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
                url = new URL("https://64e26373ba3a.ngrok.io");
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
                        .appendQueryParameter("question", params[0]);
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
                String answer = jsonObject.getString("answer");
                returnAnswer(answer);
                //Toast.makeText(ChatActivity.this, answer, Toast.LENGTH_LONG).show();
//                checkResultData(Integer.parseInt(result));
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void returnAnswer(String answer) {
        String returnAnswer=answer;
        Response responseMessage2 = new Response(returnAnswer,false,false);
        responseMessageList.add(responseMessage2);
        messageAdapter.notifyDataSetChanged();
        userInput.setText(null);
        if (!isLastVisible())
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        userInput.setText(null);
    }

    public static Context getChatActivityContext(){
        return ChatActivity.context;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            //Toast.makeText(ChatActivity.this, "返回", Toast.LENGTH_SHORT).show();
            //finish();
            Intent intent = new Intent();
            intent.setClass(ChatActivity.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("UserName",readUserName());
            bundle.putString("UserID", readUserId());
            intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }

    //讀取使用者名字
    private String readUserName(){
        //創建SharedPreferences，索引為"Data"
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        //回傳在"Userdata"索引之下的資料；若無儲存則回傳"未存任何資料"
        return sharedPreferences.getString("UserName","未存任何資料");
    }

    //讀取使用者Id
    private String readUserId(){
        //創建SharedPreferences，索引為"Data"
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        //回傳在"Userdata"索引之下的資料；若無儲存則回傳"未存任何資料"
        return sharedPreferences.getString("UserID","未存任何資料");
    }

}
