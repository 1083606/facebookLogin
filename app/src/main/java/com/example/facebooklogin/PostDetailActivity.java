package com.example.facebooklogin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.facebooklogin.ui.post.PostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class PostDetailActivity extends AppCompatActivity {
    String userId,userName;

    ImageView img;
    private Button btn_addImage;
    private Button button_ok;
    private TextView txtuserName;
    private EditText textInputEditText;
    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    //----from camera
    private static final int  PERMISSION_CODE=1000;
    private static final int  IMAGE_CAPTURE_CODE=1001;
    //----from gallery
    private static final int  IMAGE_PICK_CODE=1002;
    Uri image_uri;

    //-spinner---------
    Spinner spinnerHabbitCat;
    ArrayList<String> habbitCatList = new ArrayList<>();
    ArrayAdapter<String> habbitCatAdapter;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        txtuserName=(TextView) findViewById(R.id.userName);
        txtuserName.setText(readUserName());

        textInputEditText=(EditText) findViewById(R.id.textInputEditText);

        //spinner設定start--------------------------
        requestQueue= Volley.newRequestQueue(this);
        spinnerHabbitCat=findViewById(R.id.spinnerHabbitCat);
        String url="http://140.131.114.140/chatbot109204/data/readHabbitCat.php";
        JsonObjectRequest jsonObjectRequst= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("records");
                    for (int i=0; i<jsonArray.length() ;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String habbit_cat_name =jsonObject.optString("habbit_cat_name");
                        habbitCatList.add(habbit_cat_name);
                        habbitCatAdapter = new ArrayAdapter<>(PostDetailActivity.this,R.layout.myspinner,habbitCatList);
                        habbitCatAdapter.setDropDownViewResource(R.layout.myspinner);
                        spinnerHabbitCat.setAdapter(habbitCatAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequst);
        //spinner設定end--------------------------
        //---------------------------------------

        /*
        img=(ImageView)findViewById(R.id.img);

        //點選添加照片start--------------------------------------------------
        btn_addImage = (Button) findViewById(R.id.btn_addImage);
        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //點選添加照片end-----------------------------------------------------
        */

        //點選貼文start--------------------------------------------------
        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !("".equals(textInputEditText.getText().toString())))
                {
                    String habbit_id=String.valueOf(spinnerHabbitCat.getSelectedItemPosition()+1);
                    String content=textInputEditText.getText().toString();
                    userId=readUserId();
                    //Toast.makeText(PostDetailActivity.this,userId+habbit_id+content, Toast.LENGTH_SHORT).show();
                    new AsyncPost().execute(userId,habbit_id,content);
                }
                else {
                    Toast.makeText(PostDetailActivity.this, "請輸入貼文內容", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //點選貼文end-----------------------------------------------------

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

    /*
    //添加照片-----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void selectImage() {
        final CharSequence[] options = { "拍照", "從相簿選擇","取消" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
        builder.setTitle("添加相片");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("拍照"))
                {
                    Toast.makeText(PostDetailActivity.this,"拍照",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.CAMERA)==
                                PackageManager.PERMISSION_DENIED||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                        PackageManager.PERMISSION_DENIED){
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }
                        else {
                            openCamera();
                        }
                    }
                    else {
                        openCamera();
                    }
                }
                else if (options[item].equals("從相簿選擇"))
                {
                    Toast.makeText(PostDetailActivity.this,"從相簿選擇",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==
                                PackageManager.PERMISSION_DENIED){
                            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }
                        else {
                            pickImageFromGallery();
                        }
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void openCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "FROM THE CAMERA");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //----------
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0 && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                    openCamera();
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(this,"PERMISSION DENIED...",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode == IMAGE_CAPTURE_CODE) {
            img.setImageURI(image_uri);
        }else if (resultCode == RESULT_OK&&requestCode == IMAGE_PICK_CODE){
            img.setImageURI(data.getData());
        }
    }

    ///-------------------------------------
    //選擇照片--開始
    ///-------------------------------------
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    ///-------------------------------------
    //選擇照片--結束
    ///-------------------------------------
     */

    ///-------------------------------------
    //按空白處，鍵盤就自動收回--開始
    ///-------------------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    ///-------------------------------------
    //按空白處，鍵盤就自動收回--結束
    ///-------------------------------------


    //-------------------------------------
    //cr
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
                url = new URL("http://140.131.114.140/chatbot109204/data/createPost.php");

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
                        .appendQueryParameter("habbit_id", params[1])
                        .appendQueryParameter("content", params[2]);
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
                    //cr 成功，取出 data
                    String data = jsonObject.getString("data");
                    //傳入成功，跳至"歡迎頁面"
                    //Toast.makeText(PostDetailActivity.this, result+data, Toast.LENGTH_SHORT).show();

                    // 設定從這個活動跳至 PageB 的活動
                    Intent intent = new Intent(PostDetailActivity.this, PostFragment.class);
                    // 開始跳頁
                    startActivity(intent);
                }else{
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(PostDetailActivity.this, result+data+error, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
