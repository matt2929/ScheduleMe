package com.example.matthew.scheduleme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Calendar;

public class DakotaUltimatum extends AppCompatActivity {
    TextView text;
    static String duration;
    int year, month, day;
    boolean[] allTimes = new boolean[24];
    ArrayList<ArrayList<String>> temp = SetUpMeeting.friendsEvents;//array list where index 0 is name and everything else is the event data.
    ArrayList<ArrayList<ZhuZhuEvent>> eventArray = new ArrayList<>();
    ArrayList<ZhuZhuEvent> bigevent = new ArrayList<ZhuZhuEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = SetUpMeeting.year;
        month = SetUpMeeting.month;
        day = SetUpMeeting.day;
        duration = SetUpMeeting.duration;
        setContentView(R.layout.activity_dakota_ultimatum);
        text = (TextView) findViewById(R.id.dakotasSolution);
        ArrayList<ZhuZhuEvent> zhuZhuEvents = new ArrayList<>();
        if (Login.USERZHU.getSchedule().size() > 0) {
            for (int j = 1; j < Login.USERZHU.getSchedule().size(); j++) {
                
            }
            eventArray.add(zhuZhuEvents);
        }
        for (int i = 0; i < temp.size(); i++) {
            ArrayList<ZhuZhuEvent> tempzhuZhuEvents = new ArrayList<>();
            for (int j = 1; j < temp.get(i).size(); j++) {
                ZhuZhuEvent tempE = new ZhuZhuEvent();
                tempE.setDate(getStartDateFromString(temp.get(i).get(j)));
                tempE.setEndTime(getEndTimeFromString(temp.get(i).get(j)));
                tempE.setStartTime(getEndTimeFromString(temp.get(i).get(j)));
                tempzhuZhuEvents.add(tempE);
            }
            eventArray.add(tempzhuZhuEvents);
        }
    }

    public String getStartDateFromString(String s) {
        Log.i("getStartDateFromString", s.split("\n")[1].substring(18, 28));
        return s.split("\n")[1].substring(18, 28);
    }

    public String getEndDateFromString(String s) {
        Log.i("getEndDateFromString", s.split("\n")[2].substring(17, 27));
        return s.split("\n")[2].substring(17, 27);
    }

    public String getStartTimeFromString(String s) {
        Log.i("getStartTimeFromString", s.split("\n")[1].substring(28));
        return s.split("\n")[1].substring(28);

    }

    public String getEndTimeFromString(String s) {
        Log.i("getEndTimeFromString", s.split("\n")[2].substring(30));
        return s.split("\n")[2].substring(30);

    }

    public class ZhuZhuEvent {
        String date;
        String startTime, endTime;

        public int getMonth() {
            return Integer.valueOf(date.split("-")[1]);
        }

        public int getDay() {
            return Integer.valueOf(date.split("-")[2]);
        }

        public int getYear() {
            return Integer.valueOf(date.split("-")[0]);
        }


        public String getDate() {
            return date;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setDate(String date) {
            this.date = date;

        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String returnTheFuckingDatum(ArrayList<ArrayList<ZhuZhuEvent>> e) {
            String timesToMeet="";
            ArrayList<ZhuZhuEvent> BigTime = new ArrayList<>();
            for(int i=0;i<e.size();i++){
                for(int j=0;j<e.get(i).size();j++){

                }
            }
        }
        public boolean checkInteruption(ArrayList)
    }

    public ArrayList<ArrayList<String>> getTemp() {
        return temp;
    }
}
