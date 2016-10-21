package com.example.matthew.scheduleme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import java.util.Calendar;

public class MakeAMeeting extends AppCompatActivity implements
        View.OnClickListener{

    Button datePicker, timePicker;
    EditText dateInTxt, timeIntTxt;
    private int meetingYear, meetingMonth, meetingDay, meetingHour, meetingMinutes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_meeting);

        datePicker = (Button)findViewById(R.id.btn_date);
        timePicker = (Button)findViewById(R.id.btn_time);
        dateInTxt = (EditText)findViewById(R.id.in_date);
        timeIntTxt = (EditText)findViewById(R.id.in_time);

        datePicker.setOnClickListener(this);
        timePicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == datePicker) {
            final Calendar c = Calendar.getInstance();
            meetingYear = c.get(Calendar.YEAR);
            meetingMonth = c.get(Calendar.MONTH);
            meetingDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateInTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, meetingYear, meetingMonth, meetingDay);
            datePickerDialog.show();
        }
        if (v == timePicker) {
            final Calendar c = Calendar.getInstance();
            meetingHour = c.get(Calendar.HOUR_OF_DAY);
            meetingMinutes = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            timeIntTxt.setText(hourOfDay + ":" + minute);
                        }
                    }, meetingHour, meetingMinutes, false);
            timePickerDialog.show();
        }
    }
}
