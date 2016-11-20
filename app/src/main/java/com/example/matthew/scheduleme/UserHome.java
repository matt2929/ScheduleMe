package com.example.matthew.scheduleme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserHome extends Activity {
    Button goToCalender;
    Button testRest;
    //String valueString="username";
    //String result = "";
    EditText textView;
    ArrayList<user> users = new ArrayList<user>();
    Button signOutB;
    Button viewFriends;
    Button manageEvents;
    private static final String TAG = "SignOutActivity";
    private GoogleApiClient mGoogleApiClient;
    user thisUser;
    Button testPost;
    EditText put;
    private GoogleApiClient signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        textView = (EditText) findViewById(R.id.upcommingdata);

        // for connection class
        Intent intent = getIntent();
        thisUser = (user) intent.getSerializableExtra("testUser");
        //thisUser.setFriends(new ArrayList<String>());
        ArrayList<String> testFriends = new ArrayList<String>();
        String one = "Jimmy Johns";
        String two = "OMG MEH";
        String three = "Monkey D Luffy";
        String four = "Steve Jobs";
        String five = "Bill Gate";
        testFriends.add(one);
        testFriends.add(two);
        testFriends.add(three);
        testFriends.add(four);
        testFriends.add(five);
       // thisUser.setFriends(testFriends);

        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                intent.putExtra("testUser", thisUser);
                startActivity(intent);
            }
        });

        signOutB = (Button) findViewById(R.id.signOutBtn);
        signOutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHome.this.signOut();
                Intent intentSO = new Intent(getApplicationContext(), Login.class);
                startActivity(intentSO);
            }
        });

        viewFriends = (Button) findViewById(R.id.userhomeconnections);
        viewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentJump = new Intent(getApplicationContext(), Connection.class);
                intentJump.putExtra("testUser", thisUser);
                startActivity(intentJump);
            }
        });

        manageEvents = (Button) findViewById(R.id.userhomemonoageevent);
        manageEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAMeeting.class);
                startActivity(intent);
            }
        });

        testRest = (Button) findViewById(R.id.testrest);
        testRest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("butt1", "press");
                    //    new UserHome.HttpRequestTask().execute();
                    }
                });

        put = (EditText) findViewById(R.id.postname);
        put.setText(thisUser.getName());
        testPost = (Button) findViewById(R.id.takethenamebelowandpost);
        testPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   valueString = put.getText().toString();
            }

            protected void onStart() {
                UserHome.super.onStart();
            }
        });
    }

    /*
     * Written by Dakota Lester
     * Google Method Interpretation
     * MUST HAVE GoogleApiClient.onConnected CALLED FIRST THEN
     * SIGN OUT IS ALLOWED or EXPECTION WILL BE THROWN
     * Used to sign a person out of their google account
     */
    private void signOut() {
        if (signout != null && signout.isConnected()) {
            signout.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    signout.disconnect();
                }
            });
        }
    }

    /*
    * Written by Dakota Lester
    * Google Method Interpretation
    * MUST HAVE GoogleApiClient.onConnected CALLED FIRST THEN
    * SIGN OUT IS ALLOWED or EXPECTION WILL BE THROWN
    * Completed - need to figure where to place this in the code
    * Delete users credentials
     */
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(signout).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // Removed
                    }
                }
        );
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public class HttpTaskPost extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper();
        //    user _user = new user();
            String url = "http://warmachine.cse.buffalo.edu:8082/process_post";
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.e("nope", "");
            }
            String jsonInString = "";
            try {
                jsonInString = mapper.writeValueAsString(thisUser);
                Log.e("json, ", jsonInString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            restTemplate.postForObject(url, thisUser, user.class);
            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Log.e("what", "what");
        }
    }
}