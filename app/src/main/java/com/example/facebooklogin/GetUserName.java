package com.example.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetUserName extends AppCompatActivity {
    private EditText edit_userName;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_name);

        edit_userName = findViewById(R.id.edit_userName);
        btn_next = findViewById(R.id.btn_next);

        // 按下按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("".equals(edit_userName.getText().toString())) )
                {
                    Intent intent = new Intent();
                    intent.setClass(GetUserName.this,startdescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserName",edit_userName.getText().toString());
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦

                    startActivity(intent);
                }
            }
        });
    }
}
