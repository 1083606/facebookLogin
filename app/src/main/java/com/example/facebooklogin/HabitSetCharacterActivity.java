package com.example.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class HabitSetCharacterActivity extends AppCompatActivity {
    EditText edtname,edtmobile,edtabout;
    String mobile,name,about ;
    Button bupdate;
    TextView text_update_proimg;
    Uri uri;
    CircleImageView profile_image;
    ProgressDialog progressDialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_set_character);


        bupdate=findViewById(R.id.bttnnext);
        edtmobile=findViewById(R.id.editText_mobile);
        text_update_proimg=findViewById(R.id.text_update_proimg);
        edtname=findViewById(R.id.editText_name);
        profile_image=findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);

        text_update_proimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickIMage();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickIMage();
            }
        });

        bupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
    }

    //open gallery
    void pickIMage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(Intent.createChooser(intent,"select image"),1002);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1002){
            try {
                uri=data.getData();
                Bitmap bm= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                profile_image.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }
        //the end  onActivityResult
    }

    //update data into firebase database

    public void Update()
    {
        name=edtname.getText().toString();
        mobile=edtmobile.getText().toString();
        about=edtabout.getText().toString();

    }

}
