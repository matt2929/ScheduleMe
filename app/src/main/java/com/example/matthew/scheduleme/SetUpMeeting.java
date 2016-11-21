package com.example.matthew.scheduleme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.matthew.scheduleme.Login.USERZHU;
import static com.example.matthew.scheduleme.R.id.durations;

public class SetUpMeeting extends AppCompatActivity {
    Button save;
    DatePicker dp;
    user user = new user();
    public static int check =0;
    List<String> durations;
    Spinner spinner;
    static String duration;
    public static String freetime = "";
    static int year, month, day;
    public static String comparing="";
    ListView listView;
    int numReq = 0;
    int numCount = 0;
    List<String> tempArrayEvent = new ArrayList<>();
    ArrayList<String> invitedFriends = new ArrayList<String>();
    static ArrayList<ArrayList<String>> friendsEvents = new ArrayList<ArrayList<String>>();
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
        final ArrayList<String> friendTemp = new ArrayList<String>();
        for (int i = 0; i < USERZHU.getFriends().size(); i++) {
            friendTemp.add(USERZHU.getFriends().get(i).get(0));
        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, friendTemp));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = dp.getYear();
                month = (dp.getMonth() + 1);
                day = dp.getDayOfMonth();
                comparing = year+"-"+month+"-"+day;
                System.out.println("Date Selected:"+ comparing);
                Log.e("Picked Date", "year: " + year + " month: " + month + " day: " + day);
                Log.e("Duration", "Duration: " + duration);
                Log.e("Picked Friends", "" + friendTemp);
                check=1;
                Intent intentBack = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intentBack);
                /* for(String s:friendTemp){
                    new HttpRequestTask2(s).execute();
                }*/
            }
        });
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < USERZHU.getFriends().size(); i++) {
            names.add(USERZHU.getFriends().get(i).get(0));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                invitedFriends.add(textView.getText().toString());
                view.setBackgroundColor(Color.YELLOW);
                Toast.makeText(getBaseContext(), "" + parent.getAdapter().getItem(position), Toast.LENGTH_LONG).show();
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


    public class HttpRequestTask2 extends AsyncTask<Void, Void, Greeting> {
        String Name = "";

        public HttpRequestTask2(String name) {
            Name = name;
        }

        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
            String url = "http://warmachine.cse.buffalo.edu:" + Login.values + "/getUser2";
            username name = new username();
            name.setName(Name);
            Log.e("value", "{" + name.getName() + "}");
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            String jsonInString = "";
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String stringThis = "";
            try {
                stringThis = restTemplate.postForObject(url, name, String.class);
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                Log.e("no connection", "couldnt post :(");
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
            username usertemp = new username();
            try {
                Log.e("json returned", stringThis);
                tempArrayEvent = mapper.readValue(stringThis, List.class);
                tempArrayEvent.add(0,name.getName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Log.e("converted", "" + tempArrayEvent);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            numCount++;
            if(numCount==numReq){
                Toast.makeText(getApplicationContext(),"All times located",Toast.LENGTH_LONG).show();
            }
        }
    }

}

