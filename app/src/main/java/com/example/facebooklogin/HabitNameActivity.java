package com.example.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HabitNameActivity extends AppCompatActivity {
    private Button btn_next;
    private EditText txtHabbit_name;

    //-spinner---------
    Spinner spinnerHabbitCat;
    ArrayList<String> habbitCatList = new ArrayList<>();
    ArrayAdapter<String> habbitCatAdapter;
    RequestQueue requestQueue;
    String habbit_id="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_name);

        txtHabbit_name=findViewById(R.id.txtHabbit_name);

        //spinner設定start--------------------------
        requestQueue= Volley.newRequestQueue(this);
        spinnerHabbitCat=findViewById(R.id.spinnerHabbitCat);
        String url="http://140.131.114.140/chatbot109204/data/readHabbitCat.php";
        JsonObjectRequest jsonObjectRequst= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    habbitCatList.add(0,"選擇習慣類別");
                    JSONArray  jsonArray = response.getJSONArray("records");
                    for (int i=0; i<jsonArray.length() ;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String habbit_cat_name =jsonObject.optString("habbit_cat_name");
                        habbitCatList.add(habbit_cat_name);
                        habbitCatAdapter = new ArrayAdapter<>(HabitNameActivity.this,R.layout.myspinner,habbitCatList);
                        habbitCatAdapter.setDropDownViewResource(R.layout.myspinner);
                        spinnerHabbitCat.setAdapter(habbitCatAdapter);
                        spinnerHabbitCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                habbit_id=String.valueOf(spinnerHabbitCat.getSelectedItemPosition());
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

        btn_next = findViewById(R.id.btn_next);
        // 按下按鈕 觸發事件

        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("".equals(txtHabbit_name.getText().toString()))&&!("0".equals(String.valueOf(spinnerHabbitCat.getSelectedItemPosition()))))
                {
                    Intent intent = new Intent();
                    intent.setClass(HabitNameActivity.this,HabitMotivationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("habbit_name",txtHabbit_name.getText().toString());
                    bundle.putString("habbit_id", String.valueOf(spinnerHabbitCat.getSelectedItemPosition()));
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent);
                }
                else {
                    Toast.makeText(HabitNameActivity.this, "請輸入習慣名稱或習慣類別", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(HabitNameActivity.this,(txtHabbit_name.getText().toString())+(String.valueOf(spinnerHabbitCat.getSelectedItemPosition()+1)) , Toast.LENGTH_SHORT).show();
            }
        });

        //播放小雞Gif
        ImageView chicken_gif = (ImageView) findViewById(R.id.imgChicken);
        Glide.with(this).load(R.drawable.gifchicken).into(chicken_gif);

        /*
        //txt顯示SharedPreferences-UserName
        TextView txtUserData = findViewById(R.id.txtUserData);
        txtUserData.setText(read());
         */
    }

    /*
    private String read(){
        //創建SharedPreferences，索引為"Data"
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        //回傳在"Userdata"索引之下的資料；若無儲存則回傳"未存任何資料"
        return sharedPreferences.getString("UserID","未存任何資料")+sharedPreferences.getString("UserName","未存任何資料");
    }

     */


}
