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
    ArrayList<ArrayList<String>> temp=SetUpMeeting.friendsEvents;//array list where index 0 is name and everything else is the event data.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        year = SetUpMeeting.year;
        month=SetUpMeeting.month;
        day=SetUpMeeting.day;
        duration =  SetUpMeeting.duration;
        setContentView(R.layout.activity_dakota_ultimatum);
        text = (TextView) findViewById(R.id.dakotasSolution);
        compareEvent();
    }
    /*
     * Written by: Dakota Lester
     */
    private void compareEvent()
    {


        // Map to hold the event along with the corresponding start and end time
        // Added hardcoded data
        Map<Event, ArrayList<DateTime>> bob = new HashMap<>();
        Map<Event, ArrayList<DateTime>> tim = new HashMap<>();
        // Times to hardcode events...
        Event bobeve_1 = new Event().setSummary("Study For 331").setLocation("Lockwood").setDescription("Studying for Final");
        Event timeve_1 = new Event().setSummary("Study For 331").setLocation("Lockwood").setDescription("Studying for Final");


        // Start Time of all events
        DateTime bobstart = new DateTime("2016-11-21T08:00:00-04:00");
        DateTime timstart = new DateTime("2016-11-21T10:00:00-04:00");
        EventDateTime bobtime = new EventDateTime().setDateTime(bobstart).setTimeZone("America/New_York");
        EventDateTime timtime = new EventDateTime().setDateTime(timstart).setTimeZone("America/New_York");
        bobeve_1.setStart(bobtime);
        timeve_1.setStart(timtime);
        // End Time of all events
        DateTime bobendDateTime = new DateTime("2016-11-21T09:00:00-04:00");
        DateTime timendDateTime = new DateTime("2016-11-21T11:00:00-04:00");
        EventDateTime bobend = new EventDateTime().setDateTime(bobendDateTime).setTimeZone("America/New_York");
        EventDateTime timend = new EventDateTime().setDateTime(timendDateTime).setTimeZone("America/New_York");
        bobeve_1.setEnd(bobend);
        timeve_1.setEnd(timend);
        // Hardcode ends
        // Algo begins
        ArrayList<DateTime> bobstimes = new ArrayList<>();
        ArrayList<DateTime> timstimes = new ArrayList<>();
        // User 1
        bobstimes.add(bobstart);
        bobstimes.add(bobendDateTime);
        bob.put(bobeve_1, bobstimes);
        // User 2
        timstimes.add(timstart);
        timstimes.add(timendDateTime);
        tim.put(timeve_1, timstimes);
        // Comparisons start (2 people)
        // Converts the value to milliseconds
        // where 3600000 = 1 hr
        for(int i = 0; i < bobstimes.size(); i++)
        {
            ArrayList<Long> diffintimes = new ArrayList<>();
            try {
                // Initial difference of each users events (duration of there event)
                long diff_b = bobstimes.get(i + 1).getValue() - bobstimes.get(i).getValue();
                long diff_t = timstimes.get(i + 1).getValue() - timstimes.get(i).getValue();
                // Converrt to minutes
                long diffmin_b = TimeUnit.MILLISECONDS.toMinutes(diff_b);
                long diffmin_t = TimeUnit.MILLISECONDS.toMinutes(diff_t);
                diffintimes.add(diffmin_b);
                diffintimes.add(diffmin_t);
            } catch (IndexOutOfBoundsException e)
            {
                break;
            }
            // If conditions for free time
            if(bobstart.isDateOnly() || timstart.isDateOnly())
            {
                text.setText("There are no available free times, sorry :(");
            }
            if((timstart.getValue() - bobendDateTime.getValue()) <= 0
                    || (bobstart.getValue() - timendDateTime.getValue()) <= 0)
            {
                text.setText("There are no available free times, sorry :(");
            }
            if((timstart.getValue() - bobendDateTime.getValue()) > 0) {
                text.setText("There is free time available as follows:");
                // Gets current time
                try {
                    for (int j = 0; j < 5; j++) {
                        // Gets current time
                        Date date = new Date();
                        // Output
                        ArrayList<String> moretimes = new ArrayList<>();
                        // Makes the output look nice
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        // Add 30 minutes to the calendar free times
                        cal.add(Calendar.MINUTE, 30);
                        moretimes.add(sdf.format(cal.getTime()));
                        // settext does not display all the complete list of times
                        // as of now it will only display the previous message and
                        // current time no looping can be done with setText
                        text.setText(text.getText() + "\n" + moretimes.get(j));
                    }
                }catch (IndexOutOfBoundsException e)
                {
                    break;
                }
            }
        }
    }
}
