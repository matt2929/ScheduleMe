package com.example.matthew.scheduleme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DakotaUltimatum extends AppCompatActivity {
TextView text;
    ArrayList<ArrayList<String>> temp=SetUpMeeting.friendsEvents;//array list where index 0 is name and everything else is the event data.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dakota_ultimatum);
        text = (TextView) findViewById(R.id.dakotasSolution);


        text.setText("Put answer here");
    }
}
