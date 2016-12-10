package com.example.matthew.scheduleme;

import android.app.Activity;
import android.app.Dialog;
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


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.Auth;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
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
public class UserHome extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button goToCalender;
    //String valueString="username";
    String result = "";
    EditText textView;
    ArrayList<user> users;
    Button signOut, backHelp, help;
    Button viewFriends;
    Button manageEvents;
    Button quickEvents;
    private static final String TAG = "SignOutActivity";
    private GoogleApiClient mGoogleApiClient;
    user thisUser;
    Dialog faq;
    Button testPost;
    TextView put;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        textView = (EditText) findViewById(R.id.upcommingdata);
        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        // for connection class
        Intent intent = getIntent();
        thisUser = Login.USERZHU;
        //thisUser.setFriends(new ArrayList<String>());
        // thisUser.setFriends(testFriends);

        // for the 'View Schedule' button
        // go the schedule page and call the google calendar api
        goToCalender = (Button) findViewById(R.id.userhomeviewschedule);
        goToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                intent.putExtra("testUser", thisUser);
                startActivity(intent);
            }
        });

        signOut=(Button) findViewById(R.id.signout_button);
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.signout_button:
                        signOut();
                        break;
                }
            }
        });

        // for the 'Help' button
        // showing a pop up window for instructions about the app after clicking the button
        faq = new Dialog(this);
        faq.setTitle("Schedule App Instruction");
        faq.setContentView(R.layout.faq_popup);
        backHelp = (Button) faq.findViewById(R.id.exitHelp);
        help = (Button) findViewById(R.id.help_btn);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faq.show();
                backHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        faq.dismiss();
                    }
                });
            }
        });


        // for the 'Quick Events' button
        // go to the quick events page after clicking the button
        quickEvents = (Button) findViewById(R.id.updateCalendar);
        quickEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQuick = new Intent(getApplicationContext(), QuickEventNext.class);
                startActivity(intentQuick);
            }
        });

        // for the 'Friends' button
        // go to friends page after clicking the button
        viewFriends = (Button) findViewById(R.id.userhomeconnections);
        viewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentJump = new Intent(getApplicationContext(), Connection.class);
                intentJump.putExtra("testUser", thisUser);
                startActivity(intentJump);
            }
        });

        // for 'Manage Event' button
        // go to manage event page
        manageEvents = (Button) findViewById(R.id.userhomemonoageevent);
        manageEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJ = new Intent(getApplicationContext(), MakeAMeeting.class);
                intentJ.putExtra("testUser", thisUser);
                startActivity(intentJ);
            }
        });


     //   put = (TextView) findViewById(R.id.postname);
     //   put.setText(Login.USERZHU.getName());
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //revokeAccess();
//                        SharedPreferences prefs = getSharedPreferences(getApplicationContext().toString(),Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
                        //         System.out.println("HERERERERERERERERERERERERERERER"+getPreferences(getApplicationContext().MODE_PRIVATE).getAll().toString());
//                        editor.remove(AccountManager.KEY_ACCOUNT_NAME);
//                        editor.commit();
                        String accPref = getPreferences(getApplicationContext().MODE_PRIVATE).getString(Schedule.PREF_ACCOUNT_NAME, null);
                        //         System.out.println("ACCPREF:::" + accPref);
                        accPref = "";
                        Intent intenT = new Intent(getApplicationContext(), Login.class);
                        startActivity(intenT);

                    }
                }
        );
    }


    protected void onStart() {
        super.onStart();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
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