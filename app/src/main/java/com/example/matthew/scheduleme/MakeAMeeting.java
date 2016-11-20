package com.example.matthew.scheduleme;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

public class MakeAMeeting extends AppCompatActivity {
    user thisU;
    Dialog setupMeeting;
    Button createMeeting;
    String[] days, months, years, durations, friends;
    Spinner ds, ms, ys, duras, fs;
    String pickedDay, pickedMonth, pickedYear, pickedFriends, pickedDuration;
    DatePicker datepicker;
    TimePicker timepicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_meeting);

        Intent intent = getIntent();
        thisU = (user) intent.getSerializableExtra("testUser");
        createMeeting = (Button) findViewById(R.id.setup_meeting);
        setupMeeting = new Dialog(this);

        days = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        };
        months = new String[] {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };
        years = new String[] {
            "2016", "2017", "2018", "2019", "2020", "2021", "2022"
        };
        durations = new String[] {
            "30 minutes", "1 hour", "1 hour & 30 minutes", "2 hours", "2 hours & 30 minutes", "3 hours", "3 hours & 30 minutes",
                "4 hours", "4 hours & 30 minutes", "5 hours"
        };
        friends = new String[thisU.getFriends().size()];
        if (thisU.getFriends() != null) {
            for (int i = 0; i < thisU.getFriends().size(); i++) {
                friends[i] = thisU.getFriends().get(i).get(0);
            }
        }

        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetUpMeeting.class);
                startActivity(intent);
            }
        });
    }
}



