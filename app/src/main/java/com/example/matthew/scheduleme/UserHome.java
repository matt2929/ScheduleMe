package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserHome extends AppCompatActivity {
    Button goToCalender;
    Button logOut;
    Button goToMAM;
    String passInUN;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        goToCalender =(Button) findViewById(R.id.Schedule_Button);
        logOut = (Button) findViewById(R.id.LogOut_Button);
        goToMAM = (Button) findViewById(R.id.Meeting_Button);

        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        goToMAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAMeeting.class);
                startActivity(intent);
            }
        });

        username = (TextView) findViewById(R.id.Username);
        Intent i = getIntent();
        String text = i.getStringExtra ("text");
        username.setText(text);
    }
}
