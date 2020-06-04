package com.example.facebooklogin;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class HabitSetTimeActivity extends AppCompatActivity {
    TextView mTimeTextView;
    ImageButton mPickTimeButton;
    Context mContext = this;
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
    }
}
