package com.example.facebooklogin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class HabitSetTimeActivity extends AppCompatActivity {
    TextView mTimeTextView,mTimeTextView1,mDateTextView;
    ImageButton mPickTimeButton,mPickTimeButton1,mPickDateButton;
    Context mContext = this;
    Context mContext1 = this;
    Context mDateContext = this;
    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_habit_set_time);

        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
        Calendar calendar = Calendar.getInstance();

        final  int hour = calendar.get(calendar.HOUR_OF_DAY);
        final  int minute = calendar.get(calendar.MINUTE);

        mPickTimeButton = (ImageButton) findViewById(R.id.pick_time_button);
        mPickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       mTimeTextView.setText(hourOfDay+":"+minute);
                   }
               },hour,minute,android.text.format.DateFormat.is24HourFormat(mContext));
               timePickerDialog.show();
            }
        });


        //第二個timepicker
        mTimeTextView1 = (TextView) findViewById(R.id.time_text_view1);
        Calendar calendar1 = Calendar.getInstance();

        final  int hour1 = calendar1.get(calendar.HOUR_OF_DAY);
        final  int minute1 = calendar1.get(calendar.MINUTE);

        mPickTimeButton1 = (ImageButton) findViewById(R.id.pick_time_button1);
        mPickTimeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext1, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTimeTextView1.setText(hourOfDay+":"+minute);
                    }
                },hour1,minute1,android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });

        //DatePicker
        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        Calendar datecalendar = Calendar.getInstance();

        final  int year = datecalendar.get(calendar.YEAR);
        final  int month = datecalendar.get(calendar.MONTH);
        final  int day = datecalendar.get(calendar.DAY_OF_MONTH);

        mPickDateButton = (ImageButton) findViewById(R.id.pick_date_button);
        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mDateContext, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDateTextView.setText(month+1+"/"+dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
}
