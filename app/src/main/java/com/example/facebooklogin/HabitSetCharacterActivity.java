package com.example.facebooklogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

    //----------image-------------
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_set_character);

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
                if ( !("".equals(role_name))&& !("".equals(nick_name)) &&!("0".equals(roleCategoryId)&&!("".equals(profile_image)))){
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
                                    //post成功，跳至HabitSetCharacterActivity
                                    //Toast.makeText(login.this,user_id+"登入成功",Toast.LENGTH_SHORT).show();
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
                                    //點選"確認"
                                    Intent intent = new Intent();
                                    intent.setClass(HabitSetCharacterActivity.this ,ChatActivity.class);
                                    startActivity(intent);
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

    //open gallery
    void pickImage()
    {
        /*
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

         */

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(Intent.createChooser(intent,"select image"),1002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1002) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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
                    .setNotificationConfig(new UploadNotificationConfig())
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



    /*
    //cr createChatRoom
    public void Next()
    {
        //---------------------------------------
        role_name=editRole_name.getText().toString();
        nick_name=editNick_name.getText().toString();
        habbit_status="0";
        role_photo="0";
        user_id=readUserID();
        if ( !("".equals(role_name))&& !("".equals(nick_name)) &&!("0".equals(roleCategoryId))){
            //Toast.makeText(HabitSetCharacterActivity.this, user_id+nick_name+roleCategoryId+role_name+habbit_id+habbit_name+habbit_status+signed_time+original_intention+goodness+days, Toast.LENGTH_SHORT).show();
            new AsyncPostcreateChatRoom().execute(user_id,nick_name,roleCategoryId,role_name,role_photo,habbit_id,habbit_name,habbit_status,signed_time,original_intention,goodness,badness,days);
            //uploadFile(fileName,filePath,user_id,nick_name,roleCategoryId,role_name,habbit_id,habbit_name,habbit_status,signed_time,original_intention,goodness,badness,days);
        }else{
            Toast.makeText(HabitSetCharacterActivity.this, "請完整內容", Toast.LENGTH_SHORT).show();
        }
    }
    
     */


    /*
    //-------------------------------------
    //cr createChatRoom
    //-------------------------------------
    private class AsyncPostcreateChatRoom   extends AsyncTask<String,Void,String> {

        /// boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
        String BOUNDARY = "******";
        // 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
        String  preFix = ("\r\n--" + BOUNDARY + "--\r\n");
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

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
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //----------------------------------------
                //-----------------------------------------
                // Append parameters to URL
                //user_id,nick_name,roleCategoryId,role_name,habbit_id,habbit_name,habbit_status,signed_time,original_intention,goodness,badness,days
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("nick_name", params[1])
                        .appendQueryParameter("role_id", params[2])
                        .appendQueryParameter("role_name", params[3])
                        .appendQueryParameter("role_photo", params[4])
                        .appendQueryParameter("habbit_id", params[5])
                        .appendQueryParameter("habbit_name", params[6])
                        .appendQueryParameter("habbit_status", params[7])
                        .appendQueryParameter("signed_time", params[8])
                        .appendQueryParameter("original_intention", params[9])
                        .appendQueryParameter("goodness", params[10])
                        .appendQueryParameter("badness", params[11])
                        .appendQueryParameter("days", params[12]);
                String query = builder.build().getEncodedQuery();

                //--------------------
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
                    return (String.valueOf(result));
                } else {

                    return ("unsuccessful");
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
            try {
                //Toast.makeText(HabitSetCharacterActivity.this, strUTF8, Toast.LENGTH_LONG).show();

                JSONObject jsonObject = new JSONObject(strUTF8);
                String result = jsonObject.getString("result");
                int resultValue=Integer.parseInt(result);
                if (resultValue==0){
                    //cr 成功，取出 data
                    String data = jsonObject.getString("data");
                    String chatroom_id = jsonObject.getString("return chatroom_id: ");
                    Toast.makeText(HabitSetCharacterActivity.this, result+data+chatroom_id, Toast.LENGTH_SHORT).show();

                    //-------------------------------------
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
                                    startActivity(intent);
                                }
                            })
                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    //點選"確認"
                                    Intent intent = new Intent();
                                    intent.setClass(HabitSetCharacterActivity.this ,ChatroomActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .build();

                    //-------------------------------------------------


                }else{
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(HabitSetCharacterActivity.this, result+data+error, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
     */

}





