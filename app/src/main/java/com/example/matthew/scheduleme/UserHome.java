package com.example.matthew.scheduleme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class UserHome extends AppCompatActivity {
    Button goToCalender;
    Button testRest;
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
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
            }
            String url = "http://warmachine.cse.buffalo.edu:8082/listUsers";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            result = restTemplate.getForObject(url, String.class, "Android");


            Log.e("Value", "{" + result + "}");
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            //greetingContentText.setText(result);
        }

    }


}
