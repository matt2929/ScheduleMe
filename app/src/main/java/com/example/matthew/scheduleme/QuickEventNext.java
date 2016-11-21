package com.example.matthew.scheduleme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import android.net.Uri;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityasinghrathore on 19/11/16.
 */

public class QuickEventNext extends AppCompatActivity{
    EditText eventname, entertime, locationname, startdate, enddate, starttime, endtime;
    Button saveN;
    public static String eventN, locationN, startdateN, enddateN, starttimeN, endtimeN, friendsNames="AAA";
    public static int writer = 0;
    public static ArrayList<String> friendsList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_event);
        friendsList = new ArrayList<>();
        eventname = (EditText) findViewById(R.id.texteventname);
        entertime = (EditText) findViewById(R.id.editTime);
        saveN = (Button) findViewById(R.id.buttonsaveEE);
        locationname = (EditText) findViewById(R.id.editlocation);
        startdate = (EditText) findViewById(R.id.editstartdate);
        starttime = (EditText) findViewById(R.id.editstarttime);
        enddate = (EditText) findViewById(R.id.editenddate);
        endtime = (EditText) findViewById(R.id.editendtime);
        saveN = (Button) findViewById(R.id.buttonsaveEE);
        saveN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(getApplicationContext(),UserHome.class);
//                startActivity(intent1);
                eventN = eventname.getText().toString();
                locationN=locationname.getText().toString();
                startdateN = startdate.getText().toString();
                starttimeN = starttime.getText().toString();
                enddateN = enddate.getText().toString();
                endtimeN = endtime.getText().toString();
                friendsNames = entertime.getText().toString();
                System.out.println(eventN);
                System.out.println(locationN);
                System.out.println(startdateN);
                System.out.println(starttimeN);
                System.out.println(enddateN);
                System.out.println(endtimeN);
                System.out.println(friendsNames);
                int count=0;

                for(int i=0; i<friendsNames.length();i++){
                    if(friendsNames.charAt(i)==',') {
                        count++;
                    }
                }
                if(count==0){
                    friendsList.add(friendsNames);
                }
                while(count!=0){
                    String friendsplit=friendsNames;
                    String[] split=friendsplit.split(",");
                    friendsList.add(split[0]);
                    friendsNames=split[1];
                    count--;
                    if(count==0){
                        friendsList.add(split[1]);
                    }
                }
                try {
                    savedata();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void savedata() throws IOException{
        writer=1;
        Intent intent1 = new Intent(getApplicationContext(),Schedule.class);
        startActivity(intent1);
    }


}
