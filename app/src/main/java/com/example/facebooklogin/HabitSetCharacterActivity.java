package com.example.facebooklogin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.karumi.dexter.Dexter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class HabitSetCharacterActivity extends AppCompatActivity{
    //String user_id,habbit_name,habbit_id,original_intention,goodness,badness,signed_time,days;
    String chatroom_id;
    String role_name,nick_name;
    String roleCategoryId="0";
    String role_photo;

    EditText editRole_name,editNick_name;
    Button btnnext;
    TextView text_update_proimg;
    CircleImageView profile_image;
    ProgressDialog progressDialog;
    String id;


    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    //-spinner---------
    Spinner spinnerRole;
    ArrayList<String> roleCategoryList = new ArrayList<>();
    ArrayAdapter<String> roleCategoryAdapter;
    RequestQueue requestQueue;
    String roleCategory;

    Toolbar toolbar;

    //----------image-------------
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath=null;


    //----from camera 2
    private static final int  PERMISSION_CODE=1000;
    private static final int  IMAGE_CAPTURE_CODE=1001;
    //----from gallery
    private static final int  IMAGE_PICK_CODE=1002;
    //Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_set_character);

        //返回按钮的监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //接收從HabitMotivationActivity的bundle
        Bundle bundle = getIntent().getExtras();
        chatroom_id = bundle.getString("chatroom_id");


        btnnext=findViewById(R.id.bttnnext);
        editRole_name=findViewById(R.id.editRole_name);
        text_update_proimg=findViewById(R.id.text_update_proimg);
        editNick_name=findViewById(R.id.editNick_name);
        profile_image=findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);

        //spinner設定start--------------------------
        spinnerRole=findViewById(R.id.spinnerRoleCategory);
        requestQueue= Volley.newRequestQueue(this);
        String url="http://140.131.114.140/chatbot109204/data/readRoleCat.php";
        JsonObjectRequest jsonObjectRequst= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    roleCategoryList.add(0,"選擇角色");
                    JSONArray jsonArray = response.getJSONArray("records");
                    for (int i=0; i<jsonArray.length() ;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String role_cat_name =jsonObject.optString("role_cat_name");
                        roleCategoryList.add(role_cat_name);
                        roleCategoryAdapter = new ArrayAdapter<>(HabitSetCharacterActivity.this,R.layout.myspinner,roleCategoryList);
                        roleCategoryAdapter.setDropDownViewResource(R.layout.myspinner);
                        spinnerRole.setAdapter(roleCategoryAdapter);
                        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                roleCategory=parent.getItemAtPosition(position).toString();
                                roleCategoryId=String.valueOf(spinnerRole.getSelectedItemPosition());
                                /*
                                Toast.makeText(HabitSetCharacterActivity.this,roleCategoryId, Toast.LENGTH_SHORT).show();
                                if(parent.getItemAtPosition(position).equals(("選擇角色"))){
                                    //do nothing
                                }else{
                                    roleCategory=parent.getItemAtPosition(position).toString();
                                    roleCategoryId=String.valueOf(spinnerRole.getSelectedItemPosition());
                                    Toast.makeText(HabitSetCharacterActivity.this,roleCategoryId, Toast.LENGTH_SHORT).show();
                                }
                                */
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
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


        text_update_proimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !("".equals(role_name))&& !("".equals(nick_name)) &&!("0".equals(roleCategoryId)&&!((filePath)==null))){
                    uploadMultipart();
                    //詢問是否要新增remind Data
                    new TTFancyGifDialog.Builder(HabitSetCharacterActivity.this)
                            .setTitle("成功建立聊天室 ヽ(●´∀`●)ﾉ")
                            .setMessage("是否要新增提醒時間？")
                            .setPositiveBtnText("好啊")
                            .setPositiveBtnBackground("#14b1ab")
                            .setNegativeBtnText("不用")
                            .setNegativeBtnBackground("#c1272d")
                            .setGifResource(R.drawable.chicken_gif)      //pass your gif, png or jpg
                            .isCancellable(true)
                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    //點選"確認"
                                    Intent intent = new Intent();
                                    intent.setClass(HabitSetCharacterActivity.this ,HabitSetRemindTime.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString("chatroom_id",chatroom_id);
                                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                                    startActivity(intent);


                                }
                            })
                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    GoMainActivity();
                                }
                            })
                            .build();
                    //-------------------------------------------------
                }else{
                    Toast.makeText(HabitSetCharacterActivity.this, "請完整內容", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    //添加照片-----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void pickImage() {
        final CharSequence[] options = { "拍照", "從相簿選擇","取消" };
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitSetCharacterActivity.this);
        builder.setTitle("添加相片");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("拍照"))
                {
                    //Toast.makeText(HabitSetCharacterActivity.this,"拍照",Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(HabitSetCharacterActivity.this,"從相簿選擇",Toast.LENGTH_SHORT).show();
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
        filePath=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //----------
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
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
            profile_image.setImageURI(filePath);
        }else if (resultCode == RESULT_OK&&requestCode == IMAGE_PICK_CODE){
            filePath = data.getData();
            profile_image.setImageURI(filePath);

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


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //cr start---------------------------------------------------------------------------
    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting the actual path of the image
        String path = getPath(filePath);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //---------------------------------------
            role_name=editRole_name.getText().toString();
            nick_name=editNick_name.getText().toString();



            //Creating a multi part request
            new MultipartUploadRequest (this, uploadId, "http://140.131.114.140/chatbot109204/data/updateRolePhoto.php")
                    .addFileToUpload(path, "role_photo") //Adding file
                    .addParameter("chatroom_id", chatroom_id) //Adding text parameter to the request
                    .addParameter("role_id",roleCategoryId )
                    .addParameter("role_name",role_name )
                    .addParameter("nick_name", nick_name)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMethod("POST")
                    .setUtf8Charset()
                    .setMaxRetries(2)
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //cr end---------------------------------------------------------------------------
    //---------------------------------------------------------------------
    private String readUserID(){
        SharedPreferences preferences = this.getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        return preferences.getString("UserID","未存任何資料");
    }

    //去MainActivity
    private void GoMainActivity(){
        Intent intent = new Intent();
        intent.setClass(HabitSetCharacterActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("UserName",readUserName());
        bundle.putString("UserID", readUserId());
        intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
        startActivity(intent);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}





