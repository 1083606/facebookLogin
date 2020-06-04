/**package com.example.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HabitSetTimeActivity extends AppCompatActivit {
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_motivation);

        btn_next = findViewById(R.id.btn_next);

        // 按下按鈕 觸發事件

        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(HabitMotivationActivity.this ,HabitSetTimeActivity.class);
                startActivity(intent);
            }
        });
    }
}**/
