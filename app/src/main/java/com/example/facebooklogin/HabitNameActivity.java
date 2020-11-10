package com.example.facebooklogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.facebooklogin.ui.post.post;

import org.json.JSONArray;
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

public class HabitNameActivity extends AppCompatActivity {
    private Button btn_next;
    //-spinner---------
    Spinner spinnerHabbitCat;
    ArrayList<String> habbitCatList = new ArrayList<>();
    ArrayAdapter<String> habbitCatAdapter;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_name);

        //spinner設定
        requestQueue= Volley.newRequestQueue(this);
        spinnerHabbitCat=findViewById(R.id.spinnerHabbitCat);
        String url="http://140.131.114.140/chatbot109204/data/readHabbitCat.php";
        JsonObjectRequest jsonObjectRequst= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray  jsonArray = response.getJSONArray("records");
                    for (int i=0; i<jsonArray.length() ;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String habbit_cat_name =jsonObject.optString("habbit_cat_name");
                        habbitCatList.add(habbit_cat_name);
                        habbitCatAdapter = new ArrayAdapter<>(HabitNameActivity.this,R.layout.myspinner,habbitCatList);
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

        btn_next = findViewById(R.id.btn_next);
        // 按下按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(HabitNameActivity.this ,HabitMotivationActivity.class);
                startActivity(intent);
            }
        });

        //播放小雞Gif
        ImageView chicken_gif = (ImageView) findViewById(R.id.imgChicken);
        Glide.with(this).load(R.drawable.gifchicken).into(chicken_gif);
    }



}
