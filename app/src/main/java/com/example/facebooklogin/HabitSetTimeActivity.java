package com.example.facebooklogin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;

//-------------------------------

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
//-------------------------------

public class HabitSetTimeActivity extends AppCompatActivity implements View.OnClickListener {
    String habbit_name,habbit_id,original_intention,goodness,badness;
    TextView mTimeTextView;
    TextView mPickPunchTime;
    ImageView imgmPickPunchTime;
    Context mContext = this;
    Button btn_next;
    Spinner spinnerDays;
    Button buttonSubmitList;

    /* 原本設定每日提醒時間
    //------------------------------
    LinearLayout layoutList;
    Button buttonAdd;
    ArrayList<Remindertime> remindertimesList = new ArrayList<>();
    //------------------------------
    原本設定每日提醒時間end */
    //------------------------------

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_habit_set_time);

        //接收從HabitMotivationActivity的bundle
        Bundle bundle = getIntent().getExtras();
        habbit_name = bundle.getString("habbit_name");
        habbit_id = bundle.getString("habbit_id");
        original_intention =bundle.getString("original_intention");
        goodness=bundle.getString("goodness");
        badness=bundle.getString("badness");

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        Calendar calendar = Calendar.getInstance();
        btn_next = findViewById(R.id.btn_next);
        spinnerDays=findViewById(R.id.spinnerDays);

        /* 原本設定每日提醒時間
        //------------------------------
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);
        //-----------------------
         */

        buttonSubmitList = findViewById(R.id.btn_next);
        buttonSubmitList.setOnClickListener(this);

        final  int hour = calendar.get(calendar.HOUR_OF_DAY);
        final  int minute = calendar.get(calendar.MINUTE);

        mPickPunchTime = (TextView) findViewById(R.id.mPickPunchTime);
        imgmPickPunchTime = (ImageView) findViewById(R.id.imgmPickPunchTime);

        mPickPunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       mPickPunchTime.setText(hourOfDay+":"+minute);
                   }
               },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
               timePickerDialog.show();
            }
        });

        imgmPickPunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        mPickPunchTime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        btn_next = findViewById(R.id.btn_next);
        // 按下下一步按鈕 觸發事件
        btn_next.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if ( !("".equals(mPickPunchTime.getText().toString()))&& !("".equals(spinnerDays.getSelectedItem().toString())))
                {
                    Intent intent = new Intent();
                    intent.setClass(HabitSetTimeActivity.this ,HabitSetCharacterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("habbit_name",habbit_name);
                    bundle.putString("habbit_id", habbit_id);
                    bundle.putString("original_intention", original_intention);
                    bundle.putString("goodness", goodness);
                    bundle.putString("badness", badness);
                    bundle.putString("signed_time", mPickPunchTime.getText().toString());
                    bundle.putString("days", spinnerDays.getSelectedItem().toString());
                    intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                    startActivity(intent);
                }
                else {
                    Toast.makeText(HabitSetTimeActivity.this, "請入完整!", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(HabitSetTimeActivity.this,habbit_name+'和'+habbit_id+'和'+original_intention+mPickPunchTime.getText().toString()+spinnerDays.getSelectedItem().toString() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //--------------------------------
    /* 原本設定每日提醒時間
    //------------------------------
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_add:

                addView();

                break;
        }

    }

    private boolean checkIfValidAndRead() {
        remindertimesList.clear();
        boolean result = true;

        for(int i=0;i<layoutList.getChildCount();i++){

            View remindertimeView = layoutList.getChildAt(i);

            TextView text_remindertime = (TextView)remindertimeView.findViewById(R.id.text_remindertime);
            //AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);

            Remindertime remindertime = new Remindertime();

            remindertimesList.add(remindertime);

        }

        if(remindertimesList.size()==0){
            result = false;
            Toast.makeText(this, "Add Cricketers First!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(this, "Enter All Details Correctly!", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {

        final View remindertimeView = getLayoutInflater().inflate(R.layout.row_add_remindertime,null,false);

        TextView text_remindertime = (TextView)remindertimeView.findViewById(R.id.text_remindertime);
        //AppCompatSpinner spinnerTeam = (AppCompatSpinner)remindertimeView.findViewById(R.id.spinner_team);
        ImageView imageClose = (ImageView)remindertimeView.findViewById(R.id.image_remove);

        //ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,teamList);
        //spinnerTeam.setAdapter(arrayAdapter);


        text_remindertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0;
                int minute = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        text_remindertime.setText(decimalFormat.format(hourOfDay) + ":" + decimalFormat.format(minute));
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(remindertimeView);
            }
        });

        layoutList.addView(remindertimeView);

    }

    private void removeView(View view){

        layoutList.removeView(view);

    }
    //原本設定每日提醒時間 END------
    //------------------------------
     */
}
