package com.example.facebooklogin;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    TextView mTimeTextView;
    TextView mPickPunchTime;
    ImageView imgmPickPunchTime;
    Context mContext = this;

    private Button btn_next;

    //------------------------------
    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;

    ArrayList<Remindertime> remindertimesList = new ArrayList<>();
    //------------------------------

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_habit_set_time);

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        Calendar calendar = Calendar.getInstance();

        //-----------------------
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubmitList = findViewById(R.id.btn_next);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);
        //-------------------------

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
                    /*
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mPickPunchTime.setText(hourOfDay+":"+minute);
                    }
                     */
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
                Intent intent = new Intent();
                intent.setClass(HabitSetTimeActivity.this ,HabitSetCharacterActivity.class);
                startActivity(intent);
            }
        });
    }

    //--------------------------------

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
                    /*
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        text_remindertime.setText(hourOfDay+":"+minute);
                    }
                     */
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
    //-------------------------------------------------------

}
