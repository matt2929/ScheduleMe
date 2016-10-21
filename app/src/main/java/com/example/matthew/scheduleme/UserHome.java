package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserHome extends AppCompatActivity {
    Button goToCalender;
    Button testRest;
    String result = "";
    EditText textView;
    ArrayList<user> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        textView = (EditText) findViewById(R.id.upcommingdata);
        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intent);


            }
        });
        testRest= (Button) findViewById(R.id.testrest);
        testRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpRequestTask().execute();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
         }


    /**
     * A placeholder fragment containing a simple view.
     */

    private class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                final String url = "https://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                // Greeting greeting = restTemplate.getForObject(url, Greeting.class);

//                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope","");

            }
            String url = "http://warmachine.cse.buffalo.edu:8081/listUsers";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String getTheData = restTemplate.getForObject(url,String.class,"Android");
            Collection<user> ob;
            try {
                ob = new ObjectMapper().readValue(getTheData, new TypeReference<Collection<user>>() { });
                Log.e("Value",""+ob.size());
                users=new ArrayList<user>();
                Iterator allPeople = ob.iterator();
                while(allPeople.hasNext()){
                    users.add((user)allPeople.next());
                }
                Log.e("Value",""+users.get(0).getName());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("print","well that didnt work");
            }
            Log.e("something",getTheData);

            // user[] result = restTemplate.getForObject(url, user[].class, "Android");
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            String string = "";
            for(user u:users){
                string+="name: "+u.getName()+" password: "+u.getPassword()+" proff: "+u.getProfession()+" id: "+u.getId();
                string+="\n";
            }
            textView.setText(string);
        }
    }
}