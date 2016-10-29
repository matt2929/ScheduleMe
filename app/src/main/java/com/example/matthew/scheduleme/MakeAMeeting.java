package com.example.matthew.scheduleme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class MakeAMeeting extends AppCompatActivity {

    Button mainButt;
    EditText mainTextEdit;
    TextView mainTextView;
    ListView mainListView;
    DatePicker startDatePicker,endDatePicker;
    int monthStart =-1;
    int dateStart =-1;
    int yearStart =-1;

    int monthEnd =-1;
    int dateEnd =-1;
    int yearEnd =-1;
    String eventName="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_meeting);
        mainButt = (Button) findViewById(R.id.makemeetingbutt);
        mainTextEdit = (EditText) findViewById(R.id.makemeetingtext);
        mainListView = (ListView) findViewById(R.id.makemeetinglist);
        startDatePicker = (DatePicker) findViewById(R.id.makeameetingcalstart);
        endDatePicker = (DatePicker) findViewById(R.id.makeameetingcalend);
        mainTextView = (TextView) findViewById(R.id.makeameetingtextview);
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
    }

    public void secondView() {
        mainButt.setVisibility(View.VISIBLE);
        mainTextEdit.setVisibility(View.VISIBLE);
        mainListView.setVisibility(View.INVISIBLE);
        startDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setVisibility(View.VISIBLE);
        endDatePicker.setVisibility(View.INVISIBLE);
        mainTextView.setText("Make a name for this activity");
        mainButt.setText("set name");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName=mainTextEdit.getText().toString();
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
            public void onClick(View v){
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
        mainTextView.setText("Pick last point you would like to meet");
        mainButt.setText("Set Up A Meeting");
        mainButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Event Name: "+eventName+"\n Start: "+monthStart+"/"+dateStart+"/"+yearStart+"End: "+monthEnd+"/"+dateEnd+"/"+yearEnd,Toast.LENGTH_LONG).show();
            }
        });
    }
}






