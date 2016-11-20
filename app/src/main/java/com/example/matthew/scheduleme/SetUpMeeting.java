package com.example.matthew.scheduleme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class SetUpMeeting extends AppCompatActivity {
    Button save;
    DatePicker dp;
    List<String> durations;
    Spinner spinner;
    String duration;
    int year, month, day;
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupmeeting);
        listView = (ListView) findViewById(R.id.friendListViewZhuZhu);
        save = (Button) findViewById(R.id.saveMeeting);
        dp = (DatePicker) findViewById(R.id.datePicker);
        spinner = (Spinner) findViewById(R.id.durations);
        durations = new ArrayList<String>();
        durations.add("How long is the meeting?");
        durations.add("30 minutes");
        durations.add("60 minutes (1 hour)");
        durations.add("90 minutes (1.5 hours)");
        durations.add("120 minutes (2 hours)");
        durations.add("150 minutes (2.5 hours)");
        durations.add("180 minutes (3 hours)");
        durations.add("210 minutes (3.5 hours)");
        durations.add("240 minutes (4 hours)");
        durations.add("270 minutes (4.5 hours)");
        durations.add("300 minutes (5 hours)");
        duration = "";
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,Login.));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = dp.getYear();
                month = (dp.getMonth()+1);
                day = dp.getDayOfMonth();
             //   Log.i("Picked Date", "year: " + year + " month: " + month + " day: " + day);
             //   Log.i("Duration", "Duration: " + duration);
                Intent intentBack = new Intent(getApplicationContext(), MakeAMeeting.class);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duration = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
