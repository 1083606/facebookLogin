package com.example.facebooklogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;

import java.io.File;

public class PostDetailActivity extends AppCompatActivity {

    private  static  final  int PERMISSION_CODE=1000;
    private static final int IMAGE_CAPTURE_CODE =1001;

    private  static  final  int IMAGE_PICK_CODE=1002;

    Button btn_addImage;
    ImageView img;

    Uri image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //----------------
        img =findViewById(R.id.img);
        btn_addImage = findViewById(R.id.btn_addImage);

        //button click
        btn_addImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new TTFancyGifDialog.Builder(PostDetailActivity.this)
                        .setTitle("上傳圖片")
                        .setMessage("拍攝相片還是從相簿選擇?")
                        .setPositiveBtnText("相簿選擇")
                        .setPositiveBtnBackground("#ffba5a")
                        .setNegativeBtnText("拍攝相片")
                        .setNegativeBtnBackground("#75b79e")
                        .setGifResource(R.drawable.chicken_gif)      //pass your gif, png or jpg
                        .isCancellable(true)
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //Toast.makeText(PostDetailActivity.this,"相簿選擇",Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        ==PackageManager.PERMISSION_DENIED){
                                        String[] permission ={Manifest.permission.READ_EXTERNAL_STORAGE};
                                        requestPermissions(permission, PERMISSION_CODE);
                                    }else{
                                        pickImageGallery();
                                    }
                                }
                                else {
                                    pickImageGallery();
                                }
                            }
                        })
                        .OnNegativeClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //Toast.makeText(PostDetailActivity.this,"拍攝相片",Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    if (checkSelfPermission(Manifest.permission.CAMERA)==
                                            PackageManager.PERMISSION_DENIED ||
                                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                                    PackageManager.PERMISSION_DENIED){
                                        //----permission not enabled , request it
                                        String[] permission ={Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                        requestPermissions(permission, PERMISSION_CODE);
                                    }
                                    else{
                                        //permission already granted
                                        openCamera();
                                    }
                                }
                                else {
                                    openCamera();
                                }
                            }
                        })
                        .build();
            }
        });

    }


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

    ///camera
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_url = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        // Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_url);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }


    //handling permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera();
                    pickImageGallery();
                }
                else{
                    Toast.makeText(this,"Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&requestCode == IMAGE_CAPTURE_CODE){
            img.setImageURI(image_url);
        }

        if (requestCode==RESULT_OK &&requestCode == IMAGE_PICK_CODE){
            img.setImageResource(R.drawable.background);
            //img.setImageURI(data.getData());
        }
    }
    
    //從相簿取得相片
    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }
}
