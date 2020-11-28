package com.example.facebooklogin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HabitMotivationActivity extends AppCompatActivity {
    private Button btn_next;
    String habbit_name,habbit_id;
    private EditText eTxtOriginal_intention,eTxtGoodness,eTxtBadness;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_motivation);


        btn_next = findViewById(R.id.btn_next);
        eTxtOriginal_intention=findViewById(R.id.eTxtOriginal_intention);
        eTxtGoodness=findViewById(R.id.eTxtGoodness);
        eTxtBadness=findViewById(R.id.eTxtBadness);

        //接收從HabitNameActivity的bundle
        Bundle bundle = getIntent().getExtras();
        habbit_name = bundle.getString("habbit_name");
        habbit_id = bundle.getString("habbit_id");

        //返回按钮的监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // 按下按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("".equals(eTxtOriginal_intention.getText().toString()))&& !("".equals(eTxtGoodness.getText().toString()))&& !("".equals(eTxtBadness.getText().toString())))
                {
                    Intent intent = new Intent();
                    intent.setClass(HabitMotivationActivity.this,HabitSetTimeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("habbit_name",habbit_name);
                    bundle.putString("habbit_id", habbit_id);
                    bundle.putString("original_intention", eTxtOriginal_intention.getText().toString());
                    bundle.putString("goodness", eTxtGoodness.getText().toString());
                    bundle.putString("badness", eTxtBadness.getText().toString());
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent);
                }
                else {
                    Toast.makeText(HabitMotivationActivity.this, "請入完整!", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(HabitMotivationActivity.this,(txtHabbit_name.getText().toString())+(String.valueOf(spinnerHabbitCat.getSelectedItemPosition()+1)) , Toast.LENGTH_SHORT).show();
            }
            //-------------------------------------------------------------

        });



    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            //Toast.makeText(HabitNameActivity.this, "返回", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
