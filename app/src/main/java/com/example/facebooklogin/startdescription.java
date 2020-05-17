package com.example.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class startdescription extends AppCompatActivity {
    private TextView welcome;
    private Button btn_next;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startdescription);

        welcome = findViewById(R.id.welcome);
        btn_next = findViewById(R.id.btn_next);
        //接收從getUserName的bundle
        Bundle bundle = getIntent().getExtras();
        String UserName = bundle.getString("UserName");
        welcome.setText(UserName+"  歡迎加入慣chat始終!\n");

        // 按下按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(startdescription.this ,homePage.class );
                startActivity(intent);
            }
        });

    }
}
