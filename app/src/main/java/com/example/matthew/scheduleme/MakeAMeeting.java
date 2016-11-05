package com.example.matthew.scheduleme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeAMeeting extends AppCompatActivity {
    user User=new user();
    ArrayList<String> events = new ArrayList<>();

    Button mainButt, yesButt, noButt, exisitngButt;
    EditText mainTextEdit;
    TextView mainTextView, existingText;
    ListView mainListView, existingList;
    DatePicker startDatePicker, endDatePicker;
    String data = "";
    int monthStart = -1;
    int dateStart = -1;
    int yearStart = -1;
    int monthEnd = -1;
    int dateEnd = -1;
    int yearEnd = -1;
    String eventName = "";

    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("grandma@yahoo.com");
        friends.add("angelLover@yahoo.com");
        friends.add("aditya@yahoo.com");
        events.add("Meet Aditya for fun ;)");
        events.add("More fun with Aditya");
        events.add("homework");
        events.add("JK just more aditya");
        User.setEvents(events);
        User.setAllFriends(friends);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_meeting);
        mainButt = (Button) findViewById(R.id.makemeetingbutt);
        mainTextEdit = (EditText) findViewById(R.id.makemeetingtext);
        mainListView = (ListView) findViewById(R.id.makemeetinglist);
        startDatePicker = (DatePicker) findViewById(R.id.makeameetingcalstart);
        endDatePicker = (DatePicker) findViewById(R.id.makeameetingcalend);
        mainTextView = (TextView) findViewById(R.id.makeameetingtextview);
        yesButt = (Button) findViewById(R.id.existingyes);
        noButt = (Button) findViewById(R.id.exisitngno);
        existingList = (ListView) findViewById(R.id.exisitnglist);
        existingText = (TextView) findViewById(R.id.existingtextview);
        exisitngButt = (Button) findViewById(R.id.makemeetingexistbutt);
        firstView();
    }

    public void firstView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setText("Click Button To Make An Invite");
        mainButt.setText("Set Up A Meeting");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondView();
            }
        });
        yesButt.setVisibility(View.GONE);
        noButt.setVisibility(View.GONE);
        existingList.setVisibility(View.GONE);
        existingText.setVisibility(View.GONE);

        exisitngButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingSecondView();
            }
        });
    }

    public void secondView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.VISIBLE);
        mainListView.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        exisitngButt.setVisibility(View.INVISIBLE);
        mainTextView.setText("Make a name for this activity");
        mainButt.setText("set name");

        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = mainTextEdit.getText().toString();
                thirdView();
            }
        });
    }

    public void thirdView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.GONE);
        startDatePicker.setVisibility(View.VISIBLE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setText("Pick first point you would like to meet");
        mainButt.setText("Choose these times");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthStart = startDatePicker.getMonth();
                dateStart = startDatePicker.getDayOfMonth();
                yearStart = startDatePicker.getYear();
                fourthView();
            }
        });
    }

    public void fourthView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.GONE);
        startDatePicker.setVisibility(View.GONE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.VISIBLE);
        mainTextView.setText("Pick last point you would like to meet");
        mainButt.setText("Set Up A Meeting");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthEnd = endDatePicker.getMonth();
                dateEnd = endDatePicker.getDayOfMonth();
                yearEnd = endDatePicker.getYear();
                fifthView();
            }
        });
    }

    public void fifthView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.VISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setText("Pick Friends");
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, User.friends);

        mainListView.setAdapter(itemsAdapter);
        mainButt.setText("Set Up A Meeting");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Name: " + eventName + "\n Start: " + monthStart + "/" + dateStart + "/" + yearStart + "End: " + monthEnd + "/" + dateEnd + "/" + yearEnd, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void existingSecondView() {
        mainButt.setVisibility(View.INVISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.INVISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        yesButt.setVisibility(View.INVISIBLE);
        noButt.setVisibility(View.INVISIBLE);
        exisitngButt.setVisibility(View.INVISIBLE);
        existingList.setVisibility(View.VISIBLE);
        existingText.setVisibility(View.VISIBLE);
        existingText.setText("Existing Meetings");
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, User.events);
        existingList.setAdapter(itemsAdapter);
        existingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here you can use the position to determine what checkbox to check
                //this assumes that you have an array of your checkboxes as well. called checkbox
                existingText.setText(events.get(position));
                data=events.get(position);
                existingThirdView();
            }
        });    }

    public void existingThirdView() {
        mainButt.setVisibility(View.INVISIBLE);
        mainTextEdit.setVisibility(View.INVISIBLE);
        mainListView.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.INVISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        yesButt.setVisibility(View.VISIBLE);
        noButt.setVisibility(View.VISIBLE);
        exisitngButt.setVisibility(View.INVISIBLE);
        existingList.setVisibility(View.INVISIBLE);
        existingText.setVisibility(View.VISIBLE);
        existingText.setText(data);
    }
}



