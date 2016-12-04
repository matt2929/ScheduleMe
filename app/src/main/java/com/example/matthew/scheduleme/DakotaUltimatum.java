package com.example.matthew.scheduleme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Calendar;

public class DakotaUltimatum extends AppCompatActivity {
    TextView text;
    static String duration;
    public static int year, month, day;
    public static String dateee = "";

    ArrayList<ArrayList<String>> temp = SetUpMeeting.friendsEvents;//array list where index 0 is name and everything else is the event data.
    ArrayList<ZhuZhuEvent> eventArray = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = SetUpMeeting.year;
        month = SetUpMeeting.month;
        day = SetUpMeeting.day;
        duration = SetUpMeeting.duration;
        if(duration==null){
            duration="30";
        }
        setContentView(R.layout.activity_dakota_ultimatum);
        text = (TextView) findViewById(R.id.dakotasSolution);
        int q = 0;
        listView = (ListView) findViewById(R.id.returnedValues);
        ArrayList<ZhuZhuEvent> combonationOfMeeting = new ArrayList<ZhuZhuEvent>();
        if (Login.USERZHU.getSchedule().size() > 0) {
            for (int j = 1; j < Login.USERZHU.getSchedule().size(); j++) {
                ZhuZhuEvent tempE = new ZhuZhuEvent();
                tempE.setDate(getStartDateFromString(Login.USERZHU.getSchedule().get(j)));
                tempE.setEndTime(getEndTimeFromString(Login.USERZHU.getSchedule().get(j)));
                tempE.setStartTime(getStartTimeFromString(Login.USERZHU.getSchedule().get(j)));
                Log.e("temp month: ", "" + tempE.getMonth());
                Log.e("selected Month", "" + SetUpMeeting.month);
                Log.e("temp day: ", "" + tempE.getDay());
                Log.e("selected dat", "" + SetUpMeeting.day);
                if (tempE.getMonth() == SetUpMeeting.month && tempE.getDay() == SetUpMeeting.day) {
                    Log.e("added", "add");
                    eventArray.add(tempE);
                } else {
                    Log.e("not added", "not add");
                }
            }
        }
        Collections.sort(eventArray, new Comparator<ZhuZhuEvent>() {
            @Override
            public int compare(ZhuZhuEvent o1, ZhuZhuEvent o2) {
                if ((o1.getStartHour() == o2.getStartHour() && o1.getStartMin() < o2.getStartMin()) || o1.getStartHour() < o2.getStartHour()) {
                    return -1;
                } else if (o1.getStartHour() == o2.getStartHour() && o1.getStartMin() == o2.getStartMin()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        Log.e("sort", "ended");
        int index = 0;
        for (int i = 0; i < eventArray.size(); i++) {
            if (combonationOfMeeting.size() == 0) {
                combonationOfMeeting.add(eventArray.get(i));
            } else {
                ZhuZhuEvent first = eventArray.get(i);
                ZhuZhuEvent second = combonationOfMeeting.get(combonationOfMeeting.size() - 1);
                if ((first.getStartHour() <= second.getEndHour()&&first.getEndHour()>=second.getEndHour())) {
                    combonationOfMeeting.get(combonationOfMeeting.size()-1).setEndTime(first.getEndTime());
                } else {
                    combonationOfMeeting.add(first);
                }
            }
        }
        Log.e("combo", "ended");


        int starthr = 7;
        int deadlinehr = 23;
        int startmin = 0;
        int deadlinemin = 0;


        int duration = Integer.valueOf(SetUpMeeting.duration.split(" ")[0]);
        for(ZhuZhuEvent combonationOfMeeting1: combonationOfMeeting){
            Log.e("start","size:" +combonationOfMeeting.size()
                    +" starthr: "+ combonationOfMeeting1.getStartHour()
                    +" finishhr: "+ combonationOfMeeting1.getEndHour());
        }
        ArrayList<TimeStore> hrs = new ArrayList<TimeStore>();

        while (starthr + duration / 60 <= deadlinehr) {


            if (combonationOfMeeting.size() == 0) {
                hrs.add(new TimeStore("need a way to find date", starthr, startmin, deadlinehr, deadlinemin));
                break;

            } else {

                ZhuZhuEvent current = combonationOfMeeting.get(0);
                int durHour = duration / 60;
                int durMin = duration % 60;

                int endhr = starthr + durHour;
                int endmin = startmin + durMin;

                if (endmin > 60) {
                    endhr = endhr + 1;
                    endmin = endmin - 60;
                }
                Log.e("whats breaking", "starthr: " + starthr + " duration: " + duration + " endhr: " + endhr + " size: " + combonationOfMeeting.size());
                Log.e("this might break", "Zhu: " + current.getStartHour() + " endhr " + endhr);
                if (endhr < current.getStartHour()) {
                    Log.e("adding", "added");
                    hrs.add(new TimeStore(current.getDate(), starthr, startmin, current.getStartHour(), current.getStartMin()));
                } else if (endmin == current.getStartHour() && endmin <= current.getStartMin()) {
                    Log.e("adding", "added");
                    hrs.add(new TimeStore(current.getDate(), starthr, startmin, current.getStartHour(), current.getStartMin()));
                }

                starthr = current.getEndHour();
                startmin = current.getEndMin();
                combonationOfMeeting.remove(0);


            }
        }

        ;
        /*
        for(int comp =0; comp < (1440/ Integer.valueOf(SetUpMeeting.duration.split(" ")[0]));comp++){
            TimeStore temp = new TimeStore(comp, comp + 1);
            temp.setEventInfo("You can meet from"+(1440/ Integer.valueOf(SetUpMeeting.duration.split(" ")[0])*comp)/ Integer.valueOf(SetUpMeeting.duration.split(" ")[0])*comp+1);

            hrs.add(temp);
        }*//*
        ArrayList<TimeStore> savehrs = new ArrayList<>();
        for (TimeStore ts : hrs) {
            for (ZhuZhuEvent zzh : combonationOfMeeting) {
                ZhuZhuEvent first = zzh;
                TimeStore second = ts;
                if (first.getStartHour() == ts.getStartHour()) {
                    savehrs.add(ts);
                }
            }
        }*/

        /*for (TimeStore shrs : savehrs) {
            hrs.remove(shrs);
        }*/
        ArrayList<String> strings = new ArrayList<String>();
        Log.e("combo", "" + combonationOfMeeting.size());
        for (TimeStore tstore : hrs) {


            String value = "You can meet at: " + convertIntToNotMilitary(tstore.getStartHour()) + " to " + convertIntToNotMilitary(tstore.getEndHour());
            strings.add(value);
            Log.e("event:", value);
        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, strings));
       /*
        ArrayList<ZhuZhuEvent> masterEvents = new ArrayList<>();
        Integer a = Integer.valueOf(SetUpMeeting.duration.split(" ")[0]);
        Log.e("time duration", "" + a);
        int begginingHour = 0;
        int begginingMinute = 0;
        ZhuZhuEvent zhuZhuEvent = new ZhuZhuEvent();
        zhuZhuEvent.setStartTime(begginingHour + "\\:" + begginingMinute + " PM");
        masterEvents.add(zhuZhuEvent);
*/
    }
public String convertIntToNotMilitary(int i){
    if(i==0){
        return "12 am";
    }
    if(i>=12){
        return (i%12+" pm");
    }

    return (i+" am");
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
        Log.i("getStartTimeFromString", s.split("\n")[1]);
        return s.split("\n")[1].substring(28);

    }

    public String getEndTimeFromString(String s) {
        Log.i("getEndTimeFromString", s.split("\n")[2]);
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

        public int getStartHour() {
            int addition;
            String timeStamp = startTime.split(" ")[3];
            //  Log.e("time stamp", timeStamp);
            if (timeStamp.equals("PM")) {
                //    Log.e("pm","pm");

                //  Log.e("start",""+""+Integer.valueOf(startTime.split(":")[0].substring(4)) + 12);
                return Integer.valueOf(startTime.split(":")[0].substring(4)) + 12;
            } else {
                if (Integer.valueOf(startTime.split(":")[0].substring(4)) == 12) {
                    //  Log.e("start",""+0);
                    return 0;
                } else {
                    //        Log.e("start",""+""+Integer.valueOf(startTime.split(":")[0].substring(4)));
                    return Integer.valueOf(startTime.split(":")[0].substring(4));
                }
            }
        }

        public int getStartMin() {
            //   Log.e("startMin", "" + Integer.valueOf(startTime.split(":")[1].substring(0, 2)));
            return Integer.valueOf(startTime.split(":")[0].split(" ")[2]);
        }

        public int getEndHour() {

            String timeStamp = endTime.split(" ")[1];
            // Log.e("time stamp", timeStamp);
            // Log.e("startHour", "" + Integer.valueOf(startTime.split(":")[0].substring(4)));
            if (timeStamp.equals("PM")) {
                return Integer.valueOf(endTime.split(":")[0].split(" ")[0]) + 12;
            } else {
                if (Integer.valueOf(endTime.split(":")[0].split(" ")[0]) == 12) {
                    return 0;
                } else {

                    return Integer.valueOf(endTime.split(":")[0].split(" ")[0]);
                }
            }
        }

        public int getEndMin() {
            //Log.e("endMin", "" + Integer.valueOf(endTime.split(":")[1].substring(0, 2)));
            return Integer.valueOf(endTime.split(":")[0].substring(0, 1));
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
    }


    public ArrayList<ArrayList<String>> getTemp() {
        return temp;
    }

    public class TimeStore {
        int StartHour;
        int EndHour;
        int StartMin;
        int EndMin;
        String Date;

        String eventInfo = "";

        public TimeStore(String date, int starth, int startm, int endh, int endm) {
            Date = date;
            StartHour = starth;
            StartMin = startm;
            EndHour = endh;
            EndMin = endm;
        }

        public String getDate() {
            return Date;
        }

        public int getEndHour() {
            return EndHour;
        }

        public int getEndMin() {
            return EndMin;
        }

        public int getStartHour() {
            return StartHour;
        }

        public int getStartMin() {
            return StartMin;
        }
    }
}